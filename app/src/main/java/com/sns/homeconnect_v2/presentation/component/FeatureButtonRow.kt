package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.presentation.model.FeatureButtonItem
import com.sns.homeconnect_v2.presentation.navigation.Screens

@Composable
fun FeatureButtonRow(
    items: List<FeatureButtonItem>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.take(4).forEach { item ->
            FeatureButton(item = item)
        }
    }
}

@Preview
@Composable
fun FeatureButtonRowPreview() {
    val items = listOf(
        FeatureButtonItem(Icons.Default.Add, "Thêm thiết bị") {
        },
        FeatureButtonItem(Icons.Default.Wifi, "Kết nối Wifi") {},
        FeatureButtonItem(Icons.Default.Upload, "Chia sẻ thiết bị") {
        },
        FeatureButtonItem(Icons.Default.PhoneAndroid, "Thiết bị của tôi") {

        }
    )
    FeatureButtonRow(items = items, modifier = Modifier.padding(vertical = 8.dp))
}
