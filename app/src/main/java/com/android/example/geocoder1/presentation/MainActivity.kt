package com.android.example.geocoder1.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.example.geocoder1.R
import com.yandex.mapkit.MapKitFactory
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
import com.android.example.geocoder1.domain.recyclerview.HistoryRecyclerAdapter
import com.android.example.geocoder1.domain.models.ListHistory
import java.lang.Exception

class MainActivity : AppCompatActivity(), Session.SearchListener, HistoryRecyclerAdapter.Listener{

    private var historyList: ListHistory = ListHistory()

    private lateinit var mapView: MapView

    private lateinit var imageMark: ImageProvider

    private lateinit var searchManager: SearchManager
    private lateinit var searchSession: Session

    private lateinit var historyCardView: androidx.cardview.widget.CardView
    private lateinit var searchEditText: EditText

    private lateinit var recyclerViewHistory: RecyclerView
    private var isAnimationOff: Boolean = true

    private lateinit var historyViewAnimationOn: android.view.animation.Animation
    private lateinit var historyViewAnimationOff: android.view.animation.Animation

    private lateinit var historyClearButton: Button

    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("76b1bf32-c4dd-4039-a5e0-a879a159d132")
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)
        vm = MainViewModel()

        historyClearButton = findViewById(R.id.historyClearButton)

        searchEditText = findViewById(R.id.location)

        recyclerViewHistory = findViewById(R.id.RecyclerViewHistory)
        historyCardView = findViewById(R.id.historyCardView)

        historyViewAnimationOn = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.history_view_animation_on)
        historyViewAnimationOff = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.history_view_animation_off)
        historyCardView.startAnimation(historyViewAnimationOff)

        mapView = findViewById(R.id.mapview)
        imageMark = ImageProvider.fromResource(this, R.drawable.markgeo)

        SearchFactory.initialize(this)
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

        recyclerViewHistory.adapter = HistoryRecyclerAdapter(vm.readData(applicationContext.dataDir).listHistory, this)
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
            historyList.listHistory.add("${searchEditText.text}")
            recyclerViewHistory.adapter = HistoryRecyclerAdapter(historyList.listHistory, this)
            vm.putData(historyList, applicationContext.dataDir)
            Log.i("fileHistory", "$historyList")
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
            recyclerViewHistory.visibility = View.VISIBLE

        }
        else{
            historyCardView.startAnimation(historyViewAnimationOff)
            historyCardView.visibility = View.GONE
            historyClearButton.visibility = View.GONE
            recyclerViewHistory.visibility = View.GONE
            isAnimationOff = true
        }
    }

    fun historyClear(view: View) {
        historyList.listHistory.clear()
        recyclerViewHistory.adapter = HistoryRecyclerAdapter(historyList.listHistory, this)
        vm.putData(historyList, applicationContext.dataDir)
        historyCardView.startAnimation(historyViewAnimationOff)
        historyCardView.visibility = View.GONE
        historyClearButton.visibility = View.GONE
        recyclerViewHistory.visibility = View.GONE
        isAnimationOff = true
    }

    override fun clickHistoryItem(itemViewText: TextView) {
        val tempText: TextView = itemViewText.findViewById(R.id.textHistory)
        searchEditText.setText(tempText.text)
    }
}