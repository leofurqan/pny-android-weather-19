package com.example.weather;

import android.app.ProgressDialog;
import android.content.Context;

public class Helper {
    Context context;
    private ProgressDialog progressDialog;

    public Helper(Context context) {
        this.context = context;
    }

    public void showProgressDialog(String title, String message) {
        progressDialog = ProgressDialog.show(context, title, message);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void dismissProgressDialog() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
