package com.example.villomap

import com.example.villomap.data.FeatureCollection
import retrofit2.Response
import retrofit2.http.GET

interface VilloAPI {
    @GET("geoserver/bm_bike/wfs?service=wfs&version=1.1.0&request=GetFeature&typeName=bm_bike:villo&outputFormat=json&srsName=EPSG:4326")
    suspend fun getData(): Response<FeatureCollection>
}