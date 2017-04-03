package com.mariniana.fuelinventorymanagement

import com.mariniana.fuelinventorymanagement.api.ApiModule
import com.mariniana.fuelinventorymanagement.api.RetrofitModule
import com.mariniana.fuelinventorymanagement.firebase.FirebaseManager
import com.mariniana.fuelinventorymanagement.firebase.FirebaseModule
import com.mariniana.fuelinventorymanagement.login.LoginModule
import com.mariniana.fuelinventorymanagement.login.presenter.LoginPresenter
import com.mariniana.fuelinventorymanagement.main.MainModule
import com.mariniana.fuelinventorymanagement.main.presenter.MainPresenter
import dagger.Component
import javax.inject.Singleton

/**
 * Created by elsennovraditya on 11/30/16.
 */
@Singleton
@Component(modules = arrayOf(
    ApplicationModule::class,
    RetrofitModule::class,
    ApiModule::class,
    MainModule::class,
    FirebaseModule::class,
    LoginModule::class
))
interface FimComponent {
    fun provideMainPresenter(): MainPresenter
    fun provideLoginPresenter(): LoginPresenter
    fun provideFirebaseManager(): FirebaseManager
}
