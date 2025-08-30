package com.pow.faceswap.data

import com.google.gson.annotations.SerializedName

data class ServerFileTree(
	@SerializedName("1p") val _1p: List<ServerFile>,
	@SerializedName("2p") val _2p: List<ServerFile>,
	@SerializedName("3p") val _3p: List<ServerFile>,
	@SerializedName("4p") val _4p: List<ServerFile>
)

data class ServerFile(
	val filename: String,
	val url: String,
)
