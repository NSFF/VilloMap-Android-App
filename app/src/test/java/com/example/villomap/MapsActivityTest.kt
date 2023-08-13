package com.example.villomap

import androidx.appcompat.app.AppCompatActivity
import com.example.villomap.data.VilloDataHandler
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

class MapsActivityTest : AppCompatActivity() {

    // Test if the download and storing of data actually stored data
    @Test
    fun downloadAndStoreData() {
        val villoDataHandler : VilloDataHandler = VilloDataHandler()

        val dataFileName : String = "villoData.dat"
        // Get the internal storage directory
        val internalStorageDir = filesDir

        val villoDataFile = File(internalStorageDir, dataFileName)

        // GET VILLO DATA CODE //
        val villoData = villoDataHandler.villoData
        // GET VILLO DATA CODE //

        // Write the data class instance to the File using ObjectOutputStream and FileOutputStream
        ObjectOutputStream(FileOutputStream(villoDataFile)).use { output ->
            output.writeObject(villoData)
        }
        Assert.assertEquals(villoDataFile.length()/1024 > 3, true)
    }

}