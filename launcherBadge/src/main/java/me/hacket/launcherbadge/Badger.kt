package me.hacket.launcherbadge

import android.content.Context
import java.lang.Exception

interface Badger {

    /**
     * Called when user attempts to update notification count
     * @param context Caller context
     * @param badgeCount Desired notification count
     */
    @Throws(Exception::class)
    fun setBadgeCount(context: Context, badgeCount: Int)

    /**
     * Called to let [LauncherBadge] knows which launchers are supported by this badger. It should return a
     * @return List containing supported launchers package names
     */
    fun supportLauncherPackageNames(): List<String>
}