package com.android.example.geocoder1.domain.usecase

import com.android.example.geocoder1.data.repository.MyRepositoryImpl
import com.android.example.geocoder1.domain.models.SaveListHistoryParam

class SaveListHistoryUseCase(private val repository: MyRepositoryImpl) {
    fun execute(param: SaveListHistoryParam): Boolean {
        return repository.saveHistory(param)
    }
}