package com.example.ewang.helloworld.service.task;

import com.example.ewang.helloworld.helper.CustomActivityManager;
import com.example.ewang.helloworld.helper.DialogHelper;
import com.example.ewang.helloworld.helper.ResponseWrapper;

/**
 * Created by ewang on 2018/5/7.
 */

public interface ResponseListener {

    default void onSuccess(ResponseWrapper responseWrapper) {
    }

    default void onFail(String errMessage) {
        errMessage = errMessage == null ? "连接服务器异常" : errMessage;
        DialogHelper.showAlertDialog(CustomActivityManager.getInstance().getCurrentActivity(), "Warning", errMessage, null, null);
    }
}
