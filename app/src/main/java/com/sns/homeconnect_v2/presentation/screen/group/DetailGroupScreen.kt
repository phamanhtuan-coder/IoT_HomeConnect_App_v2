package com.sns.homeconnect_v2.presentation.screen.group

import IoTHomeConnectAppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.sns.homeconnect_v2.presentation.component.widget.*

@Composable
fun DetailGroupScreen() {
    /* ---------------------------------------------------------
       1.  Toàn bộ dữ liệu “cứng” đưa thành biến ở đầu hàm.
           Khi dùng ViewModel, bạn chỉ cần thay thế giá trị cho
           các biến này (hoặc truyền chúng qua tham số).
    ----------------------------------------------------------*/
    val groupName        = "Family Group"
    val groupDescription = "Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group "
    val users = listOf(
        Triple("Nguyễn Văn A", "Owner",  ""),
        Triple("Trần Thị B" , "Vice" ,  "https://i.pravatar.cc/150?img=8"),
        Triple("Lê Văn C"   , "Admin",  "https://i.pravatar.cc/150?img=12"),
        Triple("Phạm Thị D" , "Member", ""),
        Triple("Hoàng Văn E", "Member", "https://i.pravatar.cc/150?img=20")
    )
    val groupAvatars: List<String> =
        users
            .map { it.third }
            .filter { it.isNotBlank() }
            .take(3)
            .let { list ->
                List(3) { index -> list.getOrNull(index) ?: "" }
            }

    val memberCount = users.size.toString()

    /* ---------------------------------------------------------
       2.  State chỉ dùng cho UI (điều khiển swipe).
    ----------------------------------------------------------*/
    val revealStates = remember { users.map { mutableStateOf(false) } }

    /* ---------------------------------------------------------
       3.  Các nút FAB con.
    ----------------------------------------------------------*/
    val fabChildren = listOf(
        FabChild(icon = Icons.Default.Edit  , onClick = { /* sửa group */ }),
        FabChild(icon = Icons.Default.Delete, containerColor = Color.Red, onClick = { /* xoá group */ }),
        FabChild(icon = Icons.Default.Share , onClick = { /* share group */ })
    )

    /* ---------------------------------------------------------
       4.  UI chính
    ----------------------------------------------------------*/
    IoTHomeConnectAppTheme {
        Scaffold(
            containerColor = Color.White,
            floatingActionButton = {
                RadialFab(
                    items            = fabChildren,
                    radius           = 120.dp,
                    startDeg         = -90f,
                    sweepDeg         = -90f,
                    onParentClick    = { /* thêm thành viên */ }
                )
            },
            floatingActionButtonPosition = FabPosition.End
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                /* ------------ HEADER MÀU ĐẬM (có góc bo) -------------- */
                ColoredCornerBox(
                    backgroundColor = Color(0xFF3A4750),
                    cornerRadius    = 40.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {

                        /* ----- Thông tin tên & mô tả group ----- */
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .height(IntrinsicSize.Min)
                        ) {
                            /*  Phần trái: icon – tên – mô tả  */
                            Column(
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                // Tên
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Groups,
                                        contentDescription = "Group Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = groupName,
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(Modifier.height(12.dp))

                                // Mô tả
                                Row(
                                    modifier = Modifier
                                        .width(250.dp),               // hàng đủ rộng
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EditCalendar,
                                        contentDescription = "Description Icon",
                                        tint  = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))

                                    Text(
                                        text = groupDescription,
                                        modifier = Modifier.weight(1f),               // <── quan trọng
                                        color  = Color.White,
                                        fontSize = 16.sp,
                                        // tuỳ chọn: tối đa 2 dòng, nếu dài hơn sẽ có dấu …
                                        maxLines  = 2,
                                        overflow  = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            /*  Phần phải: avatar chồng lên nhau  */
                            Row(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy((-12).dp)
                            ) {
                                groupAvatars.forEach { url ->
                                    if (url.isNotBlank()) {
                                        Image(
                                            painter = rememberAsyncImagePainter(url),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Color.Black.copy(alpha = 0.2f))
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Default avatar",
                                            tint  = Color.White,
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Color.DarkGray)
                                                .padding(6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        /* ----- SearchBar ----- */
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            SearchBar(
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .fillMaxWidth(),
                                onSearch = { /* TODO: xử lý search */ }
                            )
                        }
                    }
                }

                /* ------------ KHUNG TRẮNG BO GÓC NGƯỢC -------------- */
                InvertedCornerHeader(
                    backgroundColor = Color.White,
                    overlayColor    = Color(0xFF3A4750)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        LabeledBox(
                            label = "Số lượng thành viên",
                            value = memberCount
                        )

                        IconButton(
                            onClick = { /* TODO: rời group */ },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Exit group",
                                tint  = Color(0xFF3A4750),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                /* ------------ DANH SÁCH THÀNH VIÊN -------------- */
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(users.size) { index ->
                        val (name, role, avatar) = users[index]
                        UserCardSwipeable(
                            userName   = name,
                            role       = role,
                            avatarUrl  = avatar,
                            isRevealed = revealStates[index].value,
                            onExpandOnly = {
                                revealStates.forEachIndexed { i, state ->
                                    state.value = i == index
                                }
                            },
                            onCollapse = { revealStates[index].value = false },
                            onDelete   = { /* TODO: xoá user */ },
                            onEdit     = { /* TODO: chỉnh sửa role */ }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailGroupScreenPreview() {
    DetailGroupScreen()
}
