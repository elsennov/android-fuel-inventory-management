package com.mariniana.fuelinventorymanagement.login.presenter

import com.google.firebase.auth.FirebaseAuth
import com.mariniana.fuelinventorymanagement.BasePresenter
import com.mariniana.fuelinventorymanagement.firebase.FirebaseManager
import com.mariniana.fuelinventorymanagement.login.model.User
import io.reactivex.Observable

/**
 * Created by elsennovraditya on 4/2/17.
 */
class LoginPresenter(private val firebaseManager: FirebaseManager) : BasePresenter() {

    fun loginObservable(email: String, password: String): Observable<User> {
        return firebaseManager.loginObservable(email, password)
    }

    fun registerFcmObservable(): Observable<Boolean> {
        return firebaseManager.registerFcmObservable()
    }

    fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

}