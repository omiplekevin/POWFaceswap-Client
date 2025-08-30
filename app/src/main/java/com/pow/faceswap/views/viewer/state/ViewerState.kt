package com.pow.faceswap.views.viewer.state

import com.pow.faceswap.data.RecentResultsResponse

data class ViewerState(
	var isLoading: Boolean = false,
	var recentResult: RecentResultsResponse? = null,
	var errMessage: String? = null,
)