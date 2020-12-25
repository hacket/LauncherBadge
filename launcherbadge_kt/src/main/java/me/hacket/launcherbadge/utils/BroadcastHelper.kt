package me.hacket.launcherbadge.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Build

object BroadcastHelper {
    fun resolveBroadcast(context: Context, intent: Intent?): List<ResolveInfo> {
        val packageManager = context.packageManager
        val receivers = packageManager.queryBroadcastReceivers(
            intent!!, 0
        )
        return receivers ?: emptyList()
    }

    @Throws(Exception::class)
    fun sendIntentExplicitly(context: Context, intent: Intent) {
        val resolveInfos = resolveBroadcast(context, intent)
        if (resolveInfos.size == 0) {
            throw IllegalAccessException("unable to resolve intent: $intent")
        }
        for (info in resolveInfos) {
            val actualIntent = Intent(intent)
            if (info != null) {
                actualIntent.setPackage(info.resolvePackageName)
                context.sendBroadcast(actualIntent)
            }
        }
    }

    @Throws(Exception::class)
    fun sendDefaultIntentExplicitly(context: Context, intent: Intent) {
        var oreoIntentSuccess = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val oreoIntent = Intent(intent)
            oreoIntent.action = IntentConstants.DEFAULT_OREO_INTENT_ACTION
            oreoIntentSuccess = try {
                sendIntentExplicitly(context, oreoIntent)
                true
            } catch (e: Exception) {
                false
            }
        }
        if (oreoIntentSuccess) {
            return
        }

        // try pre-Oreo default intent
        sendIntentExplicitly(context, intent)
    }
}