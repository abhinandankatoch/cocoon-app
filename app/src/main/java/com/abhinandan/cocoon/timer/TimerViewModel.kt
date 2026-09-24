package com.abhinandan.cocoon.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhinandan.cocoon.notifications.SessionState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class TimerStatus { IDLE, RUNNING, PAUSED, FINISHED }
enum class PomodoroPhase { WORK, BREAK }

private const val BREAK_MINUTES = 5

data class TimerUiState(
    val label: String = "Focus",
    val totalSeconds: Int = 25 * 60,
    val remainingSeconds: Int = 25 * 60,
    val status: TimerStatus = TimerStatus.IDLE,
    val isPomodoro: Boolean = false,
    val phase: PomodoroPhase = PomodoroPhase.WORK,
    val cycleCount: Int = 0
)

class TimerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState

    private var tickJob: Job? = null
    private var workMinutes = 25
    private var sessionLabel = "Focus"

    fun start(label: String, minutes: Int, isPomodoro: Boolean = false) {
        workMinutes = minutes
        sessionLabel = label
        val totalSeconds = minutes * 60
        _uiState.value = TimerUiState(
            label = label,
            totalSeconds = totalSeconds,
            remainingSeconds = totalSeconds,
            status = TimerStatus.RUNNING,
            isPomodoro = isPomodoro,
            phase = PomodoroPhase.WORK,
            cycleCount = 0
        )
        SessionState.isActive = true
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
        SessionState.isActive = false
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
                    handlePhaseComplete(current)
                } else {
                    current.copy(remainingSeconds = next)
                }
            }
        }
    }

    private fun handlePhaseComplete(current: TimerUiState): TimerUiState {
        if (!current.isPomodoro) {
            SessionState.isActive = false
            return current.copy(remainingSeconds = 0, status = TimerStatus.FINISHED)
        }

        return if (current.phase == PomodoroPhase.WORK) {
            val breakSeconds = BREAK_MINUTES * 60
            current.copy(
                phase = PomodoroPhase.BREAK,
                totalSeconds = breakSeconds,
                remainingSeconds = breakSeconds,
                label = "Break"
            )
        } else {
            val workSeconds = workMinutes * 60
            current.copy(
                phase = PomodoroPhase.WORK,
                totalSeconds = workSeconds,
                remainingSeconds = workSeconds,
                label = sessionLabel,
                cycleCount = current.cycleCount + 1
            )
        }
    }

    override fun onCleared() {
        tickJob?.cancel()
        super.onCleared()
    }
}