package com.example.app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.User
import com.example.app.repository.UserRetrofitRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRetrofitRepository: UserRetrofitRepository = UserRetrofitRepository()) : ViewModel() {
    private var _userExists : MutableLiveData<Boolean> = MutableLiveData()
    val userExists : LiveData<Boolean> get() = _userExists

    fun findUserByEmail(email: String) {
        viewModelScope.launch {
            val result = userRetrofitRepository.checkUserByEmail(email)

            if (result.isSuccessful) {
                _userExists.value = result.body() as Boolean
            }
        }
    }

    private var _user : MutableLiveData<User> = MutableLiveData()
    val user : LiveData<User> get() = _user

    fun saveUser(user: User) {
        viewModelScope.launch {
            val result = userRetrofitRepository.saveUser(user)

            if (result.isSuccessful) {
                _user.value = result.body()
                Log.e("MainViewModel saveUser()", "res = $result")
            }
        }
    }

    private var _allUserList = MutableLiveData<List<User>>()
    val allUserInfo : LiveData<List<User>> get() = _allUserList

    fun getAllUser() {
        viewModelScope.launch {
            val result = userRetrofitRepository.getAllUser()

            if (result.isSuccessful) {
                _allUserList.value = result.body()
                Log.e("MainViewModel getAllUser()", "res = $result")
            }
        }
    }
}