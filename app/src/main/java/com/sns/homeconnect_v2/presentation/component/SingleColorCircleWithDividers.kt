package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.R
import com.sns.homeconnect_v2.core.util.PADDING_OFFSET
import kotlin.math.cos
import kotlin.math.sin


/**
 * Một Composable function hiển thị một vòng tròn đơn sắc với các vạch chia.
 * Màu sắc của vòng tròn được xác định bởi tham số `selectedStatus`.
 * Số lượng vạch chia được xác định bởi tham số `dividerCount`.
 *
 * @param selectedStatus Trạng thái để hiển thị. Điều này xác định màu sắc của vòng tròn.
 *                       Các giá trị có thể là "Bình thường" (Xanh lá), "Báo động" (Đỏ), "Lỗi" (Vàng).
 *                       Mặc định là Xám nếu trạng thái không được nhận dạng.
 * @param dividerCount Số lượng vạch chia để vẽ trên vòng tròn.
 * @author Nguyễn Thanh Sang
 * @since 20-05-2025
 */
@Composable
fun SingleColorCircleWithDividers(selectedStatus: String, dividerCount: Int) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .padding(bottom = 8.dp),
        contentAlignment = Alignment.Center // Đảm bảo nội dung bên trong căn giữa
    ) {
        // Xác định màu dựa theo trạng thái
        val color = when (selectedStatus) {
            "Bình thường" -> Color.Green
            "Báo động" -> Color.Red
            "Lỗi" -> Color.Yellow
            else -> Color.Gray
        }
        Canvas(modifier = Modifier.size(200.dp)) {
            val radius = size.minDimension / 2 - PADDING_OFFSET // Trừ bớt để tránh bị cắt
            val center = Offset(size.width / 2, size.height / 2)


            // Vẽ vòng tròn với màu trạng thái
            drawCircle(
                color = color,
                radius = radius,
                center = center,
                style = Stroke(width = 40f) // Độ dày của vòng tròn
            )

            // Vẽ các đường gạch phân cách
            val lineAngleStep = 360f / dividerCount
            for (dividerIndex in 0 until dividerCount) {
                val angle = Math.toRadians((-90 + dividerIndex * lineAngleStep).toDouble())
                val startX = center.x + (radius - 20f) * cos(angle).toFloat()
                val startY = center.y + (radius - 20f) * sin(angle).toFloat()
                val endX = center.x + (radius + 20f) * cos(angle).toFloat()
                val endY = center.y + (radius + 20f) * sin(angle).toFloat()

                drawLine(
                    color = Color.Black,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 4f
                )
            }
        }

        // Icon trung tâm (nếu cần)
        Image(
            painter = painterResource(id = R.drawable.fire),
            colorFilter = ColorFilter.tint(color),
            contentDescription = null,
            modifier = Modifier.size(100.dp) // Kích thước biểu tượng
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SegmentedCirclePreview() {
    val statusList = listOf("Bình thường", "Báo động", "Lỗi")
    SingleColorCircleWithDividers(selectedStatus = statusList[0], dividerCount = 12)
}