package com.android.example.geocoder1.domain.repository

import com.android.example.geocoder1.domain.models.ListHistory
import java.io.File


interface MyFileRepository {
    fun putDataToAsset (listHistory: MutableList<String>, path: File)

    fun readDataFromAsset (path: File): ListHistory
}