package com.pow.faceswap.views.viewer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pow.faceswap.data.RestAPIFlow
import com.pow.faceswap.views.viewer.state.ViewerState
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

object ViewerViewModel : ViewModel() {
	
	private val restAPIFlow: RestAPIFlow = RestAPIFlow()
	
	private val _viewerState = MutableStateFlow(ViewerState())
	val viewerState: StateFlow<ViewerState> = _viewerState.asStateFlow()
	
	fun getRecentResults() {
		viewModelScope.launch {
			restAPIFlow.getRecentResults()
				.flowOn(Dispatchers.IO)
				.onStart {
					_viewerState.update { currentState ->
						currentState.copy(
							isLoading = true,
							errMessage = ""
						)
					}
				}
				.onCompletion {
					_viewerState.update { currentState ->
						currentState.copy(
							isLoading = false
						)
					}
				}
				.catch {
					_viewerState.update { currentState ->
						currentState.copy(
							errMessage = it.message
						)
					}
				}
				.collect {
					_viewerState.update { currentState ->
						currentState.copy(
							recentResult = it.body(),
							errMessage = null
						)
					}
				}
		}
	}
	
	fun printRecentOutput() {
		viewModelScope.launch {
			restAPIFlow.printRecentOutput()
				.flowOn(Dispatchers.IO)
				.onStart {
					_viewerState.update { currentState -> currentState.copy(isLoading = true, isPrinting = true) }
				}
				.onCompletion {
					_viewerState.update { currentState -> currentState.copy(isLoading = false, isPrinting = false) }
				}
				.catch {
					_viewerState.update { currentState -> currentState.copy(errMessage = it.message ?: "") }
				}
				.collect { Log.d("PRINT_ACTION", "${it.body()}") }
		}
	}
	
	fun getRealOutputFile(): String {
		if (_viewerState.value.recentResult != null) {
			return _viewerState.value.recentResult?.files?.find { it.contains("swapped_group.jpg") }.toString()
		}
		return ""
	}
}