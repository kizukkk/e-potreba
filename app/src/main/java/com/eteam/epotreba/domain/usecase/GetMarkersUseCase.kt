package com.eteam.epotreba.domain.usecase

import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.domain.models.MarkerModel

class GetMarkersUseCase(private val repository: MarkerRepository) {

    suspend fun execute(): List<MarkerModel> {
        return repository.getData()
    }
}