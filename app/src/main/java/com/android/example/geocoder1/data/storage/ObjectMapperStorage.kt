package com.android.example.geocoder1.data.storage

import com.android.example.geocoder1.domain.models.ListHistory
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ObjectMapperStorage: FileStorage {
    override fun putDataToAsset(history: History, path: File): Boolean {

        FileOutputStream(File(path, "history.json")).use { outputStream ->
            ObjectMapper().writeValue(outputStream, history)
        }

        return true
    }

    override fun readDataFromAsset(path: File): History{

        return FileInputStream(File(path,"history.json")).use { inputStream ->
            ObjectMapper().readValue(inputStream, History::class.java)
        }
    }
}