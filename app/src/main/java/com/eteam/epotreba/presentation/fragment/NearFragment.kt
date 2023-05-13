package com.eteam.epotreba.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eteam.epotreba.R
import com.eteam.epotreba.domain.adapter.MarkerAdapter
import com.eteam.epotreba.domain.usecase.GetMarkersUseCase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog

class NearFragment : Fragment(R.layout.fragment_near) {
    private lateinit var dialog: BottomSheetDialog
    private lateinit var recyclerView: RecyclerView

    private val adapter = MarkerAdapter()
    private val items = GetMarkersUseCase().execute()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_near, container, false)
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapsContainerView) as SupportMapFragment?

        supportMapFragment!!.getMapAsync { googleMap ->
            items.forEach {
                googleMap.addMarker(MarkerOptions().title(it.title).position(it.position))
            }
        }

        showBottomSheet()
        return view
    }

    private fun showBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        dialog = activity?.let { BottomSheetDialog(it, R.style.BottomSheetDialogTheme) }!!
        dialog.setContentView(dialogView)

        recyclerView = dialogView.findViewById<RecyclerView>(R.id.rv_marker_list)

        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        adapter.submit(items)

        dialog.show()
    }
}

//FIXME: fix map reload on fragment change
//FIXME: make bottom sheet appear only in near fragment
