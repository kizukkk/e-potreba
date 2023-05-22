package com.eteam.epotreba.domain.usecase

import com.eteam.epotreba.data.repository.MarkerRepository

class CommitMarkerUseCase(private val repository: MarkerRepository) {

    suspend fun execute(id: String, uid: String){
        repository.commitMarker(id, uid)
    }

}