package com.android.example.geocoder1.domain.usecase

import com.android.example.geocoder1.domain.models.SaveListHistoryParam
import com.android.example.geocoder1.domain.repository.MyRepository

class SaveTextHistoryUseCase(private val repository: MyRepository) {
    fun execute(param: SaveListHistoryParam): Boolean {
        return repository.saveHistory(param)
    }
}