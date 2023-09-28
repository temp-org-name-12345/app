package com.example.app

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import com.example.app.ui.theme.AppTheme
import com.example.app.viewModel.UserViewModel
import com.example.app.viewModel.UserViewModelFactory
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapViewInfo
import java.lang.Exception

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userViewModel = ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]
        val keyHash = KakaoSdk.keyHash

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        viewModel = userViewModel,
                        keyHash = keyHash
                    )
                }
            }
        }
    }
}


// KAKAO MAP DOCS : https://apis.map.kakao.com/android_v2/docs/getting-started/quickstart/
@Composable
fun MainScreen(
    viewModel: UserViewModel,
    keyHash: String
) {
    LaunchedEffect(Unit) {
        viewModel.getUserByKeyHash(keyHash)
    }

    val user by viewModel.user.observeAsState()

    Toast.makeText(LocalContext.current, user?.toString() ?: "null", Toast.LENGTH_LONG).show()

    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AndroidView(
                factory = { context: Context ->
                    val mv = MapView(context)

                    mv.start(
                        object : MapLifeCycleCallback() {
                            // 지도 API가 정상적으로 종료될 때 호출
                            override fun onMapDestroy() {
                                TODO("Not yet implemented")
                            }

                            // 인증 실패 및 지도 사용 중 에러 발생시 호출
                            override fun onMapError(error: Exception?) {
                                TODO("Not yet implemented")
                            }
                        },

                        object : KakaoMapReadyCallback() {
                            // 인증 후 API가 정상적으로 실행될 때 호출
                            override fun onMapReady(kakaoMap: KakaoMap) {
                                Log.e("Map", "지도 시작")
                            }

                            // 지도 시작 시 확대/축소 줌 레벨 설정
                            override fun getZoomLevel(): Int {
                                return super.getZoomLevel()
                            }

                            // 지도 시작 시 위치 좌표 설정
                            override fun getPosition(): LatLng {
                                return LatLng.from(37.486960, 127.115587)
                            }

                            // 지도 시작 시 App 및 MapType 설정
                            override fun getMapViewInfo(): MapViewInfo {
                                return super.getMapViewInfo()
                            }

                            // KakaoMap의 고유한 이름 설정
                            override fun getViewName(): String {
                                return super.getViewName()
                            }

                            // KakaoMap의 tag를 설정
                            override fun getTag(): Any {
                                return super.getTag()
                            }

                            // 지도 시작 시 visible 여부 설정
                            override fun isVisible(): Boolean {
                                return true
                            }
                        }
                    )

                    mv
                },

                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
