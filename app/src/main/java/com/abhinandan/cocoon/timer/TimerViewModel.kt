package com.abhinandan.cocoon.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class TimerStatus { IDLE, RUNNING, PAUSED, FINISHED }

data class TimerUiState(
    val label: String = "Focus",
    val totalSeconds: Int = 25 * 60,
    val remainingSeconds: Int = 25 * 60,
    val status: TimerStatus = TimerStatus.IDLE
)

class TimerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState

    private var tickJob: Job? = null

    fun start(label: String, minutes: Int) {
        val totalSeconds = minutes * 60
        _uiState.value = TimerUiState(
            label = label,
            totalSeconds = totalSeconds,
            remainingSeconds = totalSeconds,
            status = TimerStatus.RUNNING
        )
        startTicking()
    }

    fun pause() {
        tickJob?.cancel()
        _uiState.value = _uiState.value.copy(status = TimerStatus.PAUSED)
    }

    fun resume() {
        if (_uiState.value.status != TimerStatus.PAUSED) return
        _uiState.value = _uiState.value.copy(status = TimerStatus.RUNNING)
        startTicking()
    }

    fun stop() {
        tickJob?.cancel()
        _uiState.value = TimerUiState()
    }

    private fun startTicking() {
        tickJob?.cancel()
        tickJob = viewModelScope.launch {
            while (_uiState.value.status == TimerStatus.RUNNING && _uiState.value.remainingSeconds > 0) {
                delay(1000)
                val current = _uiState.value
                if (current.status != TimerStatus.RUNNING) break
                val next = current.remainingSeconds - 1
                _uiState.value = if (next <= 0) {
                    current.copy(remainingSeconds = 0, status = TimerStatus.FINISHED)
                } else {
                    current.copy(remainingSeconds = next)
                }
            }
        }
    }

    override fun onCleared() {
        tickJob?.cancel()
        super.onCleared()
    }
}