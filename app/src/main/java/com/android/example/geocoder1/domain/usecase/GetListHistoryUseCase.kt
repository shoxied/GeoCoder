package com.android.example.geocoder1.domain.usecase

import com.android.example.geocoder1.domain.models.ListHistory
import com.android.example.geocoder1.domain.repository.MyRepository

class GetListHistoryUseCase(private val repository: MyRepository) {
    fun execute(): ListHistory{
        return repository.getHistory()
    }
}