package me.hacket.launcherbadge;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.Random;

public class BadgeNotificationHelper {

    private static final String NOTIFICATION_CHANNEL = "me.hacket.launcher_badge";

    private int notificationId = 0;

    private NotificationManager mNotificationManager;

    public void notify(Context context, int badgeCount) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancel(notificationId);
        notificationId = new Random().nextInt(10000);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("测试miui桌面角标Title")
                .setContentText("测试miui桌面角标content")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannel();
            builder.setChannelId(NOTIFICATION_CHANNEL);
        }

        Notification notification = builder.build();

        LauncherBadge.INSTANCE.setBadgeCount(context, badgeCount, notification);

        mNotificationManager.notify(notificationId, notification);

        Log.i("hacket", "新的Notification：" + notificationId);


        //小米的需要发一个通知，进入应用后红点会自动消失，测试的时候进程只能在后台，否则没有效果
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//            String title = "这个是标题";
//            String desc = "这个是内容";
//            if (SystemUtil.hasO()){
//                String channelId = "default";
//                String channelName = "默认通知";
//                notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
//            }
//            Intent intent = new Intent();//可以用intent设置点击通知后的页面
//            Notification notification = new NotificationCompat.Builder(context, "default")
//                    .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))
//                    .setContentTitle(title)
//                    .setContentText(desc)
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.drawable.ic_launcher)
//                    .setAutoCancel(true)
//                    .build()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void setupNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, "ShortcutBadger Sample",
//                NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager.IMPORTANCE_HIGH);

        mNotificationManager.createNotificationChannel(channel);
    }
}
