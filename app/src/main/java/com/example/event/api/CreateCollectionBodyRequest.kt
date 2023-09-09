package com.example.event.api
import com.google.gson.annotations.SerializedName


data class CreateCollectionRequestBody(
    @SerializedName("collectionName") val collectionName: String,
    @SerializedName("api_name") val apiName: String,
    @SerializedName("api_type") val apiType: String
)