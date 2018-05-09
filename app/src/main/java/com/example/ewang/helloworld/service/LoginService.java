package com.example.ewang.helloworld.service;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.example.ewang.helloworld.ShowFriendsActivity;
import com.example.ewang.helloworld.helper.DialogHelper;
import com.example.ewang.helloworld.helper.JsonHelper;
import com.example.ewang.helloworld.helper.MyApplication;
import com.example.ewang.helloworld.helper.ResponseWrapper;
import com.example.ewang.helloworld.model.User;
import com.example.ewang.helloworld.service.task.RequestTask;
import com.example.ewang.helloworld.service.task.ResponseListener;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class LoginService extends Service {

    ProgressDialog progressDialog;

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(ResponseWrapper responseWrapper) {
            Object userObject = responseWrapper.getData().get("user");
            User currentUser = JsonHelper.decode(JsonHelper.encode(userObject), User.class);
            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
            editor.putString("account", currentUser.getAccount());
            editor.putString("password", currentUser.getPassword());
            editor.apply();
            MyApplication.setCurrentUser(currentUser);

            progressDialog.dismiss();
            Intent intent = new Intent(MyApplication.getCurrentActivity(), ShowFriendsActivity.class);
            startActivity(intent);
        }

        @Override
        public void onFail(String errMessage) {
            progressDialog.dismiss();
            DialogHelper.showAlertDialog(MyApplication.getCurrentActivity(), "Warning", errMessage, null, null);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        progressDialog = DialogHelper.showProgressDialog(MyApplication.getCurrentActivity(), "请稍侯", "loading", null);
        String url = intent.getStringExtra("url");
        RequestBody requestBody = new FormBody.Builder()
                .add("account", intent.getStringExtra("account"))
                .add("password", intent.getStringExtra("password"))
                .build();
        new RequestTask(responseListener).execute(url, requestBody);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
