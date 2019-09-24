package com.mcleroy.clickme.utils

import android.annotation.SuppressLint
import android.content.Context
import com.mcleroy.clickme.App
import com.mcleroy.clickme.pojo.User

object AccountUtils {

    private const val KEY_ACCOUNT_PREF: String = "account_pref"

    @JvmStatic
    fun getUser(): User? {
        val preferences = App.getContext().getSharedPreferences(App.PREF_NAME, Context.MODE_PRIVATE)
        return if (preferences.contains(KEY_ACCOUNT_PREF)) App.getGson().fromJson(preferences.getString(
            KEY_ACCOUNT_PREF, null), User::class.java) else null
    }

    @JvmStatic
    fun setUser(user: User) {
        val preferences = App.getContext().getSharedPreferences(App.PREF_NAME, Context.MODE_PRIVATE)
        preferences.edit().putString(KEY_ACCOUNT_PREF, App.getGson().toJson(user)).apply();
    }

    @JvmStatic
    fun isAccountAvailable(): Boolean {
        return getUser() != null
    }

    @SuppressLint("ApplySharedPref")
    @JvmStatic
    fun removeAccount() {
        val preferences = App.getContext().getSharedPreferences(App.PREF_NAME, Context.MODE_PRIVATE)
        preferences.edit().remove(KEY_ACCOUNT_PREF).commit()
    }
}