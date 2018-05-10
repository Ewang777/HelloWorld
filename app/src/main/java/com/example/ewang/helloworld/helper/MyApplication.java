package com.example.ewang.helloworld.helper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.ewang.helloworld.model.User;

import java.lang.ref.WeakReference;

/**
 * Created by ewang on 2018/4/21.
 */

public class MyApplication extends Application {

    private static Context context;

    private static WeakReference<Activity> currentActivity;

    private static User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        currentUser = null;
        currentActivity = null;
    }

    public static void setCurrentUser(User currentUser) {
        MyApplication.currentUser = currentUser;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static Context getContext() {
        return context;
    }

    public static Activity getCurrentActivity() {
        return currentActivity.get();
    }

    public static void setCurrentActivity(Activity currentActivity) {
        MyApplication.currentActivity = new WeakReference(currentActivity);
    }
}
