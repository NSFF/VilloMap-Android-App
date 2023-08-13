package com.example.villomap.data

data class FeatureCollection(
    val features: List<Features>,
    val totalFeatures: Int,
    val numberMatched: Int,
    val numberReturned: Int,
    val timestamp: String,
) : java.io.Serializable
