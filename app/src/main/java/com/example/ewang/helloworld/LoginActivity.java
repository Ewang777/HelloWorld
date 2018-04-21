package com.example.ewang.helloworld;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ewang.helloworld.helper.MyApplication;
import com.example.ewang.helloworld.model.User;
import com.example.ewang.helloworld.helper.JsonHelper;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    String account;

    String password;

    EditText editAccount;

    EditText editPwd;

    Button btnSure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String requestUri = null;
        btnSure = findViewById(R.id.btn_sure);
        Intent intent = getIntent();
        int requestType = intent.getIntExtra("request", 0);
        if (requestType == MainActivity.request_login) {
            btnSure.setText("登录");
            requestUri = "/login";
        } else if (requestType == MainActivity.request_reg) {
            btnSure.setText("注册");
            requestUri = "/reg";

        }

        editAccount = findViewById(R.id.edit_account);
        editPwd = findViewById(R.id.edit_pwd);

        final String requestUrl = MainActivity.basicUrl + requestUri;
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                account = editAccount.getText().toString().trim();
                password = editPwd.getText().toString().trim();
                judgeParameters(account, password);


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("account", account)
                                    .add("password", password)
                                    .build();
                            Request request = new Request.Builder()
                                    .url(requestUrl)
                                    .post(requestBody)
                                    .build();
                            Response response = okHttpClient.newCall(request).execute();
                            String data = response.body().string();
                            User currentUser = JsonHelper.decode(data, User.class);

                            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                            editor.putString("account", currentUser.getAccount());
                            editor.putString("password", currentUser.getPassword());
                            editor.apply();

                            MyApplication.setCurrentUser(currentUser);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Intent intent = new Intent(LoginActivity.this, ShowFriendsActivity.class);
                startActivity(intent);

            }
        });
    }

    void judgeParameters(String account, String pwd) {
        AlertDialog.Builder warnDialog = new AlertDialog.Builder(LoginActivity.this);
        warnDialog.setTitle("Warning");
        warnDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editAccount.setText("");
                editPwd.setText("");
            }
        });
        if (account.isEmpty() || null == account || pwd.isEmpty() || null == pwd) {
            warnDialog.setMessage("用户名或密码不得为空");
            warnDialog.show();
        }
    }
}
