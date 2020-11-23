package me.hacket.launcherbadge.support

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import me.hacket.launcherbadge.Badger
import me.hacket.launcherbadge.LauncherBadgeUtils
import me.hacket.launcherbadge.utils.BroadcastHelper

class OPPOBadger : Badger {

    companion object {
        private const val PROVIDER_CONTENT_URI = "content://com.android.badge/badge"
        private const val INTENT_ACTION = "com.oppo.unsettledevent"
        private const val INTENT_EXTRA_PACKAGENAME = "pakeageName"
        private const val INTENT_EXTRA_BADGE_COUNT = "number"
        private const val INTENT_EXTRA_BADGE_UPGRADENUMBER = "upgradeNumber"
        private const val INTENT_EXTRA_BADGEUPGRADE_COUNT = "app_badge_count"
    }

    private var mCurrentTotalCount = -1

    @SuppressLint("ObsoleteSdkInt")
    override fun setBadgeCount(context: Context, badgeCount: Int, notification: Notification?) {
        if (mCurrentTotalCount == badgeCount) {
            return
        }
        mCurrentTotalCount = badgeCount
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            executeBadgeByContentProvider(context, badgeCount)
        } else {
            val componentName = LauncherBadgeUtils.getLaunchComponentName(context)
            executeBadgeByBroadcast(context, componentName, badgeCount)
        }
    }

    override fun supportLauncherPackageNames(): List<String> {
        return listOf("com.oppo.launcher")
    }


    @Throws(Exception::class)
    private fun executeBadgeByBroadcast(
        context: Context, componentName: ComponentName?,
        badgeCount: Int
    ) {
        if (componentName == null) return
        var badgeCount = badgeCount
        if (badgeCount == 0) {
            badgeCount = -1
        }
        val intent = Intent(INTENT_ACTION)
        intent.putExtra(
            INTENT_EXTRA_PACKAGENAME,
            componentName.packageName
        )
        intent.putExtra(
            INTENT_EXTRA_BADGE_COUNT,
            badgeCount
        )
        intent.putExtra(
            INTENT_EXTRA_BADGE_UPGRADENUMBER,
            badgeCount
        )
        BroadcastHelper.sendIntentExplicitly(context, intent)
    }

    /**
     * Send request to OPPO badge content provider to set badge in OPPO home launcher.
     *
     * @param context       the context to use
     * @param badgeCount    the badge count
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Throws(Exception::class)
    private fun executeBadgeByContentProvider(
        context: Context,
        badgeCount: Int
    ) {
        try {
            val extras = Bundle()
            extras.putInt(
                INTENT_EXTRA_BADGEUPGRADE_COUNT,
                badgeCount
            )
            context.contentResolver.call(
                Uri.parse(PROVIDER_CONTENT_URI),
                "setAppBadgeCount",
                null,
                extras
            )
        } catch (ignored: Throwable) {
            throw Exception("Unable to execute Badge By Content Provider")
        }
    }


}