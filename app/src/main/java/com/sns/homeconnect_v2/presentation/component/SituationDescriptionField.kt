package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một Composable hiển thị một trường văn bản có viền (outlined text field) để mô tả tình huống.
 *
 * Trường này thường được sử dụng để thu thập thông tin chi tiết về một sự kiện hoặc hoàn cảnh cụ thể.
 * Nó có văn bản giữ chỗ (placeholder), các góc bo tròn và kiểu dáng cụ thể cho trạng thái khi được chọn (focused) và không được chọn (unfocused).
 *
 * Trường văn bản sẽ có chiều cao tối thiểu là 2 dòng và chiều cao tối đa là 5 dòng.
 * Nếu nội dung vượt quá 5 dòng, trường sẽ trở nên có thể cuộn được.
 *
 * @param value Giá trị văn bản hiện tại của trường.
 * @param onValueChange Một hàm callback được gọi khi giá trị văn bản thay đổi.
 * Nó nhận giá trị văn bản mới dưới dạng một String.
 * @param modifier Một [Modifier] tùy chọn để áp dụng cho trường văn bản. Mặc định là [Modifier].
 * @param placeholder Văn bản giữ chỗ sẽ được hiển thị khi trường trống.
 * Mặc định là "Mô tả tình hình".
 *
 * @author Nguyễn Thanh Sang
 * @since 20-05-2025
 * @see <a href="https://www.figma.com/file/your_figma_link_here">Thiết kế trên Figma</a>
 * @see <a href="https://your_jira_ticket_link_here">Yêu cầu trên Jira</a>
 * @updatedBy Nguyễn Thanh Sang
 * @updatedDate 26-05-2025
 */

@Composable
fun SituationDescriptionField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Mô tả tình hình"
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                placeholder,
                fontSize = 24.sp,
                lineHeight = 36.sp
            )
        },
        modifier = modifier.fillMaxWidth(),   // KHÔNG set .height()
        shape = RoundedCornerShape(12.dp),
        minLines = 2,    // option: luôn cao ít nhất 2 dòng
        maxLines = 5,    // tối đa 5 dòng, sau đó sẽ scroll
        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 24.sp, lineHeight = 36.sp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.LightGray,
            focusedPlaceholderColor = Color.Gray,
            unfocusedPlaceholderColor = Color.Gray
        )
    )
}

/* ---------- Preview ---------- */
@Preview(showBackground = true)
@Composable
fun SituationDescriptionFieldPreview() {
    var text by remember { mutableStateOf("") }

    Column(
        Modifier.padding(16.dp)
    ) {
        SituationDescriptionField(
            value         = text,
            onValueChange = { text = it }
        )
        Spacer(Modifier.height(12.dp))
        /* Demo placeholder tuỳ chỉnh */
        SituationDescriptionField(
            value         = text,
            onValueChange = { text = it },
            placeholder   = "Describe the situation"
        )
    }
}
