package me.hacket.launcherbadge

import android.app.Notification
import android.content.Context

interface ILauncherBadge {

    fun setBadgeCount(context: Context, count: Int, notification: Notification? = null): Boolean

    fun removeBadge(context: Context): Boolean
}