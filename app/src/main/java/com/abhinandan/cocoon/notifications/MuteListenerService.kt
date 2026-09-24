package com.abhinandan.cocoon.notifications

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class MuteListenerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (SessionState.isActive && SessionState.mutedPackages.contains(sbn.packageName)) {
            cancelNotification(sbn.key)
        }
    }
}