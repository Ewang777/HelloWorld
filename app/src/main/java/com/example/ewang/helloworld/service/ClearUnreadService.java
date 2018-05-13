package com.example.ewang.helloworld.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.example.ewang.helloworld.helper.ResponseWrapper;
import com.example.ewang.helloworld.service.task.RequestTask;
import com.example.ewang.helloworld.service.task.ResponseListener;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ClearUnreadService extends Service {

    ResponseListener responseListener = new ResponseListener() {
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", String.valueOf(intent.getLongExtra("userId", 0)))
                .add("toUserId", String.valueOf(intent.getLongExtra("toUserId", 0)))
                .build();
        new RequestTask(responseListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, requestBody);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
