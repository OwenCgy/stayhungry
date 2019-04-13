package com.example.administrator.testloginscau.base;

import android.content.Context;

/**
 * Created by Arron on 2017/4/12.
 */

public interface BaseView {
    Context getContext();

    void showError(String errorText);

    void showLoadingDialog(String contentText);

    void hideLoadingDialog();


}
