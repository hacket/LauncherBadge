package me.hacket.launcherbadge

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.util.Log
import java.io.Closeable
import java.io.IOException
import java.lang.Exception
import java.util.*

/**
 * @author hacket
 */
object LauncherBadgeUtils {
    const val TAG = "launcher_badge"
    private var mLaunchComponent: ComponentName? = null
    fun getLaunchClassName(context: Context): String {
        if (mLaunchComponent != null) {
            return mLaunchComponent!!.className
        }
        val launchComponent = getLaunchComponentName(context)
        return launchComponent?.className ?: ""
    }

    fun getLaunchComponentName(context: Context): ComponentName? {
        if (mLaunchComponent != null) {
            return mLaunchComponent
        }
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        if (launchIntent != null) {
            mLaunchComponent = launchIntent.component
        } else {
            return null
        }
        return mLaunchComponent
    }

    /**
     * get Launcher package name
     *
     *
     * 如：com.huawei.android.launcher
     *
     * @param context Context
     * @return may be null
     */
    fun getLauncherPackageName(context: Context): String? {
        // find the home launcher Package
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        var currentHomePackage: String? = null
        val resolveInfo =
            context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        // in case of duplicate apps (Xiaomi), calling resolveActivity from one will return null
        if (resolveInfo != null) {
            currentHomePackage = resolveInfo.activityInfo.packageName
        }
        return currentHomePackage
    }

    fun getLauncherPackageName2(context: Context): String? {
        val launcherList = getLauncherList(context)
        return if (launcherList.isEmpty()) {
            null
        } else launcherList[0]!!.activityInfo.packageName
    }

    private fun getLauncherList(context: Context): List<ResolveInfo?> {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val resolveInfos =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

        // Turns out framework does not guarantee to put DEFAULT Activity on top of the list.
        val resolveInfoDefault =
            context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
                ?: return resolveInfos
        // in case of duplicate apps (Xiaomi), calling resolveActivity from one will return null
        validateInfoList(resolveInfoDefault, resolveInfos)
        return resolveInfos
    }

    /**
     * Making sure the default Home activity is on top of the returned list
     *
     * @param defaultActivity default Home activity
     * @param resolveInfos    list of all Home activities in the system
     */
    private fun validateInfoList(defaultActivity: ResolveInfo, resolveInfos: List<ResolveInfo?>) {
        var indexToSwapWith = 0
        var i = 0
        val resolveInfosSize = resolveInfos.size
        while (i < resolveInfosSize) {
            val resolveInfo = resolveInfos[i]
            val currentActivityName = resolveInfo!!.activityInfo.packageName
            if (currentActivityName == defaultActivity.activityInfo.packageName) {
                indexToSwapWith = i
            }
            i++
        }
        Collections.swap(resolveInfos, 0, indexToSwapWith)
    }

    fun close(cursor: Cursor?) {
        if (cursor != null && !cursor.isClosed) {
            cursor.close()
        }
    }

    fun closeQuietly(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (e: IOException) {
            printStackTrace(e)
        }
    }

    fun d(message: String?) {
        Log.d(TAG, message)
    }

    fun i(message: String?) {
        Log.i(TAG, message)
    }

    fun w(message: String?) {
        Log.w(TAG, message)
    }

    fun e(message: String?) {
        Log.e(TAG, message)
    }

    fun printStackTrace(e: Exception) {
        e.printStackTrace()
    }
}