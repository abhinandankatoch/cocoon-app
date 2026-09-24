package com.abhinandan.cocoon

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.abhinandan.cocoon.home.HomeScreen
import com.abhinandan.cocoon.notifications.NotificationAccess
import com.abhinandan.cocoon.settings.MuteListScreen
import com.abhinandan.cocoon.timer.TimerScreen
import com.abhinandan.cocoon.ui.theme.CocoonTheme

sealed class AppScreen {
    data object Home : AppScreen()
    data class Timer(val label: String, val minutes: Int, val isPomodoro: Boolean) : AppScreen()
    data object MuteSettings : AppScreen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!NotificationAccess.isGranted(this)) {
            Toast.makeText(
                this,
                "Cocoon needs notification access to mute apps during a session",
                Toast.LENGTH_LONG
            ).show()
            NotificationAccess.requestAccess(this)
        }

        setContent {
            CocoonTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    var screen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }
                    when (val current = screen) {
                        is AppScreen.Home -> HomeScreen(
                            onStart = { preset, isPomodoro ->
                                screen = AppScreen.Timer(preset.label, preset.minutes, isPomodoro)
                            },
                            onOpenSettings = { screen = AppScreen.MuteSettings }
                        )
                        is AppScreen.Timer -> TimerScreen(
                            label = current.label,
                            minutes = current.minutes,
                            isPomodoro = current.isPomodoro,
                            onSessionEnd = { screen = AppScreen.Home }
                        )
                        is AppScreen.MuteSettings -> MuteListScreen(
                            onBack = { screen = AppScreen.Home }
                        )
                    }
                }
            }
        }
    }
}