package com.example.villomap.data

data class Properties(
    val gid: Int,
    val villo_id: Int,
    val name_fr: String,
    val name_nl: String,
    val address_fr: String,
    val address_nl: String,
    val road_fr: String,
    val road_nl: String,
    val housenr: String,
    val pccp: String,
    val mu_fr: String,
    val mu_nl: String,
    val status: String,
    val bonus: Boolean,
    val banking: Boolean,
    val bike_stands: Int
) : java.io.Serializable
