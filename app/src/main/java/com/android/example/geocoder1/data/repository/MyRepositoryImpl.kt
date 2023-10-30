package com.android.example.geocoder1.data.repository

import com.android.example.geocoder1.domain.models.ListHistory
import com.android.example.geocoder1.domain.repository.MyRepository

class MyRepositoryImpl: MyRepository {
    override fun saveHistory(listHistory: ListHistory): Boolean{
        return true
    }

    //override fun getHistory(): ListHistory{}
}