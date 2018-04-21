package com.example.ewang.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ewang.helloworld.adapter.MsgAdapter;
import com.example.ewang.helloworld.model.Msg;

import java.util.ArrayList;
import java.util.List;

public class SessionActivity extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<>();

    private EditText editText;

    private Button sendBtn;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        initMsg();
        editText = findViewById(R.id.input_text);
        sendBtn = findViewById(R.id.btn_send);
        msgRecyclerView = findViewById(R.id.msg_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);

        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                if (content.isEmpty()) {
                    return;
                }
                Msg msg = new Msg(content, Msg.TYPE_SENT);
                msgList.add(msg);
                adapter.notifyItemInserted(msgList.size() - 1);
                msgRecyclerView.scrollToPosition(msgList.size() - 1);
                editText.setText("");

            }
        });
    }

    private void initMsg() {
        msgList.add(new Msg(("我是阿汪，请求添加你为好友"), Msg.TYPE_SENT));
        msgList.add(new Msg("我通过了你的好友验证，现在我们可以开始聊天了", Msg.TYPE_RECEIVED));
    }

}
