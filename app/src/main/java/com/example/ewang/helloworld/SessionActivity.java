package com.example.ewang.helloworld;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ewang.helloworld.adapter.MsgAdapter;
import com.example.ewang.helloworld.client.ClientThread;
import com.example.ewang.helloworld.client.Constants;
import com.example.ewang.helloworld.helper.JsonHelper;
import com.example.ewang.helloworld.helper.MyApplication;
import com.example.ewang.helloworld.model.Message;
import com.example.ewang.helloworld.model.Msg;
import com.example.ewang.helloworld.model.User;
import com.example.ewang.helloworld.service.SendMessageService;
import com.example.ewang.helloworld.service.SessionService;
import com.example.ewang.helloworld.service.ShowMessagesService;

import java.util.ArrayList;
import java.util.List;

public class SessionActivity extends AppCompatActivity {

    private static List<Msg> msgList = new ArrayList<>();

    private EditText editText;

    private Button sendBtn;

    private LinearLayoutManager linearLayoutManager;

    public static RecyclerView msgRecyclerView;

    public static MsgAdapter adapter;

    private SessionService.SessionBinder sessionBinder;

    private Handler readHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            msgList.add(new Msg((String) msg.obj, Msg.TYPE_RECEIVED));
            adapter.notifyItemInserted(msgList.size() - 1);
            msgRecyclerView.scrollToPosition(msgList.size() - 1);
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        MyApplication.setCurrentActivity(this);

        long toUserId = getIntent().getLongExtra("toUserId", 0);
        String toUsername = getIntent().getStringExtra("toUsername");

        User user = MyApplication.getCurrentUser();

        editText = findViewById(R.id.input_text);
        sendBtn = findViewById(R.id.btn_send);
        msgRecyclerView = findViewById(R.id.msg_recycler_view);

        TextView textView = findViewById(R.id.username_text);
        textView.setText(toUsername);

        linearLayoutManager = new LinearLayoutManager(SessionActivity.this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);

        Intent showMessagesIntent = new Intent(SessionActivity.this, ShowMessagesService.class)
                .putExtra("url", Constants.DefaultBasicUrl.getValue() + "/session/message/get")
                .putExtra("userId", user.getId())
                .putExtra("toUserId", toUserId);
        startService(showMessagesIntent);

        //TODO socket发消息不使用AsyncTask机制 AsyncTask机制的后台线程只能运行一次
        ClientThread clientThread = new ClientThread(user.getId(), readHandler);
        clientThread.start();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                if (content.isEmpty()) {
                    return;
                }

                Message m = new Message(0, user.getId(), toUserId, content, 0, 0);

                android.os.Message androidMessage = android.os.Message.obtain();
                androidMessage.what = 0;
                androidMessage.obj = JsonHelper.encode(m);
                clientThread.writeHandler.sendMessage(androidMessage);

                Intent sendMessageIntent = new Intent(SessionActivity.this, SendMessageService.class)
                        .putExtra("url", Constants.DefaultBasicUrl.getValue() + "/session/message/send")
                        .putExtra("content", content)
                        .putExtra("userId", user.getId())
                        .putExtra("toUserId", toUserId);
                startService(sendMessageIntent);

                Msg msg = new Msg(content, Msg.TYPE_SENT);
                notifyNewMsg(msg);
                editText.setText("");

            }
        });
    }

    public static void notifyNewMsg(Msg msg) {
        msgList.add(msg);
        adapter.notifyItemInserted(msgList.size() - 1);
        msgRecyclerView.scrollToPosition(msgList.size() - 1);
    }

    public static void setAdapter(List<Msg> msgs) {
        msgList = msgs;
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        msgRecyclerView.scrollToPosition(msgList.size() - 1);
    }

}
