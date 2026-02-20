package dev.typr.foundations;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

/**
 * Discovers all {@link Analyzable} instances in a package via reflection.
 *
 * <p>Scans compiled classes in the given package, instantiates them, and collects all fields
 * that are (or wrap) {@link Analyzable}. Handles Java classes, Kotlin objects ({@code INSTANCE}),
 * and Scala objects ({@code MODULE$}).
 */
public final class AnalyzableScanner {

    public record Result(String className, String fieldName, Analyzable analyzable) {
        @Override
        public String toString() {
            return className + "." + fieldName;
        }
    }

    private AnalyzableScanner() {}

    public static List<Analyzable> scan(String packageName) {
        return scan(packageName, null);
    }

    public static List<Analyzable> scan(String packageName, Transactor transactor) {
        return scanDetailed(packageName, transactor).stream()
            .map(r -> (Analyzable) new Analyzable.Named(r.className() + "." + r.fieldName(), r.analyzable()))
            .toList();
    }

    public static List<Result> scanDetailed(String packageName) {
        return scanDetailed(packageName, null);
    }

    public static List<Result> scanDetailed(String packageName, Transactor transactor) {
        var classes = findClasses(packageName);
        var result = new ArrayList<Result>();

        for (var clazz : classes) {
            Object instance = instantiate(clazz, transactor);
            if (instance == null) continue;

            // Use the instance's actual runtime class for field scanning.
            // For Scala objects, clazz is the mirror class (Foo) but the instance
            // is from Foo$ which has the actual fields.
            collectAnalyzables(instance, instance.getClass(), clazz.getSimpleName(), result);
        }

        result.sort(Comparator.comparing(Result::toString));
        return result;
    }

    /**
     * Describes an {@link Analyzable} showing its operation structure and SQL fragments.
     */
    public static String describe(Analyzable analyzable) {
        return describeOp(analyzable, 0);
    }

    private static String describeOp(Object obj, int depth) {
        String indent = "  ".repeat(depth);
        return switch (obj) {
            case Operation.Query<?> q ->
                indent + "Query: " + truncate(q.query().render());
            case Operation.Update u ->
                indent + "Update: " + truncate(u.query().render());
            case Operation.UpdateReturning<?> u ->
                indent + "UpdateReturning: " + truncate(u.query().render());
            case Operation.UpdateReturningGeneratedKeys<?> u ->
                indent + "UpdateReturningGeneratedKeys: " + truncate(u.query().render());
            case Operation.UpdateMany<?> u ->
                indent + "UpdateMany: " + truncate(u.query().render());
            case Operation.UpdateManyReturning<?> u ->
                indent + "UpdateManyReturning: " + truncate(u.query().render());
            case Operation.UpdateReturningEach<?> u ->
                indent + "UpdateReturningEach: " + truncate(u.query().render());
            case Operation.UpdateManyTemplate<?> u ->
                indent + "UpdateManyTemplate: " + truncate(u.fragment().render());
            case Operation.StreamingCopy<?> s ->
                indent + "StreamingCopy: " + truncate(s.copyCommand());
            case Operation.Mapped<?,?> m ->
                indent + "Mapped:\n" + describeOp(m.source(), depth + 1);
            case Operation.Pure<?> p ->
                indent + "Pure(" + p.value() + ")";
            case Operation.Combine<?,?> w ->
                indent + "Combine:\n" + describeOp(w.first(), depth + 1) + "\n" + describeOp(w.second(), depth + 1);
            case Operation.IfEmpty<?> ie ->
                indent + "IfEmpty:\n" + describeOp(ie.check(), depth + 1) + "\n" + describeOp(ie.fallback(), depth + 1);
            case Operation.Then<?,?,?> t ->
                indent + "Then:\n" + describeOp(t.source(), depth + 1) + "\n" + indent + "  -> " + describeTemplate(t.continuation());
            case Operation.Configured<?> c ->
                indent + "Configured" + (c.name() != null ? "[" + c.name() + "]" : "") + ":\n" + describeOp(c.inner(), depth + 1);
            case Procedure.ProcedureCall<?> p ->
                indent + "ProcedureCall: " + p.name();
            case Procedure.FunctionCall<?> f ->
                indent + "FunctionCall: " + f.name();
            case RowTemplate<?,?> rt ->
                indent + "RowTemplate: " + truncate(rt.fragment().render());
            case Template<?,?> t ->
                indent + describeTemplate(t);
            case Analyzable.Named(var name, var inner) ->
                indent + "Named[" + name + "]:\n" + describeOp(inner, depth + 1);
            default ->
                indent + obj.getClass().getSimpleName();
        };
    }

