package com.android.example.geocoder1.data.repository

import com.android.example.geocoder1.data.storage.FileStorage
import com.android.example.geocoder1.data.storage.History
import com.android.example.geocoder1.domain.models.ListHistory
import com.android.example.geocoder1.domain.repository.MyFileRepository
import java.io.File

class FileRepositoryImpl(private val fileStorage: FileStorage): MyFileRepository{
    override fun putDataToAsset(listHistory: ListHistory, path: File): Boolean {
        val history = History(history = listHistory.listHistory)

        return fileStorage.putDataToAsset(history, path)
    }

    override fun readDataFromAsset(path: File): ListHistory {
        val history = fileStorage.readDataFromAsset(path = path)

        return ListHistory(listHistory = history.history)
    }
}