package com.sns.homeconnect_v2.presentation.component
import IoTHomeConnectAppTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CustomSwitch(
    isCheck: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = isCheck,
        onCheckedChange = { onCheckedChange(it) },
        thumbContent = {
            Icon(
                imageVector = if (isCheck) Icons.Default.Check else Icons.Default.Close,
                contentDescription = null,
                tint = Color.Black
            )
        },
        colors = SwitchDefaults.colors(
            // ToDo
            checkedThumbColor = Color.White,
            uncheckedThumbColor = Color.Gray,
            checkedTrackColor = Color(0xFF22C55E),
            uncheckedTrackColor = Color.White,
            disabledCheckedTrackColor = Color.Gray,
            disabledCheckedThumbColor = Color.White,
        )
    )
}

@Preview
@Composable
fun CustomSwitchPreview() {
    var isCheck by remember { mutableStateOf(false) }
    IoTHomeConnectAppTheme {
        CustomSwitch(isCheck = isCheck, onCheckedChange = { isCheck = it })
    }
}

