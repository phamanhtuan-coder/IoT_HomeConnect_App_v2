package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.presentation.component.widget.ActionIcon
import com.sns.homeconnect_v2.presentation.component.widget.SwipeableItemWithActions
import com.sns.homeconnect_v2.presentation.model.DeviceUi

/**
 * Một hàm composable hiển thị thẻ thiết bị có thể vuốt.
 *
 * Thẻ này hiển thị tên thiết bị, tên phòng và công tắc để điều khiển thiết bị.
 * Nó cũng hỗ trợ các hành động vuốt để chỉnh sửa và xóa thiết bị.
 *
 * @param deviceName Tên của thiết bị. Mặc định là một chuỗi rỗng.
 * @param roomName Tên của phòng nơi thiết bị được đặt. Mặc định là một chuỗi rỗng.
 * @param isRevealed Liệu các hành động vuốt có đang được hiển thị hay không. Mặc định là false.
 * @param onExpandOnly Một hàm callback được gọi khi thẻ được vuốt để hiển thị các hành động.
 * @param onCollapse Một hàm callback được gọi khi thẻ được vuốt lại để ẩn các hành động.
 * @param onDelete Một hàm callback được gọi khi hành động xóa được kích hoạt.
 * @param onEdit Một hàm callback được gọi khi hành động chỉnh sửa được kích hoạt.
 *
 * @author Nguyễn Thanh Sang
 * @since 26-05-25
 */
@Composable
fun DeviceCardSwipeable(
    deviceName: String = "",
    roomName: String = "",
    isRevealed: Boolean = false,
    deviceTypeLabel: String = "",
    onExpandOnly: () -> Unit,
    onCollapse: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onClick: () -> Unit
) {
    var isChecked by remember { mutableStateOf(false) }

    SwipeableItemWithActions(
        isRevealed = isRevealed,
        onExpanded = onExpandOnly,
        onCollapsed = onCollapse,
        actions = {
            Spacer(Modifier.width(8.dp))
            ActionIcon(
                onClick = onEdit,
                backgroundColor = Color(0xFF4CAF50),
                icon = Icons.Default.Edit
            )
            Spacer(Modifier.width(8.dp))
            ActionIcon(
                onClick = onDelete,
                backgroundColor = Color(0xFFF44336),
                icon = Icons.Default.Delete
            )
        }
    ) {
        Row(
            modifier = Modifier
                .background(color = Color(0xFFD8E4E8))
                .clickable { onClick() }
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = deviceName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = roomName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    color = Color.Black
                )

                if (deviceTypeLabel.isNotBlank()) {
                    Text(
                        text = deviceTypeLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
            }

            CustomSwitch(isCheck = isChecked, onCheckedChange = { isChecked = it })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceCardSwipeablePreview() {
    val deviceList = remember {
        mutableStateListOf(
            DeviceUi(1, "Gia đình", "bedroom", false, Icons.Default.Group, Color.Blue),
            DeviceUi(2, "Marketing", "living room", false, Icons.Default.Home, Color.Red),
            DeviceUi(3, "Kỹ thuật", "kitchen", false, Icons.Default.Group, Color.Green)
        )
    }

    LazyColumn {
        itemsIndexed(deviceList) { index, device ->
            Spacer(Modifier.height(8.dp))


            DeviceCardSwipeable(
                deviceName = device.name,
                roomName = device.room,
                isRevealed = device.isRevealed,
                onExpandOnly = {
                    deviceList.indices.forEach { i ->
                        deviceList[i] = deviceList[i].copy(isRevealed = i == index)
                    }
                },
                onClick = { /* TODO: Handle click */ },
                onCollapse = {
                    deviceList[index] = device.copy(isRevealed = false)
                },
                onDelete = { deviceList.removeAt(index) },
                onEdit = { /* TODO */ }
            )
        }
    }
}