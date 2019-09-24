package com.mcleroy.clickme.utils

import android.text.TextUtils
import android.widget.Toast

import androidx.annotation.StringRes

import com.mcleroy.clickme.App

object ToastUtils {

    fun makeToast(@StringRes messageRes: Int, _long: Boolean) {
        makeToast(App.getContext().getString(messageRes), _long)
    }

    fun makeToast(message: CharSequence, _long: Boolean) {
        if (TextUtils.isEmpty(message)) return
        val toast = Toast.makeText(
            App.getContext(),
            message,
            if (_long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        )
        toast.show()
    }
}
