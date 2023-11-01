package com.android.example.geocoder1.data.repository

import android.util.Log
import com.android.example.geocoder1.domain.models.ListHistory
import com.android.example.geocoder1.domain.repository.MyFileRepository
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FileRepositoryImpl{
    fun putDataToAsset (listHistory: ListHistory, path: File){
        FileOutputStream(File(path, "history.json")).use { outputStream ->
            ObjectMapper().writeValue(outputStream, listHistory)
        }
    }

    fun readDataFromAsset (path: File): ListHistory{
        return FileInputStream(File(path,"history.json")).use {inputStream ->
            ObjectMapper().readValue(inputStream, ListHistory::class.java)
        }
    }
}