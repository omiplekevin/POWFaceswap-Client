package com.pow.faceswap.views.client.state

import android.net.Uri
import com.pow.faceswap.data.ServerFileTree
import com.pow.faceswap.data.SwapResponse

data class ClientScreenState(
	var isLoading: Boolean = false,
	var errMessage: String? = null,
	var swapResponse: SwapResponse? = null,
	var capturedImagePath: Uri? = null,
	var headCount: Int = -1,
	var referenceFile: String = "",
	var serverFileTree: ServerFileTree? = null,
)