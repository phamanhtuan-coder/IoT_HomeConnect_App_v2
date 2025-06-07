package com.sns.homeconnect_v2

import IoTHomeConnectAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.TopSnackbar
import com.sns.homeconnect_v2.presentation.navigation.NavigationGraph
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var permissionEventHandler: PermissionEventHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            IoTHomeConnectAppTheme {
                val navController = rememberNavController()
                val snackbarViewModel: SnackbarViewModel = hiltViewModel()

                Box(modifier = Modifier.fillMaxSize()) {
                    // 1. Giao diện chính
                    NavigationGraph(
                        navController = navController,
                        snackbarViewModel = snackbarViewModel
                    )

                    // 2. Snackbar toàn cục
                    val message = snackbarViewModel.snackbarMessage.collectAsState().value
                    val variant = snackbarViewModel.snackbarVariant.collectAsState().value

                    if (message != null) {
                        TopSnackbar(
                            message = message,
                            variant = variant,
                            onDismiss = { snackbarViewModel.hideSnackbar() }
                        )
                    }
                }
            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} passing\n      in a {@link RequestMultiplePermissions} object for the {@link ActivityResultContract} and\n      handling the result in the {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            lifecycleScope.launch {
                permissionEventHandler.emitPermissionResult(this@MainActivity, grantResults)
            }
        }
    }
}