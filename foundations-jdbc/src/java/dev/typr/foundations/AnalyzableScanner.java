package dev.typr.foundations;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.JarURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Discovers all {@link Analyzable} instances in a package via reflection.
 *
 * <p>Scans compiled classes in the given package, instantiates them, and collects all fields and
 * methods that are (or wrap) {@link Analyzable}. Handles Java classes, Kotlin objects ({@code
 * INSTANCE}), and Scala objects ({@code MODULE$}).
 */
public final class AnalyzableScanner {

  public record Result(String className, String fieldName, Analyzable analyzable) {
    @Override
    public String toString() {
      return className + "." + fieldName;
    }
  }

  private AnalyzableScanner() {}

  // --- scan() overloads ---

  public static List<Analyzable> scan(String packageName) {
    return scan(packageName, (Transactor) null);
  }

  public static List<Analyzable> scan(String packageName, Transactor transactor) {
    return scanDetailed(packageName, transactor).stream()
        .map(
            r ->
                (Analyzable)
                    new Analyzable.Named(r.className() + "." + r.fieldName(), r.analyzable()))
        .toList();
  }

  public static List<Analyzable> scan(String packageName, ScanDirective... directives) {
    return scan(packageName, null, directives);
  }

  public static List<Analyzable> scan(
      String packageName, Transactor transactor, ScanDirective... directives) {
    return scanDetailed(packageName, transactor, directives).stream()
        .map(
            r ->
                (Analyzable)
                    new Analyzable.Named(r.className() + "." + r.fieldName(), r.analyzable()))
        .toList();
  }

  // --- scanDetailed() overloads ---

  public static List<Result> scanDetailed(String packageName) {
    return scanDetailed(packageName, (Transactor) null);
  }

  public static List<Result> scanDetailed(String packageName, Transactor transactor) {
    var classes = findClasses(packageName);
    var result = new ArrayList<Result>();
    var errors = new ArrayList<String>();

    for (var clazz : classes) {
      Object instance = instantiate(clazz, transactor);
      if (instance == null) {
        collectStaticAnalyzables(clazz, result);
        continue;
      }

      var fieldNames = new HashSet<String>();
      collectAnalyzables(instance, instance.getClass(), clazz.getSimpleName(), fieldNames, result);
      collectMethodAnalyzables(
          instance,
          instance.getClass(),
          clazz.getSimpleName(),
          fieldNames,
          List.of(),
          errors,
          result);
      // After the instance sweep, also scan statics on the same class: `static final
      // Operation<…>` fields AND `static Operation<…> …()` methods. Instance-path helpers
      // skip both (isAnalyzableMethod rejects static; collectAnalyzables skips static fields),
      // so without this sweep, static members on a normally-instantiable class are invisible
      // to scan().
      //
      // Exclusions:
      //   - Scala `$` classes — already handled via MODULE$ instance scan; their statics are
      //     just forwarders to the same members, double-counting would result.
      //   - Scala 3 `object X` facade classes (non-$ companion to the Scala object) — scanning
      //     their static method forwarders would double-count against the MODULE$ instance
      //     scan that already covered the vals/defs.
      //   - Kotlin objects — vals are private static fields PLUS a public instance getter; the
      //     getter path already covers them.
      if (!clazz.getName().endsWith("$") && !isKotlinObject(clazz) && !isScalaObject(clazz)) {
        collectStaticAnalyzables(clazz, result);
      }
    }

    if (!errors.isEmpty()) throwScanErrors(errors);
    result.sort(Comparator.comparing(Result::toString));
    return result;
  }

  public static List<Result> scanDetailed(String packageName, ScanDirective... directives) {
    return scanDetailed(packageName, null, directives);
  }

