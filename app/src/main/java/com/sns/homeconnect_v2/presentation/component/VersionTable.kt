package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Data class đại diện cho một mục phiên bản trong bảng.
 *
 * @property version Chuỗi phiên bản (ví dụ: "V0.020").
 * @property date Ngày cập nhật phiên bản (ví dụ: "01/01/2026").
 * @author Nguyễn Thanh Sang
 * @since 20-05-2025
 */

/* ---------- Model ---------- */
data class VersionInfo(val version: String, val date: String)

/* ---------- One row ---------- */
@Composable
private fun VersionRow(
    info: VersionInfo,
    isHeader: Boolean = false,
    zebraColor: Color? = null
) {
    val bg = zebraColor ?: Color(0xFFE7EFF2)
    val style = if (isHeader)
        MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    else
        MaterialTheme.typography.bodyMedium

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg)
            .border(0.5.dp, Color(0xFFB5C0C3))
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        /* Cột 1 (40 %) */
        Text(
            info.version,
            style = style,
            modifier = Modifier
                .weight(0.4f)
                .padding(start = 14.dp)
        )

        /* Đường kẻ dọc giữa cột */
        Box(
            modifier = Modifier
                .width(0.5.dp)
                .fillMaxHeight()
                .background(Color(0xFFB5C0C3))
        )

        /* Cột 2 (60 %) */
        Text(
            info.date,
            style = style,
            modifier = Modifier
                .weight(0.6f)
                .padding(start = 14.dp)
        )
    }
}

/* ---------- Main component ---------- */
@Composable
fun VersionTable(
    versions: List<VersionInfo>,
    modifier: Modifier = Modifier,
    maxHeight: Dp = 300.dp
) {
    Surface(
        shape  = MaterialTheme.shapes.medium,        // bo góc ngoài
        color  = Color.White,                        // nền trắng
        tonalElevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight)
    ) {
        LazyColumn {
            /* Header */
            stickyHeader {
                VersionRow(
                    info       = VersionInfo("Tên phiên bản", "Ngày cập nhật"),
                    isHeader   = true,
                    zebraColor = Color(0xFFC4D3D8)      // header màu đậm hơn chút
                )
            }
            /* Data */
            itemsIndexed(versions) { idx, item ->
                val zebra = if (idx % 2 == 0) Color(0xFFEFF3F5) else Color(0xFFE7EFF2)
                VersionRow(item, zebraColor = zebra)
            }
        }
    }
}

/* ---------- Preview ---------- */
@Preview(showBackground = true, widthDp = 380, heightDp = 420)
@Composable
fun VersionTablePreview() {
    val sample = listOf(
        VersionInfo("V0.020", "01/01/2026"),
        VersionInfo("V0.019", "01/12/2025"),
        VersionInfo("V0.018", "01/10/2025"),
        VersionInfo("V0.017", "01/08/2025"),
        VersionInfo("V0.016", "01/06/2025"),
        VersionInfo("V0.015", "01/01/2025"),
        VersionInfo("V0.014", "01/01/2024"),
        VersionInfo("V0.013", "01/01/2017"),
        VersionInfo("V0.012", "01/01/2015"),
        VersionInfo("V0.011", "01/01/2011"),
        VersionInfo("V0.010", "01/01/2009"),
        VersionInfo("V0.009", "01/01/2007"),
        VersionInfo("V0.008", "01/01/2005"),
        VersionInfo("V0.007", "01/01/2003"),
        VersionInfo("V0.006", "01/01/2001"),
        VersionInfo("V0.005", "01/01/1999"),
        VersionInfo("V0.004", "01/01/1997"),
        VersionInfo("V0.003", "01/01/1995"),
        VersionInfo("V0.002", "01/01/1993"),
        VersionInfo("V0.001", "01/01/1990")
    )
    VersionTable(versions = sample, modifier = Modifier.padding(16.dp), maxHeight= 350.dp)
}