package com.android.example.geocoder1.domain.usecase

import com.android.example.geocoder1.data.repository.MyRepositoryImpl
import com.android.example.geocoder1.domain.models.ListHistory

class SaveListHistoryUseCase(private val repository: MyRepositoryImpl) {
    fun execute(listHistory: ListHistory): Boolean {
        return repository.saveHistory(listHistory)
    }
}