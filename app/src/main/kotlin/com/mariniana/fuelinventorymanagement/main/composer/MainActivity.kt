package com.mariniana.fuelinventorymanagement.main.composer

import com.mariniana.fuelinventorymanagement.MyApplication
import com.mariniana.fuelinventorymanagement.R
import com.mariniana.fuelinventorymanagement.main.presenter.MainPresenter
import com.mariniana.fuelinventorymanagement.utils.LogUtils
import com.trello.navi2.Event
import com.trello.navi2.NaviComponent
import com.trello.navi2.component.support.NaviAppCompatActivity
import com.trello.navi2.rx.RxNavi

class MainActivity : NaviAppCompatActivity() {

    private val tag: String = MainActivity::class.java.simpleName
    private val naviComponent: NaviComponent = this
    private val mainPresenter: MainPresenter by lazy {
        MyApplication.fimComponent.provideMainPresenter()
    }

    init {
        initLayout()
    }

    private fun initLayout() {
        RxNavi
            .observe(naviComponent, Event.CREATE)
            .doOnNext { setContentView(R.layout.activity_main) }
            .takeUntil(RxNavi.observe(naviComponent, Event.DESTROY))
            .subscribe(
                {
                    LogUtils.debug(tag, "onNext in initLayout")
                },
                { LogUtils.error(tag, "onError in initLayout", it) },
                { LogUtils.debug(tag, "onComplete in initLayout") }
            )
    }

}
