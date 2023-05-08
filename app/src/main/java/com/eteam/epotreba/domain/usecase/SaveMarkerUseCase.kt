package com.eteam.epotreba.domain.usecase

import com.eteam.epotreba.data.repository.MarkerRepository
import com.eteam.epotreba.domain.models.MarkerModel

class SaveMarkerUseCase(val repository: MarkerRepository) {

    fun execute(mark: MarkerModel){
        repository.saveData(marker = mark)
    }

}