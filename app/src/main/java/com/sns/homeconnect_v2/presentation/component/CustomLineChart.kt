import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.presentation.model.DataPoint
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Một hàm composable hiển thị biểu đồ đường tùy chỉnh.
 *
 * Biểu đồ này có thể hiển thị danh sách các điểm dữ liệu, với màu đường kẻ và màu tô tùy chỉnh,
 * màu nền, khoảng đệm, bán kính góc và kích thước văn bản nhãn.
 * Nó cũng hỗ trợ cuộn ngang nếu dữ liệu vượt quá chiều rộng biểu đồ và
 * hiển thị chú giải công cụ khi một điểm dữ liệu được chạm vào.
 *
 * @author Nguyễn Thanh Sang
 * @since 25-05-2025
 * @param dataPoints Danh sách các đối tượng [DataPoint] sẽ được vẽ trên biểu đồ.
 * Mỗi [DataPoint] chứa một nhãn trục x (String) và một giá trị trục y (Float).
 * @param chartWidth Chiều rộng mong muốn của biểu đồ.
 * @param chartHeight Chiều cao mong muốn của biểu đồ.
 * @param modifier [Modifier] tùy chọn để tạo kiểu cho vùng chứa biểu đồ.
 * @param yLabelCount Số lượng nhãn sẽ hiển thị trên trục Y. Mặc định là 5.
 * @param lineColor Màu của đường nối các điểm dữ liệu. Mặc định là màu xanh lam.
 * @param fillColor Màu được sử dụng để tô vùng bên dưới đường kẻ. Mặc định là màu xanh lam bán trong suốt.
 * @param backgroundColor Màu nền của biểu đồ. Mặc định là màu trắng.
 * @param showFill Một boolean cho biết có hiển thị vùng được tô bên dưới đường kẻ hay không. Mặc định là true.
 * @param padding Khoảng đệm xung quanh nội dung biểu đồ. Mặc định là 8.dp.
 * @param cornerRadius Bán kính góc cho nền biểu đồ. Mặc định là 0.dp (không có góc bo tròn).
 * @param labelTextSize Kích thước phông chữ cho các nhãn trục. Mặc định là 12f.
 */
