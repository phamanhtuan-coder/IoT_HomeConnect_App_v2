package com.sns.homeconnect_v2.core.util.validation
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

object ValidationUtils {
    /**
     * Kiểm tra email.
     * - Email không được để trống.
     * - Email phải đúng định dạng.
     * @param email Chuỗi email cần kiểm tra.
     * @return Thông báo xác định email hợp lệ hay không.
     */
    fun validateEmail(email: String): String {
        return when {
            email.isBlank() -> "Email không được để trống."
            !ValidationRules.isValidEmailFormat(email) -> "Email không đúng định dạng."
            else -> "" // HỢP LỆ: KHÔNG TRẢ VỀ GÌ CẢ (chuỗi rỗng)
        }
    }


    /**
     * Kiểm tra mật khẩu.
     * - Đảm bảo mật khẩu tuân thủ các quy tắc:
     *   1. Có độ dài tối thiểu (được định nghĩa trong ValidationRules.MIN_PASSWORD_LENGTH).
     *   2. Chứa ít nhất một chữ số.
     *   3. Chứa ít nhất một chữ cái viết hoa.
     *   4. Chứa ít nhất một chữ cái viết thường.
     *   5. Chứa ít nhất một ký tự đặc biệt.
     * @param password Chuỗi mật khẩu cần kiểm tra.
     * @return Thông báo xác định mật khẩu hợp lệ hay không.
     */
    fun validatePassword(password: String): String {
        if (password.length < ValidationRules.MIN_PASSWORD_LENGTH) {
            return "Mật khẩu phải có ít nhất ${ValidationRules.MIN_PASSWORD_LENGTH} ký tự." // Kiểm tra độ dài.
        }
        if (!password.any { it.isDigit() }) {
            return "Mật khẩu phải chứa ít nhất một số." // Kiểm tra có chữ số hay không.
        }
        if (!password.any { it.isUpperCase() }) {
            return "Mật khẩu phải chứa ít nhất một chữ cái in hoa." // Kiểm tra có chữ in hoa hay không.
        }
        if (!password.any { it.isLowerCase() }) {
            return "Mật khẩu phải chứa ít nhất một chữ cái in thường." // Kiểm tra có chữ in thường hay không.
        }
        if (!ValidationRules.containsSpecialCharacter(password)) {
            return "Mật khẩu phải chứa ít nhất một ký tự đặc biệt." // Kiểm tra có ký tự đặc biệt hay không.
        }
        return "Mật khẩu hợp lệ." // Nếu tất cả điều kiện đều đúng.
    }

    /**
     * Kiểm tra tính hợp lệ của mật khẩu xác nhận.
     * - Đảm bảo mật khẩu xác nhận không để trống.
     * - Đảm bảo mật khẩu gốc không để trống.
     * - Đảm bảo mật khẩu và mật khẩu xác nhận khớp nhau.
     * - Đảm bảo mật khẩu đáp ứng các tiêu chí bảo mật:
     *   + Độ dài tối thiểu.
     *   + Chứa ít nhất một chữ số.
     *   + Chứa ít nhất một chữ cái in hoa.
     *   + Chứa ít nhất một chữ cái in thường.
     *   + Chứa ít nhất một ký tự đặc biệt.
     *
     * @param password Mật khẩu gốc.
     * @param confirmPassword Mật khẩu xác nhận.
     * @return Chuỗi thông báo xác định tình trạng hợp lệ của mật khẩu xác nhận.
     */
    fun validateConfirmPassword(password: String, confirmPassword: String): String {
        // Kiểm tra nếu mật khẩu xác nhận để trống
        if (confirmPassword.isBlank()) {
            return "Mật khẩu xác nhận không được để trống."
        }

        // Kiểm tra nếu mật khẩu gốc để trống
        if (password.isBlank()) {
            return "Mật khẩu gốc không được để trống."
        }

        // Kiểm tra nếu mật khẩu xác nhận không khớp
        if (password != confirmPassword) {
            return "Mật khẩu xác nhận không khớp."
        }

        // Kiểm tra độ dài tối thiểu
        if (password.length < ValidationRules.MIN_PASSWORD_LENGTH || confirmPassword.length < ValidationRules.MIN_PASSWORD_LENGTH) {
            return "Mật khẩu phải có ít nhất ${ValidationRules.MIN_PASSWORD_LENGTH} ký tự."
        }

        // Kiểm tra nếu mật khẩu không chứa số
        if (!password.any { it.isDigit() } || !confirmPassword.any { it.isDigit() }) {
            return "Mật khẩu phải chứa ít nhất một chữ số."
        }

        // Kiểm tra nếu mật khẩu không chứa chữ cái in hoa
        if (!password.any { it.isUpperCase() } || !confirmPassword.any { it.isUpperCase() }) {
            return "Mật khẩu phải chứa ít nhất một chữ cái in hoa."
        }

        // Kiểm tra nếu mật khẩu không chứa chữ cái in thường
        if (!password.any { it.isLowerCase() } || !confirmPassword.any { it.isLowerCase() }) {
            return "Mật khẩu phải chứa ít nhất một chữ cái in thường."
        }

        // Kiểm tra nếu mật khẩu không chứa ký tự đặc biệt
        if (!ValidationRules.containsSpecialCharacter(password) || !ValidationRules.containsSpecialCharacter(
                confirmPassword
            )
        ) {
            return "Mật khẩu phải chứa ít nhất một ký tự đặc biệt."
        }

        // Nếu tất cả điều kiện đều đúng
        return "Mật khẩu xác nhận hợp lệ."
    }

