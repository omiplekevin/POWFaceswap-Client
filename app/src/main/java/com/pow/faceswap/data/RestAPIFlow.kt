package com.pow.faceswap.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class RestAPIFlow {
	
	suspend fun performSwap(targetFile: File, headCount: Int): Flow<Response<SwapResponse>> =
		flow {
			val requestFile = targetFile.asRequestBody("image/*".toMediaTypeOrNull())
			val targetPart = MultipartBody.Part.createFormData("target", targetFile.name, requestFile)
			
			val numHeadsPart = headCount.toString().toRequestBody("text/plain".toMediaTypeOrNull())
			
			emit(
				RetrofitInstance()
					.restApi
					.swapFaces(targetPart, numHeadsPart)
			)
		}
	
	suspend fun getRecentResults(): Flow<Response<RecentResultsResponse>> =
		flow {
			emit(
				RetrofitInstance()
					.restApi
					.getRecentResult()
			)
		}
}