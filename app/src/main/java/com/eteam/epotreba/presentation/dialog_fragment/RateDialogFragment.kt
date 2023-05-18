package com.eteam.epotreba.presentation.dialog_fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eteam.epotreba.R
import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.domain.usecase.CommitMarkerUseCase
import com.eteam.epotreba.presentation.viewModel.MainViewModel
import kotlinx.coroutines.launch


class RateDialogFragment : DialogFragment() {


    private val viewModel: MainViewModel by activityViewModels()

    private val commitMarkerUseCase by lazy(LazyThreadSafetyMode.NONE) {
        CommitMarkerUseCase(repository = MarkerRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_vote, null)

        val rate = view.findViewById<RatingBar>(R.id.rate_marker)
        val submit = view.findViewById<Button>(R.id.but_sub_rate)

        submit.setOnClickListener{
            val marker = viewModel.passMarker

            marker.votes += 1
            marker.sumRate += rate.rating
            viewModel.updateMarker(marker)

            lifecycleScope.launch {
                commitMarkerUseCase.execute(marker.id, viewModel.currentUser!!.uid)
            }

            super.dismiss()
        }


        return view
    }
}