package com.eteam.epotreba.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.eteam.epotreba.domain.models.MarkerModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class MarkerRepository(private val context: Context) {
    private val db = Firebase.firestore



    suspend fun commitMarker(id: String, uid: String) {
        val documentRef =
            db.collection("commit").document(uid)
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
        val documentRef =
            db.collection("commit").document(uid)

        var status: Boolean = true

        documentRef.get().addOnSuccessListener { result ->

            if (result.exists()) {
                val list = result.get("markers") as ArrayList<String>

                if (list.contains(id))
                    status = false
            }

        }.await()
        Log.i(TAG, status.toString())
        return status
    }
    fun update(marker: MarkerModel) {
        db.collection("marks").document(marker.id).set(marker)
    }

    fun save(marker: MarkerModel) {
        db.collection("marks").add(marker)
            .addOnSuccessListener { Log.d("DB-Context", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("DB-Context", "Error writing document", e) }
    }

    suspend fun getData(): List<MarkerModel> {

        val markerList: MutableList<MarkerModel> = mutableListOf()
        db.collection("marks")
            .get()
            .addOnSuccessListener {
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
}