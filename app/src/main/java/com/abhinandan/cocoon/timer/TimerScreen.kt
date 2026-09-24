package com.abhinandan.cocoon.timer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TimerScreen(viewModel: TimerViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (state.status == TimerStatus.IDLE) {
            viewModel.start(label = "Physics", minutes = 45)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = state.label, style = MaterialTheme.typography.bodyMedium)

        Text(
            text = formatTime(state.remainingSeconds),
            fontSize = 56.sp,
            fontWeight = FontWeight.Medium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            FilledIconButton(onClick = {
                if (state.status == TimerStatus.RUNNING) viewModel.pause() else viewModel.resume()
            }) {
                Icon(
                    imageVector = if (state.status == TimerStatus.RUNNING) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (state.status == TimerStatus.RUNNING) "Pause" else "Resume"
                )
            }
            FilledIconButton(onClick = { viewModel.stop() }) {
                Icon(imageVector = Icons.Filled.Stop, contentDescription = "Stop")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}