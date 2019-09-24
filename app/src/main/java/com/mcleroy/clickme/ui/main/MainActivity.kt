package com.mcleroy.clickme.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mcleroy.clickme.R
import com.mcleroy.clickme.pojo.ClickSession
import com.mcleroy.clickme.pojo.User
import com.mcleroy.clickme.ui.auth.AuthActivityIntent
import com.mcleroy.clickme.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    private var user: User? = null
    private var clickSession: ClickSession? = null
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        init()
    }

    private fun init() {
        user = AccountUtils.getUser()
        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    private fun getSavedClickSession() {
        mainViewModel.getClickSession(user?.email ?: "").observe(this, clickSessionObserver)
    }

    @SuppressLint("SetTextI18n")
    private fun processClickSession(clickSession: ClickSession?) {
        this.clickSession = clickSession
        val maxAllowedClicks = 4
        val clickCount = clickSession?.clickCount ?: 0
        val remainingClicks = maxAllowedClicks - clickCount
        statusText.text = String.format("You have %s %s left", remainingClicks, if (remainingClicks == 1) "click" else "clicks")
        DebugUtils.debug(MainActivity::class.java, "Remaining clicks: $remainingClicks")
        startCountDown()
        val minuteDifference = if (clickSession != null)
            TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - clickSession.lastClickTime) else -1
        if (minuteDifference >= 60)
            resetClickSession()
    }

    private fun startCountDown() {
        if (clickSession == null) {
            windowText.visibility = View.GONE
            return
        }
        if (countDownTimer != null) countDownTimer!!.cancel()
        val windowDelay = PreferenceUtils.getWindowDelay()
        val windowTimeInMills = clickSession!!.lastClickTime + TimeUnit.MINUTES.toMillis(windowDelay)
        val differenceInMills = windowTimeInMills - System.currentTimeMillis()
        val differenceInMins = TimeUnit.MILLISECONDS.toMinutes(differenceInMills)
        windowText.visibility = if (differenceInMins >= windowDelay || clickSession!!.clickCount >= 4) View.GONE else View.VISIBLE
        if (differenceInMins >= windowDelay) return
        countDownTimer = object: CountDownTimer(differenceInMills, 1000) {
            override fun onTick(value: Long) {
                val currentDifferenceInSecs = TimeUnit.MILLISECONDS.toSeconds(value)
                val currentDifferenceInMins = TimeUnit.MILLISECONDS.toMinutes(value)
                if (currentDifferenceInMins > 0)
                    windowText.text = String.format("Window: %s %s", currentDifferenceInMins, if (currentDifferenceInMins == 1L) "min" else "mins")
                else
                    windowText.text = String.format("Window: %s %s", currentDifferenceInSecs, if (currentDifferenceInSecs == 1L) "sec" else "secs")
            }
            override fun onFinish() {
                windowText.visibility = View.GONE
            }
        }.start()
    }

    private fun validateClick(): Boolean {
        if (clickSession == null) return true
        val minuteDifference = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - clickSession!!.lastClickTime)
        DebugUtils.debug(MainActivity::class.java, "Hour diff:  minute diff: $minuteDifference")
        if (minuteDifference >= PreferenceUtils.getWindowDelay()) {
            ToastUtils.makeToast(String.format("This operation is not allowed. Try again in %s minutes", 60 - minuteDifference), false)
            return false
        }
        if (clickSession!!.clickCount >= 4 && minuteDifference < 60) {
            ToastUtils.makeToast(String.format("This operation is not allowed. Try again in %s minutes", 60 - minuteDifference), false)
            return false
        }
        return true
    }

    private fun resetClickSession() {
        mainViewModel.resetClick(user?.email ?: "").observe(this, Observer { getSavedClickSession() })
    }

    private fun changeSessionWindow() {
        SessionWindowBottomSheet(this, PreferenceUtils.getWindowDelay(), object : (SessionWindowBottomSheet.SessionWindowCallback) {
            override fun windowDelayChanged(delay: Long) {
                PreferenceUtils.changeWindowDelay(delay)
                resetClickSession()
            }
        }).show()
    }

    private fun logout() {
        AccountUtils.removeAccount()
        startActivity(AuthActivityIntent(this))
        finish()
    }

    fun incrementClick(view: View) {
        if (user == null) return
        if (!validateClick()) {
            DebugUtils.warn(MainActivity::class.java, "Click not allowed")
            return
        }
        DebugUtils.debug(MainActivity::class.java, "Performing increment")
        mainViewModel.incrementClick(user!!.email).observe(this, clickSessionObserver)
    }

    private val clickSessionObserver: Observer<ClickSession> = Observer { clickSession ->
        run {
            processClickSession(clickSession)
        }
    }

    override fun onResume() {
        super.onResume()
        getSavedClickSession()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_reset) {
            resetClickSession()
            processClickSession(clickSession)
        }
        else if (item.itemId == R.id.action_change)
            changeSessionWindow()
        else if (item.itemId == R.id.action_logout)
            logout()
        return super.onOptionsItemSelected(item)
    }
}



fun MainActivityIntent(context: Context): Intent {
    return Intent(context, MainActivity::class.java)
}
