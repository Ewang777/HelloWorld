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
import com.example.ewang.helloworld.helper.MyApplication;
import com.example.ewang.helloworld.model.Message;
import com.example.ewang.helloworld.model.User;
import com.example.ewang.helloworld.service.ShowFriendsService;

import java.util.List;
import java.util.Map;

public class ShowFriendsActivity extends AppCompatActivity {

    private Button btnOff;

    public static RecyclerView friendRecyclerView;

    private static FriendAdapter adapter;

    private static List<User> userList;

    private static Map<Long, Message> messageMap;

    private User user = MyApplication.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friends);
        MyApplication.setCurrentActivity(this);

        btnOff = findViewById(R.id.btn_off);
        friendRecyclerView = findViewById(R.id.friend_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowFriendsActivity.this);
        friendRecyclerView.setLayoutManager(layoutManager);

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

        setMessageAdapter(user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMessageAdapter(user);
    }

    void setMessageAdapter(User user) {
        Intent showFriendsIntent = new Intent(ShowFriendsActivity.this, ShowFriendsService.class)
                .putExtra("url", Constants.DefaultBasicUrl.getValue() + "/user/find")
                .putExtra("userId", user.getId());
        startService(showFriendsIntent);

    }

    public static void setAdapter(List<User> users, Map<Long, Message> latestMessageMap) {
        userList = users;
        messageMap = latestMessageMap;
        adapter = new FriendAdapter(userList, latestMessageMap);
        friendRecyclerView.setAdapter(adapter);
    }
}
