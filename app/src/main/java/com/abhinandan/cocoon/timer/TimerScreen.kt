package com.abhinandan.cocoon.timer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TimerScreen(
    label: String,
    minutes: Int,
    isPomodoro: Boolean,
    onSessionEnd: () -> Unit,
    viewModel: TimerViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(label, minutes, isPomodoro) {
        viewModel.start(label = label, minutes = minutes, isPomodoro = isPomodoro)
    }

    LaunchedEffect(state.status) {
        if (state.status == TimerStatus.FINISHED) onSessionEnd()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "breathe")
    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breatheScale"
    )

    val circleColor = if (state.phase == PomodoroPhase.BREAK) {
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.30f)
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(state.label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (state.isPomodoro) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Round ${state.cycleCount + 1}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier.size(220.dp).scale(breatheScale)
                    .background(circleColor, CircleShape)
            )
            Text(
                text = formatTime(state.remainingSeconds),
                fontSize = 52.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            FilledTonalIconButton(
                onClick = { if (state.status == TimerStatus.RUNNING) viewModel.pause() else viewModel.resume() },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = if (state.status == TimerStatus.RUNNING) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (state.status == TimerStatus.RUNNING) "Pause" else "Resume"
                )
            }
            FilledTonalIconButton(
                onClick = { viewModel.stop(); onSessionEnd() },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Filled.Stop, contentDescription = "Stop")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun formatTime(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "%02d:%02d".format(m, s)
}