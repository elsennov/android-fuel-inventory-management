package com.mariniana.fuelinventorymanagement.main.composer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mariniana.fuelinventorymanagement.R
import com.trello.navi2.component.support.NaviFragment

/**
 * Created by elsennovraditya on 4/2/17.
 */
class SellerFragment : NaviFragment() {

    init {

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater?.inflate(R.layout.fragment_seller, container, false)
    }

}