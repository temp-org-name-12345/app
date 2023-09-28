package com.example.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.app.model.User
import com.example.app.repository.UserRetrofitRepository
import com.example.app.viewModel.UserViewModel
import com.example.app.viewModel.UserViewModelFactory
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import kotlinx.coroutines.runBlocking

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
        }

        super.onCreate(savedInstanceState)

        val keyHash = Utility.getKeyHash(this)
        KakaoSdk.init(this, getString(R.string.KAKAO_NATIVE_APP_KEY))
        Log.e(TAG, "keyHash : $keyHash")

        val context = this

        runBlocking {
            val user: User? = UserRetrofitRepository().getUserByKeyHash(keyHash).body()

            runBlocking {
                val intent =
                    if (user == null) {
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.putExtra("keyHash", keyHash)
                        intent
                    } else Intent(context, MainActivity::class.java)

                startActivity(intent)
            }
        }
    }

    companion object {
        const val TAG = "SplashActivity"
    }
}