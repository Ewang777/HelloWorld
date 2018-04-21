package com.example.ewang.helloworld.helper;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ewang on 2018/4/21.
 */

public class HttpUtil {

    public static String sendRequest(String url, RequestBody requestBody) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

}
