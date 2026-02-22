package dev.typr.foundations;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public sealed interface ScanDirective {

    record SkipMethod(String declaringClass, String methodName) implements ScanDirective {}
    record ManualMethod(String declaringClass, String methodName,
                        String variantName, Analyzable analyzable) implements ScanDirective {}
    record InstanceScan(Object instance, List<ScanDirective> overrides) implements ScanDirective {}

    @FunctionalInterface interface Ref0<R> extends Serializable { R apply(); }
    @FunctionalInterface interface Ref1<A, R> extends Serializable { R apply(A a); }
    @FunctionalInterface interface Ref2<A, B, R> extends Serializable { R apply(A a, B b); }
    @FunctionalInterface interface Ref3<A, B, C, R> extends Serializable { R apply(A a, B b, C c); }

    // --- skip() factories ---

    static ScanDirective skip(Ref0<?> ref) { return skipFromLambda(ref); }
    static <A> ScanDirective skip(Ref1<A, ?> ref) { return skipFromLambda(ref); }
    static <A, B> ScanDirective skip(Ref2<A, B, ?> ref) { return skipFromLambda(ref); }
    static <A, B, C> ScanDirective skip(Ref3<A, B, C, ?> ref) { return skipFromLambda(ref); }

    static ScanDirective skip(Class<?> clazz, String methodName) {
        return new SkipMethod(clazz.getName(), methodName);
    }

    // --- manual() factories ---

    static <R> ScanDirective manual(Ref0<R> ref, String name) {
        var id = extractMethodIdentity(ref);
        var result = ref.apply();
        return new ManualMethod(id.declaringClass(), id.methodName(), name, toAnalyzable(result));
    }

    static <A, R> ScanDirective manual(Ref1<A, R> ref, String name, A a) {
        var id = extractMethodIdentity(ref);
        var result = ref.apply(a);
        return new ManualMethod(id.declaringClass(), id.methodName(), name, toAnalyzable(result));
    }

    static <A, B, R> ScanDirective manual(Ref2<A, B, R> ref, String name, A a, B b) {
        var id = extractMethodIdentity(ref);
        var result = ref.apply(a, b);
        return new ManualMethod(id.declaringClass(), id.methodName(), name, toAnalyzable(result));
    }

    static <A, B, C, R> ScanDirective manual(Ref3<A, B, C, R> ref, String name, A a, B b, C c) {
        var id = extractMethodIdentity(ref);
        var result = ref.apply(a, b, c);
        return new ManualMethod(id.declaringClass(), id.methodName(), name, toAnalyzable(result));
    }

    static ScanDirective manual(Class<?> clazz, String methodName, String variantName, Analyzable result) {
        return new ManualMethod(clazz.getName(), methodName, variantName, result);
    }

    // --- instance() factories ---

    static ScanDirective instance(Object obj) {
        return new InstanceScan(obj, List.of());
    }

    static <T> ScanDirective instance(T obj, BiConsumer<T, InstanceConfig<T>> config) {
        var cfg = new InstanceConfig<T>(obj);
        config.accept(obj, cfg);
        return new InstanceScan(obj, cfg.directives());
    }

    // --- InstanceConfig ---

    final class InstanceConfig<T> {
        private final T instance;
        private final List<ScanDirective> directives = new ArrayList<>();

        InstanceConfig(T instance) { this.instance = instance; }

        List<ScanDirective> directives() { return directives; }

        public void skip(Ref0<?> ref) { directives.add(skipFromLambda(ref)); }
        public <A> void skip(Ref1<A, ?> ref) { directives.add(skipFromLambda(ref)); }
        public <A, B> void skip(Ref2<A, B, ?> ref) { directives.add(skipFromLambda(ref)); }
        public <A, B, C> void skip(Ref3<A, B, C, ?> ref) { directives.add(skipFromLambda(ref)); }

        public void skip(Class<?> clazz, String methodName) {
            directives.add(ScanDirective.skip(clazz, methodName));
        }

        public <R> void manual(Ref0<R> ref, String name) {
            directives.add(ScanDirective.manual(ref, name));
        }
        public <A, R> void manual(Ref1<A, R> ref, String name, A a) {
            directives.add(ScanDirective.manual(ref, name, a));
        }
        public <A, B, R> void manual(Ref2<A, B, R> ref, String name, A a, B b) {
            directives.add(ScanDirective.manual(ref, name, a, b));
        }
        public <A, B, C, R> void manual(Ref3<A, B, C, R> ref, String name, A a, B b, C c) {
            directives.add(ScanDirective.manual(ref, name, a, b, c));
        }

        public void manual(Class<?> clazz, String methodName, String variantName, Analyzable result) {
            directives.add(ScanDirective.manual(clazz, methodName, variantName, result));
        }
    }

    // --- Internal helpers ---

    private static ScanDirective skipFromLambda(Serializable ref) {
        var id = extractMethodIdentity(ref);
        return new SkipMethod(id.declaringClass(), id.methodName());
    }

    private static Analyzable toAnalyzable(Object value) {
        if (value instanceof Analyzable a) return a;
        return AnalyzableScanner.extractAnalyzableViaReflection(value);
    }

    record MethodIdentity(String declaringClass, String methodName) {}

    private static MethodIdentity extractMethodIdentity(Serializable ref) {
        try {
            Method writeReplace = ref.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            var sl = (SerializedLambda) writeReplace.invoke(ref);
            var implClass = sl.getImplClass().replace('/', '.');
            return new MethodIdentity(implClass, sl.getImplMethodName());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Cannot extract method identity from " + ref.getClass().getName() +
                ". Ensure you pass a method reference (e.g., obj::method), not a lambda.", e);
        }
    }
}
