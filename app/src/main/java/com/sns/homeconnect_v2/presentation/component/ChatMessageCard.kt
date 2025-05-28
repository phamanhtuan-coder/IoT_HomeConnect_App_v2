import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage

/**
 * Lớp enum biểu thị loại tin nhắn trò chuyện.
 * Nó có thể là tin nhắn nhận được hoặc tin nhắn đã gửi.
 *
 * @author Nguyễn Thanh Sang
 * @since 26-05-25
 */
enum class ChatMessageType { RECEIVED, SENT }

/**
 * Một hàm composable hiển thị thẻ tin nhắn trò chuyện.
 *
 * Hàm này có thể hiển thị tin nhắn văn bản, tin nhắn hình ảnh hoặc tin nhắn có cả văn bản và hình ảnh.
 * Nó cũng xử lý các giao diện khác nhau cho tin nhắn nhận và gửi, bao gồm màu bong bóng,
 * màu văn bản, hình dạng bong bóng và căn chỉnh. Avatar được hiển thị cho tin nhắn nhận được nếu
 * URL avatar được cung cấp; nếu không, một biểu tượng người mặc định sẽ được hiển thị.
 *
 * @param message Nội dung văn bản của tin nhắn. Có thể là null nếu tin nhắn chỉ là hình ảnh.
 * @param time Dấu thời gian của tin nhắn.
 * @param avatarUrl URL avatar của người gửi. Chỉ được sử dụng cho tin nhắn nhận được. Nếu là null hoặc trống, một avatar mặc định sẽ được hiển thị.
 * @param imageUrl URL của hình ảnh sẽ được hiển thị trong tin nhắn. Có thể là null nếu tin nhắn chỉ là văn bản.
 * @param type Cho biết tin nhắn được nhận hay gửi. Điều này ảnh hưởng đến kiểu dáng và bố cục của thẻ.
 * @param modifier Một [Modifier] sẽ được áp dụng cho phần tử gốc của composable này.
 * @author Nguyễn Thanh Sang
 * @since 26-05-2025
 */
@Composable
fun ChatMessageCard(
    message: String? = null,
    time: String,
    avatarUrl: String? = null,
    imageUrl: String? = null,
    type: ChatMessageType,
    modifier: Modifier = Modifier
) {
    val bubbleColor = when (type) {
        ChatMessageType.RECEIVED -> Color(0xFFF5F5F5)
        ChatMessageType.SENT -> Color(0xFF408BE6)
    }
    val textColor = when (type) {
        ChatMessageType.RECEIVED -> Color.Black
        ChatMessageType.SENT -> Color.White
    }
    val bubbleShape = when (type) {
        ChatMessageType.RECEIVED -> RoundedCornerShape(20.dp, 20.dp, 20.dp, 0.dp)
        ChatMessageType.SENT -> RoundedCornerShape(20.dp, 20.dp, 0.dp, 20.dp)
    }
    val horizontalArrangement = if (type == ChatMessageType.RECEIVED)
        Arrangement.Start else Arrangement.End

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.Bottom
    ) {
        if (type == ChatMessageType.RECEIVED) {
            if (!avatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(48.dp) // TO hơn
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar mặc định",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(
            horizontalAlignment = if (type == ChatMessageType.SENT) Alignment.End else Alignment.Start
        ) {
            Surface(
                shape = bubbleShape,
                color = bubbleColor,
                tonalElevation = 2.dp,
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .widthIn(max = 340.dp), // To hơn
                ) {
                    // Hiển thị ảnh nếu có
                    if (!imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Image message",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .fillMaxWidth()
                                .heightIn(max = 240.dp)
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )
                        if (!message.isNullOrBlank()) Spacer(Modifier.height(10.dp))
                    }
                    // Hiển thị text nếu có
                    if (!message.isNullOrBlank()) {
                        Text(
                            text = message,
                            color = textColor,
                            fontSize = 22.sp // TO hơn
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = time,
                fontSize = 15.sp, // TO hơn
                color = Color.Gray,
                modifier = Modifier.padding(start = 6.dp, end = 6.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMessageCardPreview() {
    Column(modifier = Modifier.fillMaxWidth()) {
        ChatMessageCard(
            message = "Xin chào! Bạn có thể giúp tôi không?",
            time = "09:00",
            avatarUrl = "https://randomuser.me/api/portraits/men/1.jpg",
            type = ChatMessageType.RECEIVED
        )
        ChatMessageCard(
            imageUrl = "https://picsum.photos/id/1003/300/200",
            time = "09:01",
            type = ChatMessageType.SENT
        )
        ChatMessageCard(
            message = "Đây là ảnh thiết bị bị hỏng.",
            imageUrl = "https://picsum.photos/id/1015/300/200",
            time = "09:02",
            avatarUrl = "https://randomuser.me/api/portraits/men/1.jpg",
            type = ChatMessageType.RECEIVED
        )
        ChatMessageCard(
            message = "Mình đã kiểm tra, thiết bị hoạt động bình thường nhé.",
            time = "09:03",
            type = ChatMessageType.SENT
        )
    }
}

