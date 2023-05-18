package com.eteam.epotreba.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eteam.epotreba.R
import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.domain.usecase.VoteContainsMarkerUseCase
import com.eteam.epotreba.presentation.dialog_fragment.RateDialogFragment
import com.eteam.epotreba.presentation.viewModel.MainViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class DetailFragment : Fragment(R.layout.fragment_marker_details) {

    private val viewModel: MainViewModel by activityViewModels()

    private var contains: Boolean = true

    private val voteContainsMarkerUseCase by lazy(LazyThreadSafetyMode.NONE) {
        VoteContainsMarkerUseCase(repository = MarkerRepository())
    }

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
        val voteButton = view.findViewById<ImageButton>(R.id.but_vote)
//        val monit = view.findViewById<ImageView>(R.id.monetization_icon)
//        monit.visibility = View.GONE

        //val deleteMarkerButton = view.findViewById<Button>(R.id.but_delete)


        val marker = viewModel.passMarker
        val votes = marker.votes
        val sumRate = marker.sumRate

        if (marker.price == 0.0)
            priceTab.visibility = View.GONE



        val df = DecimalFormat("#.#")

        title.text = marker.title
        about.text = marker.about
        price.text = marker.price.toString()



        rate.text = if(sumRate == 0.0) "0" else
            df.format(sumRate / votes).toString()

        voteButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                contains = voteContainsMarkerUseCase.execute(viewModel.passMarker.id, viewModel.currentUser!!.uid)
                if (contains){
                    val dialog = RateDialogFragment()
                    dialog.show(parentFragmentManager, "dialog")
                }
                else Toast.makeText(activity, "Ви вже виставили оцінку!", Toast.LENGTH_SHORT).show()
            }
        }


//        deleteMarkerButton.setOnClickListener {
//            lifecycleScope.launch {
//                viewModel.delete(viewModel.passMarker)
//                viewModel.update()
//            }
//        }

        return view
    }

}