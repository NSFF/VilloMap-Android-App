package com.example.villomap.data

data class Features(
    val type: String,
    val id: String,
    val geometry: Geometry,
    val geometry_name: String,
    val properties: Properties,
)
