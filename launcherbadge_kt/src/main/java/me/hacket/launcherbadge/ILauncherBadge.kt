package me.hacket.launcherbadge

import android.app.Notification
import android.content.Context

interface ILauncherBadge {

    /**
     * set the launcher badge
     *
     * @param context Context the context
     * @param badgeCount Int Number of badge
     * @param notification Notification just miui launcher support
     */
    fun setBadgeCount(
        context: Context,
        badgeCount: Int,
        notification: Notification? = null
    ): Boolean

    /**
     * remove badge from launcher
     */
    fun removeBadge(context: Context): Boolean
}
