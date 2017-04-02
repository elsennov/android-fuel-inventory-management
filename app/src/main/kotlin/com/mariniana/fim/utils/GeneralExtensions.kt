package com.mariniana.fim.utils

import android.content.Context
import org.jetbrains.anko.connectivityManager

/**
 * Detect whether it is connected to internet or not
 */
fun Context.isNetworkAvailable(): Boolean {
    return this.connectivityManager.activeNetworkInfo?.isConnected ?: false
}
