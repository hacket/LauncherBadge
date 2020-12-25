package me.hacket.launcherbadge.support

import android.app.Notification
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import me.hacket.launcherbadge.Badger
import me.hacket.launcherbadge.LauncherBadgeUtils

/**
 * Nowa Launcher
 */
class NowaBadger : Badger {

    companion object {
        private const val CONTENT_URI = "content://com.teslacoilsw.notifier/unread_count"
        private const val COUNT = "count"
        private const val TAG = "tag"
    }

    override fun setBadgeCount(context: Context, badgeCount: Int, notification: Notification?) {

        val componentName = LauncherBadgeUtils.getLaunchComponentName(context)
        val contentValues = ContentValues()
        contentValues.put(
            TAG,
            componentName?.packageName + "/" + componentName?.className
        )
        contentValues.put(COUNT, badgeCount)
        context.contentResolver.insert(
            Uri.parse(CONTENT_URI),
            contentValues
        )
    }

    override fun supportLauncherPackageNames(): List<String> {
        return listOf("com.teslacoilsw.launcher")
    }
}
