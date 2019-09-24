package com.mcleroy.clickme.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mcleroy.clickme.R
import com.mcleroy.clickme.pojo.User
import com.mcleroy.clickme.ui.main.MainActivityIntent
import com.mcleroy.clickme.utils.AccountUtils
import com.mcleroy.clickme.utils.TextWatcherImpl
import com.mcleroy.clickme.utils.Utils
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        init()
    }

    private fun init() {
        emailAddressText.addTextChangedListener(object: TextWatcherImpl() {
            override fun afterTextChanged(p0: Editable?) {
                proceedBtn.isEnabled = Utils.isEmailAddressValid(p0.toString());
            }
        })
    }

    fun login(view: View) {
        AccountUtils.setUser(User(emailAddressText.text.toString()))
        startActivity(MainActivityIntent(this))
        finish()
    }
}

fun Context.AuthActivityIntent(context: Context): Intent {
    return Intent(context, AuthActivity::class.java)
}
