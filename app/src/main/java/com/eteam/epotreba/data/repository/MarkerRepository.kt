package com.eteam.epotreba.data.repository

import android.util.Log
import com.eteam.epotreba.data.database.GetSomeData
import com.eteam.epotreba.domain.models.MarkerModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MarkerRepository() {
    private val db = Firebase.firestore


    fun saveData(markerModel: MarkerModel){
        TODO("Implement save data to DB")
    }

    fun getData(): List<MarkerModel>{
        return GetSomeData().execute()
    }
}