package com.example.event.api

import com.google.gson.annotations.SerializedName

class CreateCollectionResponseData {
    @SerializedName("success")
    var success: Boolean = false

    @SerializedName("error_message")
    lateinit var errorMessage: String

//    @SerializedName("collections")
//    lateinit var templates: List<CollectionList>


    @SerializedName("collectionName")
    lateinit var collectionName: String

    @SerializedName("api_name")
    lateinit var api_name: String

    @SerializedName("api_type")
    lateinit var api_type: String


}