  public static List<Result> scanDetailed(
      String packageName, Transactor transactor, ScanDirective... directives) {
    var directiveList = List.of(directives);
    var classes = findClasses(packageName);
    var result = new ArrayList<Result>();
    var errors = new ArrayList<String>();
    var scannedInstances = new HashSet<Object>();

    for (var clazz : classes) {
      Object instance = instantiate(clazz, transactor);
      if (instance == null) {
        collectStaticAnalyzables(clazz, result);
        continue;
      }
      scannedInstances.add(instance);

      var instanceDirectives = findDirectivesForInstance(instance, directiveList);
      var fieldNames = new HashSet<String>();
      collectAnalyzables(instance, instance.getClass(), clazz.getSimpleName(), fieldNames, result);
      collectMethodAnalyzables(
          instance,
          instance.getClass(),
          clazz.getSimpleName(),
          fieldNames,
          instanceDirectives,
          errors,
          result);
      if (!clazz.getName().endsWith("$")) {
        collectStaticAnalyzables(clazz, result);
      }
    }

    for (var directive : directiveList) {
      if (directive instanceof ScanDirective.InstanceScan is) {
        if (scannedInstances.contains(is.instance())) continue;
        scannedInstances.add(is.instance());

        var inst = is.instance();
        var clazz = inst.getClass();
        var simpleName = stripSuffix(clazz.getSimpleName());

        var fieldNames = new HashSet<String>();
        collectAnalyzables(inst, clazz, simpleName, fieldNames, result);
        collectMethodAnalyzables(
            inst, clazz, simpleName, fieldNames, is.overrides(), errors, result);
      }
    }

    if (!errors.isEmpty()) throwScanErrors(errors);
    result.sort(Comparator.comparing(Result::toString));
    return result;
  }

  /** Describes an {@link Analyzable} showing its operation structure and SQL fragments. */
  public static String describe(Analyzable analyzable) {
    return describeOp(analyzable, 0);
  }

  private static String describeOp(Object obj, int depth) {
    String indent = "  ".repeat(depth);
    return switch (obj) {
      case OperationRead.Query<?> q -> indent + "Query: " + truncate(q.query().render());
      case OperationRead.Mapped<?, ?> m -> indent + "Mapped:\n" + describeOp(m.source(), depth + 1);
      case OperationRead.Pure<?> p -> indent + "Pure(" + p.value() + ")";
      case OperationRead.Combine<?, ?> w ->
          indent
              + "Combine:\n"
              + describeOp(w.first(), depth + 1)
              + "\n"
              + describeOp(w.second(), depth + 1);
      case OperationRead.IfEmpty<?> ie ->
          indent
              + "IfEmpty:\n"
              + describeOp(ie.check(), depth + 1)
              + "\n"
              + describeOp(ie.fallback(), depth + 1);
      case OperationRead.Then<?, ?> t ->
          indent
              + "Then:\n"
              + describeOp(t.source(), depth + 1)
              + "\n"
              + indent
              + "  -> "
              + describeTemplate(t.continuation());
      case OperationRead.Configured<?> c ->
          indent
              + "Configured"
              + (c.name().map(n -> "[" + n + "]").orElse(""))
              + ":\n"
              + describeOp(c.inner(), depth + 1);
      case OperationRead.Streaming<?> s -> indent + "Streaming: " + truncate(s.query().render());
      case Operation.Update u -> indent + "Update: " + truncate(u.query().render());
      case Operation.Execute e -> indent + "Execute: " + truncate(e.query().render());
      case Operation.UpdateReturning<?> u ->
          indent + "UpdateReturning: " + truncate(u.query().render());
      case Operation.UpdateReturningGeneratedKeys<?> u ->
          indent + "UpdateReturningGeneratedKeys: " + truncate(u.query().render());
      case Operation.UpdateMany<?> u -> indent + "UpdateMany: " + truncate(u.query().render());
      case Operation.UpdateManyReturning<?> u ->
          indent + "UpdateManyReturning: " + truncate(u.query().render());
      case Operation.UpdateReturningEach<?> u ->
          indent + "UpdateReturningEach: " + truncate(u.query().render());
      case Operation.UpdateManyTemplate<?> u ->
          indent + "UpdateManyTemplate: " + truncate(u.fragment().render());
      case Operation.StreamingCopy<?> s -> indent + "StreamingCopy: " + truncate(s.copyCommand());
      case Operation.Mapped<?, ?> m -> indent + "Mapped:\n" + describeOp(m.source(), depth + 1);
      case Operation.Then<?, ?> t ->
          indent
              + "Then:\n"
              + describeOp(t.source(), depth + 1)
              + "\n"
              + indent
              + "  -> "
              + describeTemplate(t.continuation());
      case Operation.Combine<?, ?> w ->
          indent
              + "Combine:\n"
              + describeOp(w.first(), depth + 1)
              + "\n"
              + describeOp(w.second(), depth + 1);
      case Operation.IfEmpty<?> ie ->
          indent
              + "IfEmpty:\n"
              + describeOp(ie.check(), depth + 1)
              + "\n"
              + describeOp(ie.fallback(), depth + 1);
      case Operation.Configured<?> c ->
          indent
              + "Configured"
              + (c.name().map(n -> "[" + n + "]").orElse(""))
              + ":\n"
              + describeOp(c.inner(), depth + 1);
      case Procedure.ProcedureCall<?> p -> indent + "ProcedureCall: " + p.name();
      case Procedure.FunctionCall<?> f -> indent + "FunctionCall: " + f.name();
      case RowTemplate<?, ?> rt -> indent + "RowTemplate: " + truncate(rt.fragment().render());
      case Template<?, ?> t -> indent + describeTemplate(t);
      case Analyzable.Named(var name, var inner) ->
          indent + "Named[" + name + "]:\n" + describeOp(inner, depth + 1);
      default -> indent + obj.getClass().getSimpleName();
    };
  }

