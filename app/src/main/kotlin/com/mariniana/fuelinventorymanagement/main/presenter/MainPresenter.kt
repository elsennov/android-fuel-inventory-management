package com.mariniana.fuelinventorymanagement.main.presenter

import com.mariniana.fuelinventorymanagement.BasePresenter
import com.mariniana.fuelinventorymanagement.api.ApiManager
import com.mariniana.fuelinventorymanagement.firebase.FirebaseManager
import io.reactivex.Observable

/**
 * Created by elsennovraditya on 11/30/16.
 */

open class MainPresenter(private val apiManager: ApiManager,
                         private val firebaseManager: FirebaseManager) : BasePresenter() {

    fun logoutObservable(): Observable<Boolean> {
        return firebaseManager.logoutObservable()
    }

    fun getUserRoleObservable(): Observable<String> {
        return firebaseManager.getUserRoleObservable()
    }

    fun doesRefillNeededObservable(refillId: String): Observable<Boolean> {
        if (refillId.isNullOrEmpty()) {
            return firebaseManager.getLatestRefillObservable()
        } else {
            return firebaseManager.getRefillObservable(refillId)
        }
    }

    fun sendFuelObservable(): Observable<Boolean> {

    }

}