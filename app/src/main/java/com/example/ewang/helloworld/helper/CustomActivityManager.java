package com.example.ewang.helloworld.helper;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by ewang on 2018/5/14.
 * 饿汉式单例模式创建ActivityManager
 */

public class CustomActivityManager {

    private static CustomActivityManager customActivityManage = new CustomActivityManager();

    private WeakReference<Activity> currentActivity;

    private boolean isAppForeground;

    private CustomActivityManager() {
    }

    public static CustomActivityManager getInstance() {
        return customActivityManage;
    }

    public Activity getCurrentActivity() {
        return currentActivity.get();
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = new WeakReference<>(currentActivity);
    }

    public boolean isAppForeground() {
        return isAppForeground;
    }

    public void setAppForeground(boolean appForeground) {
        isAppForeground = appForeground;
    }
}
