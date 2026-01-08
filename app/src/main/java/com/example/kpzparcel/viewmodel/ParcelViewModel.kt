package com.example.kpzparcel.viewmodel

import androidx.lifecycle.ViewModel
import com.example.kpzparcel.api.ParcelApi
import com.example.kpzparcel.model.ParcelData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Define the states for the UI (Loading, Success, Error)
sealed class ParcelUiState {
    object Loading : ParcelUiState()
    data class Success(val parcel: ParcelData) : ParcelUiState()
    data class Error(val message: String) : ParcelUiState()
}

class ParcelViewModel : ViewModel() {

    // Setup Retrofit connection
    private val api: ParcelApi = Retrofit.Builder()
        .baseUrl("https://kpzparcel.my/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ParcelApi::class.java)

    // State holder
    private val _uiState = MutableStateFlow<ParcelUiState>(ParcelUiState.Loading)
    val uiState: StateFlow<ParcelUiState> = _uiState.asStateFlow()

    // Function to fetch data
    fun fetchParcel(trackingId: String) {
        _uiState.value = ParcelUiState.Loading

        api.getParcelInfo(trackingId).enqueue(object : Callback<com.example.kpzparcel.model.ParcelResponse> {
            override fun onResponse(call: Call<com.example.kpzparcel.model.ParcelResponse>, response: Response<com.example.kpzparcel.model.ParcelResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = ParcelUiState.Success(response.body()!!.data)
                } else {
                    _uiState.value = ParcelUiState.Error("Parcel not found")
                }
            }

            override fun onFailure(call: Call<com.example.kpzparcel.model.ParcelResponse>, t: Throwable) {
                _uiState.value = ParcelUiState.Error(t.message ?: "Network Error")
            }
        })
    }
}