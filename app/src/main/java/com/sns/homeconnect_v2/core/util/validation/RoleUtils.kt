package com.sns.homeconnect_v2.core.util.validation

enum class RoleLevel(val level: Int) {
    MEMBER(0), ADMIN(1), VICE(2), OWNER(3);
}

/* Giữ nguyên – dùng khi bạn chỉ có chuỗi */
fun hasPermission(role: String, required: RoleLevel): Boolean =
    stringToRole(role) >= required

/* Thêm overload – gọn hơn khi đã có enum */
fun hasPermission(current: RoleLevel, required: RoleLevel): Boolean =
    current >= required

/* Hàm chuyển đổi để tránh lặp lại mã */
private fun stringToRole(role: String): RoleLevel = when (role.lowercase()) {
    "owner" -> RoleLevel.OWNER
    "vice"  -> RoleLevel.VICE
    "admin" -> RoleLevel.ADMIN
    else    -> RoleLevel.MEMBER
}
