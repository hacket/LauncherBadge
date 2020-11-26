package me.hacket.launcherbadge

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val deviceName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getString(contentResolver, Settings.Global.DEVICE_NAME)
        } else {
            TODO("VERSION.SDK_INT < JELLY_BEAN_MR1")
        }
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)

        val listPackages = mutableListOf<String>()
        val resolveInfos: List<ResolveInfo> = packageManager
            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        resolveInfos.forEach {
            listPackages.add(it.activityInfo.packageName)
        }

        tv_info.text = """
            默认Launcher：${LauncherBadgeUtils.getLauncherPackageName(this)}
            所有安装Launcher(${listPackages.size})：${listPackages}
            deviceName：$deviceName
            brand：${Build.BRAND}(${Build.MODEL})
            version：${Build.VERSION.SDK_INT}(${Build.VERSION.RELEASE})
        """.trimIndent()

        et_count.setSelection(et_count.text.length)

        btn_set_badge_count.setOnClickListener {
            val count = et_count.text.toString().toIntOrNull() ?: 1
            val miuiLauncherList = listOf(
                "com.miui.miuilite",
                "com.miui.home",
                "com.miui.miuihome",
                "com.miui.miuihome2",
                "com.miui.mihome",
                "com.miui.mihome2",
                "com.i.miui.launcher"
            )
            if (miuiLauncherList.contains(LauncherBadgeUtils.getLauncherPackageName(this))) {
                BadgeNotificationHelper().notify(this, count)
                // finish() // 在小米设备上APP打开着的情况下，是不显示角标的，只有APP在后台才会显示角标
            } else {
                if (LauncherBadge.setBadgeCount(applicationContext, count, null)) {
                    Toast.makeText(this, "设置角标=${count}成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "设置角标=${count}失败！！！", Toast.LENGTH_SHORT).show()
                }
            }

            startActivity(intent)
        }
        btn_clear_badge_count.setOnClickListener {
            if (LauncherBadge.removeBadge(applicationContext)) {
                Toast.makeText(this, "清除角标成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "清除角标失败！！！", Toast.LENGTH_SHORT).show()
            }
        }
    }
}