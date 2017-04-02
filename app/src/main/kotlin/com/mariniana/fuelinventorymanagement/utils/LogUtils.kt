package com.mariniana.fuelinventorymanagement.utils

import android.util.Log

/**
 * Created by elsennovraditya on 6/10/16.
 */
object LogUtils {

    private var logTag: String? = null
    var enableLogging: Boolean = false

    fun enableLogging(enableLogging: Boolean, logTag: String) {
        this.enableLogging = enableLogging
        this.logTag = logTag
    }

    fun debug(tag: String, message: String) {
        if (LogUtils.enableLogging) {
            Log.d(LogUtils.logTag, tag + " - " + message)
        }
    }

    fun error(tag: String, message: String, throwable: Throwable) {
        if (LogUtils.enableLogging) {
            Log.e(LogUtils.logTag, tag + " - " + message, throwable)
        }
    }

}
