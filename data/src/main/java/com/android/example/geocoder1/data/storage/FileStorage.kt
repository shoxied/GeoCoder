package com.android.example.geocoder1.data.storage

import java.io.File

interface FileStorage {

    fun putDataToAsset (history: History, path: File)

    fun readDataFromAsset (path: File): History

}