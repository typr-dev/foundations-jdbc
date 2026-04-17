package dev.typr.foundationskt

interface Analyzable {
    val analyzable: dev.typr.foundations.Analyzable

    /**
     * Human-readable description. Non-verbose returns just the name for named analyzables
     * (wrapped with [Named] or [Operation.named]); verbose appends the detailed rendering
     * (rendered fragments, operation type, etc.). Unnamed analyzables always return the
     * detailed rendering.
     */
    fun description(verbose: Boolean = false): String = analyzable.description(verbose)
}