  private static String describeTemplate(Object template) {
    if (template instanceof Template<?, ?> t) {
      return "Template: " + truncate(t.fragment().render());
    }
    return "Template(" + template.getClass().getSimpleName() + ")";
  }

  private static String truncate(String sql) {
    var s = sql.replaceAll("\\s+", " ").trim();
    return s.length() > 80 ? s.substring(0, 77) + "..." : s;
  }

  // --- Field scanning ---

  private static void collectAnalyzables(
      Object instance,
      Class<?> clazz,
      String simpleName,
      Set<String> fieldNames,
      List<Result> result) {
    boolean isScalaObject = clazz.getName().endsWith("$");

    for (var field : declaredFieldsOrEmpty(clazz)) {
      boolean isStatic = Modifier.isStatic(field.getModifiers());
      if (isStatic && !isScalaObject) continue;
      if ("MODULE$".equals(field.getName())) continue;

      field.setAccessible(true);
      Object value;
      try {
        value = isStatic ? field.get(null) : field.get(instance);
      } catch (IllegalAccessException e) {
        continue;
      } catch (LinkageError le) {
        // Class init triggered by field.get() may fail if the initializer references
        // classes not on this classpath (e.g. private Kotlin ThreadLocal state). Skip.
        continue;
      }
      if (value == null) continue;

      if (value instanceof Analyzable a) {
        fieldNames.add(field.getName());
        result.add(new Result(simpleName, field.getName(), a));
      } else {
        var analyzable = extractAnalyzableViaReflection(value);
        if (analyzable != null) {
          fieldNames.add(field.getName());
          result.add(new Result(simpleName, field.getName(), analyzable));
        }
      }
    }
  }

  // --- Static field and method scanning (Kotlin file-facade classes) ---

  private static void collectStaticAnalyzables(Class<?> clazz, List<Result> result) {
    var fieldNames = collectStaticFieldAnalyzables(clazz, result);
    collectStaticMethodAnalyzables(clazz, fieldNames, result);
  }

  /**
   * Heuristic: a Scala {@code object X} in package {@code p} compiles to both {@code p.X} (a facade
   * class with static forwarders) and {@code p.X$} (the actual singleton, with a {@code public
   * static final X$ MODULE$} field). When we're scanning the facade {@code p.X}, we want to skip
   * the static-method sweep because the MODULE$ instance scan on {@code p.X$} already covers every
   * val/def via the Scala singleton instance.
   */
  private static boolean isScalaObject(Class<?> clazz) {
    try {
      var companion = Class.forName(clazz.getName() + "$");
      var f = companion.getDeclaredField("MODULE$");
      return Modifier.isStatic(f.getModifiers()) && f.getType() == companion;
    } catch (ClassNotFoundException | NoSuchFieldException ignored) {
      return false;
    }
  }

  /**
   * Heuristic: a Kotlin {@code object X} compiles to a class with a {@code public static final X
   * INSTANCE} field. Used to skip the static-field sweep on Kotlin objects, since their {@code
   * val}s end up as private static fields PLUS a public instance getter; the instance-path getter
   * scan already covers them, so sweeping statics would double-count.
   */
  private static boolean isKotlinObject(Class<?> clazz) {
    try {
      var f = clazz.getDeclaredField("INSTANCE");
      return Modifier.isStatic(f.getModifiers()) && f.getType() == clazz;
    } catch (NoSuchFieldException e) {
      return false;
    }
  }

