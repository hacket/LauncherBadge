package me.hacket.launcherbadge.support

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import me.hacket.launcherbadge.Badger
import me.hacket.launcherbadge.LauncherBadgeUtils

class ZTEBadger : Badger {
    @SuppressLint("ObsoleteSdkInt")
    override fun setBadgeCount(context: Context, badgeCount: Int, notification: Notification?) {
        val extra = Bundle()
        extra.putInt("app_badge_count", badgeCount)
        extra.putString(
            "app_badge_component_name",
            LauncherBadgeUtils.getLaunchComponentName(context)?.flattenToString()
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            context.contentResolver.call(
                Uri.parse("content://com.android.launcher3.cornermark.unreadbadge"),
                "setAppUnreadCount", null, extra
            )
        }
    }

    override fun supportLauncherPackageNames(): List<String> {
        return emptyList()
    }
}
