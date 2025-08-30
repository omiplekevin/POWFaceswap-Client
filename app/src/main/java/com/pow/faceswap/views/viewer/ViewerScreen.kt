package com.pow.faceswap.views.viewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerView(
	modifier: Modifier,
	title: String,
	navigateUp: () -> Unit,
	viewerModel: ViewerViewModel = viewModel(),
) {
	
	val viewerState by viewerModel.viewerState.collectAsState()
	var printTextFieldValue by remember { mutableStateOf("") }
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	
	MaterialTheme {
		Scaffold(
			snackbarHost = {
				SnackbarHost(snackbarHostState)
			},
			topBar = {
				TopAppBar(title = {
					Text(title)
				}, navigationIcon = {
					IconButton(onClick = navigateUp) {
						Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
					}
				})
			}) { paddingValues ->
			
			Column(
				modifier = modifier
					.padding(paddingValues)
					.verticalScroll(rememberScrollState())
			) {
				if (viewerState.recentResult != null) {
					AsyncImage(
						model = viewerState.recentResult?.files[0],
						contentDescription = null,
					)
				}
				Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
					Button(onClick = {
						viewerModel.getRecentResults()
					}) { Text("Refresh") }
					
					Button(onClick = {
//						scope.launch {
//							snackbarHostState.showSnackbar("Printing... <install printer service>")
//						}
						viewerModel.printRecentOutput()
					}) {
						Text("Print")
					}
				}
			}
		}
	}
}