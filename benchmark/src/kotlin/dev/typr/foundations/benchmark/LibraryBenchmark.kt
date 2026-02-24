package dev.typr.foundations.benchmark

interface LibraryBenchmark {
    val name: String
    fun selectAll(): List<Item>
}
