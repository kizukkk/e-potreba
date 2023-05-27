package com.eteam.epotreba.presentation.viewModel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.domain.models.MarkerModel
import com.eteam.epotreba.domain.usecase.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@SuppressLint("StaticFieldLeak")
class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val context = getApplication<Application>().applicationContext
    private val repository: MarkerRepository = MarkerRepository(context)

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    var markerList: MutableLiveData<List<MarkerModel>> =
        MutableLiveData<List<MarkerModel>>(emptyList())

    var favoriteList: MutableLiveData<List<String>> =
        MutableLiveData<List<String>>(emptyList())

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

    private val favoriteMarkerUseCase by lazy(LazyThreadSafetyMode.NONE) {
        FavoriteMarkerUseCase(repository = repository)
    }

    init {
        viewModelScope.launch {
            updateList()
        }
    }

    private suspend fun getMarkers(): List<MarkerModel> {
        return getMarkersUseCase.execute()
    }

    suspend fun updateList() {
        val update = getMarkers()
        favoriteList.postValue(getFavorite())
        for (marker in update) {
            marker.distance = getDistance(marker.position)
        }

        markerList.postValue(update)
    }

    suspend fun delete(marker: MarkerModel) {
        deleteMarkerUseCase.execute(marker.id)
    }

    fun passMarkerToFragment(marker: MarkerModel) {
        passMarker = marker
    }

    fun updateMarker(marker: MarkerModel) {
        return updateMarkerUseCase.execute(marker)
    }

    suspend fun commitMarker(rating: Double) {
        passMarker.votes += 1
        passMarker.sumRate += rating
        updateMarker(passMarker)
        commitMarkerUseCase.execute(passMarker.id, currentUser!!.uid)
    }

    suspend fun checkVoteMarker(): Boolean {
        return voteContainsMarkerUseCase.execute(passMarker.id, currentUser!!.uid)
    }

    private suspend fun getDistance(lat: LatLng): Double {
        var dist = .0

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val markerLocation = Location(LocationManager.GPS_PROVIDER)
        markerLocation.latitude = lat.latitude; markerLocation.longitude = lat.longitude;


        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.wtf("Location Exception", "don't allow define location")
            return .0
        }

        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            val location: Location? = task.result
            if (location != null) {
                dist = location.distanceTo(markerLocation).toDouble()
            }
        }.await()


        return dist
    }

    fun saveToFavorite() {
        favoriteMarkerUseCase.add(passMarker.id, currentUser!!.uid)
        viewModelScope.launch {
            favoriteList.value = getFavorite()
        }
    }

    fun deleteFromFavorite() {
        favoriteMarkerUseCase.delete(passMarker.id, currentUser!!.uid)
        viewModelScope.launch {
            favoriteList.value = getFavorite()
        }


    }

    private suspend fun getFavorite(): List<String> {
        return favoriteMarkerUseCase.get(currentUser!!.uid)
    }

}