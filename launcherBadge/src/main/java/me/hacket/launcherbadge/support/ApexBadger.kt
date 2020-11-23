package me.hacket.launcherbadge.support

import android.app.Notification
import android.content.Context
import android.content.Intent
import me.hacket.launcherbadge.Badger
import me.hacket.launcherbadge.LauncherBadgeUtils
import me.hacket.launcherbadge.utils.BroadcastHelper

/**
 * Apex Launcher
 */
class ApexBadger : Badger {

    companion object {
        private const val INTENT_UPDATE_COUNTER = "com.anddoes.launcher.COUNTER_CHANGED"
        private const val PACKAGENAME = "package"
        private const val COUNT = "count"
        private const val CLASS = "class"
    }

    override fun setBadgeCount(context: Context, badgeCount: Int, notification: Notification?) {
        val componentName = LauncherBadgeUtils.getLaunchComponentName(context)
        val intent = Intent(INTENT_UPDATE_COUNTER)
        intent.putExtra(PACKAGENAME, componentName?.packageName)
        intent.putExtra(COUNT, badgeCount)
        intent.putExtra(CLASS, componentName?.className)

        BroadcastHelper.sendIntentExplicitly(context, intent)
    }

    override fun supportLauncherPackageNames(): List<String> {
        return listOf("com.anddoes.launcher")
    }
}