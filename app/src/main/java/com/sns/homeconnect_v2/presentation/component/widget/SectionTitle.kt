package com.sns.homeconnect_v2.presentation.component.widget

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SectionTitle(
    text: String,
    fontSize: TextUnit = 20.sp,
    textAlign: TextAlign = TextAlign.Center,
) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp,
            lineHeight = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            textAlign = textAlign,
            color = colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true, name = "SectionTitle Preview")
@Composable
fun SectionTitlePreview() {
    IoTHomeConnectAppTheme {
        SectionTitle(text = "Thiết bị đang hoạt động")
    }
}