    /**
     * Kiểm tra họ tên.
     * - Họ tên không được để trống.
     * - Họ tên phải có độ dài tối thiểu (được định nghĩa trong ValidationRules.MIN_FULLNAME_LENGTH).
     * @param fullName Chuỗi họ tên cần kiểm tra.
     * @return Thông báo xác định họ tên hợp lệ hay không.
     */
    fun validateFullName(fullName: String): String {
        return if (fullName.isBlank() || fullName.length < ValidationRules.MIN_FULLNAME_LENGTH) {
            "Họ tên không hợp lệ. Phải có ít nhất ${ValidationRules.MIN_FULLNAME_LENGTH} ký tự." // Kiểm tra độ dài tối thiểu.
        } else {
            "Họ tên hợp lệ." // Nếu hợp lệ.
        }
    }

    /**
     * Kiểm tra địa chỉ.
     * - Địa chỉ không được để trống.
     * - Địa chỉ phải có độ dài tối thiểu (được định nghĩa trong ValidationRules.MIN_ADDRESS_LENGTH).
     * @param address Chuỗi địa chỉ cần kiểm tra.
     * @return Thông báo xác định địa chỉ hợp lệ hay không.
     */
    fun validateAddress(address: String): String {
        return if (address.isBlank() || address.length < ValidationRules.MIN_ADDRESS_LENGTH) {
            "Địa chỉ không hợp lệ. Phải có ít nhất ${ValidationRules.MIN_ADDRESS_LENGTH} ký tự." // Kiểm tra độ dài tối thiểu.
        } else {
            "Địa chỉ hợp lệ." // Nếu hợp lệ.
        }
    }

    /**
     * Kiểm tra số điện thoại.
     * - Số điện thoại không được để trống.
     * - Số điện thoại phải tuân theo định dạng Regex (được định nghĩa trong ValidationRules.PHONE_NUMBER_REGEX).
     * @param phoneNumber Chuỗi số điện thoại cần kiểm tra.
     * @return Thông báo xác định số điện thoại hợp lệ hay không.
     */
    fun validatePhoneNumber(phoneNumber: String): String {
        return if (phoneNumber.isBlank() || !phoneNumber.matches(Regex(ValidationRules.PHONE_NUMBER_REGEX))) {
            "Số điện thoại không hợp lệ. Phải từ 10-15 số." // Kiểm tra Regex và độ dài.
        } else {
            "Số điện thoại hợp lệ." // Nếu hợp lệ.
        }
    }

