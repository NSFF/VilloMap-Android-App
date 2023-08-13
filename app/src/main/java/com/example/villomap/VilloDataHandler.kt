package com.example.villomap

import android.util.Log
import com.example.villomap.data.FeatureCollection
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