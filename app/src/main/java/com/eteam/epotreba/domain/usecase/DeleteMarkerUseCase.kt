package com.eteam.epotreba.domain.usecase

import com.eteam.epotreba.data.repository.MarkerRepository

class DeleteMarkerUseCase(private val repository: MarkerRepository) {

    suspend fun execute(id: String) {
        repository.deleteData(id)
    }
}