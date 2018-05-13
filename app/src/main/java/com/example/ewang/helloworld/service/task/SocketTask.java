package com.example.ewang.helloworld.service.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ewang.helloworld.SessionActivity;
import com.example.ewang.helloworld.ShowSessionListActivity;
import com.example.ewang.helloworld.client.Constants;
import com.example.ewang.helloworld.helper.JsonHelper;
import com.example.ewang.helloworld.helper.MyApplication;
import com.example.ewang.helloworld.model.Message;
import com.example.ewang.helloworld.model.Msg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * Created by ewang on 2018/5/8.
 */

public class SocketTask extends AsyncTask<Object, Message, Boolean> {

    private Socket socket;

    private OutputStream outputStream;

    private BufferedReader bufferedReader;

    @Override
    protected Boolean doInBackground(Object... objects) {
        try {
            socket = new Socket(Constants.ServerIP.getValue(), 7777);
            if (socket == null) {
                return false;
            }
            outputStream = socket.getOutputStream();
            long id = (long) objects[0];
            outputStream.write((String.valueOf(id) + "\n").getBytes(Constants.CharsetName.getValue()));

            EventBus.getDefault().register(this);

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                    Constants.CharsetName.getValue()));

            String msgJson;
            while ((msgJson = readFromServer()) != null) {
                Message message = JsonHelper.decode(msgJson, Message.class);
                publishProgress(message);
            }

            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Message... values) {
        //TODO 插入本地数据库/缓存
        Message m = values[0];
        if (MyApplication.getCurrentActivity() instanceof SessionActivity) {
            SessionActivity.notifyNewMsg(m, Msg.TYPE_RECEIVED);
        } else if (MyApplication.getCurrentActivity() instanceof ShowSessionListActivity) {
            ShowSessionListActivity.notifyNewMsg(m.getUserId(), m.getContent());
        }
        super.onProgressUpdate(values);
    }

    public String readFromServer() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public void closeSocket() {
        try {
            socket.close();
            bufferedReader.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("fail:", "in closing socket");
        }

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSendMsgEvent(Message m) {
        String msgJson = JsonHelper.encode(m);
        try {
            outputStream.write((msgJson + "\n").getBytes(Constants.CharsetName.getValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
