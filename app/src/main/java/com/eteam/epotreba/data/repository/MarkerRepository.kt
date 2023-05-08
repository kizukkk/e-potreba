package com.eteam.epotreba.data.repository

import android.util.Log
import com.eteam.epotreba.domain.models.MarkerModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class MarkerRepository() {
    private val db = Firebase.firestore


    fun saveData(marker: MarkerModel){
        db.collection("marks").add(marker)
            .addOnSuccessListener { Log.d("DB-Context", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("DB-Context", "Error writing document", e) }
    }

    suspend fun getData(): List<MarkerModel>{

        val markerList: MutableList<MarkerModel> = mutableListOf()
        db.collection("marks")
            .get()
            .addOnSuccessListener {
                for (result in it.documents) {
                    val position = result.data?.get("position") as? Map<*, *>
                    val lat = position?.get("latitude").toString().toDouble()
                    val lng = position?.get("longitude").toString().toDouble()


                    val item = MarkerModel(
                        title = result.data?.get("title").toString(),
                        about = result.data?.get("about").toString(),
                        rate = result.data?.get("rate").toString().toDouble() ,
                        position = LatLng(lat,lng)
                    )
                    markerList.add(item)
                }
            }.await()
        return markerList
    }
}