  /**
   * Scan static fields for Analyzable values and return the set of field names collected.
   *
   * <p>Used both by the static-only path (classes we couldn't instantiate) and by the instance
   * path, so that Java classes like {@code public final class UserRepo { static final
   * Operation<...> q = ...; }} still have their static fields discovered.
   */
  private static Set<String> collectStaticFieldAnalyzables(Class<?> clazz, List<Result> result) {
    var simpleName = clazz.getSimpleName();
    if (simpleName.endsWith("Kt")) {
      simpleName = simpleName.substring(0, simpleName.length() - 2);
    }

    var fieldNames = new HashSet<String>();

    for (var field : declaredFieldsOrEmpty(clazz)) {
      if (!Modifier.isStatic(field.getModifiers())) continue;

      field.setAccessible(true);
      Object value;
      try {
        value = field.get(null);
      } catch (IllegalAccessException e) {
        continue;
      } catch (LinkageError le) {
        // Class init triggered by field.get() may fail. Skip this class quietly.
        continue;
      }
      if (value == null) continue;

      if (value instanceof Analyzable a) {
        fieldNames.add(field.getName());
        result.add(new Result(simpleName, field.getName(), a));
      } else {
        var analyzable = extractAnalyzableViaReflection(value);
        if (analyzable != null) {
          fieldNames.add(field.getName());
          result.add(new Result(simpleName, field.getName(), analyzable));
        }
      }
    }
    return fieldNames;
  }

  /** Scan public static methods for Analyzable return values (Kotlin top-level functions). */
  private static void collectStaticMethodAnalyzables(
      Class<?> clazz, Set<String> fieldNames, List<Result> result) {
    var simpleName = clazz.getSimpleName();
    if (simpleName.endsWith("Kt")) {
      simpleName = simpleName.substring(0, simpleName.length() - 2);
    }
    for (var method : declaredMethodsOrEmpty(clazz)) {
      try {
        if (!isStaticAnalyzableMethod(method)) continue;
      } catch (LinkageError ignored) {
        continue;
      }
      if (method.getParameterCount() == 0 && isGetterForField(method.getName(), fieldNames))
        continue;

      Object[] args;
      if (method.getParameterCount() == 0) {
        args = new Object[0];
      } else {
        try {
          args = constructDummyArgs(method);
        } catch (Exception e) {
          continue;
        }
      }

      try {
        method.setAccessible(true);
        var value = method.invoke(null, args);
        var analyzable = toAnalyzable(value);
        if (analyzable != null) {
          result.add(new Result(simpleName, reportMemberName(method.getName(), clazz), analyzable));
        }
      } catch (Exception ignored) {
      }
    }
  }

  /** Getting declared methods can throw on classpath issues; fall back to an empty array. */
  private static Method[] declaredMethodsOrEmpty(Class<?> clazz) {
    try {
      return clazz.getDeclaredMethods();
    } catch (LinkageError ignored) {
      return new Method[0];
    }
  }

  /** Getting declared fields can throw on classpath issues (missing field types); fall back. */
  private static java.lang.reflect.Field[] declaredFieldsOrEmpty(Class<?> clazz) {
    try {
      return clazz.getDeclaredFields();
    } catch (LinkageError ignored) {
      return new java.lang.reflect.Field[0];
    }
  }

  /**
   * Report-friendly name for a discovered member. Kotlin `val` and `var` properties compile to JVM
   * getter methods (`val foo` → `getFoo()`). When the declaring class carries the {@code
   * kotlin.Metadata} annotation, strip the `get` prefix and lower-case the first letter, so reports
   * say "UserRepo.findById" instead of "UserRepo.getFindById".
   */
  private static String reportMemberName(String methodName, Class<?> declaringClass) {
    if (!isKotlinClass(declaringClass)) return methodName;
    if (methodName.length() > 3
        && methodName.startsWith("get")
        && Character.isUpperCase(methodName.charAt(3))) {
      return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
    }
    return methodName;
  }

  private static boolean isKotlinClass(Class<?> clazz) {
    try {
      for (var annotation : clazz.getAnnotations()) {
        if ("kotlin.Metadata".equals(annotation.annotationType().getName())) return true;
      }
    } catch (Throwable ignored) {
    }
    return false;
  }

