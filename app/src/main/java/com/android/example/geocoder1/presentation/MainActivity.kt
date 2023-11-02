package com.android.example.geocoder1.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.example.geocoder1.R
import com.android.example.geocoder1.data.repository.FileRepositoryImpl
import com.android.example.geocoder1.data.storage.ObjectMapperStorage
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
import com.android.example.geocoder1.domain.usecase.PutDataToAssetUseCase
import com.android.example.geocoder1.domain.usecase.ReadDataFromAssetUseCase
import java.lang.Exception

class MainActivity : AppCompatActivity(), Session.SearchListener{



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

    private val fileRepository = FileRepositoryImpl(fileStorage = ObjectMapperStorage())

    private val putDataToAssetUseCase = PutDataToAssetUseCase(myFileRepository = fileRepository)

    private val readDataFromAssetUseCase = ReadDataFromAssetUseCase(myFileRepository = fileRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("76b1bf32-c4dd-4039-a5e0-a879a159d132")
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)

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

        try {
            recyclerViewHistory.adapter = HistoryRecyclerAdapter(readDataFromAssetUseCase.execute(applicationContext.dataDir).listHistory)
        }
        catch (error:Exception){
            Log.d("Error", "$error")
        }
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
            recyclerViewHistory.adapter = HistoryRecyclerAdapter(historyList.listHistory)
            putDataToAssetUseCase.execute(historyList, applicationContext.dataDir)
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
        recyclerViewHistory.adapter = HistoryRecyclerAdapter(historyList.listHistory)
        fileRepository.putDataToAsset(historyList, applicationContext.dataDir)
    }

    fun readData(view: View) {
        try {
            Log.i("filesDir", applicationContext.dataDir.absolutePath)
            val messageFile = readDataFromAssetUseCase.execute(applicationContext.dataDir)
            Log.i("fileHistoryMessageFile", "$messageFile")
        }
        catch (e:Exception){
            Log.d("EError", "${e.message}")
        }
    }
}