package com.mariniana.fim

import android.app.Application
import com.mariniana.fim.utils.LogUtils

/**
 * Created by elsennovraditya on 11/30/16.
 */
class MyApplication : Application() {

    companion object {
        lateinit var fimComponent: FimComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
        initLogging()
    }

    private fun initDagger() {
        fimComponent = DaggerFimComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    private fun initLogging() {
        LogUtils.enableLogging(enableLogging = true, logTag = "FuelInventoryManagement")
    }

}