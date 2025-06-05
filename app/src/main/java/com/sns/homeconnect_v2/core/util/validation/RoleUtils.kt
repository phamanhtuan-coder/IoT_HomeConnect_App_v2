package com.sns.homeconnect_v2.core.util.validation

enum class RoleLevel(val level: Int) {
    MEMBER(0),
    ADMIN(1),
    VICE(2),
    OWNER(3);
}

fun hasPermission(role: String, required: RoleLevel): Boolean {
    val level = when (role.lowercase()) {
        "owner"   -> RoleLevel.OWNER
        "vice"  -> RoleLevel.VICE
        "admin" -> RoleLevel.ADMIN
        else    -> RoleLevel.MEMBER
    }
    return level >= required
}
