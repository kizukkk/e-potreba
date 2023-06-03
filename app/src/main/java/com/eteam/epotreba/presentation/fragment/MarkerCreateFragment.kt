package com.eteam.epotreba.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            val title: String = binding.titleField.text.toString()
            val about: String = binding.aboutField.text.toString();
            val lat: Double = binding.latField.text.toString().toDouble();
            val lng: Double = binding.lngField.text.toString().toDouble();
            val price: Double = binding.priceField.text.toString().toDouble();
            val marker = MarkerModel(
                title, about, LatLng(lat, lng), 0.0, viewModel.currentUser!!.uid, price
            )


            saveMarkerUseCase.execute(marker)

            Toast.makeText(viewModel.getApplication(), "Успішно додано!", Toast.LENGTH_LONG).show()
            lifecycleScope.launch {
                viewModel.updateList()
                requireActivity().supportFragmentManager.popBackStack()
            }


        }
        return binding.root
    }

}
