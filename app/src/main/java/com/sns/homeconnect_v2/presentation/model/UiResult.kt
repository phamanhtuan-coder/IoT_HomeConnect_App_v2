package com.sns.homeconnect_v2.presentation.model

sealed interface UiResult {
    data class Success(val msg: String) : UiResult
    data class Error(val msg: String)   : UiResult
}