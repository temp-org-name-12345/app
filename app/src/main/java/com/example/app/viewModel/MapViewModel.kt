package com.example.app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app.model.Location
import com.example.app.repository.KakaoMapRepository
import kotlinx.coroutines.launch

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
    private val mapRepository: KakaoMapRepository = KakaoMapRepository()
) : ViewModel() {

    private val _searchInfo = MutableLiveData<Location>()
    val searchInfo : LiveData<Location> get() = _searchInfo
    val onLocationSearch = { query: String -> searchLocationInfo(query) }

    private fun searchLocationInfo(query: String) {
        viewModelScope.launch {
            val result = mapRepository.searchLocation(key, query)

            if (result.isSuccessful) {
                _searchInfo.value = result.body()
            }
        }
    }
}