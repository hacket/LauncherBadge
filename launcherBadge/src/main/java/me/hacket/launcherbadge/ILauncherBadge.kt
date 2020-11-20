package me.hacket.launcherbadge

import android.content.Context

interface ILauncherBadge {

    fun setBadgeCount(context: Context, count: Int): Boolean

    fun removeBadge(context: Context): Boolean
}