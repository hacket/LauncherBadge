package me.hacket.launcherbadge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LauncherBadgeUtils {

    private static final String TAG = "launcher_badge";

    private static ComponentName mLaunchComponent = null;

    @NonNull
    public static String getLaunchClassName(@NonNull Context context) {
        if (mLaunchComponent != null) {
            return mLaunchComponent.getClassName();
        }
        ComponentName launchComponent = getLaunchComponentName(context);
        if (launchComponent == null) {
            return "";
        } else {
            return launchComponent.getClassName();
        }
    }

    @Nullable
    public static ComponentName getLaunchComponentName(@NonNull Context context) {
        if (mLaunchComponent != null) return mLaunchComponent;
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (launchIntent != null) {
            mLaunchComponent = launchIntent.getComponent();
        } else {
            return null;
        }
        return mLaunchComponent;
    }

    /**
     * get Launcher package name
     * <p>
     * 如：com.huawei.android.launcher
     *
     * @param context Context
     * @return may be null
     */
    @Nullable
    public static String getLauncherPackageName(@NonNull Context context) {
        // find the home launcher Package
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        String currentHomePackage = null;
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        // in case of duplicate apps (Xiaomi), calling resolveActivity from one will return null
        if (resolveInfo != null) {
            currentHomePackage = resolveInfo.activityInfo.packageName;
        }
        return currentHomePackage;
    }


    @Nullable
    public static String getLauncherPackageName2(@NonNull Context context) {
        List<ResolveInfo> launcherList = getLauncherList(context);
        if (launcherList.isEmpty()) {
            return null;
        }
        return launcherList.get(0).activityInfo.packageName;
    }

    private static List<ResolveInfo> getLauncherList(@NonNull Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // Turns out framework does not guarantee to put DEFAULT Activity on top of the list.
        ResolveInfo resolveInfoDefault = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        // in case of duplicate apps (Xiaomi), calling resolveActivity from one will return null
        if (resolveInfoDefault == null) {
            return resolveInfos;
        }
        validateInfoList(resolveInfoDefault, resolveInfos);
        return resolveInfos;
    }

    /**
     * Making sure the default Home activity is on top of the returned list
     *
     * @param defaultActivity default Home activity
     * @param resolveInfos    list of all Home activities in the system
     */
    private static void validateInfoList(ResolveInfo defaultActivity, List<ResolveInfo> resolveInfos) {
        int indexToSwapWith = 0;
        for (int i = 0, resolveInfosSize = resolveInfos.size(); i < resolveInfosSize; i++) {
            ResolveInfo resolveInfo = resolveInfos.get(i);
            String currentActivityName = resolveInfo.activityInfo.packageName;
            if (currentActivityName.equals(defaultActivity.activityInfo.packageName)) {
                indexToSwapWith = i;
            }
        }
        Collections.swap(resolveInfos, 0, indexToSwapWith);
    }



    public static void close(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }


    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            LauncherBadgeUtils.printStackTrace(e);
        }
    }


    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void w(String message) {
        Log.w(TAG, message);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void printStackTrace(Exception e) {
        e.printStackTrace();
    }
}
