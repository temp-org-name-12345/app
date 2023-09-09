package com.example.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
        }

        super.onCreate(savedInstanceState)

        val keyHash = Utility.getKeyHash(this@SplashActivity)
        Log.e(TAG, "keyHash : $keyHash")

        lifecycleScope.launchWhenCreated {
            KakaoSdk.init(this@SplashActivity, getString(R.string.KAKAO_NATIVE_APP_KEY))

            delay(2000)

            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val TAG = "SplashActivity"
    }
}