package dev.typr.foundations.benchmark

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val tags: String,
    val quantity: Int,
    val price: BigDecimal,
    val weight: Double,
    val active: Boolean,
    val rating: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val releaseDate: LocalDate,
    val sku: String,
    val notes: String,
)
