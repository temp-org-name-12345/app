package com.example.app.util

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.app.MainActivity
import com.example.app.model.User
import com.example.app.viewModel.UserViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

object KakaoLogin {
    private const val TAG = "Kakao Login"

    fun login(context: Context, keyHash: String, viewModel: UserViewModel) {
        val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) Log.e(TAG, "로그인 실패 $error")
            else if (token != null) Log.e(TAG, "로그인 성공 ${token.accessToken} ${token.idToken}")
        }

        val instance = UserApiClient.instance

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
                val profileUrl = account?.profile?.profileImageUrl

                viewModel.saveUser(
                    User(
                        nickname = nickname!!,
                        email = email!!,
                        profileUrl = profileUrl!!,
                        keyHash = keyHash
                    )
                )

                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}