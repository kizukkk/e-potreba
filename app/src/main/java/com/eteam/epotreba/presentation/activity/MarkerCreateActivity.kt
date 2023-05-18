package com.eteam.epotreba.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.databinding.ActivityMarkerCreateBinding
import com.eteam.epotreba.domain.models.MarkerModel
import com.eteam.epotreba.domain.usecase.SaveMarkerUseCase
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth


class MarkerCreateActivity : AppCompatActivity() {

    private val uid = FirebaseAuth.getInstance().uid

    private val saveMarkerUseCase by lazy(LazyThreadSafetyMode.NONE){
        SaveMarkerUseCase(repository = MarkerRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMarkerCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.createButton.setOnClickListener {

            val title: String = binding.titleField.text.toString()
            val about: String = binding.aboutField.text.toString();
            val lat: Double = binding.latField.text.toString().toDouble();
            val lng: Double = binding.lngField.text.toString().toDouble();
            val price: Double = binding.priceField.text.toString().toDouble();
            val marker = MarkerModel(
                title,about, LatLng(lat,lng),0.0,uid.toString(),price)


            saveMarkerUseCase.execute(marker)

            Toast.makeText(this, "Успішно додано!", Toast.LENGTH_LONG).show()

        }

    }
}
