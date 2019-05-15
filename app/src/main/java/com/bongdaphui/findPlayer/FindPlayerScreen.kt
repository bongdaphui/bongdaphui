package com.bongdaphui.findPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.footballClub.ClubAdapter
import com.bongdaphui.model.ClubModel
import kotlinx.android.synthetic.main.view_empty.*


class FindPlayerScreen : BaseFragment() {

    private var listClubModel: ArrayList<ClubModel> = ArrayList()

    var adapterClub: ClubAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.frg_find_player, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(false)

        showFooter(true)

    }

    override fun onBindView() {

        view_empty.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()

        showProgress(false)
    }

}