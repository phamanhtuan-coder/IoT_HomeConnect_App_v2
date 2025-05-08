package com.sns.homeconnect_v2.presentation.component

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WifiToggle(wifiManager: WifiManager, context: Context) {
    if (wifiManager.isWifiEnabled) {
        Text("Wi-Fi: Enabled", modifier = Modifier)
    } else {
        Button(
            onClick = {
                context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            },
            modifier = Modifier
        ) {
            Text("Enable Wi-Fi")
        }
    }
}