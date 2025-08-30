package com.pow.faceswap.views.selector

import androidx.lifecycle.ViewModel
import com.pow.faceswap.navgraph.RootScreens
import com.pow.faceswap.views.selector.state.ScreenOptionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object SelectionScreenViewModel : ViewModel() {
	
	private val screenSelection = MutableStateFlow(ScreenOptionState())
	val screenSelectionState: StateFlow<ScreenOptionState> = screenSelection.asStateFlow()
	
	fun setSelectedScreen(screenType: RootScreens) {
		screenSelection.update { currentState ->
			currentState.copy(
				selectedScreen = screenType.path
			)
		}
	}
}