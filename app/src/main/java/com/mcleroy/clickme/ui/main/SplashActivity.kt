package com.mcleroy.clickme.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mcleroy.clickme.R
import com.mcleroy.clickme.ui.auth.AuthActivityIntent
import com.mcleroy.clickme.utils.AccountUtils

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }

    private fun init() {
        if (!AccountUtils.isAccountAvailable())
            startActivity(AuthActivityIntent(this))
        else
            startActivity(MainActivityIntent(this))
        finish()
        overridePendingTransition(0, 0)
    }
}
