package com.pow.faceswap.views.client

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientGalleryView(
	modifier: Modifier = Modifier,
	viewModel: ClientScreenViewModel = viewModel(),
	navigateUp: () -> Unit,
) {
	val clientState by viewModel.clientState.collectAsState()
	
	Scaffold(topBar = {
		TopAppBar(
			title = {
				Text("Gallery")
			},
			navigationIcon = {
				IconButton(onClick = navigateUp) {
					Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
				}
			}
		)
	}) { paddingValues ->
		Log.d("REFERENCE", "items: ${clientState.serverFileTree}")
		
		LazyVerticalStaggeredGrid(
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize(),
			columns = StaggeredGridCells.Adaptive(200.dp),
			verticalItemSpacing = 4.dp,
			horizontalArrangement = Arrangement.spacedBy(4.dp),
			content = {
				val fileList = when (clientState.headCount) {
					1 -> clientState.serverFileTree?._1p ?: listOf()
					2 -> clientState.serverFileTree?._2p ?: listOf()
					3 -> clientState.serverFileTree?._3p ?: listOf()
					else -> clientState.serverFileTree?._4p ?: listOf()
				}
				
				items(count = fileList.size) { index ->
					AsyncImage(
						model = fileList[index].url,
						contentScale = ContentScale.Crop,
						contentDescription = null,
						modifier = Modifier
							.fillMaxWidth()
							.wrapContentHeight()
							.clickable(
								onClick = {
									viewModel.setReferenceFile(fileList[index].filename)
									navigateUp()
								}
							)
					)
				}
			},
		)
	}
}