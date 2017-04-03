package com.mariniana.fuelinventorymanagement.main.composer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.mariniana.fuelinventorymanagement.MyApplication
import com.mariniana.fuelinventorymanagement.R
import com.mariniana.fuelinventorymanagement.main.presenter.MainPresenter
import com.mariniana.fuelinventorymanagement.utils.LogUtils
import com.trello.navi2.Event
import com.trello.navi2.component.support.NaviFragment
import com.trello.navi2.rx.RxNavi
import kotlinx.android.synthetic.main.fragment_supplier.*

/**
 * Created by elsennovraditya on 4/2/17.
 */
class SupplierFragment : NaviFragment() {

    companion object {
        const val REFILL_ID = "refill_id"
    }

    private val naviComponent = this
    private val mainPresenter: MainPresenter by lazy {
        MyApplication.fimComponent.provideMainPresenter()
    }

    init {
        initSendFuel()
    }

    private fun initSendFuel() {
        RxNavi
            .observe(naviComponent, Event.VIEW_CREATED)
            .map { arguments.getString(REFILL_ID, "") }
            .flatMap { mainPresenter.doesRefillNeededObservable(it) }
            .filter { it }
            .flatMap { RxView.clicks(send_fuel) }
            .flatMap { mainPresenter.sendFuelObservable() }
            .filter { it }
            .subscribe(
                {
                    LogUtils.debug(tag, "onNext in initLogout")
                    send_fuel.isEnabled = false
                },
                { LogUtils.error(tag, "onError in initLogout", it) },
                { LogUtils.debug(tag, "onComplete in initLogout") }
            )
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater?.inflate(R.layout.fragment_supplier, container, false)
    }

}