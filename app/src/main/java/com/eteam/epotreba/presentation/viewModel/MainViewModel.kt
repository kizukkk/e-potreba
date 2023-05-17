package com.eteam.epotreba.presentation.viewModel

import androidx.lifecycle.*
import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.domain.models.MarkerModel
import com.eteam.epotreba.domain.usecase.DeleteMarkerUseCase
import com.eteam.epotreba.domain.usecase.GetMarkersUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    var markerList: MutableLiveData<List<MarkerModel>> =
        MutableLiveData<List<MarkerModel>>(emptyList())

    val currentUser = FirebaseAuth.getInstance().currentUser

    lateinit var passMarker: MarkerModel

    private val getMarkersUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetMarkersUseCase(repository = MarkerRepository())
    }

    private val deleteMarkerUseCase by lazy(LazyThreadSafetyMode.NONE) {
        DeleteMarkerUseCase(repository = MarkerRepository())
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

    suspend fun delete(marker: MarkerModel){
        deleteMarkerUseCase.execute(marker.id)
    }


    fun passMarkerToFragment(marker: MarkerModel){
        passMarker = marker
    }


}