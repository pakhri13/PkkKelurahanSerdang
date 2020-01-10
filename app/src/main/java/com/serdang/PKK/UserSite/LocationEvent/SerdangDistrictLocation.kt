package com.serdang.PKK.UserSite.LocationEvent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.serdang.PKK.R

class SerdangDistrictLocation : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelurahan_district_location)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_kelurahan) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val kemayoran = LatLng(-6.1588947,106.8589872)
        mMap.addMarker(MarkerOptions().position(kemayoran).title("Kelurahan Serdang"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kemayoran,17f))
    }
}