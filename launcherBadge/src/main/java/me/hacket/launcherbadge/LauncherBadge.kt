package me.hacket.launcherbadge

import android.content.Context
import android.os.Build
import me.hacket.launcherbadge.support.*

object LauncherBadge : ILauncherBadge {

    private val supportBadgers: List<Badger> by lazy {
        listOf(
            SamsungBadger(),
            HuaweiBadger(),
            OPPOBadger(),
            VivoBadger(),
            ZTEBadger(),
            ZukBadger(),
            DefaultBadger()
        )
    }

    private var mSupportBadger: Badger? = null

    // Initialize Badger if a launcher is available (eg. set as default on the device)
    // Returns true if a launcher is available, in this case, the Badger will be set and sShortcutBadger will be non null.
    private fun initBadger(context: Context): Boolean {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName);
        if (launchIntent == null) {
            Utils.e("Unable to find launch intent for package " + context.packageName)
            return false
        }

        mSupportBadger = supportBadgers.find { badger ->
            badger.supportLauncherPackageNames()
                .find { pkn -> pkn == Utils.getLauncherPackageName(context) } != null
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


    override fun setBadgeCount(context: Context, badgeCount: Int): Boolean {
        if (mSupportBadger == null) {
            if (!initBadger(context)) {
                Utils.e("No default launcher available")
            }
        }
        if (mSupportBadger == null) {
            return false
        }

        try {
            mSupportBadger?.setBadgeCount(context, badgeCount)
            return true
        } catch (e: Exception) {
            Utils.printStackTrace(e)
            Utils.e("setBadgeCount error ${e.message}\t\t$mSupportBadger")
        }
        return false
    }

    override fun removeBadge(context: Context): Boolean {
        if (mSupportBadger == null) {
            if (!initBadger(context)) {
                Utils.e("No default launcher available")
            }
        }
        if (mSupportBadger == null) {
            return false
        }
        try {
            mSupportBadger?.setBadgeCount(context, 0)
            return true
        } catch (e: Exception) {
            Utils.printStackTrace(e)
            Utils.e("setBadgeCount error ${e.message}\t\t$mSupportBadger")
        }
        return false
    }

}