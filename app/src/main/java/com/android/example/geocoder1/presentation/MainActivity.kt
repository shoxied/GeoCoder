package com.android.example.geocoder1.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity(), Session.SearchListener{

    private var historyList: MutableList<String> = mutableListOf()

    private lateinit var mapView: MapView

    private lateinit var imageMark: ImageProvider

    private lateinit var searchManager: SearchManager
    private lateinit var searchSession: Session

    private lateinit var historyCardView: androidx.cardview.widget.CardView
    private lateinit var searchEditText: EditText

    private lateinit var RecyclerViewHistory: RecyclerView
    private var isAnimationOff: Boolean = true

    private lateinit var historyViewAnimationOn: android.view.animation.Animation
    private lateinit var historyViewAnimationOff: android.view.animation.Animation

    private lateinit var historyClearButton: Button

    var objectMapper: ObjectMapper = ObjectMapper()
//    private fun PutDataToAsset(){
//        try {
//            val text = "sdfsdfsdffsd"
//            objectMapper.writeValue(File("geocoder1/history.json"), text)
//        }
//        catch (error:Exception){
//            Log.d("Error", "message:${error.message}")
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("76b1bf32-c4dd-4039-a5e0-a879a159d132")
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)

        try {
            val path: File
            val text:String = "sdfsdfsdffsd"
            val fileOutputStream: FileOutputStream = openFileOutput("C:\\Users\\Oleg\\AndroidStudioProjects\\GeoCoder\\app\\src\\main\\java\\com\\android\\example\\geocoder1\\history.txt", MODE_PRIVATE)
            fileOutputStream.write(text.toByteArray())
            fileOutputStream.close()
            objectMapper.writeValue(File("C:\\Users\\Oleg\\AndroidStudioProjects\\GeoCoder\\app\\src\\main\\java\\com\\android\\example\\geocoder1\\history.json"), text)
        }
        catch (error:Exception){
            Log.d("Error", "message:${error.message}")
        }
//      try {
//            jsonOnbject = JSONObject(GetDataFromAsset("history.json"))
//            jsonArray = jsonOnbject.getJSONArray("history")
//
//            for (i in 0 until jsonArray.length()) {
//                var userData: JSONObject = jsonArray.getJSONObject(i)
//                historyList.add("$userData")
//            }
//        }
//        catch (error:Exception){
//            Log.d("Error", "message: ${error.message}")
//        }
        historyClearButton = findViewById(R.id.historyClearButton)

        searchEditText = findViewById(R.id.location)

        RecyclerViewHistory = findViewById(R.id.RecyclerViewHistory)
        historyCardView = findViewById(R.id.historyCardView)

        historyViewAnimationOn = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.history_view_animation_on)
        historyViewAnimationOff = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.history_view_animation_off)
        historyCardView.startAnimation(historyViewAnimationOff)

        mapView = findViewById(R.id.mapview)
        imageMark = ImageProvider.fromResource(this, R.drawable.markgeo)

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
            if (response!=null) {
                mapView.map.mapObjects.addPlacemark(resultLocation, imageMark)
                mapView.mapWindow.map.move(
                    CameraPosition(resultLocation, 14.0f, 0.0f, 0.0f),
                    com.yandex.mapkit.Animation(com.yandex.mapkit.Animation.Type.SMOOTH, 2.0f),
                    null
                )
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
        if ("${searchEditText.text}" != "") {
            historyList.add("${searchEditText.text}")
            RecyclerViewHistory.adapter = HistoryRecyclerAdapter(historyList)
            makeQuery("${searchEditText.text}")
        }else{
            Toast.makeText(this@MainActivity, "Empty query", Toast.LENGTH_SHORT).show()
        }
    }

    fun historyAnimation(view: View) {
        if (isAnimationOff) {
            isAnimationOff = false
            historyCardView.startAnimation(historyViewAnimationOn)
            historyCardView.visibility = View.VISIBLE
            historyClearButton.visibility = View.VISIBLE
            RecyclerViewHistory.visibility = View.VISIBLE

        }
        else{
            historyCardView.startAnimation(historyViewAnimationOff)
            historyCardView.visibility = View.GONE
            historyClearButton.visibility = View.GONE
            RecyclerViewHistory.visibility = View.GONE
            isAnimationOff = true
        }
    }

    fun historyClear(view: View) {
        historyList.clear()
        RecyclerViewHistory.adapter = HistoryRecyclerAdapter(historyList)
    }
}