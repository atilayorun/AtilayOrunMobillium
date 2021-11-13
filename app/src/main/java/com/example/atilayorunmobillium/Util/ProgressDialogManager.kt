package com.example.atilayorunmobillium.Util

import android.app.ProgressDialog
import android.content.Context

class ProgressDialogManager {
    var progressDialog: ProgressDialog? = null

    fun showProgressDialog(context: Context, message: String, setCancelable: Boolean) {
        if (progressDialog == null)
            progressDialog = ProgressDialog(context)
        progressDialog?.setMessage(message)
        progressDialog?.setCancelable(setCancelable)
        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }
}