package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import com.sns.homeconnect_v2.data.remote.dto.request.AttributeRequest
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_led.LedUiState
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

@Composable
fun EffectSelector(
    state: LedUiState,
    snackbarViewModel: SnackbarViewModel,          // ➊ thêm đối số này
    onApply: (
        effect: String,
        speed: Int?,
        count: Int?,
        color1: String?,
        color2: String?
    ) -> Unit
) {
    if (state !is LedUiState.EffectsLoaded) return

    /* ---------- local UI state ---------- */
    var expanded       by remember { mutableStateOf(false) }
    var selectedEffect by remember { mutableStateOf(state.effects.available_effects.first()) }

    val params = state.effects.effects
        .firstOrNull { it.name == selectedEffect }
        ?.params ?: emptyList()

    var speed  by remember { mutableIntStateOf(500) }
    var count  by remember { mutableIntStateOf(10) }
    var color1 by remember { mutableStateOf("#FF0000") }
    var color2 by remember { mutableStateOf("#00FF00") }

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

        /* ▼▼▼ dropdown chọn effect ▼▼▼ */
        OutlinedTextField(
            value = selectedEffect,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Hiệu ứng") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, null)
                }
            },
            readOnly = true
        )
        DropdownMenu(expanded, { expanded = false }) {
            state.effects.available_effects.forEach { e ->
                DropdownMenuItem(
                    text = { Text(e) },
                    onClick = { selectedEffect = e; expanded = false }
                )
            }
        }

        /* ▼▼▼ tham số động ▼▼▼ */
        if ("speed" in params) {
            Slider(
                value = speed.toFloat(),
                onValueChange = { speed = it.toInt() },
                valueRange = 50f..2000f,
                steps = 10
            )
            Text("Speed: $speed ms")
        }

        if ("count" in params) {
            Slider(
                value = count.toFloat(),
                onValueChange = { count = it.toInt() },
                valueRange = 1f..50f,
                steps = 9
            )
            Text("Count: $count")
        }

        if ("color1" in params) FancyColorSlider(
            attribute = AttributeRequest(brightness = 100, color = color1),   // ➋ thêm brightness
            onColorChange = { color1 = it }
        )

        if ("color2" in params) FancyColorSlider(
            attribute = AttributeRequest(brightness = 100, color = color2),   // ➋ thêm brightness
            onColorChange = { color2 = it }
        )

        /* ▼▼▼ nút Apply ▼▼▼ */
        ActionButtonWithFeedback(
            label  = "Áp dụng",
            style  = HCButtonStyle.PRIMARY,
            height = 56.dp,
            modifier = Modifier.fillMaxWidth(),
            snackbarViewModel = snackbarViewModel,                           // ➊ truyền vào
            onAction = { onS, _ ->
                onApply(selectedEffect, speed, count, color1, color2)
                onS("Đang áp dụng…")
            }
        )
    }
}
