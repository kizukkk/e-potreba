package com.eteam.epotreba.presentation.viewModel

import androidx.lifecycle.*
import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.domain.models.MarkerModel
import com.eteam.epotreba.domain.usecase.DeleteMarkerUseCase
import com.eteam.epotreba.domain.usecase.GetMarkersUseCase
import com.eteam.epotreba.domain.usecase.UpdateMarkerUseCase
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

    private val updateMarkerUseCase by lazy(LazyThreadSafetyMode.NONE) {
        UpdateMarkerUseCase(repository = MarkerRepository())
    }
    init {
        viewModelScope.launch {
            updateList()
        }
    }

    private suspend fun getMarkers(): List<MarkerModel> {
        return getMarkersUseCase.execute()
    }

    suspend fun updateList(){
        val update = getMarkers()
        markerList.postValue(update)
    }

    suspend fun delete(marker: MarkerModel){
        deleteMarkerUseCase.execute(marker.id)
    }


    fun passMarkerToFragment(marker: MarkerModel){
        passMarker = marker
    }

    fun updateMarker(marker: MarkerModel){
        return updateMarkerUseCase.execute(marker)
    }


}