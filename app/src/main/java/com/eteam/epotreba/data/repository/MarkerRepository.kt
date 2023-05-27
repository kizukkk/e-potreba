package com.eteam.epotreba.data.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.eteam.epotreba.domain.models.MarkerModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MarkerRepository(private val context: Context) {
    private val db = Firebase.firestore

    suspend fun commitMarker(id: String, uid: String) {
        val documentRef = db.collection("commit").document(uid)
        documentRef.get().addOnSuccessListener { result ->
            if (result.exists()) {
                val list = result.get("markers") as ArrayList<String>

                list.add(id)
                documentRef.update("markers", list)
            } else {
                val list = object {
                    val markers = arrayListOf(id)
                }
                db.collection("commit").document(uid).set(list)

            }
        }.await()
    }

    suspend fun voteContains(id: String, uid: String): Boolean {
        val documentRef = db.collection("commit").document(uid)

        var status = true

        documentRef.get().addOnSuccessListener { result ->

            if (result.exists()) {
                val list = result.get("markers") as ArrayList<String>

                if (list.contains(id)) status = false
            }

        }.await()
        return status
    }

    fun updateData(marker: MarkerModel) {
        db.collection("marks").document(marker.id).set(marker)
    }

    fun saveData(marker: MarkerModel) {
        db.collection("marks").add(marker)
            .addOnSuccessListener { Log.d("DB-Context", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("DB-Context", "Error writing document", e) }
    }

    suspend fun getData(): List<MarkerModel> {

        val markerList: MutableList<MarkerModel> = mutableListOf()
        db.collection("marks").get().addOnSuccessListener {
            for (result in it.documents) {
                val position = result.data?.get("position") as? Map<*, *>
                val lat = position?.get("latitude").toString().toDouble()
                val lng = position?.get("longitude").toString().toDouble()


                val item = MarkerModel(
                    id = result.id,
                    title = result.data?.get("title").toString(),
                    about = result.data?.get("about").toString(),
                    sumRate = result.data?.get("sumRate").toString().toDouble(),
                    position = LatLng(lat, lng),
                    userId = result.data?.get("userId").toString(),
                    votes = result.data?.get("votes").toString().toInt(),
                    price = result.data?.get("price").toString().toDouble()

                )
                item.address = getAddress(item.position).toString()
                markerList.add(item)
            }
        }.await()
        return markerList
    }

    suspend fun deleteData(id: String) {
        db.collection("marks").document(id).delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }.await()
    }

    private fun getAddress(lat: LatLng): String {
        val geocoder = Geocoder(context)
        val list = geocoder.getFromLocation(lat.latitude, lat.longitude, 1)
        val address = list?.get(0)?.getAddressLine(0)

        var street = ""; var number = ""; var city = ""

        try {
            street = address?.split(", ")?.get(0) ?: ""
            number = address?.split(", ")?.get(1) ?: ""
            city = address?.split(", ")?.get(2) ?: ""
        } catch (exception: Exception) {
            Log.wtf(exception.toString(), "some of address property is null")
        }

        return listOf(street, number, city).joinToString(", ")
    }

    fun saveToFavorite(id: String, uid: String){
        val documentRef = db.collection("favorite").document(uid)

        documentRef.get().addOnSuccessListener { result ->
            if (result.exists()) {
                val list = result.get("markers") as ArrayList<String>

                list.add(id)
                documentRef.update("markers", list)
            } else {
                val list = object {
                    val markers = arrayListOf(id)
                }
                db.collection("favorite").document(uid).set(list)

            }
        }
    }

    fun deleteFromFavorite(id: String, uid: String){
        val documentRef = db.collection("favorite").document(uid)

        documentRef.get().addOnSuccessListener { result ->
            if (result.exists()) {
                val list = result.get("markers") as ArrayList<String>

                list.remove(id)
                documentRef.update("markers", list)
            }
        }
    }

    suspend fun getFavorite(uid: String): List<String>{
        var favList = emptyList<String>()
        val documentRef = db.collection("favorite").document(uid)

        documentRef.get().addOnSuccessListener { result ->
            if (result.exists()) {
                favList = result.get("markers") as ArrayList<String>
            }
        }.await()

        return favList
    }

}