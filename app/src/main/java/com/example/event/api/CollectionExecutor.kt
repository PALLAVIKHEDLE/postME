package com.example.event.api


import android.content.ContentValues.TAG
import android.content.Intent
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.event.GetCollectionFragment
import com.example.event.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import android.annotation.SuppressLint
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "CollectionListApi"



class CollectionExecutor {
    @SuppressLint("MissingInflatedId")
    private val api: CollectionListApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.getpostman.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.api = retrofit.create(CollectionListApi::class.java)
    }

 fun getCollectionLists():LiveData<List<CollectionList>>{
    val responseLiveData: MutableLiveData<List<CollectionList>> = MutableLiveData()
     Log.e(TAG, "responseLiveData  $responseLiveData")

    val collectionListRequest: Call<CollectionListResponseData> = this.api.getCollectionList()
     Log.e(TAG, "collectionListRequest  $collectionListRequest")


     collectionListRequest.enqueue(object: Callback<CollectionListResponseData> {

        override fun onFailure(call: Call<CollectionListResponseData>, t: Throwable) {
            Log.e(TAG, "Response received from collection List fetch failed")
        }

        override fun onResponse(
            call: Call<CollectionListResponseData>,
            response: Response<CollectionListResponseData>
        ) {
            Log.d(TAG, "Response received from  get collection endpoint")

            val GetCollectionListResponse: CollectionListResponseData? = response.body()
            Log.d(TAG, "GetCollectionListResponse: $GetCollectionListResponse ")


            var finalList: List<CollectionList> = GetCollectionListResponse?.templates ?: mutableListOf()
            finalList = finalList.filterNot {
                it.name.isBlank()
            }
            responseLiveData.value = finalList
            Log.d(TAG, "FinalList: $finalList ")

        }
    })

    return responseLiveData
 }

    fun createCollection(collectionName: String,api_name:String, api_type:String ): LiveData<CreateCollectionResponseData> {
        val responseLiveData: MutableLiveData<CreateCollectionResponseData> = MutableLiveData()


        val myRequest = MyRequest(
            Collection(
                Info(
                    collectionName,
                    "This collection makes a request to the Postman Echo service to get a list of request headers sent by an HTTP client.",
                    "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
                ),
                listOf(
                    Item(
                        api_name,
                        listOf(
                            Event(
                                "test",
                                Script(
                                    "7d2334fc-a84a-4c3d-b26c-7529afa4c0ae",
                                    listOf(
                                        "pm.test(\"Status code is 200\", function () {",
                                        "    pm.response.to.have.status(200);",
                                        "});"
                                    ),
                                    "text/javascript"
                                )
                            )
                        ),
                        Request(
                            "https://echo.getpostman.com/headers",
                            api_type,
                            listOf(
                                Header(
                                    "Content-Type",
                                    "application/json"
                                )
                            )
                        )
                    )
                    )
                )
            )




        val createCollectionRequest: Call<CreateCollectionResponseData> = this.api.createCollection(
          myRequest
        )

        Log.d(TAG, "Enquieuing  a request to create the collection # $collectionName $api_name, $api_type ")
        createCollectionRequest.enqueue(object: Callback<CreateCollectionResponseData> {

            override fun onFailure(call: Call<CreateCollectionResponseData>, t: Throwable) {
                Log.e(TAG, "Failed to Create new collection!")
            }

            override fun onResponse(
                call: Call<CreateCollectionResponseData>,
                response: Response<CreateCollectionResponseData>
            ) {
                Log.d(TAG, "response received from create new collection endpoint $myRequest")
                val createCollectionResponse: CreateCollectionResponseData? = response.body()
                Log.d(TAG, "response received from create new collection endpoint $createCollectionResponse, $response")

//                if (createCollectionResponse != null && createCollectionResponse.success) {
                if (createCollectionResponse != null ) {
                    Log.d(TAG, "Got new collection")
                } else {
                    Log.d(TAG, "Request for new collection has failed")
//                    Log.d(TAG, "Request to caption image has failed: ${CreateCollectionResponseData?.errorMessage}")
                }
            }
        })

        return responseLiveData
    }
}

