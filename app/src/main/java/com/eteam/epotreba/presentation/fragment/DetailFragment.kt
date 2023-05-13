package com.eteam.epotreba.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eteam.epotreba.R
import com.eteam.epotreba.presentation.viewModel.MainViewModel
import kotlinx.coroutines.launch

class DetailFragment : Fragment(R.layout.fragment_marker_detail) {

    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_marker_detail, container, false)

        val title = view.findViewById<TextView>(R.id.title_filed)
        val about = view.findViewById<TextView>(R.id.about_field)
        val rate = view.findViewById<TextView>(R.id.score_field)

        val deleteMarkerButton = view.findViewById<Button>(R.id.but_delete)

        title.text = viewModel.passMarker.title
        about.text = viewModel.passMarker.about
        rate.text = viewModel.passMarker.rate.toString()


        deleteMarkerButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.delete(viewModel.passMarker)
                viewModel.update()
            }
        }

        return view
    }

}