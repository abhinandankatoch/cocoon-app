package com.abhinandan.cocoon.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Preset(val label: String, val minutes: Int, val icon: ImageVector)

private val defaultPresets = listOf(
    Preset("Physics", 45, Icons.Filled.Science),
    Preset("Coding", 60, Icons.Filled.Code),
    Preset("Reading", 30, Icons.AutoMirrored.Filled.MenuBook)
)

@Composable
fun HomeScreen(
    onStart: (Preset, Boolean) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenProgress: () -> Unit
) {
    var pomodoroEnabled by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val titleTransition = rememberInfiniteTransition(label = "titleBreathe")
            val titleScale by titleTransition.animateFloat(
                initialValue = 0.97f,
                targetValue = 1.03f,
                animationSpec = infiniteRepeatable(
                    animation = tween(5000, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "titleScale"
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Cocoon",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 0.5.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.scale(titleScale)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Settle in before you begin",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                defaultPresets.forEach { preset ->
                    PresetCard(preset = preset, onClick = { onStart(preset, pomodoroEnabled) })
                }
                TextButton(onClick = { /* add preset — coming soon */ }) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add preset")
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Pomodoro mode", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    Switch(checked = pomodoroEnabled, onCheckedChange = { pomodoroEnabled = it })
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        IconButton(
            onClick = onOpenProgress,
            modifier = Modifier.align(Alignment.TopStart).padding(top = 28.dp, start = 8.dp)
        ) {
            Icon(Icons.Filled.BarChart, contentDescription = "Progress", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        IconButton(
            onClick = onOpenSettings,
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 28.dp, end = 8.dp)
        ) {
            Icon(Icons.Filled.NotificationsOff, contentDescription = "Mute settings", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun PresetCard(preset: Preset, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = preset.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(preset.label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            }
            Text("${preset.minutes} min", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}