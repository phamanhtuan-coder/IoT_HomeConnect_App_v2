import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.ColorFilter
import com.sns.homeconnect_v2.R

/**
 * Đại diện cho trạng thái của một mục.
 *
 * Enum này được sử dụng để hiển thị chỉ báo trực quan về trạng thái bằng cách sử dụng [StatusCircle].
 * @author Nguyễn Thanh Sang
 * @since 26-05-2025
 */
enum class Status {
    APPROVED,    // Đã duyệt (xanh lá)
    PENDING,     // Chờ duyệt (vàng)
    REJECTED     // Huỷ bỏ (đỏ)
}

/**
 * Một hàm Composable dùng để hiển thị chỉ báo trạng thái hình tròn.
 *
 * Biểu tượng và màu sắc của hình tròn được xác định bởi tham số [status].
 *
 * @param status Trạng thái hiện tại cần hiển thị. Điều này xác định biểu tượng và màu sắc của hình tròn.
 * @param modifier [Modifier] tùy chọn để áp dụng cho composable.
 * @author Nguyễn Thanh Sang
 * @since 26-05-2025
 */
@Composable
fun StatusCircle(status: Status, modifier: Modifier = Modifier) {
    val (icon, color) = when (status) {
        Status.APPROVED  -> R.drawable.shield_check      to Color(0xFF00C853)
        Status.PENDING   -> R.drawable.shield_exclamation to Color(0xFFFFC107)
        Status.REJECTED  -> R.drawable.shield_xmark       to Color.Red
    }
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            colorFilter = ColorFilter.tint(color)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatusCirclePreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(32.dp)
    ) {
        StatusCircle(status = Status.APPROVED)
        StatusCircle(status = Status.PENDING)
        StatusCircle(status = Status.REJECTED)
    }
}
