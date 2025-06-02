package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderAll
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.presentation.model.FeatureButtonItem

@Composable
fun FeatureButtonSection(
    modifier: Modifier = Modifier,
    title: String = "Tính năng",
    items: List<FeatureButtonItem>,
    columns: Int = 4,
    horizontalSpacing: Int = 12,
    verticalSpacing: Int = 24
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 14.dp)
        )

        val rows = items.chunked(columns)

        rows.forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.spacedBy(
                    horizontalSpacing.dp,
                    Alignment.Start
                ),
                verticalAlignment = Alignment.Top
            ) {
                rowItems.forEach { item ->
                    FeatureButton(item = item)
                }
            }

            if (rowIndex < rows.lastIndex)
                Spacer(Modifier.height(verticalSpacing.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun FeatureButtonSectionPreview() {
    val sampleItems = listOf(
        FeatureButtonItem(Icons.Default.Warning, "Báo mất\nthiết bị") {},
        FeatureButtonItem(Icons.Default.Sync, "Chuyển\nquyền sở hữu") {},
        FeatureButtonItem(Icons.Default.Home, "Quản lý nhà") {},
        FeatureButtonItem(Icons.Default.GridView, "Phòng") {},
        FeatureButtonItem(Icons.Default.Folder, "Nhóm thiết bị") {},
        FeatureButtonItem(Icons.Default.BorderAll, "Lịch sử\nhoạt động") {},
        FeatureButtonItem(Icons.Default.Download, "Cập nhật\nphần mềm") {}
    )

    FeatureButtonSection(items = sampleItems, modifier = Modifier.padding(16.dp))
}

