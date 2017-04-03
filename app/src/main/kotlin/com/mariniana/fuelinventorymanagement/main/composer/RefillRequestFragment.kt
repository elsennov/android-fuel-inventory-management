package com.mariniana.fuelinventorymanagement.main.composer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.mariniana.fuelinventorymanagement.MyApplication
import com.mariniana.fuelinventorymanagement.R
import com.mariniana.fuelinventorymanagement.main.model.Refill
import com.mariniana.fuelinventorymanagement.main.presenter.MainPresenter
import com.mariniana.fuelinventorymanagement.utils.LogUtils
import com.trello.navi2.Event
import com.trello.navi2.rx.RxNavi
import io.reactivex.Observable
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.fragment_request_refill.*

/**
 * Created by elsennovraditya on 4/3/17.
 */
class RefillRequestFragment : SellerContentFragment() {

    companion object {
        private val TAG = RefillRequestFragment::class.java.simpleName
        const val REFILL_ID = "refill_id"

        fun getInstance(refillId: String): RefillRequestFragment {
            val bundle = Bundle()
            bundle.putString(REFILL_ID, refillId)

            val requestRefillFragment = RefillRequestFragment()
            requestRefillFragment.arguments = bundle
            return requestRefillFragment
        }
    }

    private val naviComponent = this
    private val mainPresenter: MainPresenter by lazy {
        MyApplication.fimComponent.provideMainPresenter()
    }

    init {
        initRequestRefillButton()
    }

    private fun initRequestRefillButton() {
        RxNavi
            .observe(naviComponent, Event.VIEW_CREATED)
            .map { arguments.getString(REFILL_ID, "") }
            .flatMap {
                mainPresenter
                    .getRefillObservable(it)
                    .onErrorResumeNext(Function { Observable.just(Refill.empty) })
            }
            .doOnNext { request_refill.isEnabled = it.status == Refill.NOTIFIED }
            .filter { it.status == Refill.NOTIFIED }
            .flatMap { (id) ->
                RxView
                    .clicks(request_refill)
                    .flatMap {
                        mainPresenter
                            .requestRefillObservable(id ?: "")
                            .onErrorResumeNext(Function { Observable.just(false) })
                    }
                    .filter { it }
            }
            .takeUntil(RxNavi.observe(naviComponent, Event.DESTROY_VIEW))
            .subscribe(
                {
                    LogUtils.debug(TAG, "onNext in initRequestRefillButton")
                    request_refill.isEnabled = false
                },
                { LogUtils.error(TAG, "onError in initRequestRefillButton", it) },
                { LogUtils.debug(TAG, "onComplete in initRequestRefillButton") }
            )
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater?.inflate(R.layout.fragment_request_refill, container, false)
    }

    override fun getTitle(): String {
        return "Refill"
    }

}