package com.android.example.geocoder1.domain.usecase

import com.android.example.geocoder1.domain.models.ListHistory
import com.android.example.geocoder1.domain.repository.MyFileRepository
import java.io.File

class PutDataToAssetUseCase(private val myFileRepository: MyFileRepository) {

    fun execute(listHistory: ListHistory, path: File): Boolean{
        return myFileRepository.putDataToAsset(listHistory = listHistory, path = path)
    }
}