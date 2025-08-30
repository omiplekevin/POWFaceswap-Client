package com.pow.faceswap.views.client

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import java.io.File

@Composable
fun ClientImagePreviewView(
	modifier: Modifier = Modifier,
	viewModel: ClientScreenViewModel = viewModel(),
	navigateUp: () -> Unit,
) {
	val context = LocalContext.current
	val clientState by viewModel.clientState.collectAsState()
	
	Scaffold { paddingValues ->
		Column(
			modifier = modifier
				.padding(paddingValues)
				.fillMaxSize(),
			verticalArrangement = Arrangement.SpaceBetween
		) {
			val uri = clientState.capturedImagePath
			if (clientState.isLoading) {
				LinearProgressIndicator()
			}
			AsyncImage(
				model = uri,
				contentScale = ContentScale.Fit,
				contentDescription = "Client image preview!",
				onState = {
					when (it) {
						AsyncImagePainter.State.Empty -> {}
						is AsyncImagePainter.State.Error -> {
							Log.e("COIL_IMAGE", "failed to load ${uri?.path ?: "null/empty"}")
						}
						
						is AsyncImagePainter.State.Loading -> {
							Log.d("COIL_IMAGE", "loading ${uri?.path ?: "null/empty"}")
						}
						
						is AsyncImagePainter.State.Success -> {
							Log.d("COIL_IMAGE", "loaded ${uri?.path ?: "null/empty"}")
						}
					}
				}
			)
			
			Text(
				"output folder: ${clientState.swapResponse?.output_folder}," +
						"\noutput image: ${clientState.swapResponse?.group_image}," +
						"\nstrip image: ${clientState.swapResponse?.strip_image}," +
						"\nerrMessage: ${clientState.errMessage}," +
						"\nisLoading: ${clientState.isLoading}"
			)
			
			Row {
				Button(onClick = navigateUp) {
					Text("Re-take!")
				}
				Button(onClick = {
					val resolvedFile = clientState.capturedImagePath?.toRelativePath(context) ?: ""
					val headCount = clientState.headCount
					viewModel.performSwap(
						getFileFromRelativePath(context, resolvedFile) ?: File(""),
						headCount
					)
				}) {
					Text("Done")
				}
			}
		}
	}
}

fun Uri.toRelativePath(context: Context): String? {
	val projection = arrayOf(
		MediaStore.Images.Media.RELATIVE_PATH,
		MediaStore.Images.Media.DISPLAY_NAME
	)
	
	context.contentResolver.query(this, projection, null, null, null)?.use { cursor ->
		val relativePathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)
		val displayNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
		
		if (cursor.moveToFirst()) {
			val relativePath = cursor.getString(relativePathIndex) ?: ""
			val displayName = cursor.getString(displayNameIndex) ?: ""
			return "$relativePath$displayName"
		}
	}
	return null
}

fun getFileFromRelativePath(context: Context, relativePath: String): File? {
	val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
	return File(picturesDir, relativePath.removePrefix("Pictures/"))
}