package com.mariniana.fuelinventorymanagement.main.composer

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.mariniana.fuelinventorymanagement.MyApplication
import com.mariniana.fuelinventorymanagement.R
import com.mariniana.fuelinventorymanagement.login.composer.LoginActivity
import com.mariniana.fuelinventorymanagement.login.model.User
import com.mariniana.fuelinventorymanagement.main.presenter.MainPresenter
import com.mariniana.fuelinventorymanagement.utils.LogUtils
import com.trello.navi2.Event
import com.trello.navi2.NaviComponent
import com.trello.navi2.component.support.NaviAppCompatActivity
import com.trello.navi2.rx.RxNavi
import io.reactivex.subjects.PublishSubject

class MainActivity : NaviAppCompatActivity() {

    private val tag: String = MainActivity::class.java.simpleName
    private val naviComponent: NaviComponent = this
    private val mainPresenter: MainPresenter by lazy {
        MyApplication.fimComponent.provideMainPresenter()
    }
    private val logoutPubSub by lazy { PublishSubject.create<Boolean>() }

    init {
        initLayout()
        initLogout()
    }

    private fun initLayout() {
        RxNavi
            .observe(naviComponent, Event.CREATE)
            .doOnNext { setContentView(R.layout.activity_main) }
            .flatMap { mainPresenter.getCurrentUserRoleObservable() }
            .takeUntil(RxNavi.observe(naviComponent, Event.DESTROY))
            .subscribe(
                {
                    LogUtils.debug(tag, "onNext in initLayout")
                    if (it == User.ROLE_SELLER) {
                        showSellerFragment()
                        title = "SPBU User"
                    }

                    if (it == User.ROLE_SUPPLIER) {
                        showSupplierFragment()
                        title = "Pertamina User"
                    }
                },
                { LogUtils.error(tag, "onError in initLayout", it) },
                { LogUtils.debug(tag, "onComplete in initLayout") }
            )
    }

    private fun showSellerFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SellerFragment.getInstance(""))

            .commitAllowingStateLoss()
    }

    private fun showSupplierFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SupplierFragment.getInstance(""))
            .commitAllowingStateLoss()
    }

    private fun initLogout() {
        RxNavi
            .observe(naviComponent, Event.CREATE)
            .flatMap { logoutPubSub }
            .flatMap { mainPresenter.logoutObservable() }
            .subscribe(
                {
                    LogUtils.debug(tag, "onNext in initLogout")
                    startLoginActivity()
                },
                { LogUtils.error(tag, "onError in initLogout", it) },
                { LogUtils.debug(tag, "onComplete in initLogout") }
            )
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_logout) {
            logoutPubSub.onNext(true)
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

}
