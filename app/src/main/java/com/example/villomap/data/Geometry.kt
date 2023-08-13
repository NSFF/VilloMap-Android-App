package com.example.villomap.data

data class Geometry(
    val type: String,
    // coordinates = [longitude, latitude]
    val coordinates: List<Double>,
)
