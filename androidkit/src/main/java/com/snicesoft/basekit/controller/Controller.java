package com.snicesoft.basekit.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import com.snicesoft.basekit.util.NetworkUtil;

public class Controller {
    private Context context;
    private ProgressDialog progressDialog;

    public Controller(Context context) {
        this.context = context;
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
            progressDialog = new ProgressDialog(context, 0x3);
        else
            progressDialog = new ProgressDialog(context);
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
