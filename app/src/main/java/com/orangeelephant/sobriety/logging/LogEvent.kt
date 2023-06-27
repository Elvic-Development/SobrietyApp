package com.orangeelephant.sobriety.logging

import android.util.Log
import com.orangeelephant.sobriety.ApplicationDependencies

object LogEvent {
    // error
    fun e(Tag: String, message: String?, exception: Exception) {
        Log.e(Tag, message, exception)
        ApplicationDependencies.getLogger().logToDb(Tag, message, exception.stackTrace.toString())
    }

    // warning
    fun w(Tag: String, message: String) {
        Log.w(Tag, message)
        ApplicationDependencies.getLogger().logToDb(Tag, message, null)
    }

    // information
    fun i(Tag: String, message: String) {
        Log.i(Tag, message)
        ApplicationDependencies.getLogger().logToDb(Tag, message, null)
    }

    // debug
    fun d(Tag: String, message: String) {
        Log.d(Tag, message)
        ApplicationDependencies.getLogger().logToDb(Tag, message, null)
    }

    // verbose
    fun v(Tag: String, message: String) {
        Log.v(Tag, message)
        ApplicationDependencies.getLogger().logToDb(Tag, message, null)
    }

    // failure
    fun wtf(Tag: String, message: String) {
        Log.wtf(Tag, message)
        ApplicationDependencies.getLogger().logToDb(Tag, message, null)
    }
}