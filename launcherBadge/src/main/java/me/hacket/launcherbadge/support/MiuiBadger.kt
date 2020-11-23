package me.hacket.launcherbadge.support

import android.app.Notification
import android.content.Context
import me.hacket.launcherbadge.Badger
import me.hacket.launcherbadge.LauncherBadgeUtils

class MiuiBadger : Badger {
    override fun setBadgeCount(context: Context, badgeCount: Int, notification: Notification?) {
        applyNotification(context, notification, badgeCount)
    }

    // Miui Launcher用
    private fun applyNotification(
        context: Context,
        notification: Notification?,
        badgeCount: Int
    ) {
        if (notification == null) return
        try {
            val field =
                notification.javaClass.getDeclaredField("extraNotification")
            val extraNotification = field[notification]
            val method = extraNotification.javaClass.getDeclaredMethod(
                "setMessageCount",
                Int::class.javaPrimitiveType
            )
            method.invoke(extraNotification, badgeCount)
        } catch (e: Exception) {
            LauncherBadgeUtils.printStackTrace(e)
        }
    }

    override fun supportLauncherPackageNames(): List<String> {
        return listOf(
            "com.miui.miuilite",
            "com.miui.home",
            "com.miui.miuihome",
            "com.miui.miuihome2",
            "com.miui.mihome",
            "com.miui.mihome2",
            "com.i.miui.launcher"
        )
    }
}