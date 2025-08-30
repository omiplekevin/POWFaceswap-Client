package com.pow.faceswap.views.selector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pow.faceswap.MainActivity
import com.pow.faceswap.R
import com.pow.faceswap.navgraph.RootScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenSelectorView(
	modifier: Modifier,
	viewModel: SelectionScreenViewModel = viewModel(),
	onScreenSelected: (screen: String, title: String) -> Unit,
) {
	
	var text by remember { mutableStateOf(MainActivity._SERVER_IP) }
	
	MaterialTheme {
		Scaffold(
			modifier = modifier, topBar = {
				CenterAlignedTopAppBar(title = {
					Text(text = "PitikOnWheels")
				})
			}) { paddingValues ->
			Column(
				modifier = Modifier
					.padding(paddingValues)
					.fillMaxSize(),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center,
			) {
				val clientTitle = LocalContext.current.getString(R.string.client_title)
				val viewerTitle = LocalContext.current.getString(R.string.viewer_title)
				
				OutlinedTextField(
					value = text,
					onValueChange = {
						text = it
						MainActivity._SERVER_IP = it
					},
					label = { Text("Server IP") },
					placeholder = {
						Text("xxx.xxx.xxx.xxx:port")
					}
				)
				
				Button(onClick = {
					viewModel.setSelectedScreen(RootScreens.Client)
					onScreenSelected(RootScreens.Client.path, clientTitle)
				}) {
					Text(clientTitle)
				}
				Button(onClick = {
					viewModel.setSelectedScreen(RootScreens.Viewer)
					onScreenSelected(RootScreens.Viewer.path, viewerTitle)
				}) {
					Text(viewerTitle)
				}
			}
		}
	}
}

@Preview
@Composable
fun ScreenSelectorPreview() {
	MaterialTheme {
		Scaffold { paddingValues ->
			Column(
				modifier = Modifier
					.padding(paddingValues)
					.fillMaxSize(),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center,
			) {
				Button(onClick = { //viewModel.setSelectedScreen(RootScreens.Client)
				}) {
					Text("Client")
				}
				Button(onClick = { //viewModel.setSelectedScreen(RootScreens.Viewer)
				}) { Text("Viewer") }
			}
		}
	}
}