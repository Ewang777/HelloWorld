package com.example.ewang.helloworld.helper;

import android.app.Activity;
import android.app.AlertDialog;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ewang on 2018/4/21.
 */

public class HttpUtil {

    public static ResponseWrapper sendRequest(String url, RequestBody requestBody, Activity activity, AlertDialog dialogNeedsToDismiss) {
        String data = "{}";
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            data = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialogNeedsToDismiss != null) {
                        dialogNeedsToDismiss.dismiss();
                    }
                    DialogHelper.showAlertDialog(activity, "Warning", "连接服务器异常", null, null);
                }
            });
            return null;
        }

        return JsonHelper.decode(data, ResponseWrapper.class);
    }

}
