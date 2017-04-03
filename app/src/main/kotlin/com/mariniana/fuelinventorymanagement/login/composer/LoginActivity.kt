package com.mariniana.fuelinventorymanagement.login.composer

import android.content.Intent
import com.jakewharton.rxbinding2.view.RxView
import com.mariniana.fuelinventorymanagement.MyApplication
import com.mariniana.fuelinventorymanagement.R
import com.mariniana.fuelinventorymanagement.login.model.User
import com.mariniana.fuelinventorymanagement.login.presenter.LoginPresenter
import com.mariniana.fuelinventorymanagement.main.composer.MainActivity
import com.mariniana.fuelinventorymanagement.utils.LogUtils
import com.trello.navi2.Event
import com.trello.navi2.component.support.NaviAppCompatActivity
import com.trello.navi2.rx.RxNavi
import io.reactivex.Observable
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by elsennovraditya on 4/2/17.
 */
class LoginActivity : NaviAppCompatActivity() {

    private val tag = LoginActivity::class.java.simpleName
    private val naviComponent = this

    private val loginPresenter: LoginPresenter by lazy {
        MyApplication.fimComponent.provideLoginPresenter()
    }

    init {
        initLayout()
        initLogin()
    }

    private fun initLayout() {
        RxNavi
            .observe(naviComponent, Event.CREATE)
            .map { loginPresenter.isLoggedIn() }
            .doOnNext { if (it) startMainActivity() }
            .filter { !it }
            .doOnNext { setContentView(R.layout.activity_login) }
            .takeUntil(RxNavi.observe(naviComponent, Event.DESTROY))
            .subscribe(
                { LogUtils.debug(tag, "onNext in initLayout") },
                { LogUtils.error(tag, "onError in initLayout", it) },
                { LogUtils.debug(tag, "onComplete in initLayout") }
            )
    }

    private fun initLogin() {
        RxNavi
            .observe(naviComponent, Event.CREATE)
            .map { loginPresenter.isLoggedIn() }
            .filter { !it }
            .flatMap { RxView.clicks(login) }
            .filter { email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty() }
            .flatMap {
                loginPresenter
                    .loginObservable(
                        email.text.toString(),
                        password.text.toString()
                    )
                    .onErrorResumeNext(Function {
                        Observable.just(User.empty)
                    })
            }
            .filter { !it.isEmpty() }
            .flatMap {
                loginPresenter
                    .registerFcmObservable()
                    .onErrorResumeNext(Function { Observable.just(false) })
            }
            .filter { it }
            .takeUntil(RxNavi.observe(naviComponent, Event.DESTROY))
            .subscribe(
                {
                    LogUtils.debug(tag, "onNext in initLogin $it")
                    startMainActivity()
                },
                { LogUtils.error(tag, "onError in initLogin", it) },
                { LogUtils.debug(tag, "onComplete in initLogin") }
            )
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

}