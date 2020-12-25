package me.hacket.launcherbadge.support

import android.app.Notification
import android.content.Context
import android.net.Uri
import android.os.Bundle
import me.hacket.launcherbadge.Badger

class ZukBadger : Badger {

    companion object {
        private val CONTENT_URI = Uri.parse("content://com.android.badge/badge")
    }

    override fun setBadgeCount(context: Context, badgeCount: Int, notification: Notification?) {
        val extra = Bundle()
        extra.putInt("app_badge_count", badgeCount)
        context.contentResolver.call(CONTENT_URI, "setAppBadgeCount", null, extra)
    }

    override fun supportLauncherPackageNames(): List<String> {
        return listOf("com.zui.launcher")
    }
}
