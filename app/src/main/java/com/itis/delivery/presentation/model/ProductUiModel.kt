package com.itis.delivery.presentation.model

data class ProductUiModel(
    val id: Long,
    val name: String,
    val brands: String,
    val quantity: String,
    val imageUrl: String,
    val price: Int
)

data class NutrimentsDataUiModel(
    val carbohydrates: Double,
    val energyKcal: Double,
    val fat: Double,
    val proteins: Double,
    val salt: Double,
    val saturatedFat: Double,
    val sugars: Double
)