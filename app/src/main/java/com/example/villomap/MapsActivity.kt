package com.example.villomap

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.villomap.data.FeatureCollection
import com.example.villomap.data.VilloDataHandler
import com.example.villomap.databinding.ActivityMapsBinding
import com.example.villomap.util.getCurrentDateTime
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vmadalin.easypermissions.EasyPermissions
import java.io.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var villoData : FeatureCollection
    private val villoDataHandler : VilloDataHandler = VilloDataHandler()
    private lateinit var userLocation : LatLng
    private var permissionDenied = false
    private val zoomInLvl = 11.0f
    private val updateTextWaitingTime : Long = 3500 // value in milliseconds
    private val dataFileName : String = "villoData.dat"
    private lateinit var villoDataFile: File

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // GET VILLO DATA CODE //
        downloadOrLoadData()
        // GET VILLO DATA CODE //

        // GOOGLE MAPS CODE //
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment.getMapAsync(this)
        // GOOGLE MAPS CODE //

    }

    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            applicationContext,
            ACCESS_FINE_LOCATION
        )

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            applicationContext,
            "Permission Granted!",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            return
        }

        // 2. Otherwise, request permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        TODO("Not yet implemented")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
            return
        }

        if (hasLocationPermission()
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        Toast.makeText(this, "Permission was not granted: Location",Toast.LENGTH_SHORT).show()
    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun downloadOrLoadData(){
        // Get the internal storage directory
        val internalStorageDir = filesDir
        // Create a File object with the desired file name
        villoDataFile = File(internalStorageDir, dataFileName)

        // if file is bigger than 3KB, it means it is empty, otherwise we load existing data
        if(villoDataFile.length()/1024 >= 4){
            Log.d("VilloApp", "Existing internally stored data size: "+ (villoDataFile.length()/1024).toString() + " KB")
            // Read the data class instance from the File using ObjectInputStream and FileInputStream
            villoData = ObjectInputStream(FileInputStream(villoDataFile)).use { input ->
                input.readObject() as FeatureCollection // Cast the read object to Player type
            }
        } else {
            downloadAndStoreData()
        }
    }

    private fun downloadAndStoreData(){
        // GET VILLO DATA CODE //
        villoData = villoDataHandler.villoData
        // GET VILLO DATA CODE //

        // Write the data class instance to the File using ObjectOutputStream and FileOutputStream
        ObjectOutputStream(FileOutputStream(villoDataFile)).use { output ->
            output.writeObject(villoData)
        }

        Log.d("VilloApp", "Updated internally stored data, size: "+ (villoDataFile.length()/1024).toString() + " KB")

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // remove all markers in case there were
        mMap.clear()
        enableMyLocation()

        var coordinate: LatLng

        // loop over all OPEN villo addresses and add them to maps
        for (feature in villoData.features) {
            if (feature.properties.status == "OPEN") {
                coordinate = LatLng(
                    feature.geometry.coordinates[1],
                    feature.geometry.coordinates[0]
                )
                mMap.addMarker(
                    MarkerOptions()
                        .position(coordinate)
                        .title(feature.properties.address_nl + " " + feature.properties.mu_nl + " " + feature.properties.pccp)
                        .icon(
                            BitmapDescriptorFactory
                                .defaultMarker(
                                    BitmapDescriptorFactory
                                        .HUE_ORANGE
                                )
                        )
                )
            }
        }

        // zoom in on user location if permission granted otherwise the first villo stand
        if (hasLocationPermission()) {
            // Get the last known location
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                // Check if the location is not null
                if (location != null) {
                    // Get the latitude and longitude once we received the user location
                    userLocation = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(zoomInLvl))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
                }
            }
        } else {
            var coordinate: LatLng = LatLng(
                villoData.features[0].geometry.coordinates[1],
                villoData.features[0].geometry.coordinates[0]
            )
            mMap.moveCamera(CameraUpdateFactory.zoomTo(zoomInLvl))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate))
        }

        // Set an OnMarkerClickListener on the map and redirect to google maps routing
        mMap.setOnMarkerClickListener { clickedMarker ->
            val intent: Intent = Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/dir/?api=1"+
                        "&destination=" +
                        clickedMarker.title?.replace(" ","+") +
                        "%2CBelgium" + "&travelmode=walking"
                ))
            startActivity(intent)
            Toast.makeText(this, "${clickedMarker.title}", Toast.LENGTH_SHORT).show()
            // Return true to indicate that the click event is consumed
            true
        }
    }

    fun onClickUpdate(view: View) {
        var updateButton : TextView = findViewById(R.id.updateButton)
        updateButton.text = "Last update: " + getCurrentDateTime().toString()

        downloadAndStoreData()
        // refresh map
        onMapReady(mMap)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                // This method will be executed once the timer is over
                updateButton.text = "Update Data"
            },
            updateTextWaitingTime // value in milliseconds
        )
    }
}

