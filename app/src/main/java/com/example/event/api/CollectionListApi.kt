package com.example.event.api

// Retrofit interface

import retrofit2.Call
import retrofit2.http.*

interface CollectionListApi {
    @Headers("x-api-key:PMAK-644ab752a319152b0ca6432c-4f1c88660036403ae2a00e82e3d352bd59")
    @GET("collections")
    fun getCollectionList() : Call<CollectionListResponseData>


    @Headers("x-api-key:PMAK-644ab752a319152b0ca6432c-4f1c88660036403ae2a00e82e3d352bd59")
    @POST("collections")
    fun createCollection(@Body request: MyRequest): Call<CreateCollectionResponseData>

//    fun createCollection(
//        @Field("collectionName")collectionName:String,
//        @Field("api_name")api_name:String,
//        @Field("api_type")api_type:String,
//  )
//    :Call<CreateCollectionResponseData>
//    suspend fun createCollection(@Body request: MyRequest): Response<CreateCollectionResponseData>

//    fun createCollection(
//        @Field("collectionName")collectionName:String,
//        @Field("api_name")api_name:String,
//        @Field("api_type")api_type:String,
//    ): Call<CreateCollectionResponseData  @Body requestBody: CreateCollectionRequestBody>

}
