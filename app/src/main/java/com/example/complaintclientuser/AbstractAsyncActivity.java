package com.example.complaintclientuser;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

public abstract class AbstractAsyncActivity extends AppCompatActivity {

    protected final static String TAG = AbstractAsyncActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private boolean destroyed = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }

    public void showLoadingProgressDialog() {
        this.showProgressdialog("Loading...");
    }

    private void showProgressdialog(String text) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
        }
        progressDialog.setMessage(text);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && !destroyed){
            progressDialog.dismiss();
        }
    }
}
