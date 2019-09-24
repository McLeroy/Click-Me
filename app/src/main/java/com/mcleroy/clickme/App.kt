package com.mcleroy.clickme

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class App: Application() {

    init {
        context = this
        gson = GsonBuilder().create()
    }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        public var PREF_NAME = "com.mcleroy.clickme.PREFS"
        private var context: App? = null
        private var gson: Gson? = null;

        fun getContext(): Context {
            return context!!.applicationContext
        }

        fun getGson(): Gson {
            return gson!!
        }
    }
}