    /**
     * Kiểm tra ID thiết bị.
     * - ID không được để trống.
     * - ID phải tuân thủ định dạng: chỉ gồm chữ cái, số và dấu gạch dưới (_).
     * - Độ dài từ 5 đến 20 ký tự.
     * @param deviceId ID thiết bị cần kiểm tra.
     * @return Thông báo xác định ID hợp lệ hay không.
     */
    fun validateDeviceId(deviceId: String): String {
        return if (deviceId.isBlank() || !deviceId.matches(Regex(ValidationRules.DEVICE_ID_REGEX))) {
            "ID thiết bị không hợp lệ. Phải từ 5-20 ký tự, chỉ gồm chữ cái, số và dấu gạch dưới (_)."
        } else {
            "ID thiết bị hợp lệ."
        }
    }

    /**
     * Kiểm tra Tên thiết bị.
     * - Tên không được để trống.
     * - Tên phải có độ dài từ 3 đến 50 ký tự.
     * @param deviceName Tên thiết bị cần kiểm tra.
     * @return Thông báo xác định Tên thiết bị hợp lệ hay không.
     */
    fun validateDeviceName(deviceName: String): String {
        return if (deviceName.isBlank() || deviceName.length < ValidationRules.MIN_DEVICE_NAME_LENGTH || deviceName.length > ValidationRules.MAX_DEVICE_NAME_LENGTH) {
            "Tên thiết bị không hợp lệ. Phải có từ ${ValidationRules.MIN_DEVICE_NAME_LENGTH} đến ${ValidationRules.MAX_DEVICE_NAME_LENGTH} ký tự."
        } else {
            "Tên thiết bị hợp lệ."
        }
    }

    /**
     * Kiểm tra Tên Space.
     * - Tên Space không được để trống.
     * - Tên Space phải có độ dài từ 3 đến 50 ký tự.
     * @param spaceName Chuỗi tên Space cần kiểm tra.
     * @return Thông báo xác định tên Space hợp lệ hay không.
     */
    fun validateSpaceName(spaceName: String): String {
        // Kiểm tra nếu Tên Space để trống
        if (spaceName.isBlank()) {
            return "Tên Space không được để trống."
        }

        // Kiểm tra độ dài của Tên Space
        if (spaceName.length < 3 || spaceName.length > 50) {
            return "Tên Space phải có từ 3 đến 50 ký tự."
        }

        // Nếu tất cả các điều kiện đều hợp lệ
        return "Tên Space hợp lệ."
    }

    /**
     * Kiểm tra tính hợp lệ của SSID.
     * - SSID không được để trống.
     * - SSID phải có độ dài từ 3 đến 32 ký tự.
     * - SSID không được chứa khoảng trắng.
     * - SSID chỉ được chứa chữ cái, số và các ký tự đặc biệt '-', '_', '.'.
     *
     * @param ssid Chuỗi SSID cần kiểm tra.
     * @return Chuỗi thông báo về tình trạng hợp lệ của SSID.
     */
    fun validateSSID(ssid: String): String {
        // Kiểm tra nếu SSID để trống
        if (ssid.isBlank()) {
            return "SSID không được để trống." // Trả về thông báo nếu SSID bị bỏ trống
        }

        // Kiểm tra độ dài của SSID (phải nằm trong khoảng 3-32 ký tự)
        if (ssid.length < 3 || ssid.length > 32) {
            return "SSID phải có từ 3 đến 32 ký tự." // Trả về thông báo nếu độ dài không hợp lệ
        }

        // Kiểm tra nếu SSID chứa khoảng trắng
        if (ssid.contains(" ")) {
            return "SSID không được chứa khoảng trắng." // Trả về thông báo nếu SSID chứa khoảng trắng
        }

        // Kiểm tra nếu SSID chứa các ký tự không hợp lệ
        if (!ssid.all { it.isLetterOrDigit() || "-_.".contains(it) }) {
            return "SSID chỉ được chứa chữ cái, số và các ký tự '-', '_', '.'." // Trả về thông báo nếu SSID có ký tự không hợp lệ
        }

        // Nếu tất cả điều kiện đều đúng, SSID hợp lệ
        return "SSID hợp lệ" // Trả về thông báo SSID hợp lệ
    }
}