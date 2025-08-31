package com.pow.faceswap.views.viewer

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.pow.faceswap.utils.ThreadSafeCountdown
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScreen(
	modifier: Modifier = Modifier,
	title: String,
	navigateUp: () -> Unit,
	viewerModel: ViewerViewModel = viewModel(),
) {
	val viewerState by viewerModel.viewerState.collectAsState()
	val snackbarHostState = remember { SnackbarHostState() }
	var countdownValue by remember { mutableIntStateOf(0) }
	var countdownVisible by remember { mutableStateOf(false) }
	
	// MODIFY initialCount TO DESIRED LENGTH OF COUNT
	val MAX_COUNTDOWN: Int = 30
	
	val countdown = remember {
		ThreadSafeCountdown(
			initialCount = MAX_COUNTDOWN,
			onTick = { count ->
				CoroutineScope(Dispatchers.Main).launch {
					countdownValue = count
					countdownVisible = true
				}
			},
			onComplete = {
				CoroutineScope(Dispatchers.Main).launch {
					countdownVisible = false
					navigateUp()
				}
			}
		)
	}
	
	viewerModel.getRecentResults()
	
	MaterialTheme {
		Scaffold(
			snackbarHost = {
				SnackbarHost(snackbarHostState)
			},
			topBar = {
				TopAppBar(
					title = {
						Text(title)
					},
					navigationIcon = {
						IconButton(onClick = navigateUp) {
							Icon(
								imageVector = Icons.AutoMirrored.Filled.ArrowBack,
								contentDescription = "Back"
							)
						}
					}
				)
			}
		) { paddingValues ->
			Column(
				modifier = modifier
					.padding(paddingValues)
					.verticalScroll(rememberScrollState())
			) {
				if (countdownVisible) {
					Text("Returning to camera in $countdownValue...")
					LinearProgressIndicator(
						progress = { countdownValue.toFloat() / MAX_COUNTDOWN },
						modifier = Modifier
							.fillMaxWidth()
							.height(8.dp)
					)
				}
				
				if (viewerState.recentResult != null) {
					AsyncImage(
						model = viewerModel.getRealOutputFile(),
						contentDescription = null,
					)
				}
				Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
					Button(
						onClick = {
							viewerModel.getRecentResults()
						},
						modifier = Modifier.weight(1f)
					) { Text("Refresh") }
					
					Button(
						onClick = {
							countdownValue = MAX_COUNTDOWN // Immediately update UI
							countdownVisible = true
							countdown.restart()
							viewerModel.printRecentOutput()
						},
						modifier = Modifier.weight(1f)
					) {
						Text("Print")
					}
				}
			}
		}
	}
	
	DisposableEffect(Unit) {
		onDispose {
			countdown.stop()
		}
	}
}

@Preview
@Composable
fun ViewerScreenPreview() {
	Scaffold { paddingValues ->
		Column(
			modifier = Modifier
				.padding(paddingValues)
				.padding(horizontal = 8.dp)
				.verticalScroll(rememberScrollState())
		) {
			AsyncImage(
				model = "",
				contentDescription = null,
			)
			Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
				Button(
					onClick = {},
					modifier = Modifier.weight(1f)
				) { Text("Refresh") }
				
				Button(
					onClick = {},
					modifier = Modifier.weight(1f)
				) {
					Text("Print")
				}
			}
		}
	}
}