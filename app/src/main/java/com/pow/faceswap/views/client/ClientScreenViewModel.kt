package com.pow.faceswap.views.client

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pow.faceswap.data.RestAPIFlow
import com.pow.faceswap.views.client.state.ClientScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

object ClientScreenViewModel : ViewModel() {
	
	private val restApiFlow: RestAPIFlow = RestAPIFlow()
	
	private val _clientState = MutableStateFlow(ClientScreenState())
	val clientState: StateFlow<ClientScreenState> = _clientState.asStateFlow()
	
	fun resetState() {
		_clientState.update { currentState ->
			currentState.copy(
				isLoading = false,
				errMessage = null,
				swapResponse = null,
				capturedImagePath = null,
				headCount = -1,
				referenceFile = "",
				serverFileTree = null,
			)
		}
	}
	
	fun setHeadcount(headCount: Int) {
		_clientState.update { currentState ->
			currentState.copy(
				headCount = headCount
			)
		}
	}
	
	fun setCapturedImagePath(path: Uri?) {
		_clientState.update { currentState ->
			currentState.copy(
				capturedImagePath = path
			)
		}
	}
	
	fun setReferenceFile(referenceFile: String) {
		_clientState.update { currentState ->
			currentState.copy(
				referenceFile = referenceFile
			)
		}
	}
	
	fun getServerFileTree() {
		viewModelScope.launch {
			restApiFlow.getServerFileTree()
				.flowOn(Dispatchers.IO)
				.onStart {
					_clientState.update { currentState ->
						currentState.copy(isLoading = true)
					}
				}
				.onCompletion {
					_clientState.update { currentState ->
						currentState.copy(isLoading = false)
					}
				}
				.catch {
					_clientState.update { currentState ->
						currentState.copy(errMessage = it.message ?: "Uh oh! Something went wrong!")
					}
				}
				.collect {
					_clientState.update { currentState ->
						currentState.copy(serverFileTree = it.body(), errMessage = null)
					}
				}
		}
	}
	
	fun performSwap(capturedImage: File, numHeads: Int, referenceFile: String) {
		viewModelScope.launch {
			restApiFlow.performSwap(capturedImage, numHeads, referenceFile)
				.flowOn(Dispatchers.IO)
				.onStart {
					_clientState.update { currentState ->
						currentState.copy(isLoading = true, swapResponse = null)
					}
				}
				.onCompletion {
					_clientState.update { currentState ->
						currentState.copy(
							isLoading = false,
						)
					}
				}
				.catch {
					_clientState.update { currentState ->
						currentState.copy(
							isLoading = false,
							errMessage = it.message ?: "An error occured!"
						)
					}
				}
				.collect { response ->
					when (response.isSuccessful) {
						true -> {
							_clientState.update { currentState ->
								currentState.copy(
									isLoading = false,
									swapResponse = response.body(),
									errMessage = ""
								)
							}
						}
						
						false -> {
							_clientState.update { currentState ->
								currentState.copy(
									isLoading = false,
									swapResponse = null,
									errMessage = response.errorBody()?.toString() ?: "Response was unsuccessfull!"
								)
							}
						}
					}
				}
		}
	}
}