package com.android.example.geocoder1.data.repository

import com.android.example.geocoder1.domain.models.SaveListHistoryParam
import com.android.example.geocoder1.domain.repository.MyRepository

class MyRepositoryImpl: MyRepository {
    override fun saveHistory(saveListHistory: SaveListHistoryParam): Boolean{
        return true
    }

    //fun getHistory(): MutableList<String> {}
}