//package com.sns.homeconnect_v2.presentation.viewmodel.auth
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.sns.homeconnect_v2.domain.usecase.auth.NewPasswordUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//sealed class NewPassWordState {
//    object Idle : NewPassWordState()               // Chưa làm gì
//    object Loading : NewPassWordState()            // Đang loading
//    data class Success(val message: String?) : NewPassWordState()
//    data class Error(val error: String?) : NewPassWordState()
//}
//
//@HiltViewModel
//class NewPasswordViewModel @Inject constructor(
//    private val newPasswordUseCase: NewPasswordUseCase,
//): ViewModel() {
//    private val _newPasswordState = MutableStateFlow<NewPassWordState>(NewPassWordState.Idle)
//    val newPasswordState = _newPasswordState.asStateFlow()
//
//    fun newPassword(email: String, password: String) {
//        // Reset state
//        _newPasswordState.value = NewPassWordState.Loading
//        viewModelScope.launch {
//           newPasswordUseCase(email, password).fold(
//                onSuccess = { response ->
//                    Log.d("NewPasswordViewModel", "Success: ${response.message}")
//                    _newPasswordState.value = NewPassWordState.Success(response.message)
//                },
//                onFailure = { e ->
//                    Log.e("NewPasswordViewModel", "Error: ${e.message}")
//                    _newPasswordState.value = NewPassWordState.Error(e.message ?: "Đặt lại mật khẩu thất bại!")
//                }
//            )
//        }
//    }
//}