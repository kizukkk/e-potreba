package com.eteam.epotreba.presentation.viewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.domain.models.MarkerModel
import com.eteam.epotreba.domain.usecase.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@SuppressLint("StaticFieldLeak")
class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val context = getApplication<Application>().applicationContext
    private val repository: MarkerRepository = MarkerRepository(context)

    var markerList: MutableLiveData<List<MarkerModel>> =
        MutableLiveData<List<MarkerModel>>(emptyList())

    var temp: String = ""

    val currentUser = FirebaseAuth.getInstance().currentUser

    lateinit var passMarker: MarkerModel


    private val commitMarkerUseCase by lazy(LazyThreadSafetyMode.NONE) {
        CommitMarkerUseCase(repository = repository)
    }

    private val getMarkersUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetMarkersUseCase(repository = repository)
    }

    private val deleteMarkerUseCase by lazy(LazyThreadSafetyMode.NONE) {
        DeleteMarkerUseCase(repository = repository)
    }

    private val updateMarkerUseCase by lazy(LazyThreadSafetyMode.NONE) {
        UpdateMarkerUseCase(repository = repository)
    }

    private val voteContainsMarkerUseCase by lazy(LazyThreadSafetyMode.NONE) {
        VoteContainsMarkerUseCase(repository = repository)
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

    suspend fun commitMarker( rating: Double){
        passMarker.votes += 1
        passMarker.sumRate += rating
        updateMarker(passMarker)
        commitMarkerUseCase.execute(passMarker.id, currentUser!!.uid)
    }

    suspend fun checkVoteMarker():Boolean{
        return voteContainsMarkerUseCase.execute(passMarker.id, currentUser!!.uid)
    }

}