package com.android.example.geocoder1.presentation

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.example.geocoder1.R
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
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
import com.android.example.geocoder1.domain.usecase.HistoryRecyclerAdapter
import com.android.example.geocoder1.domain.usecase.SearchListener

private const val API_KEY = "76b1bf32-c4dd-4039-a5e0-a879a159d132"
class MainActivity : AppCompatActivity(), Session.SearchListener{

    var historyList: MutableList<String> = mutableListOf()

    lateinit var mapView: MapView

    lateinit var imageMark: ImageProvider
    val startLocation: Point = Point(55.030264, 82.922684)

    lateinit var searchManager: SearchManager
    lateinit var searchSession: Session

    lateinit var historyLayout: androidx.cardview.widget.CardView
    lateinit var location: EditText

    lateinit var RecyclerViewHistory: RecyclerView
    var isAnimationOff: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(API_KEY)
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)

        RecyclerViewHistory = findViewById(R.id.RecyclerViewHistory)
        historyLayout = findViewById(R.id.historyLayout)

        location = findViewById(R.id.location)

        historyLayout.alpha = 0.0f

        mapView = findViewById(R.id.mapview)
        imageMark = ImageProvider.fromResource(this, R.drawable.markgeo)
        mapView.mapWindow.map.move(CameraPosition(startLocation, 14.0f, 0.0f, 0.0f),
            com.yandex.mapkit.Animation(com.yandex.mapkit.Animation.Type.SMOOTH, 5.0f),
            null
        )
        mapView.map.mapObjects.addPlacemark(startLocation, imageMark)

        SearchFactory.initialize(this)
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onSearchResponse(response: Response) {
        val mapObjects: MapObjectCollection = mapView.map.mapObjects
        mapObjects.clear()
        for(searchResult in response.collection.children){
            val resultLocation = searchResult.obj!!.geometry[0].point!!
            if(response!=null){
                mapView.map.mapObjects.addPlacemark(resultLocation, imageMark)
                mapView.mapWindow.map.move(CameraPosition(resultLocation, 14.0f, 0.0f, 0.0f),
                    com.yandex.mapkit.Animation(com.yandex.mapkit.Animation.Type.SMOOTH, 2.0f),
                    null)
            }
        }
    }

    override fun onSearchError(error: Error) {
        var errorMessage = "Unknown error"
        if (error is RemoteError){
            errorMessage = "Remote error"
        }
        else if (error is NetworkError){
            errorMessage = "Network error"
        }
        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
    }

    fun makeQuery(query: String){
        searchSession = searchManager.submit(query, VisibleRegionUtils.toPolygon(mapView.map.visibleRegion), SearchOptions(), this)
    }

    fun MoveToLocation(view: View) {
        if ("${location.text}" != "") {
            historyList.add("${location.text}")
            RecyclerViewHistory.adapter = HistoryRecyclerAdapter(historyList)
            makeQuery("${location.text}")
        }else{
            Toast.makeText(this@MainActivity, "Empty query", Toast.LENGTH_SHORT).show()
        }
    }

    fun historyAnimation(view: View) {
        if (isAnimationOff) {
            historyLayout.alpha = 1.0f
            isAnimationOff = false
        }
        else{
            historyLayout.alpha = 0.0f
            isAnimationOff = true
        }
    }

    fun historyClear(view: View) {
        historyList.clear()
        RecyclerViewHistory.adapter = HistoryRecyclerAdapter(historyList)
    }
}