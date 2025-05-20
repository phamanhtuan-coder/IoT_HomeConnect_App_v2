package com.sns.homeconnect_v2.presentation.component

import android.Manifest
import android.app.Activity
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.sns.homeconnect_v2.core.permission.PermissionManager
import androidx.camera.core.ImageAnalysis
import androidx.compose.runtime.LaunchedEffect
import androidx.camera.core.Preview as CameraPreview
import com.google.mlkit.vision.barcode.BarcodeScanning
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview

/**
 * Composable function hiển thị màn hình quét mã QR.
 * Nó yêu cầu quyền truy cập camera và hiển thị bản xem trước camera nếu quyền được cấp.
 * Nếu quyền bị từ chối và người dùng đã từ chối trước đó mà không chọn "Không hỏi lại",
 * một giải thích sẽ được hiển thị. Nếu người dùng đã chọn "Không hỏi lại", họ sẽ được chuyển đến cài đặt ứng dụng.
 *
 * @param permissionManager Một instance của [PermissionManager] để xử lý các yêu cầu quyền.
 * @author Nguyễn Thanh Sang
 * @since 19-05-2025
 */
@Composable
fun QrScanScreen(permissionManager: PermissionManager) {
    val context = LocalContext.current
    val activity = context as? Activity
    val cameraPermission = Manifest.permission.CAMERA

    var showCamera by remember { mutableStateOf(false) }

    // Launcher để xin quyền
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        showCamera = isGranted
        if (!isGranted) {
            if (activity != null && !permissionManager.shouldShowRationale(activity, cameraPermission)) {
                permissionManager.openAppSettings()
            } else {
                Toast.makeText(context, "Cần quyền camera để quét mã", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Gọi xin quyền ngay khi composable được hiển thị
    LaunchedEffect(Unit) {
        if (permissionManager.isPermissionGranted(cameraPermission)) {
            showCamera = true
        } else {
            permissionLauncher.launch(cameraPermission)
        }
    }

    // Giao diện
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showCamera) {
            QrCameraPreview(onCodeScanned = {
                Toast.makeText(context, "Mã đã quét: $it", Toast.LENGTH_LONG).show()
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QrScanScreenPreview() {
    val context = LocalContext.current
    val permissionManager = remember { PermissionManager(context) }
    QrScanScreen(permissionManager = permissionManager)
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun QrCameraPreview(
    modifier: Modifier = Modifier,
    onCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        modifier = modifier, // vẫn giữ modifier từ bên ngoài
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT // 👈 Điều này giúp nhận đúng kích thước từ Compose
                )
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            val preview = CameraPreview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val scanner = BarcodeScanning.getClient()
            val analyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            analyzer.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            barcodes.firstOrNull()?.rawValue?.let {
                                onCodeScanned(it)
                            }
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                } else {
                    imageProxy.close()
                }
            }

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        analyzer
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}