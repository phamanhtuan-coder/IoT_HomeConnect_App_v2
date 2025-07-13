package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.core.util.rememberResponsiveLayoutConfig
import com.sns.homeconnect_v2.presentation.navigation.Screens

@Composable
fun WiFiCard(
    navController: NavHostController,
    wifiName: String,
    isConnected: Boolean
) {
    val layoutConfig = rememberResponsiveLayoutConfig()
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = layoutConfig.textFieldSpacing)
            .clickable {
                if (isConnected) {
                    navController.navigate(Screens.WifiConnection.route)
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isConnected) colorScheme.primaryContainer else colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(layoutConfig.outerPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Wifi,
                contentDescription = null,
                tint = if (isConnected) colorScheme.onPrimaryContainer else colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(layoutConfig.textFieldSpacing))
            Text(
                text = wifiName,
                fontSize = layoutConfig.textFontSize,
                color = if (isConnected) colorScheme.onPrimaryContainer else colorScheme.onSurface
            )
        }
    }
}