package com.example.ewang.helloworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.ewang.helloworld.adapter.SessionAdapter;
import com.example.ewang.helloworld.client.Constants;
import com.example.ewang.helloworld.helper.MyApplication;
import com.example.ewang.helloworld.model.Session;
import com.example.ewang.helloworld.model.User;
import com.example.ewang.helloworld.service.LoginService;
import com.example.ewang.helloworld.service.ShowFriendsService;

import java.util.List;
import java.util.Map;

public class ShowSessionListActivity extends AppCompatActivity {

    private Button btnOff;

    public static RecyclerView friendRecyclerView;

    private static SessionAdapter adapter;

    private static List<User> userList;

    private static Map<Long, String> messageMap;

    private static Map<Long, Session> sessionMap;

    private User user = MyApplication.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friends);
        MyApplication.setCurrentActivity(this);

        btnOff = findViewById(R.id.btn_off);
        friendRecyclerView = findViewById(R.id.friend_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowSessionListActivity.this);
        friendRecyclerView.setLayoutManager(layoutManager);

        btnOff.setVisibility(View.VISIBLE);
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowSessionListActivity.this, MainActivity.class);
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

    //app退出要即使关闭socket的线程
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginService.socketTask.closeSocket();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MyApplication.setCurrentActivity(this);
        setMessageAdapter(user);
    }

    void setMessageAdapter(User user) {
        Intent showFriendsIntent = new Intent(ShowSessionListActivity.this, ShowFriendsService.class)
                .putExtra("url", Constants.DefaultBasicUrl.getValue() + "/session/get")
                .putExtra("userId", user.getId());
        startService(showFriendsIntent);

    }

    public static void setAdapter(List<User> users, Map<Long, String> latestMessageMap, Map<Long, Session> mSessionMap) {
        userList = users;
        messageMap = latestMessageMap;
        sessionMap = mSessionMap;
        adapter = new SessionAdapter(userList, messageMap, sessionMap);
        friendRecyclerView.setAdapter(adapter);
    }

    public static void notifyNewMsg(Long userId, String msgContent) {
        Session oldSession = sessionMap.get(userId);
        sessionMap.put(userId, new Session(oldSession.getId(), oldSession.getUserId(), oldSession.getToUserId(),
                oldSession.getCreateTime().getTime(), oldSession.getUpdateTime().getTime(), oldSession.getUnread() + 1));
        messageMap.put(userId, msgContent);
        adapter.notifyDataSetChanged();
    }
}
