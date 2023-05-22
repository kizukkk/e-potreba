package com.eteam.epotreba.domain.usecase

import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.domain.models.MarkerModel

class VoteContainsMarkerUseCase(private val repository: MarkerRepository) {

    suspend fun execute(id: String, uid: String): Boolean{
        return repository.voteContains(id, uid)
    }

}