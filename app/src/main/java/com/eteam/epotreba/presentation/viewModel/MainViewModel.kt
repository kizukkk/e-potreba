package com.eteam.epotreba.presentation.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.domain.models.MarkerModel
import com.eteam.epotreba.domain.usecase.GetMarkersUseCase
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    var markerList: MutableLiveData<List<MarkerModel>> =
        MutableLiveData<List<MarkerModel>>(emptyList())

    private val getMarkersUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetMarkersUseCase(repository = MarkerRepository())
    }

    init {
        viewModelScope.launch {
            update()
        }
    }

    private suspend fun getMarkers(): List<MarkerModel> {
        return getMarkersUseCase.execute()
    }

    suspend fun update(){
        val update = getMarkers()
        markerList.postValue(update)
    }


}