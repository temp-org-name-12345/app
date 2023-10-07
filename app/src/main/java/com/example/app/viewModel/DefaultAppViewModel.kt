package com.example.app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app.repository.DefaultAppRepository
import kotlinx.coroutines.launch

class DefaultAppViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DefaultAppViewModel::class.java)) {
            return DefaultAppViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

class DefaultAppViewModel(
    private val defaultAppRepository : DefaultAppRepository = DefaultAppRepository
) : ViewModel() {

    private val _thumbnailImage : MutableLiveData<String> = MutableLiveData()
    val thumbnailImage : LiveData<String> get() = _thumbnailImage

    fun getAppThumbnailImage() {
        viewModelScope.launch {
            val result = defaultAppRepository.getAppThumbnail()
            if (result.isSuccessful) _thumbnailImage.value = result.body()
            else throw IllegalStateException(result.message())
        }
    }

    private val _previewImages : MutableLiveData<List<String>> = MutableLiveData()
    val previewImages : LiveData<List<String>> get() = _previewImages

    fun getAppPreviewImages() {
        viewModelScope.launch {
            val res = defaultAppRepository.getAppPreview()
            if (res.isSuccessful) _previewImages.value = res.body()
            else throw IllegalStateException(res.message())
        }
    }

}