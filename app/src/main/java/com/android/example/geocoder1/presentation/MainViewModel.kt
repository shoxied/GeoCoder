package com.android.example.geocoder1.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import com.android.example.geocoder1.data.repository.FileRepositoryImpl
import com.android.example.geocoder1.data.storage.ObjectMapperStorage
import com.android.example.geocoder1.domain.models.ListHistory
import com.android.example.geocoder1.domain.usecase.PutDataToAssetUseCase
import com.android.example.geocoder1.domain.usecase.ReadDataFromAssetUseCase
import java.io.File

class MainViewModel: ViewModel() {

    private val fileRepository = FileRepositoryImpl(fileStorage = ObjectMapperStorage())

    private val putDataToAssetUseCase = PutDataToAssetUseCase(myFileRepository = fileRepository)

    private val readDataFromAssetUseCase = ReadDataFromAssetUseCase(myFileRepository = fileRepository)

    fun readData(file: File): ListHistory {
        return readDataFromAssetUseCase.execute(file)
    }

    fun putData(historyList: ListHistory, file: File){
        putDataToAssetUseCase.execute(historyList, file)
    }
}