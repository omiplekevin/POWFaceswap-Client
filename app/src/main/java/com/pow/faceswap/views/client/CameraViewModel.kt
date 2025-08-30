package com.pow.faceswap.views.client

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Locale

object CameraViewModel : ViewModel() {
	private var cameraProvider: ProcessCameraProvider? = null
	private var imageCapture: ImageCapture? = null
	
	private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
	val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest
	
	fun bindToCamera(context: Context, lifecycleOwner: LifecycleOwner) {
		val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
		cameraProviderFuture.addListener({
			cameraProvider = cameraProviderFuture.get()
			
			val preview = Preview.Builder().build().apply {
				setSurfaceProvider { request ->
					_surfaceRequest.value = request
				}
			}
			
			imageCapture = ImageCapture.Builder()
				.setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY) // quick capture
				.build()
			
			try {
				cameraProvider?.unbindAll()
				cameraProvider?.bindToLifecycle(
					lifecycleOwner,
					CameraSelector.DEFAULT_BACK_CAMERA,
					preview,
					imageCapture
				)
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}, ContextCompat.getMainExecutor(context))
	}
	
	fun takePhoto(
		context: Context,
		onPhotoCaptured: (Uri?) -> Unit
	) {
		val imageCapture = imageCapture ?: return
		
		// Save to MediaStore (Gallery)
		val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
			
			.format(System.currentTimeMillis())
		val contentValues = ContentValues().apply {
			put(MediaStore.MediaColumns.DISPLAY_NAME, name)
			put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Compose")
			}
		}
		
		val outputOptions = ImageCapture.OutputFileOptions
			.Builder(
				context.contentResolver,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				contentValues
			)
			.build()
		
		imageCapture.takePicture(
			outputOptions,
			ContextCompat.getMainExecutor(context),
			object : ImageCapture.OnImageSavedCallback {
				override fun onError(exc: ImageCaptureException) {
					exc.printStackTrace()
					onPhotoCaptured(null)
				}
				
				override fun onImageSaved(output: ImageCapture.OutputFileResults) {
					onPhotoCaptured(output.savedUri)
				}
			}
		)
	}
}