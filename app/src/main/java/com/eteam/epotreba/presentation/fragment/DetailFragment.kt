package com.eteam.epotreba.presentation.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eteam.epotreba.R
import com.eteam.epotreba.presentation.dialog_fragment.RateDialogFragment
import com.eteam.epotreba.presentation.viewModel.MainViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat

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
        val priceTab = view.findViewById<FrameLayout>(R.id.price_item)
        val price = view.findViewById<TextView>(R.id.price_marker)
        val address = view.findViewById<TextView>(R.id.address_marker)

        val voteButton = view.findViewById<ImageButton>(R.id.but_vote)
        val editButton = view.findViewById<ImageButton>(R.id.but_edit)
        val favoriteButton = view.findViewById<ImageView>(R.id.ic_favorite)

        if (viewModel.favoriteList.value!!.contains(viewModel.passMarker.id))
            favoriteButton.setColorFilter(Color.MAGENTA)

        val marker = viewModel.passMarker
        val votes = marker.votes
        val sumRate = marker.sumRate


        if (marker.price == 0.0)
            priceTab.visibility = View.GONE

        if (marker.userId != viewModel.currentUser!!.uid)
            editButton.visibility = View.GONE


        val df = DecimalFormat("#.#")

        title.text = marker.title
        about.text = marker.about
        price.text = marker.price.toString()
        address.text = marker.address
        rate.text = if(sumRate == 0.0) "0" else
            df.format(sumRate / votes).toString()

        voteButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val contains = viewModel.checkVoteMarker()
                if (contains){
                    val dialog = RateDialogFragment()
                    dialog.show(parentFragmentManager, "dialog")
                }
                else Toast.makeText(activity, R.string.fragment_detal_alert_vote, Toast.LENGTH_SHORT).show()
            }
        }

        editButton.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if(transaction != null){
                transaction.replace(R.id.fragmentContainerView, EditMarkerFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        favoriteButton.setOnClickListener {
            if (viewModel.favoriteList.value!!.contains(viewModel.passMarker.id)) {
                favoriteButton.setColorFilter(Color.BLACK)
                viewModel.deleteFromFavorite()
            }
            else{
                favoriteButton.setColorFilter(Color.MAGENTA)
                viewModel.saveToFavorite()
            }
        }

        return view
    }



}