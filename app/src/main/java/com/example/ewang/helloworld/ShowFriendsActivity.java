package com.example.ewang.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.ewang.helloworld.adapter.FriendAdapter;
import com.example.ewang.helloworld.helper.DialogHelper;
import com.example.ewang.helloworld.helper.JsonHelper;
import com.example.ewang.helloworld.helper.MyApplication;
import com.example.ewang.helloworld.helper.ResponseWrapper;
import com.example.ewang.helloworld.model.Message;
import com.example.ewang.helloworld.model.User;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShowFriendsActivity extends AppCompatActivity {

    private FriendAdapter adapter;

    private RecyclerView friendRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friends);

        final User user = MyApplication.getCurrentUser();
        friendRecyclerView = findViewById(R.id.friend_recycler_view);

        TextView usernameTextView = findViewById(R.id.text_current_username);
        usernameTextView.setText(usernameTextView.getText() + user.getUsername());

        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowFriendsActivity.this);
        friendRecyclerView.setLayoutManager(layoutManager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("userId", String.valueOf(user.getId()))
                        .build();
                Request request = new Request.Builder()
                        .url(MainActivity.basicUrl + "/user/find")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
                    ResponseWrapper responseWrapper = JsonHelper.decode(data, ResponseWrapper.class);
                    if (responseWrapper.isSuccess()) {
                        Map<String, Object> dataMap = responseWrapper.getData();
                        List<User> userList = JsonHelper.decode(
                                JsonHelper.encode(dataMap.get("userList")), new TypeReference<List<User>>() {
                                });
                        Map<Long, Message> messageMap = JsonHelper.decode(
                                JsonHelper.encode(dataMap.get("messageMap")), new TypeReference<Map<Long, Message>>() {
                                });

                        adapter = new FriendAdapter(userList, messageMap);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                friendRecyclerView.setAdapter(adapter);
                            }
                        });
                    } else {
                        DialogHelper.showAlertDialog(ShowFriendsActivity.this, "Warning", responseWrapper.getErrMessage(), null, null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    DialogHelper.showAlertDialog(ShowFriendsActivity.this, "Warning", "连接服务器异常", null, null);
                }
            }
        }).start();
    }
}
