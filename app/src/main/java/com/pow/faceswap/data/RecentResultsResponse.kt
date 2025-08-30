package com.pow.faceswap.data

data class RecentResultsResponse(
	val latest_folder: String,
	val files: List<String> = arrayListOf<String>()
)