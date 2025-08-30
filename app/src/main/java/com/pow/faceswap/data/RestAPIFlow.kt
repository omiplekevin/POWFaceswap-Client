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
	
	suspend fun getServerFileTree(): Flow<Response<ServerFileTree>> =
		flow {
			emit(
				RetrofitInstance()
					.restApi
					.getServerFileTree()
			)
		}
	
	suspend fun performSwap(targetFile: File, headCount: Int, referenceFile: String): Flow<Response<SwapResponse>> =
		flow {
			val requestFile = targetFile.asRequestBody("image/*".toMediaTypeOrNull())
			val targetPart = MultipartBody.Part.createFormData("target", targetFile.name, requestFile)
			
			val numHeadsPart = headCount.toString().toRequestBody("text/plain".toMediaTypeOrNull())
			val refFile = referenceFile.toString().toRequestBody("text/plain".toMediaTypeOrNull())
			
			emit(
				RetrofitInstance()
					.restApi
					.swapFaces(targetPart, numHeadsPart, refFile)
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
	
	suspend fun printRecentOutput(): Flow<Response<String>> =
		flow { 
			emit(
				RetrofitInstance()
					.restApi
					.printLatestOutput()
			)
		}
}