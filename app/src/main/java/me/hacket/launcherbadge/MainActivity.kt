package me.hacket.launcherbadge

import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val deviceName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getString(contentResolver, Settings.Global.DEVICE_NAME)
        } else {
            TODO("VERSION.SDK_INT < JELLY_BEAN_MR1")
        }

        tv_info.text = """
            launcher:${Utils.getLauncherPackageName2(this)}
            deviceName:$deviceName
            brand:${Build.BRAND}(${Build.MODEL})
            version:${Build.VERSION.SDK_INT}(${Build.VERSION.RELEASE})
        """.trimIndent()

        et_count.setSelection(et_count.text.length)

        btn_set_badge_count.setOnClickListener {
            val count = et_count.text.toString().toIntOrNull() ?: 1
            if (LauncherBadge.setBadgeCount(applicationContext, count)) {
                Toast.makeText(this, "设置角标=${count}成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "设置角标=${count}失败！！！", Toast.LENGTH_SHORT).show()
            }
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