package com.snicesoft.basekit.controller;

import android.app.ProgressDialog;
import android.content.Context;

import com.snicesoft.basekit.util.DialogUtil;
import com.snicesoft.basekit.util.NetworkUtil;

public class Controller {
    private Context context;
    private ProgressDialog progressDialog;

    public Controller(Context context) {
        this.context = context;
        progressDialog = DialogUtil.getProgressDialog(context);
    }

    protected Context getContext() {
        return context;
    }

    protected boolean checkNext() {
        return NetworkUtil.isConnect(getContext());
    }

    /**
     * @param message
     * @param flag    0 setCancelable 1 setCanceledOnTouchOutside
     */
    protected void showDialog(CharSequence message, boolean... flag) {
        if (flag != null) {
            if (flag.length > 0)
                progressDialog.setCancelable(flag[0]);
            if (flag.length > 1)
                progressDialog.setCanceledOnTouchOutside(flag[1]);
        }
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    protected void closeDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
