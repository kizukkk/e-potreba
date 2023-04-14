package com.eteam.epotreba

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.eteam.epotreba.markers_action.MarkerCreateActivity


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val addMarkerButton = view.findViewById<Button>(R.id.addMarkerButton)

        addMarkerButton.setOnClickListener {
            activity?.let {
                val intent = Intent(it, MarkerCreateActivity::class.java)
                it.startActivity(intent)
            }
        }

        return view
    }

}
