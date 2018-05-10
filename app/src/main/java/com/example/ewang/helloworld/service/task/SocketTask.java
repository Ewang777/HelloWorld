package com.example.ewang.helloworld.service.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ewang.helloworld.SessionActivity;
import com.example.ewang.helloworld.client.Constants;
import com.example.ewang.helloworld.helper.JsonHelper;
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

/**
 * Created by ewang on 2018/5/8.
 */

public class SocketTask extends AsyncTask<Object, Msg, Boolean> {

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

            String msg;
            while ((msg = readFromServer()) != null) {
                publishProgress(new Msg(msg, Msg.TYPE_RECEIVED));
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
    protected void onProgressUpdate(Msg... values) {
        SessionActivity.notifyNewMsg(values[0]);
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
