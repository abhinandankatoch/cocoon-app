package com.abhinandan.cocoon

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.abhinandan.cocoon.notifications.NotificationAccess
import com.abhinandan.cocoon.timer.TimerScreen

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
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TimerScreen()
                }
            }
        }
    }
}