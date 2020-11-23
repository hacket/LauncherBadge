package me.hacket.launcherbadge.support

import android.app.Notification
import android.content.Context
import android.content.Intent
import me.hacket.launcherbadge.Badger
import me.hacket.launcherbadge.LauncherBadgeUtils

class VivoBadger : Badger {
    override fun setBadgeCount(context: Context, badgeCount: Int, notification: Notification?) {
        val intent = Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM")
        intent.putExtra("packageName", context.packageName)
        intent.putExtra("className", LauncherBadgeUtils.getLaunchComponentName(context)?.className)
        intent.putExtra("notificationNum", badgeCount)
        context.sendBroadcast(intent)
    }

    override fun supportLauncherPackageNames(): List<String> {
        return listOf("com.vivo.launcher")
    }
}