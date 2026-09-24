package com.abhinandan.cocoon.progress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.abhinandan.cocoon.data.CocoonDatabase
import com.abhinandan.cocoon.data.SessionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class DayTotal(val label: String, val minutes: Int)

data class ProgressUiState(
    val streakDays: Int = 0,
    val weekTotals: List<DayTotal> = emptyList(),
    val monthMinutes: Int = 0,
    val monthSessionCount: Int = 0,
    val recentSessions: List<SessionEntity> = emptyList(),
    val loading: Boolean = true
)

class ProgressViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionDao = CocoonDatabase.getInstance(application).sessionDao()

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState

    init {
        viewModelScope.launch {
            sessionDao.getAllSessions().collect { sessions ->
                _uiState.value = buildState(sessions)
            }
        }
    }

    private fun buildState(sessions: List<SessionEntity>): ProgressUiState {
        val dayLabelFormat = SimpleDateFormat("EEE", Locale.getDefault())

        val weekTotals = (6 downTo 0).map { offset ->
            val dayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -offset) }
            val dayStart = startOfDay(dayCal)
            val dayEnd = dayStart + 24 * 60 * 60 * 1000L
            val minutesThatDay = sessions
                .filter { it.timestampMillis in dayStart until dayEnd }
                .sumOf { it.durationMinutes }
            DayTotal(label = dayLabelFormat.format(dayCal.time).take(1), minutes = minutesThatDay)
        }

        var streak = 0
        var cursor = Calendar.getInstance()
        while (true) {
            val dayStart = startOfDay(cursor)
            val dayEnd = dayStart + 24 * 60 * 60 * 1000L
            val hasSession = sessions.any { it.timestampMillis in dayStart until dayEnd }
            if (!hasSession) break
            streak++
            cursor = (cursor.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }
        }

        val monthStartCal = Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 1) }
        val monthStart = startOfDay(monthStartCal)
        val sessionsThisMonth = sessions.filter { it.timestampMillis >= monthStart }

        return ProgressUiState(
            streakDays = streak,
            weekTotals = weekTotals,
            monthMinutes = sessionsThisMonth.sumOf { it.durationMinutes },
            monthSessionCount = sessionsThisMonth.size,
            recentSessions = sessions.take(5),
            loading = false
        )
    }

    private fun startOfDay(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}