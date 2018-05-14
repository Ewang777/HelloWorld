package com.example.ewang.helloworld.service;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ewang.helloworld.helper.CustomActivityManager;

/**
 * Created by ewang on 2018/5/14.
 */

public class BaseActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.i(this.getLocalClassName(), "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(this.getLocalClassName(), "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(this.getLocalClassName(), "onResume");
        CustomActivityManager.getInstance().setCurrentActivity(this);
        CustomActivityManager.getInstance().setAppForeground(true);
        //移除所有通知
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(this.getLocalClassName(), "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomActivityManager.getInstance().setAppForeground(false);
        Log.i(this.getLocalClassName(), "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(this.getLocalClassName(), "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(this.getLocalClassName(), "onDestroy");
    }
}
