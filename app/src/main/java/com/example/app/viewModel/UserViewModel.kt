package com.example.app.viewModel

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.DatePicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app.model.AddLocationReq
import com.example.app.model.RoadAddress
import com.example.app.model.User
import com.example.app.repository.UserRetrofitRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.UUID

class UserViewModelFactory(private val keyHash: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(keyHash) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

class UserViewModel(
    private val keyHash: String,
    private val userRetrofitRepository: UserRetrofitRepository = UserRetrofitRepository()
) : ViewModel() {

    private var _user : MutableLiveData<User> = MutableLiveData(null)
    val user : LiveData<User> get() = _user

    fun saveUser(user: User) {
        viewModelScope.launch {
            val result = userRetrofitRepository.saveUser(user)

            if (result.isSuccessful) {
                _user.value = result.body()
            }

            else {
                _user.value = null
            }
        }
    }

    suspend fun getUserByKeyHash() : User? {
        val result = userRetrofitRepository.getUserByKeyHash(keyHash)

        if (result.isSuccessful) {
            _user.value = result.body()
        } else {
            _user.value = null
        }

        return _user.value
    }

    private val _thumbnailImage : MutableLiveData<List<String>> = MutableLiveData()
    val thumbnailImage : LiveData<List<String>> get() = _thumbnailImage

    fun getAppThumbnailImage() {
        viewModelScope.launch {
            val result = userRetrofitRepository.getAppThumbnail()

            if (result.isSuccessful) _thumbnailImage.value = result.body()
            else throw IllegalStateException(result.message())
        }
    }

    /* 유저 주소 검색 State */
    private val _searchInput : MutableLiveData<String> = MutableLiveData("")
    val searchInput : LiveData<String> get() = _searchInput
    val onSearchInputChanged = { input: String -> _searchInput.value = input }

    /* 유저가 선택한 주소 저장 */
    private val _selectedAddress : MutableLiveData<RoadAddress> = MutableLiveData()
    val selectedAddress : LiveData<RoadAddress> get() = _selectedAddress
    val onAddressSelected = { addr: RoadAddress? -> setSelectedAddress(addr) }

    private fun setSelectedAddress(addr: RoadAddress?) {
        addr?.let { _selectedAddress.value = it }
    }

    /* Building Input 정보 */
    private val _buildingInput : MutableLiveData<String> = MutableLiveData("")
    val buildingInput : LiveData<String> get() = _buildingInput
    val onBuildingInputChanged = { input: String -> _buildingInput.value = input }

    /* 유저 날짜 선택 State */
    private val _dateState : MutableLiveData<LocalDate> = MutableLiveData()
    val dateState : LiveData<LocalDate> get() = _dateState

    val onDateSetListener = { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
        _dateState.value = LocalDate.of(year, month + 1, dayOfMonth)
    }

    /* 유저 사진 선택 State */
    private val _selectedImages : MutableLiveData<List<Uri>> = MutableLiveData(emptyList())
    val selectedImages : LiveData<List<Uri>> get() = _selectedImages
    val onImagesSelected = { uris: List<@JvmSuppressWildcards Uri> -> _selectedImages.value = uris }

    /* isSpecial 여부 */
    private val _isSpecial : MutableLiveData<Boolean> = MutableLiveData(false)
    val isSpecial : LiveData<Boolean> get() = _isSpecial
    val onIsSpecialChanged = { it: Boolean -> _isSpecial.value = it }

    /* 장소 등록 */
    private fun addLocationReq(images: List<MultipartBody.Part?>, req: AddLocationReq) {
        val result = userRetrofitRepository.addLocationReq(
            images = images,
            req = req
        )

        result.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _submitResponse.value = response.body()
                Log.e("UserViewModel", "res = $response")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "err = $t")
            }
        })
    }

    /* SUBMIT */
    private val _submitButtonEnabled : MutableLiveData<Boolean> = MutableLiveData(true)
    val submitButtonEnabled : LiveData<Boolean> get() = _submitButtonEnabled

    fun handleSubmitButtonEnable() {
        val v = _submitButtonEnabled.value ?: true
        _submitButtonEnabled.value = !v
    }

    private val _submitResponse : MutableLiveData<String> = MutableLiveData("")
    val submitResponse : LiveData<String> get() = _submitResponse

    fun onSubmitButtonClick(context: Context) {
        val date = dateState.value ?: LocalDate.now()

        val req = AddLocationReq(
            lat = selectedAddress.value?.lat?.toDouble(),
            lng = selectedAddress.value?.lng?.toDouble(),
            addressName = selectedAddress.value?.fullAddress,
            storeName = buildingInput.value ?: "",
            year = date.year,
            month = date.monthValue,
            day = date.dayOfMonth,
            isSpecial = isSpecial.value ?: false,
            userId = user.value?.id
        )

        val parts = selectedImages.value
            ?.map { item ->
                val ret = item.asMultipart(
                    "images",
                    context.contentResolver
                )
                ret
            }
            ?: listOf()

        addLocationReq(parts, req)
    }

    /* uiVisible */
    private val _uiVisible : MutableLiveData<Int> = MutableLiveData(1)

    val uiVisible : LiveData<Int> get() = _uiVisible
    val onNextInput = { _uiVisible.value = _uiVisible.value!! + 1 }

    // 선택된 파일의 uri -> multipart
    // 출처 : https://ohdbjj.tistory.com/49
    @SuppressLint("Range")
    fun Uri.asMultipart(name: String, contentResolver: ContentResolver): MultipartBody.Part? {
        return contentResolver.query(this, null, null, null, null)?.let {
            if (it.moveToNext()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                val requestBody = object : RequestBody() {
                    override fun contentType(): MediaType? {
                        return contentResolver.getType(this@asMultipart)?.toMediaType()
                    }

                    override fun writeTo(sink: BufferedSink) {
                        sink.writeAll(contentResolver.openInputStream(this@asMultipart)?.source()!!)
                    }
                }
                it.close()
                MultipartBody.Part.createFormData(name, displayName, requestBody)
            } else {
                it.close()
                null
            }
        }
    }
}