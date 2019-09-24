package com.mcleroy.clickme.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcleroy.clickme.pojo.ClickSession
import com.mcleroy.clickme.repository.ClickSessionRepository
import com.mcleroy.clickme.utils.AppExecutors
import com.mcleroy.clickme.utils.DebugUtils

class MainViewModel() : ViewModel() {


    fun getClickSession(email: String): LiveData<ClickSession> {
        return ClickSessionRepository.get().getClickSession(email)
    }

    fun incrementClick(email: String): LiveData<ClickSession> {
        val clickSessionLiveData = MutableLiveData<ClickSession>()
        DebugUtils.debug(MainViewModel::class.java, "Execution Started")
        AppExecutors.get().getDiskIO().execute {
            val result = ClickSessionRepository.get().getClickSessionSync(email)
            val clickSession = result ?: ClickSession(email, 0, System.currentTimeMillis())
            clickSession.clickCount = clickSession.clickCount + 1
            ClickSessionRepository.get().addClickSession(clickSession)
            clickSessionLiveData.postValue(ClickSessionRepository.get().getClickSessionSync(email))
            DebugUtils.debug(MainViewModel::class.java, "Execution complete")
        }
        return clickSessionLiveData
    }

    fun resetClick(email: String): LiveData<Boolean> {
        val clickSessionLiveData = MutableLiveData<Boolean>()
        AppExecutors.get().getDiskIO().execute {
            ClickSessionRepository.get().deleteClickSession(email)
            clickSessionLiveData.postValue(true)
        }
        return clickSessionLiveData
    }
}