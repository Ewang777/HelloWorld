package com.example.ewang.helloworld.client;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by ewang on 2018/5/5.
 */

public class ClientThread extends Thread {

    private final long id;

    public Handler writeHandler;

    private Handler readHandler;

    private Socket socket;

    private OutputStream outputStream;

    private BufferedReader bufferedReader;


    public ClientThread(long id, Handler readHandler) {
        this.id = id;
        this.readHandler = readHandler;
    }

    @Override
    public void run() {

        createSession(id);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
                    String content;
                    while ((content = readFromServer()) != null) {
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = content;
                        readHandler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        Looper.prepare();
        writeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String data = (String) msg.obj;
                try {
                    outputStream.write((data + "\n").getBytes(Constants.CharsetName.getValue()));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("ClientSocket写出消息失败");
                }
                super.handleMessage(msg);
            }
        };
        Looper.loop();
    }

    void createSession(long id) {
        try {
            socket = new Socket(Constants.ServerIP.getValue(), 7777);
            outputStream = socket.getOutputStream();
            outputStream.write((String.valueOf(id) + "\n").getBytes(Constants.CharsetName.getValue()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("开启新ClientSocket写出id失败");
        }
    }

    public String readFromServer() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
