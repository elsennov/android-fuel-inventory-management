package com.mariniana.fuelinventorymanagement.main.composer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mariniana.fuelinventorymanagement.R
import com.trello.navi2.Event
import com.trello.navi2.component.support.NaviFragment
import com.trello.navi2.rx.RxNavi
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_seller.*

/**
 * Created by elsennovraditya on 4/2/17.
 */
class SellerFragment : NaviFragment() {

    companion object {
        private val TAG = SupplierFragment::class.java.simpleName
        const val REFILL_ID = "refill_id"

        fun getInstance(refillId: String): SellerFragment {
            val bundle = Bundle()
            bundle.putString(REFILL_ID, refillId)

            val sellerFragment = SellerFragment()
            sellerFragment.arguments = bundle
            return sellerFragment
        }
    }

    private val naviComponent = this

    init {
        initTabLayout()
    }

    private fun initTabLayout() {
        RxNavi
            .observe(naviComponent, Event.CREATE)
            .observeOn(AndroidSchedulers.mainThread())
            .takeUntil(RxNavi.observe(naviComponent, Event.DESTROY))
            .subscribe { setupTabLayout() }
    }

    private fun setupTabLayout() {
        supplier_tab.setupWithViewPager(supplier_view_pager)
        supplier_view_pager.adapter = SellerPagerAdapter(
            childFragmentManager, getSellerContentFragmentList()
        )
        supplier_view_pager.offscreenPageLimit = 2
    }

    private fun getSellerContentFragmentList(): MutableList<SellerContentFragment> {
        val supplierFragmentList = mutableListOf<SellerContentFragment>()
        supplierFragmentList.add(MonitorFragment())
        supplierFragmentList.add(RequestRefillFragment.getInstance(arguments.getString(REFILL_ID, "")))
        return supplierFragmentList
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater?.inflate(R.layout.fragment_seller, container, false)
    }

}