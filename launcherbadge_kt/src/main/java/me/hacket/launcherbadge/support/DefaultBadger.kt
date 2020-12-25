package me.hacket.launcherbadge.support

import android.app.Notification
import android.content.Context
import android.content.Intent
import me.hacket.launcherbadge.Badger
import me.hacket.launcherbadge.LauncherBadgeUtils

class DefaultBadger : Badger {

    companion object {
        private const val INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE"
        private const val INTENT_EXTRA_BADGE_COUNT = "badge_count"
        private const val INTENT_EXTRA_PACKAGENAME = "badge_count_package_name"
        private const val INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name"
    }

    override fun setBadgeCount(context: Context, badgeCount: Int, notification: Notification?) {
        val launchClassName = LauncherBadgeUtils.getLaunchClassName(context)
        val intent = Intent(INTENT_ACTION)
        intent.putExtra(INTENT_EXTRA_BADGE_COUNT, badgeCount)
        intent.putExtra(INTENT_EXTRA_PACKAGENAME, context.packageName)
        intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, launchClassName)
        context.sendBroadcast(intent)
    }

    override fun supportLauncherPackageNames(): List<String> {
        return emptyList()
    }
}
