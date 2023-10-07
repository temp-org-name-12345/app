package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app.repository.KakaoMapRepository

class MapViewModelFactory(private val key: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(key) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

class MapViewModel(
    private val key: String,
    private val mapRepository: KakaoMapRepository = KakaoMapRepository
) : ViewModel() {


}