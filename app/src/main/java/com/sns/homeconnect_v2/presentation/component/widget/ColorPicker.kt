package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(
    colors: List<Pair<Color, String>>,
    selectedColorLabel: String,
    onColorSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Chọn màu sắc:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            colors.forEach { (color, label) ->
                Surface(
                    shape = CircleShape,
                    color = color,
                    border = if (label == selectedColorLabel)
                        BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
                    else null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onColorSelected(label) }
                ) {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewColorPickerCombined() {
    var selectedLabel by remember { mutableStateOf("blue") }

    val colorOptions = listOf(
        Color.Red to "red",
        Color.Green to "green",
        Color.Blue to "blue",
        Color.Yellow to "yellow",
        Color.Cyan to "cyan",
        Color.Magenta to "magenta",
        Color.Gray to "gray",
        Color.Black to "black",
        Color.White to "white",
        Color(0xFF2196F3) to "customBlue"
    )

    ColorPicker(
        colors = colorOptions,
        selectedColorLabel = selectedLabel,
        onColorSelected = { selectedLabel = it }
    )

}