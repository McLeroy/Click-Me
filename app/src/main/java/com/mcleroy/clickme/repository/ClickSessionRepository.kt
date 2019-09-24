package com.mcleroy.clickme.repository

import androidx.lifecycle.LiveData
import com.mcleroy.clickme.database.ClickSessionDatabase
import com.mcleroy.clickme.pojo.ClickSession

class ClickSessionRepository {

    private val clickSessionDao: ClickSessionDatabase.ClickSessionDao

    init {
        clickSessionDao = ClickSessionDatabase.get().clickSessionDao
    }

    companion object {

        private var instance = ClickSessionRepository()

        fun get(): ClickSessionRepository {
            return instance
        }
    }

    fun getClickSession(email: String): LiveData<ClickSession> {
        return clickSessionDao.getClickSession(email)
    }

    fun getClickSessionSync(email: String): ClickSession? {
        return clickSessionDao.getClickSessionSync(email)
    }

    fun addClickSession(clickSession: ClickSession) {
        clickSessionDao.insert(clickSession)
    }

    fun deleteClickSession(email: String) {
        clickSessionDao.deleteClickSession(email)
    }
}
