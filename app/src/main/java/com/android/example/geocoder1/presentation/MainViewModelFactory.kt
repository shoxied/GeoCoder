package com.android.example.geocoder1.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.example.geocoder1.data.repository.FileRepositoryImpl
import com.android.example.geocoder1.data.storage.ObjectMapperStorage
import com.android.example.geocoder1.domain.usecase.PutDataToAssetUseCase
import com.android.example.geocoder1.domain.usecase.ReadDataFromAssetUseCase

class MainViewModelFactory: ViewModelProvider.Factory {

    private val fileRepository =
        FileRepositoryImpl(fileStorage = ObjectMapperStorage())

    private val putDataToAssetUseCase =
        PutDataToAssetUseCase(myFileRepository = fileRepository)

    private val readDataFromAssetUseCase =
        ReadDataFromAssetUseCase(myFileRepository = fileRepository)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(putDataToAssetUseCase, readDataFromAssetUseCase) as T
    }
}