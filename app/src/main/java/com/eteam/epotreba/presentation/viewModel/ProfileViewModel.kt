package com.eteam.epotreba.presentation.viewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.domain.models.MarkerModel
import com.eteam.epotreba.domain.usecase.FavoriteMarkerUseCase
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("StaticFieldLeak")
class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val context = getApplication<Application>().applicationContext

    var selectedList: Int = 0

    private val getFavoriteMarkerUseCase by lazy(LazyThreadSafetyMode.NONE) {
        FavoriteMarkerUseCase(MarkerRepository(context))
    }

    suspend fun getFavoriteMarkers(source: MutableLiveData<List<MarkerModel>>): List<MarkerModel>{
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val listOfId = getFavoriteMarkerUseCase.get(uid)
        val favList = mutableListOf<MarkerModel>()

        val mark = source.value ?: return emptyList()

        for (item in mark) {
            if (listOfId.contains(item.id)) {
                favList.add(item)
            }
        }


        return favList
    }

    fun getOwnMarkers(source: MutableLiveData<List<MarkerModel>>): List<MarkerModel>{
        val owned = mutableListOf<MarkerModel>()

        val mark = source.value ?: return emptyList()
        for (item in mark) {
            if (item.userId ==  FirebaseAuth.getInstance().currentUser!!.uid) {
                owned.add(item)
            }
        }
        return owned
    }

}

