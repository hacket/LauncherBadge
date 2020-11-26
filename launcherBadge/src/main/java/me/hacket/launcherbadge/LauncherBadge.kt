package me.hacket.launcherbadge

import android.app.Notification
import android.content.Context
import android.os.Build
import me.hacket.launcherbadge.support.*

object LauncherBadge : ILauncherBadge {

    private val supportBadgers: List<Badger> by lazy {
        listOf(
            SamsungBadger(),
            HuaweiBadger(),
            MiuiBadger(),
            NowaBadger(),
            ApexBadger(),
            OPPOBadger(),
            VivoBadger(),
            ZTEBadger(),
            ZukBadger(),
            HtcBadger(),
            SonyBadger(),
            LGBadger(),
            DefaultBadger()
        )
    }

    private var mSupportBadger: Badger? = null

    // Initialize Badger if a launcher is available (eg. set as default on the device)
    // Returns true if a launcher is available, in this case, the Badger will be set and sShortcutBadger will be non null.
    private fun initBadger(context: Context): Boolean {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        if (launchIntent == null) {
            LauncherBadgeUtils.e("Unable to find launch intent for package " + context.packageName)
            return false
        }

        mSupportBadger = supportBadgers.find { badger ->
            badger.supportLauncherPackageNames()
                .find { pkn -> pkn == LauncherBadgeUtils.getLauncherPackageName2(context) } != null
        }

        if (mSupportBadger == null) {
            mSupportBadger = when {
                Build.MANUFACTURER.equals("ZUK", ignoreCase = true) -> ZukBadger()
                Build.MANUFACTURER.equals("OPPO", ignoreCase = true) -> OPPOBadger()
                Build.MANUFACTURER.equals("VIVO", ignoreCase = true) -> VivoBadger()
                Build.MANUFACTURER.equals("ZTE", ignoreCase = true) -> ZTEBadger()
                else -> DefaultBadger()
            }
        }
        return true
    }


    override fun setBadgeCount(
        context: Context,
        badgeCount: Int,
        notification: Notification?
    ): Boolean {
        if (mSupportBadger == null) {
            if (!initBadger(context)) {
                LauncherBadgeUtils.e("No default launcher available")
            }
        }
        if (mSupportBadger == null) {
            return false
        }

        try {
            if (notification == null) {
                mSupportBadger?.setBadgeCount(context, badgeCount, null)
            } else {
                if (mSupportBadger is MiuiBadger) { // 目前只支持Miui Launcher绑定到Notification
                    mSupportBadger?.setBadgeCount(context, badgeCount, notification)
                }
            }
            return true
        } catch (e: Exception) {
            LauncherBadgeUtils.printStackTrace(e)
            LauncherBadgeUtils.e("setBadgeCount error ${e.message}\t\t$mSupportBadger")
        }
        return false
    }

    override fun removeBadge(context: Context): Boolean {
        if (mSupportBadger == null) {
            if (!initBadger(context)) {
                LauncherBadgeUtils.e("No default launcher available")
            }
        }
        if (mSupportBadger == null) {
            return false
        }
        try {
            mSupportBadger?.setBadgeCount(context, 0)
            return true
        } catch (e: Exception) {
            LauncherBadgeUtils.printStackTrace(e)
            LauncherBadgeUtils.e("setBadgeCount error ${e.message}\t\t$mSupportBadger")
        }
        return false
    }

}