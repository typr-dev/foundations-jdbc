@file:Suppress("unused")
package dev.typr.foundationskt

import dev.typr.foundations.ScanDirective as JavaScanDirective

sealed interface ScanDirective {
    fun toJava(): JavaScanDirective
}

private class SkipDirective(
    private val declaringClass: String,
    private val methodName: String
) : ScanDirective {
    override fun toJava(): JavaScanDirective =
        JavaScanDirective.SkipMethod(declaringClass, methodName)
}

private class ManualDirective(
    private val declaringClass: String,
    private val methodName: String,
    private val variantName: String,
    private val analyzable: dev.typr.foundations.Analyzable
) : ScanDirective {
    override fun toJava(): JavaScanDirective =
        JavaScanDirective.ManualMethod(declaringClass, methodName, variantName, analyzable)
}

private class InstanceDirective(
    private val instance: Any,
    private val overrides: List<ScanDirective>
) : ScanDirective {
    override fun toJava(): JavaScanDirective =
        JavaScanDirective.InstanceScan(instance, overrides.map { it.toJava() })
}

private class JavaDirectiveWrapper(
    private val java: JavaScanDirective
) : ScanDirective {
    override fun toJava(): JavaScanDirective = java
}

fun ScanDirective(java: JavaScanDirective): ScanDirective = JavaDirectiveWrapper(java)

fun skip(clazz: Class<*>, methodName: String): ScanDirective =
    SkipDirective(clazz.name, methodName)

fun manual(clazz: Class<*>, methodName: String, variantName: String, result: Analyzable): ScanDirective =
    ManualDirective(clazz.name, methodName, variantName, result.analyzable)

fun <T : Any> instance(obj: T, block: InstanceScope<T>.() -> Unit): ScanDirective {
    val scope = InstanceScope(obj)
    scope.block()
    return InstanceDirective(obj, scope.directives)
}

fun instance(obj: Any): ScanDirective = InstanceDirective(obj, emptyList())

class InstanceScope<T : Any>(val obj: T) {
    internal val directives = mutableListOf<ScanDirective>()

    fun skip(clazz: Class<*>, methodName: String) {
        directives.add(dev.typr.foundationskt.skip(clazz, methodName))
    }

    fun skip(java: JavaScanDirective) {
        directives.add(JavaDirectiveWrapper(java))
    }

    fun manual(clazz: Class<*>, methodName: String, variantName: String, result: Analyzable) {
        directives.add(dev.typr.foundationskt.manual(clazz, methodName, variantName, result))
    }

    fun manual(java: JavaScanDirective) {
        directives.add(JavaDirectiveWrapper(java))
    }
}
