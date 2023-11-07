package com.android.example.geocoder1.domain.usecase

import com.android.example.geocoder1.domain.models.ListHistory
import com.android.example.geocoder1.domain.repository.MyFileRepository
import java.io.File

class ReadDataFromAssetUseCase(private val myFileRepository: MyFileRepository) {

    fun execute(path: File): ListHistory {
        return myFileRepository.readDataFromAsset(path = path)
    }
}