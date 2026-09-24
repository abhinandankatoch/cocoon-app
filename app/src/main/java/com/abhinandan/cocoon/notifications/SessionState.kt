package com.abhinandan.cocoon.notifications

object SessionState {
    @Volatile
    var isActive: Boolean = false

    // Placeholder list until the real mute-list settings screen exists
    val mutedPackages: MutableSet<String> = mutableSetOf(
        "com.instagram.android",
        "com.whatsapp",
        "com.google.android.gm",
        "com.twitter.android",
        "com.Slack"
    )
}