    private static String describeTemplate(Object template) {
        if (template instanceof Template<?,?> t) {
            return "Template: " + truncate(t.fragment().render());
        }
        return "Template(" + template.getClass().getSimpleName() + ")";
    }

    private static String truncate(String sql) {
        var s = sql.replaceAll("\\s+", " ").trim();
        return s.length() > 80 ? s.substring(0, 77) + "..." : s;
    }

    private static void collectAnalyzables(Object instance, Class<?> clazz, String simpleName, List<Result> result) {
        // Scala 3 objects compile vals as static fields on the $ class
        boolean isScalaObject = clazz.getName().endsWith("$");

        for (var field : clazz.getDeclaredFields()) {
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            if (isStatic && !isScalaObject) continue;
            // Skip MODULE$ itself
            if ("MODULE$".equals(field.getName())) continue;

            field.setAccessible(true);
            Object value;
            try {
                value = isStatic ? field.get(null) : field.get(instance);
            } catch (IllegalAccessException e) {
                continue;
            }
            if (value == null) continue;

            if (value instanceof Analyzable a) {
                result.add(new Result(simpleName, field.getName(), a));
            } else {
                // Kotlin/Scala wrappers: try getAnalyzable() or analyzable() via reflection
                var analyzable = extractAnalyzableViaReflection(value);
                if (analyzable != null) {
                    result.add(new Result(simpleName, field.getName(), analyzable));
                }
            }
        }
    }

    private static Analyzable extractAnalyzableViaReflection(Object value) {
        // Kotlin wrapper: val analyzable generates getAnalyzable()
        try {
            var method = value.getClass().getMethod("getAnalyzable");
            if (Analyzable.class.isAssignableFrom(method.getReturnType())) {
                return (Analyzable) method.invoke(value);
            }
        } catch (Exception ignored) {}

        // Scala wrapper: def analyzable generates analyzable()
        try {
            var method = value.getClass().getMethod("analyzable");
            if (Analyzable.class.isAssignableFrom(method.getReturnType())) {
                return (Analyzable) method.invoke(value);
            }
        } catch (Exception ignored) {}

        return null;
    }

    private static Object instantiate(Class<?> clazz, Transactor transactor) {
        try {
            return instantiate0(clazz, transactor);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Object instantiate0(Class<?> clazz, Transactor transactor)
        throws ReflectiveOperationException {
        // Kotlin object — static INSTANCE field
        try {
            var field = clazz.getDeclaredField("INSTANCE");
            if (Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                return field.get(null);
            }
        } catch (NoSuchFieldException ignored) {}

        // Scala object — ClassName$ class with MODULE$ field
        try {
            var companionClass = Class.forName(clazz.getName() + "$");
            var field = companionClass.getDeclaredField("MODULE$");
            if (Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                return field.get(null);
            }
        } catch (ClassNotFoundException | NoSuchFieldException ignored) {}

        // No-arg constructor
        try {
            var ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (NoSuchMethodException ignored) {}

        // Single Transactor constructor
        if (transactor != null) {
            try {
                var ctor = clazz.getDeclaredConstructor(Transactor.class);
                ctor.setAccessible(true);
                return ctor.newInstance(transactor);
            } catch (NoSuchMethodException ignored) {}
        }

        return null;
    }

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

    private static void findClassesInJar(URL resource, String path, String packageName, List<Class<?>> classes) {
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
                // Skip inner/synthetic classes
                if (relative.contains("$")) continue;

                var className = entryName
                    .substring(0, entryName.length() - 6)
                    .replace('/', '.');

                try {
                    classes.add(Class.forName(className, false, Thread.currentThread().getContextClassLoader()));
                } catch (ClassNotFoundException ignored) {}
            }
        } catch (IOException ignored) {}
    }

    private static void findClassesInDirectory(File directory, String packageName, List<Class<?>> classes) {
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
                    classes.add(Class.forName(packageName + "." + className, false, Thread.currentThread().getContextClassLoader()));
                } catch (ClassNotFoundException ignored) {}
            }
        }
    }
}
