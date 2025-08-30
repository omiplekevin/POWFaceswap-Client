package com.pow.faceswap.views.selector.state

import com.pow.faceswap.navgraph.RootScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ScreenOptionState(
	var selectedScreen: String = RootScreens.ScreenSelect.path
)