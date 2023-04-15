package com.eteam.epotreba.domain.usecase

import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.domain.models.MarkerModel

class SaveMarkerUseCase {
    private val repo = MarkerRepository()

    fun execute(mark: MarkerModel){
        repo.saveData(markerModel = mark)
    }

}