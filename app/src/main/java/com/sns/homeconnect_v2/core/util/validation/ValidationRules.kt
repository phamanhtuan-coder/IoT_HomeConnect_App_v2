package com.example.ungdungquanlyvadieukhieniot_homeconnect.ui.validation

object ValidationRules {
    // Độ dài tối thiểu của mật khẩu
    const val MIN_PASSWORD_LENGTH = 8

    // Độ dài tối thiểu của họ tên
    const val MIN_FULLNAME_LENGTH = 3

    // Độ dài tối thiểu của địa chỉ
    const val MIN_ADDRESS_LENGTH = 10

    // Regex để kiểm tra số điện thoại: chỉ chấp nhận từ 10 đến 11 chữ số
    const val PHONE_NUMBER_REGEX = "^[0-9]{10,11}\$"

    // Danh sách các ký tự đặc biệt được yêu cầu trong mật khẩu
    const val PASSWORD_SPECIAL_CHARS = "!@#\$%^&*()_-+=<>?/"

    // Quy tắc ID thiết bị
    const val DEVICE_ID_REGEX = "^[a-zA-Z0-9_]{5,20}\$" // Chỉ chữ cái, số và dấu gạch dưới, từ 5-20 ký tự.

    // Quy tắc Tên thiết bị
    const val MIN_DEVICE_NAME_LENGTH = 3
    const val MAX_DEVICE_NAME_LENGTH = 50

    /**
     * Kiểm tra xem email có đúng định dạng không.
     * Sử dụng bộ lọc có sẵn `android.util.Patterns.EMAIL_ADDRESS`.
     * @param email: Chuỗi email cần kiểm tra.
     * @return `true` nếu định dạng hợp lệ, `false` nếu không.
     */
    fun isValidEmailFormat(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Kiểm tra xem mật khẩu có chứa ít nhất một ký tự đặc biệt không.
     * Ký tự đặc biệt được định nghĩa trong `PASSWORD_SPECIAL_CHARS`.
     * @param password: Chuỗi mật khẩu cần kiểm tra.
     * @return `true` nếu chứa ký tự đặc biệt, `false` nếu không.
     */
    fun containsSpecialCharacter(password: String): Boolean {
        return password.any { PASSWORD_SPECIAL_CHARS.contains(it) }
    }
}