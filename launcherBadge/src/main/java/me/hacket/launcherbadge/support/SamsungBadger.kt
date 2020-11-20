package me.hacket.launcherbadge.support

import android.content.Context
import android.content.Intent
import me.hacket.launcherbadge.Badger
import me.hacket.launcherbadge.Utils

/**
 * 三星
 *
 * 测试通过机型：
 * 1. Galaxy S6 edge(SM-G9250)  超过99，不显示99+
 */
class SamsungBadger : Badger {

    override fun setBadgeCount(context: Context, badgeCount: Int) {
        val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
        intent.putExtra("badge_count", badgeCount)
        intent.putExtra("badge_count_package_name", context.packageName)
        intent.putExtra("badge_count_class_name", Utils.getLaunchClassName(context))
        context.sendBroadcast(intent)
    }

    override fun supportLauncherPackageNames(): List<String> {
        return listOf(
            "com.sec.android.app.launcher",
            "com.sec.android.app.twlauncher"
        )
    }
}