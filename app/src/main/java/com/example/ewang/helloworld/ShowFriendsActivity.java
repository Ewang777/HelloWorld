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

import java.io.IOException;
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
                        List<User> userList = (List<User>) dataMap.get("userList");
                        Map<Long, Message> messageMap = (Map<Long, Message>) dataMap.get("messageMap");

                        adapter = new FriendAdapter(userList, messageMap);
                    } else {
                        DialogHelper.showAlertDialog(ShowFriendsActivity.this, "Warning", responseWrapper.getErrMessage(), null, null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    DialogHelper.showAlertDialog(ShowFriendsActivity.this, "Warning", "连接服务器异常", null, null);
                }
            }
        }).start();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friendRecyclerView.setLayoutManager(layoutManager);
        friendRecyclerView.setAdapter(adapter);

    }
}
