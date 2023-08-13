package com.example.villomap.data

import com.example.villomap.network.RetrofitHelper
import com.example.villomap.network.VilloAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VilloDataHandler {
    lateinit var villoData : FeatureCollection

    init {
        downloadData()
    }
    // Download villo data
    fun downloadData() {
        // GET VILLO DATA CODE //
        val villoAPI = RetrofitHelper.getInstance().create(VilloAPI::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            villoData = villoAPI.getData().body()!!
        }
    }
}