package com.pow.faceswap.views.client

import android.net.Uri
import androidx.camera.compose.CameraXViewfinder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientScreen(
	modifier: Modifier,
	title: String,
	navigateUp: () -> Unit,
	onImagePreview: () -> Unit,
	viewModel: ClientScreenViewModel = viewModel(),
) {
	Scaffold(topBar = {
		TopAppBar(title = {
			Text(title)
		}, navigationIcon = {
			IconButton(onClick = navigateUp) {
				Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
			}
		})
	}) { paddingValues ->
		Column(modifier = Modifier.padding(paddingValues)) {
			CameraPreviewScreen(
				modifier
					.background(color = Color(255, 0, 0, 1)), onPhotoCaptured = { photoRelativePath, headCount ->
					viewModel.setCapturedImagePathAndHeadCount(photoRelativePath, headCount)
					onImagePreview()
				})
		}
	}
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreviewScreen(modifier: Modifier = Modifier, onPhotoCaptured: (Uri?, Int) -> Unit) {
	val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
	if (cameraPermissionState.status.isGranted) {
		CameraPreviewContent(onPhotoCaptured = onPhotoCaptured)
	} else {
		Column(
			modifier = modifier
				.fillMaxSize()
				.wrapContentSize()
				.widthIn(max = 480.dp), horizontalAlignment = Alignment.CenterHorizontally
		) {
			val textToShow = if (cameraPermissionState.status.shouldShowRationale) {                // If the user has denied the permission but the rationale can be shown,
				// then gently explain why the app requires this permission
				"Whoops! Looks like we need your camera to work our magic!" + "Don't worry, we just wanna see your pretty face (and maybe some cats).  " + "Grant us permission and let's get this party started!"
			} else {                // If it's the first time the user lands on this feature, or the user
				// doesn't want to be asked again for this permission, explain that the
				// permission is required
				"Hi there! We need your camera to work our magic! ✨\n" + "Grant us permission and let's get this party started! \uD83C\uDF89"
			}
			Text(textToShow, textAlign = TextAlign.Center)
			Spacer(Modifier.height(16.dp))
			Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
				Text("Unleash the Camera!")
			}
		}
	}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CameraPreviewContent(
	modifier: Modifier = Modifier,
	viewModel: CameraViewModel = viewModel(),
	lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
	onPhotoCaptured: (Uri?, Int) -> Unit,
) {
	val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
	val context = LocalContext.current
	var checkedIndex by remember { mutableIntStateOf(-1) }
	
	LaunchedEffect(lifecycleOwner) {
		viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
	}
	
	Box(modifier = modifier.background(color = Color(255, 0, 0, 1))) {
		surfaceRequest?.let { request ->
			CameraXViewfinder(
				surfaceRequest = request,
			)
		}
		
		Column(
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.background(color = Color(255, 0, 0, 1))
		) {
			Row(
				horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)
			) {
				(1..4).forEach { index ->
					Row(
						verticalAlignment = Alignment.CenterVertically
					) {
						Checkbox(
							checked = checkedIndex == index, onCheckedChange = {
								checkedIndex = if (checkedIndex == index) -1 else index
							})
						Text(text = "$index face${if (index > 1) "s" else ""}")
					}
				}
			}
			
			// Debug: show selected index
			if (checkedIndex != -1) {
				Text(
					text = "Selected: $checkedIndex", modifier = Modifier.padding(top = 8.dp)
				)
			}            // Capture Button
			Button(
				enabled = (checkedIndex != -1), onClick = {
					viewModel.takePhoto(context) { path ->
						onPhotoCaptured(path, checkedIndex)
					}
				}, modifier = Modifier.padding(16.dp)
			) {
				Text("Capture")
			}
		}
	}
}