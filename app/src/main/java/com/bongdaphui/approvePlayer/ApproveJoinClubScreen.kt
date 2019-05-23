package com.bongdaphui.approvePlayer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.UserModel
import kotlinx.android.synthetic.main.fragment_approve_join_club.*

/**
 * Created by ChuTien on ${1/25/2017}.
 */
class ApproveJoinClubScreen : BaseFragment() {
    private val listPlayer : ArrayList<PlayerApprove> = ArrayList()
    private var userModel: UserModel? = null
    private var mAdapter: ApproveAdapterPlayer? = null
    override fun onBindView() {
        initView()
        initData()
    }

    private fun initData() {
        listPlayer.clear()
        BaseRequest().getListApprovePlayer(getUIDUser(),object:GetDataListener<ApprovePlayerResponse>{
            override fun onSuccess(list: ArrayList<ApprovePlayerResponse>) {

                val listClub = arrayListOf<String>()
                list.sortBy { it.idClub }
                for (approvePlayerResponse in list) {
                    var section:PlayerApprove? = null
                    if (!listClub.contains(approvePlayerResponse.idClub)) {
                        section = PlayerApprove("","",approvePlayerResponse.nameClub,"",true)
                    }
                    section?.let { listPlayer.add(it) }
                    listPlayer.add(PlayerApprove(approvePlayerResponse.idPlayer,approvePlayerResponse.photoPlayer,approvePlayerResponse.namePlayer,approvePlayerResponse.message,false))
                }
                mAdapter?.notifyDataSetChanged()
            }

            override fun onSuccess(item: ApprovePlayerResponse) {

            }

            override fun onFail(message: String) {

            }

        })

    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        userModel = getDatabase().getUserDAO().getItemById(getUIDUser())
        recycler_approve_player.setHasFixedSize(true)
        recycler_approve_player.setItemViewCacheSize(20)

        mAdapter = context?.let {
            ApproveAdapterPlayer(it, listPlayer, object: OnItemClickListener<PlayerApprove>{
                override fun onItemClick(item: PlayerApprove, position: Int, type: Int) {

                }

            })
        }
        mAdapter?.setHasStableIds(true)
        recycler_approve_player.adapter = mAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_approve_join_club, container, false)

    }
}