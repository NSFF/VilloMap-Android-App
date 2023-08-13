package com.example.villomap

import android.Manifest
import android.Manifest.permission
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.villomap.data.FeatureCollection
import com.example.villomap.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.vmadalin.easypermissions.EasyPermissions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var villoData : FeatureCollection
    private lateinit var userLocation : LatLng
    // Define a constant for the request code
    val REQUEST_LOCATION_PERMISSION = 0
    private lateinit var locationCallback: LocationCallback
    private var permissionDenied = false

    // Define an array of permissions to request
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    object PermissionConstants{
        const val PERM_LOC_RAT_MESS: String = "This application will not use your current location"
        const val REQUEST_CODE_LOCATION_PERMISSION: Int = 1
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // GET VILLO DATA CODE //
        val villoAPI = RetrofitHelper.getInstance().create(VilloAPI::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            villoData = villoAPI.getData().body()!!
            if (villoData != null)
            // Checking the results
                Log.d("Villo Data: ", villoData.toString())
                Log.d("Villo Data: ", villoData.features[0].properties.name_nl.toString())
        }
        // GET VILLO DATA CODE //
/*
        // Check if the permissions are already granted
        if (ActivityCompat.checkSelfPermission(this, locationPermissions[0]) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, locationPermissions[1]) == PackageManager.PERMISSION_GRANTED) {
            // Permissions are granted, proceed with getting the location
            //Log.d("VilloApp","latitude.toString()")

        } else {
            // Permissions are not granted, request them
            ActivityCompat.requestPermissions(this, locationPermissions, REQUEST_LOCATION_PERMISSION)
        }
*/
        /*
        checkLocationPermission()
        fetchLocation()*/

        // GOOGLE MAPS CODE //
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment.getMapAsync(this)
        // GOOGLE MAPS CODE //
    }
/*
    @SuppressLint("MissingPermission")
    private fun initMap() {
        mMap.isMyLocationEnabled = true

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                updateMapLocation(location)
            }

        initLocationTracking()

    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission")
    private fun initLocationTracking() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    updateMapLocation(location)
                }
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest(),
            locationCallback,
            null)
    }

    override fun onResume() {
        super.onResume()
        if( ::mMap.isInitialized ) {
            initLocationTracking()
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun updateMapLocation(location: Location?) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(
            location?.latitude ?: 0.0,
            location?.longitude ?: 0.0)))

        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f))
    }

    // Override the onRequestPermissionsResult method to handle the result of the request
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                // Check if the permissions are granted
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Permissions are granted, proceed with getting the location
                    // Get the last known location
                    /*fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                        // Check if the location is not null
                        if (location != null) {
                            // Get the latitude and longitude from the location object
                            val latitude = location.latitude
                            val longitude = location.longitude

                            // Do something with the location, such as showing a toast
                            Toast.makeText(this, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_SHORT).show()
                            Log.d("VilloApp",latitude.toString())
                        }
                    }*/
                    initMap()
                } else {
                    // Permissions are denied, show a message or handle it accordingly
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
*/
    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            applicationContext,
            ACCESS_FINE_LOCATION
        )
/*
    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            PermissionConstants.PERM_LOC_RAT_MESS,
            PermissionConstants.REQUEST_CODE_LOCATION_PERMISSION,
            ACCESS_FINE_LOCATION
        )
    }

    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }*/

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        TODO("Not yet implemented")
    }

*/
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            applicationContext,
            "Permission Granted!",
            Toast.LENGTH_SHORT
        ).show()
        //checkLocationPermission()
    }
/*
    private fun checkLocationPermission() {
        if (!hasLocationPermission()) {
            requestLocationPermission()
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        if(hasLocationPermission()){
            // Get the last known location
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                // Check if the location is not null
                if (location != null) {
                    // Get the latitude and longitude from the location object
                    userLocation = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }*/

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
        //mMap.setOnMyLocationButtonClickListener(this)
        //mMap.setOnMyLocationClickListener(this)
        enableMyLocation()

        /*var coordinate: LatLng

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

        if (hasLocationPermission()) {
            mMap.isMyLocationEnabled = true
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
        } else {
            var coordinate: LatLng = LatLng(
                villoData.features[0].geometry.coordinates[1],
                villoData.features[0].geometry.coordinates[0]
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate))
        }*/
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




}