@Composable
fun CustomLineChart(
    dataPoints: List<DataPoint>,
    chartWidth: Dp,
    chartHeight: Dp,
    modifier: Modifier = Modifier,
    yLabelCount: Int = 5,
    lineColor: Color = Color(0xFF2196F3),
    fillColor: Color = Color(0x552196F3),
    backgroundColor: Color = Color.White,
    showFill: Boolean = true,
    padding: Dp = 8.dp,
    cornerRadius: Dp = 0.dp,
    labelTextSize: Float = 12f,
) {
    val scrollState = rememberScrollState()
    var touchedIndex by remember { mutableStateOf<Int?>(null) }

    val minY = 0f // Luôn bắt đầu từ 0
    val maxY = dataPoints.maxOfOrNull { it.yValue } ?: 1f
    val yRange = (maxY - minY).takeIf { it > 0 } ?: 1f

    val yAxisWidth = 34.dp
    val chartContentWidth = (dataPoints.size * 80).dp.coerceAtLeast(chartWidth)
    val canvasHeight = chartHeight

    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(cornerRadius))
            .padding(padding)
    ) {
        Row {
            Canvas(
                modifier = Modifier
                    .width(yAxisWidth)
                    .height(canvasHeight)
            ) {
                val bottomPadding = 32f
                val topPadding = 12f
                val usableHeight = size.height - bottomPadding - topPadding

                for (i in 0..yLabelCount) {
                    val y = size.height - bottomPadding - i * usableHeight / yLabelCount
                    // Lưới ngang
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(size.width, y),
                        end = Offset(0f, y),
                        strokeWidth = 1f
                    )
                    // Label Y (căn phải)
                    drawContext.canvas.nativeCanvas.drawText(
                        String.format(Locale.US, "%.1f", minY + i * yRange / yLabelCount),
                        size.width - 8f,
                        y + 10f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.DKGRAY
                            textSize = labelTextSize * 3
                            textAlign = android.graphics.Paint.Align.RIGHT
                        }
                    )
                }
            }

            // 2. Biểu đồ + trục X (cuộn ngang)
            Row(modifier = Modifier.horizontalScroll(scrollState)) {
                Canvas(
                    modifier = Modifier
                        .width(chartContentWidth)
                        .height(canvasHeight)
                        .pointerInput(dataPoints) {
                            detectTapGestures { offset ->
                                val leftPadding = 10f
                                val usableWidth = size.width - leftPadding
                                val xStep = usableWidth / (dataPoints.size - 1).coerceAtLeast(1)
                                val tapIndex = ((offset.x - leftPadding) / xStep)
                                    .roundToInt().coerceIn(0, dataPoints.lastIndex)
                                touchedIndex = tapIndex
                            }
                        }
                ) {
                    val leftPadding = 10f
                    val bottomPadding = 32f
                    val topPadding = 12f
                    val usableHeight = size.height - bottomPadding - topPadding
                    val usableWidth = size.width - leftPadding

                    // Vẽ lưới ngang (đồng bộ với canvas trái)
                    for (i in 0..yLabelCount) {
                        val y = size.height - bottomPadding - i * usableHeight / yLabelCount
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                    }

                    // Tính điểm cho line
                    val points = dataPoints.mapIndexed { index, dataPoint ->
                        val x = leftPadding + index * usableWidth / (dataPoints.size - 1).coerceAtLeast(1)
                        val yRatio = (dataPoint.yValue - minY) / yRange
                        val y = size.height - bottomPadding - yRatio * usableHeight
                        Offset(x, y)
                    }

                    // Fill dưới line
                    if (showFill && points.size > 1) {
                        val fillPath = Path().apply {
                            moveTo(points.first().x, size.height - bottomPadding)
                            points.forEach { lineTo(it.x, it.y) }
                            lineTo(points.last().x, size.height - bottomPadding)
                            close()
                        }
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(listOf(fillColor, Color.Transparent))
                        )
                    }

                    // Đường line
                    for (i in 0 until points.size - 1) {
                        drawLine(
                            color = lineColor,
                            start = points[i],
                            end = points[i + 1],
                            strokeWidth = 4f
                        )
                    }

                    // --- Vẽ label trục X (căn hợp lý để không bị cắt) ---
                    for (i in dataPoints.indices) {
                        val x = leftPadding + i * usableWidth / (dataPoints.size - 1).coerceAtLeast(1)
                        val labelOffset = when (i) {
                            0 -> x + 2f
                            dataPoints.lastIndex -> x - 106f // đổi từ -48f thành -24f hoặc nhỏ hơn
                            else -> x - 16f // cho các label giữa nhỏ lại (tùy font size của bạn)
                        }
                        drawContext.canvas.nativeCanvas.drawText(
                            dataPoints[i].xLabel,
                            labelOffset,
                            size.height - 2f,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.DKGRAY
                                textSize = labelTextSize * 3
                            }
                        )
                    }

                    // Trục X
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height - bottomPadding),
                        end = Offset(size.width, size.height - bottomPadding),
                        strokeWidth = 2f
                    )

                    // Tooltip/label khi chạm
                    touchedIndex?.let { index ->
                        if (index in points.indices) {
                            val point = points[index]
                            drawCircle(
                                color = Color.Red,
                                radius = 10f,
                                center = point
                            )
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset(point.x + 10, point.y - 60),
                                size = Size(120f, 50f),
                                cornerRadius = CornerRadius(16f, 16f)
                            )
                            drawContext.canvas.nativeCanvas.drawText(
                                "${dataPoints[index].yValue}",
                                point.x + 20f,
                                point.y - 25f,
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.BLACK
                                    textSize = 40f
                                    isFakeBoldText = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LineChartPreview() {
    val sampleData = listOf(
        DataPoint("00:00", 14f),
        DataPoint("01:00", 12f),
        DataPoint("02:00", 10f),
        DataPoint("03:00", 9f),
        DataPoint("04:00", 8f),
        DataPoint("05:00", 10f),
        DataPoint("06:00", 14f),
        DataPoint("07:00", 17f),
        DataPoint("08:00", 20f),
        DataPoint("09:00", 22f),
        DataPoint("10:00", 25f),
        DataPoint("11:00", 28f),
        DataPoint("12:00", 31f),
        DataPoint("13:00", 32f),
        DataPoint("14:00", 30f),
        DataPoint("15:00", 28f),
        DataPoint("16:00", 25f),
        DataPoint("17:00", 23f),
        DataPoint("18:00", 21f),
        DataPoint("19:00", 18f),
        DataPoint("20:00", 16f),
        DataPoint("21:00", 15f),
        DataPoint("22:00", 13f),
        DataPoint("23:00", 0f),
    )

    CustomLineChart(
        dataPoints = sampleData,
        chartWidth = 350.dp,
        chartHeight = 240.dp,
        yLabelCount = 5,
        lineColor = Color(0xFF2196F3),
        fillColor = Color(0x552196F3),
        backgroundColor = Color(0xFFF7F2FA),
        showFill = true,
        cornerRadius = 8.dp,
        labelTextSize = 12f
    )
}
