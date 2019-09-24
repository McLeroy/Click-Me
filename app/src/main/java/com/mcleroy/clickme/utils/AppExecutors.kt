package com.mcleroy.clickme.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors {

    companion object {
        private var instance = AppExecutors()

        fun get(): AppExecutors {
            return instance
        }
    }

    private var diskIO: Executor

    init {
        diskIO = Executors.newSingleThreadExecutor()
    }

    fun getDiskIO(): Executor {
        return diskIO
    }
}