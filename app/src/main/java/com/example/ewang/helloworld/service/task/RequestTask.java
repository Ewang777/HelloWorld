package com.example.ewang.helloworld.service.task;

import android.os.AsyncTask;

import com.example.ewang.helloworld.helper.DialogHelper;
import com.example.ewang.helloworld.helper.JsonHelper;
import com.example.ewang.helloworld.helper.MyApplication;
import com.example.ewang.helloworld.helper.ResponseWrapper;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ewang on 2018/5/7.
 */

public class RequestTask extends AsyncTask<Object, Void, ResponseWrapper> {

    ResponseListener responseListener;

    public RequestTask(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    @Override
    protected ResponseWrapper doInBackground(Object... objects) {
        String url = (String) objects[0];
        RequestBody requestBody = (RequestBody) objects[1];
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String data = response.body().string();
            return JsonHelper.decode(data, ResponseWrapper.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(ResponseWrapper responseWrapper) {
        super.onPostExecute(responseWrapper);
        if (responseWrapper == null) {
            DialogHelper.showAlertDialog(MyApplication.getCurrentActivity(), "Warning", "连接服务器异常", null, null);
            return;
        }
        if (responseWrapper.isSuccess()) {
            responseListener.onSuccess(responseWrapper);
        } else {
            responseListener.onFail(responseWrapper.getErrMessage());
        }
    }
}
