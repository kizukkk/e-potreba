package com.eteam.epotreba.markers_action

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.eteam.epotreba.databinding.ActivityMainBinding
import com.eteam.epotreba.databinding.ActivityMarkerCreateBinding
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class MarkerCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarkerCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarkerCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.firestore

        binding.createButton.setOnClickListener{

            val title: String = binding.titleField.text.toString()
            val lat: Double =  binding.latField.text.toString().toDouble();
            val lng:Double = binding.lngField.text.toString().toDouble();

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