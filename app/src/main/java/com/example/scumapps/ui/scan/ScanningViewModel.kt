package com.example.scumapps.ui.scan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scumapps.service.ApiConfig
import com.example.scumapps.service.RekomendasiItem
import com.example.scumapps.service.ScanResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanningViewModel : ViewModel() {

    private val _wasteType = MutableLiveData<String>()
    val wasteType: LiveData<String> = _wasteType

    private val _listRecommend = MutableLiveData<List<RekomendasiItem>>()
    val listRecommend: LiveData<List<RekomendasiItem>> = _listRecommend

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadImageApi(img: MultipartBody.Part) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadPicture(img)
        client.enqueue(object : Callback<ScanResponse> {
            override fun onResponse(
                call: Call<ScanResponse>,
                response: Response<ScanResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _wasteType.value = responseBody.hasilDeteksi
                        _listRecommend.value = responseBody.rekomendasi
                        Log.e(TAG, "Api Successfully Called")
                    }
                } else {
                    Log.e(TAG, "onFailure: data not found")
                }
            }

            override fun onFailure(call: Call<ScanResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: image failed to upload")
            }
        })
    }
    companion object {
        private const val TAG = "ScanningViewModel"
    }
}