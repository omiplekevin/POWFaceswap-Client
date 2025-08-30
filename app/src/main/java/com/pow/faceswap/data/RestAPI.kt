package com.pow.faceswap.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RestAPI {
	
	@Multipart
	@POST("/swap")
	suspend fun swapFaces(
		@Part target: MultipartBody.Part,
		@Part("num_heads") numHeads: RequestBody,
		@Part("reference_file") referenceFile: RequestBody,
	): Response<SwapResponse>
	
	@GET("/latest-folder")
	suspend fun getRecentResult(): Response<RecentResultsResponse>
	
	@GET("/list-references")
	suspend fun getServerFileTree(): Response<ServerFileTree>
	
	@GET("/print-latest-pdf")
	suspend fun printLatestOutput(): Response<String>
}