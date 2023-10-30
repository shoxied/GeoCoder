package com.android.example.geocoder1.domain.repository

import com.android.example.geocoder1.domain.models.ListHistory


interface MyRepository {
    fun saveHistory(listHistory: ListHistory): Boolean

    //fun getHistory(): ListHistory
}