package com.example.ewang.helloworld.helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ProgressBar;

import com.example.ewang.helloworld.LoginActivity;

import java.util.function.Function;

/**
 * Created by ewang on 2018/4/23.
 */

public class DialogHelper {
    public static void showAlertDialog(Context context, String title, String message,
                                       DialogInterface.OnClickListener onPositiveClickListener,
                                       DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog.Builder warnDialog = new AlertDialog.Builder(context);
        warnDialog.setTitle(title);
        warnDialog.setMessage(message);
        warnDialog.setPositiveButton("确定", onPositiveClickListener);

        if (onNegativeClickListener != null) {
            warnDialog.setNegativeButton("取消", onNegativeClickListener);
        }
        warnDialog.show();

    }

    public static void showProgressDialog(Context context, String title, String message, ProgressDialog.OnCancelListener onCancelListener) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        if (onCancelListener != null) {
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(onCancelListener);
            progressDialog.show();

        }
    }
}
