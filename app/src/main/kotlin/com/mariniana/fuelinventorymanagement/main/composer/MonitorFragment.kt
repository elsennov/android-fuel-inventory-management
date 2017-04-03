package com.mariniana.fuelinventorymanagement.main.composer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mariniana.fuelinventorymanagement.MyApplication
import com.mariniana.fuelinventorymanagement.R
import com.mariniana.fuelinventorymanagement.main.presenter.MainPresenter
import com.mariniana.fuelinventorymanagement.utils.LogUtils
import com.trello.navi2.Event
import com.trello.navi2.rx.RxNavi
import io.reactivex.Observable
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.fragment_monitor.*

/**
 * Created by elsennovraditya on 4/3/17.
 */
class MonitorFragment : SellerContentFragment() {

    companion object {
        private val TAG = MonitorFragment::class.java.simpleName
    }

    private val naviComponent = this
    private val mainPresenter: MainPresenter by lazy {
        MyApplication.fimComponent.provideMainPresenter()
    }

    init {
        initCurrentVolume()
    }

    private fun initCurrentVolume() {
        RxNavi
            .observe(naviComponent, Event.VIEW_CREATED)
            .flatMap {
                mainPresenter
                    .getCurrentVolumeObservable()
                    .onErrorResumeNext(Function { Observable.just(0.0) })
            }
            .takeUntil(RxNavi.observe(naviComponent, Event.DESTROY_VIEW))
            .subscribe(
                {
                    LogUtils.debug(TAG, "onNext in initCurrentVolume")
                    current_volume.text = "$it L"
                },
                { LogUtils.error(TAG, "onError in initCurrentVolume", it) },
                { LogUtils.debug(TAG, "onComplete in initCurrentVolume") }
            )
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater?.inflate(R.layout.fragment_monitor, container, false)
    }

    override fun getTitle(): String {
        return "Monitoring"
    }

}