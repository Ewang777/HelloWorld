package com.example.ewang.helloworld.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.ewang.helloworld.helper.DialogHelper;
import com.example.ewang.helloworld.helper.MyApplication;
import com.example.ewang.helloworld.helper.ResponseWrapper;
import com.example.ewang.helloworld.service.task.RequestTask;
import com.example.ewang.helloworld.service.task.ResponseListener;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class SendMessageService extends Service {

    private int failTime = 0;

    private String content;

    private long userId;

    private long toUserId;

    private String url;

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(ResponseWrapper responseWrapper) {
        }

        @Override
        public void onFail(String errMessage) {
            if (failTime > 10) {
                DialogHelper.showAlertDialog(MyApplication.getCurrentActivity(), "Warnning", "网络异常，消息未发出", null, null);
            } else {
                doTask();
                failTime++;
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getStringExtra("url");
        content = intent.getStringExtra("content");
        userId = intent.getLongExtra("userId", 0);
        toUserId = intent.getLongExtra("toUserId", 0);
        doTask();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void doTask() {
        RequestBody requestBody = new FormBody.Builder()
                .add("content", content)
                .add("userId", String.valueOf(userId))
                .add("toUserId", String.valueOf(toUserId))
                .build();
        new RequestTask(responseListener).execute(url, requestBody);
    }
}
