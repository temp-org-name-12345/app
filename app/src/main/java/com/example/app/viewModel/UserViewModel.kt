package com.example.app.viewModel

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.ui.platform.LocalContext
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Multipart
import java.time.LocalDateTime
import java.util.Calendar

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

    /* 유저 날짜 선택 State */
    private val _dateState : MutableLiveData<LocalDateTime> = MutableLiveData()
    val dateState : LiveData<LocalDateTime> get() = _dateState

    val onDateSetListener = { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val min = calendar.get(Calendar.MINUTE)
        _dateState.value = LocalDateTime.of(year, month + 1, dayOfMonth, hour, min)
    }

    /* isSpecial 여부 */
    private val _isSpecial : MutableLiveData<Boolean> = MutableLiveData(false)
    val isSpecial : LiveData<Boolean> get() = _isSpecial
    val onIsSpecialChanged = { it: Boolean -> _isSpecial.value = it }

    /* 장소 등록 */
    fun addLocationReq(images: List<MultipartBody.Part?>, req: AddLocationReq) {
        viewModelScope.launch {
            val jsonObj = JSONObject(
                "{" +
                        "\"userId\":\"${req.userId}\"," +
                        "\"lat\":\"${req.lat}\"," +
                        "\"lng\":\"${req.lng}\"," +
                        "\"visitDate\":\"${req.visitDate}\"," +
                        "\"isSpecial\":\"${req.isSpecial}\"," +
                        "\"name\":\"${req.name}\"" +
                    "}"
            ).toString()

            val jsonBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObj)
            userRetrofitRepository.addLocationReq(images, jsonBody)
        }
    }
}