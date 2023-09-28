package com.example.app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app.model.User
import com.example.app.repository.UserRetrofitRepository
import kotlinx.coroutines.launch

class UserViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

class UserViewModel(
    private val userRetrofitRepository: UserRetrofitRepository = UserRetrofitRepository()
) : ViewModel() {
    companion object {
        private const val TAG = "UserViewModel"
    }

    private var _user : MutableLiveData<User> = MutableLiveData(null)
    val user : LiveData<User> get() = _user

    fun saveUser(user: User) {
        viewModelScope.launch {
            val result = userRetrofitRepository.saveUser(user)

            if (result.isSuccessful) {
                Log.e("$TAG saveUser()", "res = $result")
                _user.value = result.body()
            }

            else {
                Log.e("$TAG saveUser()", result.body().toString())
                _user.value = null
            }
        }
    }

    suspend fun getUserByKeyHash(keyHash: String) : User? {
        val result = userRetrofitRepository.getUserByKeyHash(keyHash)

        if (result.isSuccessful) {
            Log.e("$TAG getUserByKeyHash()", "res = $result")
            _user.value = result.body()
        } else {
            Log.e("$TAG getUserByKeyHash()", result.body().toString())
            _user.value = null
        }

        return _user.value
    }
}