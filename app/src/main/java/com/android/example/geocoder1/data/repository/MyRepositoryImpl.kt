package com.android.example.geocoder1.data.repository

import android.content.Context
import com.android.example.geocoder1.domain.models.ListHistory
import com.android.example.geocoder1.domain.models.SaveListHistoryParam
import com.android.example.geocoder1.domain.repository.MyRepository

private var listHistory: MutableList<String> = mutableListOf()

class MyRepositoryImpl(private val context: Context): MyRepository {

    override fun saveHistory(saveParam: SaveListHistoryParam): Boolean{
        listHistory = saveParam.listHistory
        return true
    }

    override fun getHistory(): ListHistory {
        return ListHistory(listHistory = mutableListOf("jhjhj", "ghhg"))
    }
}