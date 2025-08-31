package com.pow.faceswap.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pow.faceswap.views.client.ClientGalleryView
import com.pow.faceswap.views.client.ClientImagePreviewView
import com.pow.faceswap.views.client.ClientScreen
import com.pow.faceswap.views.selector.ScreenSelectorView
import com.pow.faceswap.views.viewer.ViewerScreen

sealed class RootScreens(val path: String) {
	data object Root : RootScreens(path = "root")
	data object ScreenSelect : RootScreens(path = "screen_select")
	data object Client : RootScreens(path = "client")
	data object ClientImagePreview : RootScreens(path = "client_image_preview")
	data object ClientGalleryView : RootScreens(path = "client_gallery")
	data object Viewer : RootScreens(path = "viewer")
}

@Composable
fun NavGraphRoot(navGraphController: NavHostController) {
	NavHost(
		navController = navGraphController, route = RootScreens.Root.path, startDestination = RootScreens.ScreenSelect.path
	) {
		composable(route = RootScreens.ScreenSelect.path) {
			ScreenSelectorView(Modifier, onScreenSelected = { screenPath, screenTitle ->
				it.savedStateHandle.apply {
					set("title", screenTitle)
				}
				navGraphController.navigate(screenPath)
			})
		}
		
		composable(route = RootScreens.Client.path) {
			ClientScreen(
				modifier = Modifier,
				title = navGraphController.previousBackStackEntry?.savedStateHandle?.get<String>("title") ?: "",
				navigateUp = {
					navGraphController.popBackStack()
				},
				onHeadCountSelected = {
					navGraphController.navigate(RootScreens.ClientGalleryView.path)
				},
				onImagePreview = {
					//change to image preview screen
					navGraphController.navigate(RootScreens.ClientImagePreview.path)
				})
		}
		
		composable(route = RootScreens.ClientImagePreview.path) {
			ClientImagePreviewView(
				modifier = Modifier,
				goToViewer = {
					navGraphController.navigate(RootScreens.Viewer.path) {
						popUpTo(RootScreens.Client.path)
					}
				},
				navigateUp = {
					navGraphController.popBackStack()
				}
			)
		}
		
		composable(route = RootScreens.ClientGalleryView.path) {
			ClientGalleryView(
				modifier = Modifier,
				navigateUp = {
					navGraphController.popBackStack()
				}
			)
		}
		
		composable(route = RootScreens.Viewer.path) {
			ViewerScreen(
				modifier = Modifier,
				title = navGraphController.previousBackStackEntry?.savedStateHandle?.get<String>("title") ?: "",
				navigateUp = {
					navGraphController.popBackStack()
				})
		}
	}
}