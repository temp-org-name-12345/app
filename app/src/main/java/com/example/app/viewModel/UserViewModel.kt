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

    suspend fun getUserByKeyHash(keyHash: String) : User? {
        val result = userRetrofitRepository.getUserByKeyHash(keyHash)

        if (result.isSuccessful) {
            _user.value = result.body()
        } else {
            _user.value = null
        }

        return _user.value
    }
}