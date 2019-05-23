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
import com.bongdaphui.profile.ProfileScreen
import com.bongdaphui.utils.IntentExtraName
import kotlinx.android.synthetic.main.fragment_approve_join_club.*
import kotlinx.android.synthetic.main.frg_schedule.*
import kotlinx.android.synthetic.main.view_empty.*

/**
 * Created by ChuTien on ${1/25/2017}.
 */
class ApproveJoinClubScreen : BaseFragment() {
    private val listPlayer: ArrayList<PlayerApprove> = ArrayList()
    private var userModel: UserModel? = null
    private var mAdapter: ApproveAdapterPlayer? = null
    override fun onBindView() {
        initView()
        initData()
    }

    private fun initData() {
        listPlayer.clear()
        BaseRequest().getListApprovePlayer(getUIDUser(), object : GetDataListener<ApprovePlayerResponse> {
            override fun onSuccess(list: ArrayList<ApprovePlayerResponse>) {

                val listClub = arrayListOf<String>()
                list.sortBy { it.idClub }
                for (approvePlayerResponse in list) {
                    var section: PlayerApprove? = null
                    if (!listClub.contains(approvePlayerResponse.idClub)) {
                        section = PlayerApprove("", "", approvePlayerResponse.nameClub, "", "", true)
                    }
                    section?.let { listPlayer.add(it) }
                    listPlayer.add(
                        PlayerApprove(
                            approvePlayerResponse.idPlayer,
                            approvePlayerResponse.photoPlayer,
                            approvePlayerResponse.namePlayer,
                            approvePlayerResponse.message,
                            approvePlayerResponse.idClub,
                            false
                        )
                    )
                }
                mAdapter?.notifyDataSetChanged()
                showEmptyView(listPlayer.size == 0)
            }

            override fun onSuccess(item: ApprovePlayerResponse) {

            }

            override fun onFail(message: String) {
                showEmptyView(true)
            }

        })

    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        userModel = getDatabase().getUserDAO().getItemById(getUIDUser())
        recycler_approve_player.setHasFixedSize(true)
        recycler_approve_player.setItemViewCacheSize(20)

        mAdapter = context?.let {
            ApproveAdapterPlayer(it, listPlayer, object : OnItemClickListener<PlayerApprove> {
                override fun onItemClick(item: PlayerApprove, position: Int, type: Int) {
                    val profileFragment = ProfileScreen.getInstance(item.id, true)
                    val bundle = Bundle()
                    bundle.putString(IntentExtraName.ID_CLUB, item.idClub)
                    profileFragment.arguments = bundle
                    addFragment(profileFragment)
                }

            })
        }
        mAdapter?.setHasStableIds(true)
        recycler_approve_player.adapter = mAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_approve_join_club, container, false)

    }

    private fun showEmptyView(isShow: Boolean) {

        recycler_approve_player.visibility = if (isShow) View.GONE else View.VISIBLE

        view_empty.visibility = if (isShow) View.VISIBLE else View.GONE

    }
}