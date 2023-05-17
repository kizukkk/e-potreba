package com.eteam.epotreba.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.eteam.epotreba.R
import com.eteam.epotreba.presentation.viewModel.MainViewModel

class DetailFragment : Fragment(R.layout.fragment_marker_details) {

    private val viewModel: MainViewModel by activityViewModels()


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_marker_details, container, false)

        val title = view.findViewById<TextView>(R.id.title_marker)
        val about = view.findViewById<TextView>(R.id.about_marker)
        val rate = view.findViewById<TextView>(R.id.score_marker)
        val price = view.findViewById<FrameLayout>(R.id.price_item)
//        val monit = view.findViewById<ImageView>(R.id.monetization_icon)
//        price.visibility = View.GONE
//        monit.visibility = View.GONE

        //val deleteMarkerButton = view.findViewById<Button>(R.id.but_delete)

        title.text = viewModel.passMarker.title
        about.text = viewModel.passMarker.about
        rate.text = viewModel.passMarker.rate.toString()


//        deleteMarkerButton.setOnClickListener {
//            lifecycleScope.launch {
//                viewModel.delete(viewModel.passMarker)
//                viewModel.update()
//            }
//        }

        return view
    }

}