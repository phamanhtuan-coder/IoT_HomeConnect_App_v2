package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetWithTrigger(
    isSheetVisible: Boolean,
    onDismiss: () -> Unit,
    sheetContent: @Composable ColumnScope.() -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = Modifier.fillMaxSize()) {
        if (isSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { onDismiss() },
                sheetState = sheetState
            ) {
                sheetContent()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomSheetControlOutside() {
    var isSheetVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        IconButton(onClick = { isSheetVisible = true }) {
            Icon(Icons.Default.FilterList, contentDescription = "Open Sheet")
        }

        BottomSheetWithTrigger(
            isSheetVisible = isSheetVisible,
            onDismiss = { isSheetVisible = false },
            sheetContent = {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Nội dung trong Bottom Sheet")
                    Spacer(Modifier.height(12.dp))
                }
            }
        )
    }
}
