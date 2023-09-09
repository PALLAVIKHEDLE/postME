package com.example.event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.event.api.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val TAG = "CollectionViewModel"
const val INITIAL_VALUE ="Test"

class CollectionViewModel(): ViewModel() {
    private var collectionName: String? =INITIAL_VALUE

    val collectionListLiveData: LiveData<List<CollectionList>> = CollectionExecutor().getCollectionLists();


    private var createCollectionLiveData: LiveData<CreateCollectionResponseData> = MutableLiveData()

    fun createCollection(collectionName: String, api_name:String, api_type:String): LiveData<CreateCollectionResponseData> {
        Log.d(TAG, "Received request to create collection ")
        this.createCollectionLiveData = CollectionExecutor().createCollection(
            collectionName, api_name, api_type

        )
        return this.createCollectionLiveData

    }



    override fun onCleared() {
        super.onCleared()
        Log.d("MainActivity", "OnCleared of CounterViewActivity called")
    }

    private val prefs = PreferencesRepository.getRepository()
    fun getCollectionName(): String? {
        return this.collectionName
    }
    fun setCollectionName(b: String) {
        saveCollectionName(b)
    }
    fun saveCollectionName(collectionName: String) {
        viewModelScope.launch {
            prefs.saveCollectionName(collectionName)
        }
    }
    fun loadCollectionName() {
        GlobalScope.launch {
            prefs.collectionName.collectLatest {
                collectionName = it.toString()
            }
        }
        Thread.sleep(1000)
    }

}