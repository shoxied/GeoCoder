package com.android.example.geocoder1.domain.usecase

import android.content.Context
import android.widget.Toast
import com.android.example.geocoder1.R
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError

class SearchListener(ctx: Context): Session.SearchListener {

    private val imageMark: ImageProvider = ImageProvider.fromResource(ctx, R.drawable.markgeo)

    private var searchManager: SearchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    private lateinit var searchSession: Session

    fun SearchResponse(response: Response, mapView: MapView) {
        val mapObjects: MapObjectCollection = mapView.map.mapObjects
        mapObjects.clear()
        for(searchResult in response.collection.children){
            val resultLocation = searchResult.obj!!.geometry[0].point!!
            if(response!=null){
                mapView.map.mapObjects.addPlacemark(resultLocation, imageMark)
                mapView.mapWindow.map.move(
                    CameraPosition(resultLocation, 14.0f, 0.0f, 0.0f),
                    com.yandex.mapkit.Animation(com.yandex.mapkit.Animation.Type.SMOOTH, 2.0f),
                    null)
            }
        }
    }

    fun SearchError(error: Error, ctx: Context) {
        var errorMessage = "Unknown error"
        if (error is RemoteError){
            errorMessage = "Remote error"
        }
        else if (error is NetworkError){
            errorMessage = "Network error"
        }
        Toast.makeText(ctx, errorMessage, Toast.LENGTH_SHORT).show()
    }

    fun makeQuery(query: String, mapView: MapView){
        searchSession = searchManager.submit(query, VisibleRegionUtils.toPolygon(mapView.map.visibleRegion), SearchOptions(), this)
    }

    override fun onSearchResponse(p0: Response) {
        TODO("Not yet implemented")
    }

    override fun onSearchError(p0: Error) {
        TODO("Not yet implemented")
    }
}