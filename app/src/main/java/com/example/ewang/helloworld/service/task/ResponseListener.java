package com.example.ewang.helloworld.service.task;

import com.example.ewang.helloworld.helper.DialogHelper;
import com.example.ewang.helloworld.helper.MyApplication;
import com.example.ewang.helloworld.helper.ResponseWrapper;

/**
 * Created by ewang on 2018/5/7.
 */

public interface ResponseListener {

    void onSuccess(ResponseWrapper responseWrapper);

    default void onFail(String errMessage) {
        DialogHelper.showAlertDialog(MyApplication.getCurrentActivity(), "Warning", errMessage, null, null);
    }
}
