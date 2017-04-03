package com.mariniana.fuelinventorymanagement.main.composer

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by elsennovraditya on 4/3/17.
 */
class SellerPagerAdapter(fragmentManager: FragmentManager,
                         private val fragmentList: MutableList<SellerContentFragment>) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragmentList[position].getTitle()
    }

}