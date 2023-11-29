package com.example.exchanger.api

data class CurrencyData(
    val conversion_rates: Rate
)

data class Rate(
    val USD: Double,
    val EUR: Double,
    val PLN: Double,
    val RUB: Double
)
