package com.eteam.epotreba.presentation.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.eteam.epotreba.R
import com.eteam.epotreba.domain.adapter.MarkerAdapter
import com.eteam.epotreba.domain.models.MarkerModel
import com.eteam.epotreba.presentation.viewModel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.tasks.await


class NearFragment : Fragment(R.layout.fragment_near) {
    private lateinit var dialog: BottomSheetDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val adapter = MarkerAdapter()
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_near, container, false)
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapsContainerView) as SupportMapFragment?

        val drawable = context?.let { it1 -> ActivityCompat.getDrawable(it1, R.drawable.ic_icon_wc) }
        drawable?.setBounds(0,0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bitmap = drawable?.let { it1 -> createBitmap(it1.intrinsicWidth, drawable.intrinsicHeight) }
        val canvas = bitmap?.let { it1 -> Canvas(it1) }

        if (drawable != null) {
            if (canvas != null) {
                drawable.draw(canvas)
            }
        }

        val icon = bitmap?.let { it1 -> BitmapDescriptorFactory.fromBitmap(it1) }


        var fusedLocationClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }

        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
        }

        supportMapFragment!!.getMapAsync { googleMap ->

            val nightModeFlags =
                context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK);
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                googleMap.setMapStyle(context?.let { MapStyleOptions.loadRawResourceStyle(it, R.raw.night_map) });
            }

            fusedLocationClient?.lastLocation?.addOnCompleteListener { task ->
                val location: Location? = task.result
                if (location != null) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude),
                        13F
                    ))

                }
            }
            viewModel.markerList.observe(this as LifecycleOwner) {
                it.forEach { markerData ->
                    val marker =
                        MarkerOptions().title(markerData.title).position(markerData.position)
                    marker.icon(icon)
                    googleMap.addMarker(marker)
                }
            }
        }

        return view
    }
}
