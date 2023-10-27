package com.android.example.geocoder1.domain.repository

import com.android.example.geocoder1.domain.models.SaveListHistoryParam

interface MyRepository {
    fun saveHistory(saveListHistory: SaveListHistoryParam): Boolean
}