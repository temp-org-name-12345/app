package com.example.app.util

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.app.MainActivity
import com.example.app.User
import com.example.app.repository.UserRetrofitRepository
import com.example.app.viewModel.MainViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

object KakaoLogin {
    val TAG = "Kakao Login"
    private val userRetrofitRepository = UserRetrofitRepository()
    private val viewModel = MainViewModel(userRetrofitRepository)

    fun login(context: Context) {
        val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) Log.e(TAG, "로그인 실패 $error")
            else if (token != null) Log.e(TAG, "로그인 성공 ${token.accessToken} ${token.idToken}")
        }

        val instance = UserApiClient.instance
        var kakaoUser: User? = null

        if (instance.isKakaoTalkLoginAvailable(context)) {
            instance.loginWithKakaoTalk(context) { token, error ->
                // 로그인 실패
                if (error != null) {
                    Log.e(TAG, "로그인 실패 $error")

                    // 사용자가 취소 버튼을 눌렀을 때
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    else {
                        instance.loginWithKakaoAccount(context, callback = mCallback)
                    }
                }
            }
        }

        instance.me { user, error ->
            if (error != null) Log.e(TAG, "사용자 정보 요청 실패 $error")
            else if (user != null) {
                Log.e(TAG, "사용자 정보 요청 성공 $user")

                val account = user.kakaoAccount

                val nickname = account?.profile?.nickname
                val email = account?.email
                val profileImageUrl = account?.profile?.profileImageUrl

                viewModel.saveUser(
                    User(
                        nickname = nickname!!,
                        email = email!!,
                        profileImageUrl = profileImageUrl!!,
                    )
                )

                Log.d(TAG, "서버에 전송합니다")

                toMain(context, kakaoUser!!)
            }
        }
    }

    private fun toMain(context: Context, user: User) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("user", user)
        context.startActivity(intent)
    }
}