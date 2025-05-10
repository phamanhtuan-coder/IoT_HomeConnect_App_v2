package com.sns.homeconnect_v2.presentation.component.widget

import IoTHomeConnectAppTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Tìm kiếm...",
    onSearch: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val colorScheme = MaterialTheme.colorScheme

    OutlinedTextField(
        value = searchText,
        onValueChange = {
            searchText = it
            onSearch(it)
        },
        placeholder = {
            Text(
                text = hint,
                color = colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 16.sp
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = colorScheme.onSurface.copy(alpha = 0.7f)
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorScheme.surface,
            focusedContainerColor = colorScheme.surface,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = colorScheme.primary,
            cursorColor = colorScheme.primary,
            unfocusedTextColor = colorScheme.onSurface,
            focusedTextColor = colorScheme.onSurface
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchBarPreview() {
    IoTHomeConnectAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            SearchBar(
                modifier = Modifier
                    .width(300.dp), // hoặc .fillMaxWidth(0.8f)
                onSearch = { query ->
                    // Không cần xử lý trong preview
                }
            )
        }
    }
}