package com.sns.homeconnect_v2.presentation.viewmodel.snackbar

// presentation/viewmodel/snackbar/SnackbarViewModel.kt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SnackbarViewModel @Inject constructor() : ViewModel() {
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    private val _snackbarVariant = MutableStateFlow(SnackbarVariant.INFO)
    val snackbarVariant = _snackbarVariant.asStateFlow()

    fun showSnackbar(msg: String, variant: SnackbarVariant = SnackbarVariant.INFO) {
        _snackbarMessage.value = msg
        _snackbarVariant.value = variant
    }

    fun hideSnackbar() {
        _snackbarMessage.value = null
    }
}

