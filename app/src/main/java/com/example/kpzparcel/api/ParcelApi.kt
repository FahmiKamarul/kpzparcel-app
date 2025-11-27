package com.example.kpzparcel.api

import com.example.kpzparcel.model.ParcelResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ParcelApi {
    @GET("api/parcels/{id}")
    fun getParcelInfo(@Path("id") parcelId: String): Call<ParcelResponse>
}