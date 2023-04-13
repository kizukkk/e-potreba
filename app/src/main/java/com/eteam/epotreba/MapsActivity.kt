package com.eteam.epotreba

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

public class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var marker: LatLng

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment;
        mapFragment.getMapAsync(this);
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap;
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID;
        // Add a marker in Sydney and move the camera
        val iran = LatLng(29.0, 52.0);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(iran, 5.0f));

        addMarker(29.1, 52.2, "ssss");
    }


    private fun addMarker(lat: Double, lng: Double, title: String) {
        marker = LatLng(lat, lng);
        mMap.addMarker(MarkerOptions().position(marker).title(title));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 10.0f));
    }

}
