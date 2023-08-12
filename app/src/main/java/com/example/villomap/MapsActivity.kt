package com.example.villomap

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.villomap.data.FeatureCollection
import com.example.villomap.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vmadalin.easypermissions.EasyPermissions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val villoApiUrl : String = "https://data.mobility.brussels/geoserver/bm_bike/wfs?service=wfs&version=1.1.0&request=GetFeature&typeName=bm_bike:villo&outputFormat=json&srsName=EPSG:4326"
    object PermissionConstants{
        const val PERM_LOC_RAT_MESS: String = "This application will not use your current location"
        const val REQUEST_CODE_LOCATION_PERMISSION: Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // GET VILLO DATA CODE //
        val villoAPI = RetrofitHelper.getInstance().create(VilloAPI::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val result = villoAPI.getData()
            if (result != null)
            // Checking the results
                Log.d("VilloApp: ", result.body().toString())
        }
        // GET VILLO DATA CODE //

        // GOOGLE MAPS CODE //
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // GOOGLE MAPS CODE //

        setViewVisibility()
    }


    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            applicationContext,
            ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            PermissionConstants.PERM_LOC_RAT_MESS,
            PermissionConstants.REQUEST_CODE_LOCATION_PERMISSION,
            ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        TODO("Not yet implemented")
    }


    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            applicationContext,
            "Permission Granted!",
            Toast.LENGTH_SHORT
        ).show()
        setViewVisibility()
    }

    private fun setViewVisibility() {
        if (hasLocationPermission()) {

        } else {

        }
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val test = LatLng(50.0, 50.0)
        mMap.addMarker(MarkerOptions()
                        .position(sydney)
                        .title("Marker in whatever")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory
                                                .HUE_ORANGE)))
        mMap.addMarker(MarkerOptions()
            .position(test)
            .title("Marker in Sydney")
            .icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory
                    .HUE_ORANGE)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(test))
    }
}

