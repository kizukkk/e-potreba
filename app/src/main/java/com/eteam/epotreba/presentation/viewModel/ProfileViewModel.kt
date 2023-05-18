package com.eteam.epotreba.presentation.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.eteam.epotreba.domain.models.MarkerModel
import com.google.firebase.auth.FirebaseAuth


class ProfileViewModel : ViewModel() {

    var selectedList: Int = 0


    fun getFavoriteMarkers(source: MutableLiveData<List<MarkerModel>>): List<MarkerModel>{
        return emptyList()
    }

    fun getOwnMarkers(source: MutableLiveData<List<MarkerModel>>): List<MarkerModel>{
        val owned = mutableListOf<MarkerModel>()

        val mark = source.value
        if (mark != null) {
            for (item in mark) {
                if (item.userId ==  FirebaseAuth.getInstance().currentUser!!.uid) {
                    owned.add(item)
                }
            }
        }
        Log.i("Test", owned.toString())
        return owned
    }

}

