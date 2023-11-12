package com.android.example.geocoder1.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.geocoder1.domain.models.ListHistory
import com.android.example.geocoder1.domain.usecase.PutDataToAssetUseCase
import com.android.example.geocoder1.domain.usecase.ReadDataFromAssetUseCase
import java.io.File
import java.lang.Exception

class MainViewModel(
    private val putDataToAssetUseCase: PutDataToAssetUseCase,
    private val readDataFromAssetUseCase: ReadDataFromAssetUseCase
    ): ViewModel() {

    private var historyLive = MutableLiveData<ListHistory>()

    fun getHistoryLive(): LiveData<ListHistory>{
        return historyLive
    }

    fun readData(file: File) {
        historyLive.value = readDataFromAssetUseCase.execute(file)
    }

    fun putData(historyList: ListHistory, file: File){
        putDataToAssetUseCase.execute(historyList, file)
    }
}