package com.eteam.epotreba.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.databinding.FragmentMarkerCreateBinding
import com.eteam.epotreba.domain.models.MarkerModel
import com.eteam.epotreba.domain.usecase.SaveMarkerUseCase
import com.eteam.epotreba.presentation.viewModel.MainViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import com.eteam.epotreba.R


class MarkerCreateFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()


    private val saveMarkerUseCase by lazy(LazyThreadSafetyMode.NONE) {
        SaveMarkerUseCase(repository = MarkerRepository(viewModel.getApplication()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentMarkerCreateBinding.inflate(layoutInflater)


        binding.createButton.setOnClickListener {

            val title: String;
            val about: String;
            val lat: Double;
            val lng: Double

            try {
                title = getDataFromRequiredField(binding.titleField)
                lat = getDataFromRequiredField(binding.latField).toDouble()
                lng = getDataFromRequiredField(binding.lngField).toDouble()
                about = getDataFromRequiredField(binding.aboutField)

            } catch (e: NullPointerException) {
                Log.wtf("MarkerCreateFields", "Fields must be not null")
                return@setOnClickListener

            }
            val price: Double = if(binding.priceField.text.isNotEmpty())
                binding.priceField.text.toString().toDouble()
            else 0.0


            if (price > 1000) {
                binding.priceField.error = getString(R.string.fragment_marker_create_price_alert)
                return@setOnClickListener
            }

            val marker = MarkerModel(
                title, about, LatLng(lat, lng), 0.0, viewModel.currentUser!!.uid, price
            )


            saveMarkerUseCase.execute(marker)

            Toast.makeText(
                viewModel.getApplication(),
                R.string.fragment_marker_create_successful,
                Toast.LENGTH_LONG
            ).show()
            lifecycleScope.launch {
                viewModel.updateList()
                requireActivity().supportFragmentManager.popBackStack()
            }


        }
        return binding.root
    }

    private fun getDataFromRequiredField(field: EditText): String {

        field.text.takeIf { it.isEmpty() }?.let {
            field.error = getString(R.string.fragment_marker_create_required)

            throw NullPointerException()
        }

        return field.text.toString()


    }

}
