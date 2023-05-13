package com.eteam.epotreba.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eteam.epotreba.databinding.ActivityMarkerCreateBinding
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MarkerCreateActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMarkerCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createButton.setOnClickListener {

            val title: String = binding.titleField.text.toString()
            val lat: Double = binding.latField.text.toString().toDouble();
            val lng: Double = binding.lngField.text.toString().toDouble();

            //Toast.makeText(this, "$title, $lat, $lng", Toast.LENGTH_LONG).show()

            val mark = hashMapOf(
                "title" to title,
                "position" to LatLng(lat, lng)
            )

            db.collection("marks")
                .add(mark)
        }


    }
}
