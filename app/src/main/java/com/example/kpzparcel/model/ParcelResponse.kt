package com.example.kpzparcel.model

// 1. This matches the outer { "data": ... } wrapper
data class ParcelResponse(
    val data: ParcelData
)

// 2. This matches the fields inside "data"
data class ParcelData(
    val tracking_number: String,
    val customer_name: String,
    val shelf_number: Int,
    val status: String,
    val date_arrived: String,
    val weight: String,
    val price: Double
)