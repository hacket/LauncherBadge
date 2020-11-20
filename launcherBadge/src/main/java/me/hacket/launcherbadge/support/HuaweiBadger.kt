package me.hacket.launcherbadge.support

import android.content.Context
import android.net.Uri
import android.os.Bundle
import me.hacket.launcherbadge.Badger
import me.hacket.launcherbadge.Utils

/**
 * 华为
 *
 * 测试通过机型：
 *  1.Honor8X(JSN-AL00a) 27(8.1.0) 超过99，显示99+
 */
class HuaweiBadger : Badger {

    override fun setBadgeCount(context: Context, badgeCount: Int) {
        val localBundle = Bundle()
        localBundle.putString("package", context.packageName)
        localBundle.putString("class", Utils.getLaunchClassName(context))
        localBundle.putInt("badgenumber", badgeCount)
        context.contentResolver.call(
            Uri.parse("content://com.huawei.android.launcher.settings/badge/"),
            "change_badge",
            null,
            localBundle
        )
    }

    override fun supportLauncherPackageNames(): List<String> {
        return listOf("com.huawei.android.launcher")
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}[${supportLauncherPackageNames()}]"
    }
}