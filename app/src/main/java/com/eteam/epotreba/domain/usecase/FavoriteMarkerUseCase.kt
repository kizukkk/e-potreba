package com.eteam.epotreba.domain.usecase

import com.eteam.epotreba.data.repository.MarkerRepository

class FavoriteMarkerUseCase(private val repository: MarkerRepository) {

    fun add(id: String, uid: String){
        repository.saveToFavorite(id, uid)
    }

    fun delete(id: String, uid: String){
        repository.deleteFromFavorite(id, uid)
    }

    suspend fun get(uid: String): List<String>{
        return repository.getFavorite(uid)
    }

}