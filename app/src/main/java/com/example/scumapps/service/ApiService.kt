package com.example.scumapps.service

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("/detect")
    fun uploadPicture(
        @Part image: MultipartBody.Part
    ): Call<ScanResponse>

//    @GET("/{waste}")
//    fun getReccomendation(
//        @Path("waste") waste: String
//    ): Call<>
}