  private static boolean isStaticAnalyzableMethod(Method method) {
    int mods = method.getModifiers();
    // Visibility is not a criterion — the scanner is a test-scope tool and unlocks
    // private/package-private members via setAccessible(true) before invocation.
    if (!Modifier.isStatic(mods)) return false;
    if (method.isSynthetic() || method.isBridge()) return false;
    try {
      if (method.getParameterCount() > 10) return false;

      var returnType = method.getReturnType();
      if (Analyzable.class.isAssignableFrom(returnType)) return true;

      try {
        returnType.getMethod("getAnalyzable");
        return true;
      } catch (NoSuchMethodException ignored) {
      }

      try {
        returnType.getMethod("analyzable");
        return true;
      } catch (NoSuchMethodException ignored) {
      }

      return false;
    } catch (LinkageError ignored) {
      return false;
    }
  }

  // --- Method scanning ---

  private static void collectMethodAnalyzables(
      Object instance,
      Class<?> clazz,
      String simpleName,
      Set<String> fieldNames,
      List<ScanDirective> directives,
      List<String> errors,
      List<Result> result) {
    var consumed = new HashSet<ScanDirective>();

    for (var method : declaredMethodsOrEmpty(clazz)) {
      try {
        if (!isAnalyzableMethod(method)) continue;
      } catch (LinkageError ignored) {
        // Private Kotlin methods may reference classes not on this project's classpath.
        continue;
      }

      var className = clazz.getName();
      var methodName = method.getName();

      if (method.getParameterCount() == 0 && isGetterForField(methodName, fieldNames)) continue;

      if (isSkipped(className, methodName, directives, consumed)) continue;

      var manuals = findManuals(className, methodName, directives, consumed);
      if (!manuals.isEmpty()) {
        for (var manual : manuals) {
          result.add(
              new Result(
                  simpleName, methodName + "[" + manual.variantName() + "]", manual.analyzable()));
        }
        continue;
      }

      if (method.getParameterCount() == 0) {
        try {
          method.setAccessible(true);
          var value = method.invoke(instance);
          var analyzable = toAnalyzable(value);
          if (analyzable != null) {
            result.add(new Result(simpleName, reportMemberName(methodName, clazz), analyzable));
          }
        } catch (LinkageError ignored) {
          // Skip — class loading failed, nothing to report.
        } catch (Exception e) {
          errors.add(
              formatMethodError(
                  simpleName,
                  methodName,
                  method,
                  "Failed to invoke no-arg method: " + e.getMessage()));
        }
        continue;
      }

      Object[] args;
      try {
        args = constructDummyArgs(method);
      } catch (LinkageError ignored) {
        continue; // parameter types unresolvable — skip quietly
      } catch (Exception e) {
        errors.add(
            formatMethodError(
                simpleName,
                methodName,
                method,
                "Cannot construct dummy args for parameter types: " + e.getMessage()));
        continue;
      }

      try {
        method.setAccessible(true);
        var value = method.invoke(instance, args);
        var analyzable = toAnalyzable(value);
        if (analyzable != null) {
          result.add(new Result(simpleName, reportMemberName(methodName, clazz), analyzable));
        }
      } catch (LinkageError ignored) {
        // Skip — class loading failed during invocation.
      } catch (Exception e) {
        errors.add(
            formatMethodError(
                simpleName,
                methodName,
                method,
                "Failed to invoke with dummy args: " + e.getMessage()));
      }
    }
  }

  private static String formatMethodError(
      String simpleName, String methodName, Method method, String reason) {
    var paramTypes = method.getParameterTypes();
    var params = new StringBuilder();
    for (int i = 0; i < paramTypes.length; i++) {
      if (i > 0) params.append(", ");
      params.append(paramTypes[i].getSimpleName());
    }
    return simpleName
        + "."
        + methodName
        + "("
        + params
        + ")\n"
        + "    "
        + reason
        + "\n"
        + "    \u2192 ScanDirective.skip("
        + simpleName
        + ".class, \""
        + methodName
        + "\")";
  }

  private static void throwScanErrors(List<String> errors) {
    var sb = new StringBuilder();
    sb.append("AnalyzableScanner: ")
        .append(errors.size())
        .append(" method(s) could not be auto-invoked.\n\n");
    for (int i = 0; i < errors.size(); i++) {
      sb.append(i + 1).append(". ").append(errors.get(i));
      if (i < errors.size() - 1) sb.append("\n\n");
    }
    sb.append("\n\nAdd skip() or manual() directives to your scan() call to resolve these.");
    throw new IllegalStateException(sb.toString());
  }

