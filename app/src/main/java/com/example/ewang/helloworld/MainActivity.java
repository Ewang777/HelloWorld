package com.example.ewang.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ewang.helloworld.helper.MyApplication;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final int request_login = 0;
    static final int request_reg = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.setCurrentActivity(this);

        Button btnLogin = findViewById(R.id.btn_login);
        Button btnReg = findViewById(R.id.btn_reg);

        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int data = 0;
        switch (view.getId()) {
            case R.id.btn_login:
                data = request_login;
                break;
            case R.id.btn_reg:
                data = request_reg;
                break;
            default:
                break;
        }
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("request", data);
        startActivity(intent);
    }
}
