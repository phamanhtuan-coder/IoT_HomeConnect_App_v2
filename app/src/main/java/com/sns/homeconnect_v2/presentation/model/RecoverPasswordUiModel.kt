package com.sns.homeconnect_v2.presentation.model

data class RecoverPasswordUiModel(
    val email: String = "",
    val emailError: String = "",
    val errorMessage: String = ""
) {
    fun isValid(): Boolean {
        return emailError.isEmpty() && email.isNotBlank()
    }
}