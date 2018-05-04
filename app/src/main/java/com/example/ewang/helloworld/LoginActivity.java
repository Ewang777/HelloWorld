package com.example.ewang.helloworld;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ewang.helloworld.helper.DialogHelper;
import com.example.ewang.helloworld.helper.HttpUtil;
import com.example.ewang.helloworld.helper.MyApplication;
import com.example.ewang.helloworld.helper.ResponseWrapper;
import com.example.ewang.helloworld.model.User;
import com.example.ewang.helloworld.helper.JsonHelper;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

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
            requestUri = "/user/login";
        } else if (requestType == MainActivity.request_reg) {
            btnSure.setText("注册");
            requestUri = "/user/reg";

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


                ProgressDialog progressDialog = DialogHelper.showProgressDialog(LoginActivity.this, "请稍侯", "loading", null);

                Thread loginThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        RequestBody requestBody = new FormBody.Builder()
                                .add("account", account)
                                .add("password", password)
                                .build();
                        ResponseWrapper responseWrapper = HttpUtil.sendRequest(requestUrl, requestBody, LoginActivity.this);


                        if (responseWrapper.isSuccess()) {
                            Map<String, Object> userMap = (Map<String, Object>) responseWrapper.getData().get("user");

                            User currentUser = JsonHelper.decode(JsonHelper.encode(userMap), User.class);
                            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                            editor.putString("account", currentUser.getAccount());
                            editor.putString("password", currentUser.getPassword());
                            editor.apply();

                            MyApplication.setCurrentUser(currentUser);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(LoginActivity.this, ShowFriendsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DialogHelper.showAlertDialog(LoginActivity.this, "Warning", responseWrapper.getErrMessage(), null, null);
                                }
                            });
                        }
                        progressDialog.dismiss();
                    }
                });

                loginThread.start();


            }
        });
    }

    void judgeParameters(String account, String pwd) {
        if (account.isEmpty() || null == account || pwd.isEmpty() || null == pwd) {
            DialogHelper.showAlertDialog(LoginActivity.this, "Warning", "用户名或密码不得为空", (dialogInterface, i) -> {
                editAccount.setText("");
                editPwd.setText("");
            }, null);

        }
    }
}
