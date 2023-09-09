package com.example.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.AppTheme
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import java.io.Serializable

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    IconButton(
        onClick = { kakaoLogin(context) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.kakao_login_large_narrow),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(256.dp)
                .height(80.dp)
        )
    }
}

private const val TAG = "KAKAO LOGIN"

data class User(
    val nickname: String,
    val profileImageUrl: String,
    val email: String
) : Serializable

private fun kakaoLogin(context: Context) {
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

            kakaoUser = User(
                nickname = nickname!!,
                email = email!!,
                profileImageUrl = profileImageUrl!!,
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