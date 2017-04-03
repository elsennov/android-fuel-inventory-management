package com.mariniana.fuelinventorymanagement.login

import com.mariniana.fuelinventorymanagement.firebase.FirebaseManager
import com.mariniana.fuelinventorymanagement.login.presenter.LoginPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by elsennovraditya on 4/2/17.
 */
@Module
class LoginModule {

    @Provides
    fun provideLoginPresenter(firebaseManager: FirebaseManager): LoginPresenter {
        return LoginPresenter(firebaseManager)
    }

}