  private static boolean isAnalyzableMethod(Method method) {
    int mods = method.getModifiers();
    // Visibility is not a criterion — the scanner is a test-scope tool and unlocks
    // private/package-private members via setAccessible(true) before invocation.
    if (Modifier.isStatic(mods)) return false;
    if (method.isSynthetic() || method.isBridge()) return false;
    if (method.getDeclaringClass() == Object.class) return false;
    // Private / package-private Kotlin methods may reference classes (e.g. kotlin.Unit) not
    // present on the scanning classpath — introspecting their signature throws
    // NoClassDefFoundError. Treat as "not analyzable" and continue.
    try {
      if (method.getParameterCount() > 10) return false;

      var returnType = method.getReturnType();
      if (Analyzable.class.isAssignableFrom(returnType)) return true;

      try {
        returnType.getMethod("getAnalyzable");
        return true;
      } catch (NoSuchMethodException ignored) {
      }

      try {
        returnType.getMethod("analyzable");
        return true;
      } catch (NoSuchMethodException ignored) {
      }

      return false;
    } catch (LinkageError ignored) {
      return false;
    }
  }

  private static boolean isSkipped(
      String className,
      String methodName,
      List<ScanDirective> directives,
      Set<ScanDirective> consumed) {
    for (var d : directives) {
      if (d instanceof ScanDirective.SkipMethod s
          && matchesMethod(s.declaringClass(), s.methodName(), className, methodName)) {
        consumed.add(d);
        return true;
      }
    }
    return false;
  }

  private static List<ScanDirective.ManualMethod> findManuals(
      String className,
      String methodName,
      List<ScanDirective> directives,
      Set<ScanDirective> consumed) {
    var manuals = new ArrayList<ScanDirective.ManualMethod>();
    for (var d : directives) {
      if (d instanceof ScanDirective.ManualMethod m
          && matchesMethod(m.declaringClass(), m.methodName(), className, methodName)) {
        manuals.add(m);
        consumed.add(d);
      }
    }
    return manuals;
  }

  private static boolean isGetterForField(String methodName, Set<String> fieldNames) {
    if (fieldNames.contains(methodName)) return true;
    if (methodName.startsWith("get") && methodName.length() > 3) {
      var fieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
      return fieldNames.contains(fieldName);
    }
    return false;
  }

  private static boolean matchesMethod(
      String directiveClass, String directiveMethod, String actualClass, String actualMethod) {
    if (!directiveMethod.equals(actualMethod)) return false;
    return actualClass.equals(directiveClass)
        || actualClass.endsWith(
            "$" + directiveClass.substring(directiveClass.lastIndexOf('.') + 1));
  }

  private static List<ScanDirective> findDirectivesForInstance(
      Object instance, List<ScanDirective> allDirectives) {
    var result = new ArrayList<ScanDirective>();
    for (var d : allDirectives) {
      if (d instanceof ScanDirective.InstanceScan is && is.instance() == instance) {
        result.addAll(is.overrides());
      } else if (!(d instanceof ScanDirective.InstanceScan)) {
        result.add(d);
      }
    }
    return result;
  }

  // --- Analyzable extraction ---

  static Analyzable toAnalyzable(Object value) {
    if (value == null) return null;
    if (value instanceof Analyzable a) return a;
    return extractAnalyzableViaReflection(value);
  }

  static Analyzable extractAnalyzableViaReflection(Object value) {
    try {
      var method = value.getClass().getMethod("getAnalyzable");
      if (Analyzable.class.isAssignableFrom(method.getReturnType())) {
        return (Analyzable) method.invoke(value);
      }
    } catch (Exception ignored) {
    }

    try {
      var method = value.getClass().getMethod("analyzable");
      if (Analyzable.class.isAssignableFrom(method.getReturnType())) {
        return (Analyzable) method.invoke(value);
      }
    } catch (Exception ignored) {
    }

    return null;
  }

  // --- Dummy argument construction ---

