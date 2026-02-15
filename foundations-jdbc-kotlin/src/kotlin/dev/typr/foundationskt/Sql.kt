package dev.typr.foundationskt

class SqlContext {
    val fragments = mutableListOf<Fragment>()

    fun register(fragment: Fragment): String {
        val index = fragments.size
        fragments.add(fragment)
        return "\u0000$index\u0000"
    }
}

object Sql {
    @PublishedApi
    internal val threadLocal = ThreadLocal<SqlContext>()

    fun currentContext(): SqlContext? = threadLocal.get()

    inline operator fun invoke(block: () -> String): Fragment {
        val ctx = SqlContext()
        threadLocal.set(ctx)
        try {
            val result = block()
            return buildFragment(result, ctx)
        } finally {
            threadLocal.remove()
        }
    }

    @PublishedApi
    internal fun buildFragment(template: String, ctx: SqlContext): Fragment {
        if (ctx.fragments.isEmpty()) {
            return Fragment.of(template)
        }
        val parts = template.split('\u0000')
        val javaFragments = mutableListOf<dev.typr.foundations.Fragment>()
        for (i in parts.indices) {
            if (i % 2 == 0) {
                if (parts[i].isNotEmpty()) {
                    javaFragments.add(dev.typr.foundations.Fragment.of(parts[i]))
                }
            } else {
                val index = parts[i].toInt()
                javaFragments.add(ctx.fragments[index].underlying)
            }
        }
        return Fragment(dev.typr.foundations.Fragment.Concat(javaFragments))
    }
}

operator fun <T> DbType<T>.invoke(value: T): Fragment =
    Fragment(dev.typr.foundations.Fragment.value(value, underlying))
