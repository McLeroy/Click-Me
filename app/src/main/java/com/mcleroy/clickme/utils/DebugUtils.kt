package com.mcleroy.clickme.utils

import android.util.Log

object DebugUtils {

    fun debug(caller: Class<*>, message: String) {
        Log.d(caller.simpleName, message)
    }

    fun warn(caller: Class<*>, message: String) {
        Log.w(caller.simpleName, message)
    }
}