  private static Object[] constructDummyArgs(Method method) {
    var params = method.getParameterTypes();
    var args = new Object[params.length];
    for (int i = 0; i < params.length; i++) {
      args[i] = constructDummy(params[i], new HashSet<>());
    }
    return args;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  static Object constructDummy(Class<?> type, Set<Class<?>> visited) {
    if (type == boolean.class || type == Boolean.class) return false;
    if (type == byte.class || type == Byte.class) return (byte) 0;
    if (type == short.class || type == Short.class) return (short) 0;
    if (type == int.class || type == Integer.class) return 0;
    if (type == long.class || type == Long.class) return 0L;
    if (type == float.class || type == Float.class) return 0.0f;
    if (type == double.class || type == Double.class) return 0.0;
    if (type == char.class || type == Character.class) return '\0';
    if (type == String.class) return "";
    if (type == BigDecimal.class) return BigDecimal.ZERO;
    if (type == BigInteger.class) return BigInteger.ZERO;
    if (type == UUID.class) return new UUID(0, 0);

    if (type == LocalDate.class) return LocalDate.of(2000, 1, 1);
    if (type == LocalTime.class) return LocalTime.MIDNIGHT;
    if (type == LocalDateTime.class) return LocalDateTime.of(2000, 1, 1, 0, 0);
    if (type == OffsetDateTime.class)
      return OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    if (type == OffsetTime.class) return OffsetTime.of(LocalTime.MIDNIGHT, ZoneOffset.UTC);
    if (type == ZonedDateTime.class)
      return ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    if (type == Instant.class) return Instant.EPOCH;
    if (type == Duration.class) return Duration.ZERO;
    if (type == java.time.Period.class) return java.time.Period.ZERO;

    if (type == Optional.class) return Optional.empty();
    if (type == List.class) return List.of();
    if (type == Set.class) return Set.of();
    if (type == Map.class) return Map.of();
    if (type == java.util.Collection.class) return List.of();

    // Scala types — resolved by name to avoid hard dependency on Scala runtime
    var typeName = type.getName();
    if (typeName.equals("scala.Option")) {
      try {
        return Class.forName("scala.None$").getField("MODULE$").get(null);
      } catch (Exception e) {
        throw new IllegalArgumentException(
            "Scala Option type found but scala.None$ not on classpath", e);
      }
    }
    if (typeName.equals("scala.collection.immutable.List")
        || typeName.equals("scala.collection.immutable.Seq")
        || typeName.equals("scala.collection.Seq")) {
      try {
        return Class.forName("scala.collection.immutable.Nil$").getField("MODULE$").get(null);
      } catch (Exception e) {
        throw new IllegalArgumentException(
            "Scala List type found but scala.Nil$ not on classpath", e);
      }
    }
    if (typeName.equals("scala.collection.immutable.Map")
        || typeName.equals("scala.collection.Map")) {
      try {
        var mapModule = Class.forName("scala.collection.immutable.Map$");
        var instance = mapModule.getField("MODULE$").get(null);
        return mapModule.getMethod("empty").invoke(instance);
      } catch (Exception e) {
        throw new IllegalArgumentException(
            "Scala Map type found but cannot construct empty Map", e);
      }
    }

    if (type.isArray()) return Array.newInstance(type.getComponentType(), 0);

    if (type.isEnum()) {
      var constants = type.getEnumConstants();
      if (constants.length > 0) return constants[0];
      throw new IllegalArgumentException("Empty enum: " + type.getName());
    }

    if (!visited.add(type)) {
      throw new IllegalArgumentException("Recursive type: " + type.getName());
    }

    try {
      if (type.isRecord()) {
        return constructDummyRecord(type, visited);
      }
      return constructDummyViaConstructor(type, visited);
    } finally {
      visited.remove(type);
    }
  }

  private static Object constructDummyRecord(Class<?> type, Set<Class<?>> visited) {
    var components = type.getRecordComponents();
    var paramTypes = new Class<?>[components.length];
    var args = new Object[components.length];
    for (int i = 0; i < components.length; i++) {
      paramTypes[i] = components[i].getType();
      args[i] = constructDummy(paramTypes[i], visited);
    }
    try {
      var ctor = type.getDeclaredConstructor(paramTypes);
      ctor.setAccessible(true);
      return ctor.newInstance(args);
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot construct record: " + type.getName(), e);
    }
  }

  private static Object constructDummyViaConstructor(Class<?> type, Set<Class<?>> visited) {
    if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
      throw new IllegalArgumentException("Cannot construct abstract type: " + type.getName());
    }

    var constructors = type.getDeclaredConstructors();
    List<Constructor<?>> sorted = new ArrayList<>(List.of(constructors));
    sorted.sort(Comparator.comparingInt(Constructor::getParameterCount));

    for (var ctor : sorted) {
      try {
        var paramTypes = ctor.getParameterTypes();
        var args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
          args[i] = constructDummy(paramTypes[i], visited);
        }
        ctor.setAccessible(true);
        return ctor.newInstance(args);
      } catch (Exception ignored) {
      }
    }

