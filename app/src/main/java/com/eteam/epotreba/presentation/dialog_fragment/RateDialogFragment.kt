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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_vote, null)
        val rate = view.findViewById<RatingBar>(R.id.rate_marker)
        val submit = view.findViewById<Button>(R.id.but_sub_rate)

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent);


        submit.setOnClickListener{
            lifecycleScope.launch {
                viewModel.commitMarker(rate.rating.toDouble())
            }
            super.dismiss()
        }

        return view
    }
}