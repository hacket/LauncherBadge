package me.hacket.launcherbadge.support

import android.app.Notification
import android.content.Context
import android.content.Intent
import me.hacket.launcherbadge.Badger
import me.hacket.launcherbadge.LauncherBadgeUtils
import me.hacket.launcherbadge.utils.BroadcastHelper

class HtcBadger : Badger {

    companion object {
        private const val INTENT_UPDATE_SHORTCUT = "com.htc.launcher.action.UPDATE_SHORTCUT"
        private const val INTENT_SET_NOTIFICATION = "com.htc.launcher.action.SET_NOTIFICATION"
        private const val PACKAGENAME = "packagename"
        private const val COUNT = "count"
        private const val EXTRA_COMPONENT = "com.htc.launcher.extra.COMPONENT"
        private const val EXTRA_COUNT = "com.htc.launcher.extra.COUNT"
    }

    override fun setBadgeCount(context: Context, badgeCount: Int, notification: Notification?) {
        val componentName = LauncherBadgeUtils.getLaunchComponentName(context)

        val intent1 = Intent(INTENT_SET_NOTIFICATION)
        intent1.putExtra(EXTRA_COMPONENT, componentName?.flattenToShortString())
        intent1.putExtra(EXTRA_COUNT, badgeCount)

        val intent = Intent(INTENT_UPDATE_SHORTCUT)
        intent.putExtra(PACKAGENAME, componentName?.packageName)
        intent.putExtra(COUNT, badgeCount)

        val intent1Success: Boolean = try {
            BroadcastHelper.sendIntentExplicitly(context, intent1)
            true
        } catch (e: Exception) {
            false
        }

        val intentSuccess: Boolean = try {
            BroadcastHelper.sendIntentExplicitly(context, intent)
            true
        } catch (e: Exception) {
            false
        }

        if (!intent1Success && !intentSuccess) {
            throw Exception("unable to resolve intent: $intent")
        }
    }

    override fun supportLauncherPackageNames(): List<String> {
        return listOf("com.htc.launcher")
    }
}