    throw new IllegalArgumentException("No suitable constructor for: " + type.getName());
  }

  // --- Class instantiation ---

  private static Object instantiate(Class<?> clazz, Transactor transactor) {
    try {
      return instantiate0(clazz, transactor);
    } catch (Throwable ignored) {
      return null;
    }
  }

  private static Object instantiate0(Class<?> clazz, Transactor transactor)
      throws ReflectiveOperationException {
    try {
      var field = clazz.getDeclaredField("INSTANCE");
      if (Modifier.isStatic(field.getModifiers())) {
        field.setAccessible(true);
        return field.get(null);
      }
    } catch (NoSuchFieldException ignored) {
    }

    try {
      var companionClass = Class.forName(clazz.getName() + "$");
      var field = companionClass.getDeclaredField("MODULE$");
      if (Modifier.isStatic(field.getModifiers())) {
        field.setAccessible(true);
        return field.get(null);
      }
    } catch (ClassNotFoundException | NoSuchFieldException ignored) {
    }

    try {
      var ctor = clazz.getDeclaredConstructor();
      ctor.setAccessible(true);
      return ctor.newInstance();
    } catch (NoSuchMethodException ignored) {
    }

    if (transactor != null) {
      try {
        var ctor = clazz.getDeclaredConstructor(Transactor.class);
        ctor.setAccessible(true);
        return ctor.newInstance(transactor);
      } catch (NoSuchMethodException ignored) {
      }
    }

    return null;
  }

  // --- Class discovery ---

  private static List<Class<?>> findClasses(String packageName) {
    var path = packageName.replace('.', '/');
    var classes = new ArrayList<Class<?>>();

    try {
      var classLoader = Thread.currentThread().getContextClassLoader();
      Enumeration<URL> resources = classLoader.getResources(path);

      while (resources.hasMoreElements()) {
        var resource = resources.nextElement();
        if ("file".equals(resource.getProtocol())) {
          var directory = new File(resource.toURI());
          findClassesInDirectory(directory, packageName, classes);
        } else if ("jar".equals(resource.getProtocol())) {
          findClassesInJar(resource, path, packageName, classes);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to scan package: " + packageName, e);
    }

    classes.sort(Comparator.comparing(Class::getName));
    return classes;
  }

  private static void findClassesInJar(
      URL resource, String path, String packageName, List<Class<?>> classes) {
    try {
      var connection = (JarURLConnection) resource.openConnection();
      var jarFile = connection.getJarFile();
      var entries = jarFile.entries();
      var prefix = path + "/";

      while (entries.hasMoreElements()) {
        var entry = entries.nextElement();
        var entryName = entry.getName();

        if (!entryName.startsWith(prefix) || !entryName.endsWith(".class")) continue;

        var relative = entryName.substring(prefix.length());
        if (relative.contains("$")) continue;

        var className = entryName.substring(0, entryName.length() - 6).replace('/', '.');

        try {
          classes.add(
              Class.forName(className, false, Thread.currentThread().getContextClassLoader()));
        } catch (ClassNotFoundException ignored) {
        }
      }
    } catch (IOException ignored) {
    }
  }

  private static void findClassesInDirectory(
      File directory, String packageName, List<Class<?>> classes) {
    if (!directory.exists()) return;

    var files = directory.listFiles();
    if (files == null) return;

    for (var file : files) {
      if (file.isDirectory()) {
        findClassesInDirectory(file, packageName + "." + file.getName(), classes);
      } else if (file.getName().endsWith(".class")) {
        var className = file.getName().substring(0, file.getName().length() - 6);
        if (className.contains("$")) continue;

        try {
          classes.add(
              Class.forName(
                  packageName + "." + className,
                  false,
                  Thread.currentThread().getContextClassLoader()));
        } catch (ClassNotFoundException ignored) {
        }
      }
    }
  }

  private static String stripSuffix(String name) {
    return name.endsWith("$") ? name.substring(0, name.length() - 1) : name;
  }
}
