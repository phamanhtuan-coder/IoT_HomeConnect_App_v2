package com.sns.homeconnect_v2

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionEventHandler @Inject constructor():ViewModel() {
    private val _permissionResultFlow = MutableSharedFlow<Pair<Context, IntArray>>(replay = 1)
    val permissionResultFlow = _permissionResultFlow.asSharedFlow()

    suspend fun emitPermissionResult(context: Context, grantResults: IntArray) {
        _permissionResultFlow.emit(context to grantResults)
    }
}