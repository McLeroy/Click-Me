package com.mcleroy.clickme.utils

import android.annotation.SuppressLint
import android.content.Context
import com.mcleroy.clickme.App

object PreferenceUtils {

    private const val KEY_WINDOW_DELAY: String = "window_delay"

    @JvmStatic
    fun getWindowDelay(): Long {
        val preferences = App.getContext().getSharedPreferences(App.PREF_NAME, Context.MODE_PRIVATE)
        return preferences.getLong(KEY_WINDOW_DELAY, 5)
    }

    @SuppressLint("ApplySharedPref")
    @JvmStatic
    fun changeWindowDelay(newDelay: Long) {
        val preferences = App.getContext().getSharedPreferences(App.PREF_NAME, Context.MODE_PRIVATE)
        preferences.edit().putLong(KEY_WINDOW_DELAY, newDelay).commit()
    }
}