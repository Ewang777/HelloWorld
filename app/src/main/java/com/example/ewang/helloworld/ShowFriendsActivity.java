package com.example.ewang.helloworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.ewang.helloworld.adapter.FriendAdapter;
import com.example.ewang.helloworld.client.Constants;
import com.example.ewang.helloworld.helper.DialogHelper;
import com.example.ewang.helloworld.helper.HttpUtil;
import com.example.ewang.helloworld.helper.JsonHelper;
import com.example.ewang.helloworld.helper.MyApplication;
import com.example.ewang.helloworld.helper.ResponseWrapper;
import com.example.ewang.helloworld.model.Message;
import com.example.ewang.helloworld.model.User;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ShowFriendsActivity extends AppCompatActivity {

    private FriendAdapter adapter;

    private RecyclerView friendRecyclerView;

    private Button btnOff;

    private User user = MyApplication.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friends);

        btnOff = findViewById(R.id.btn_off);
        btnOff.setVisibility(View.VISIBLE);
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowFriendsActivity.this, MainActivity.class);
                MyApplication.setCurrentUser(null);
                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                editor.remove("account");
                editor.remove("password");
                editor.apply();
                finish();
                startActivity(intent);
            }
        });

        friendRecyclerView = findViewById(R.id.friend_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowFriendsActivity.this);
        friendRecyclerView.setLayoutManager(layoutManager);

        setMessageAdapter(user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMessageAdapter(user);
    }

    void setMessageAdapter(User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody requestBody = new FormBody.Builder()
                        .add("userId", String.valueOf(user.getId()))
                        .build();
                String url = Constants.DefaultBasicUrl.getValue() + "/user/find";
                ResponseWrapper responseWrapper = HttpUtil.sendRequest(url, requestBody, ShowFriendsActivity.this, null);

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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogHelper.showAlertDialog(ShowFriendsActivity.this, "Warning", responseWrapper.getErrMessage(), null, null);
                        }
                    });
                }
            }
        }).start();

    }
}
