package com.example.event.api

import com.google.gson.annotations.SerializedName

class CollectionListResponseData {
    @SerializedName("collections")
    lateinit var templates: List<CollectionList>

}