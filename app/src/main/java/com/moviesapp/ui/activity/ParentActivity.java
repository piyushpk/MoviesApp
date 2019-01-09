package com.moviesapp.ui.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

public class ParentActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    // dismiss loading dialog
    protected void dismissLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // show loading dialog
    protected void showLoadingDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    // show loading dialog
    protected void showLoadingDialog(String msg) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }
}
