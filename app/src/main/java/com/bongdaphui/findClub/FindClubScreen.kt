package com.bongdaphui.findClub

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.addClub.AddClubScreen
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.clubInfo.ClubInfoScreen
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.footballClub.ClubAdapter
import com.bongdaphui.listener.ConfirmListener
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.model.ClubModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import kotlinx.android.synthetic.main.fragment_club.*


class FindClubScreen : BaseFragment() {

    private var listClubModel: ArrayList<ClubModel> = ArrayList()

    var adapterClub: ClubAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_club, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        setTitle(activity!!.resources.getString(R.string.list_fc))

        showButtonBack(false)

        showFooter(true)

        loadListClub()

    }

    override fun onBindView() {

        //get token FCM
        /*FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(Constant().TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                Log.d(Constant().TAG, "token $token")
            })*/

        initAdapter()
//
        loadListClub()

        onClick()

    }

    private fun initAdapter() {

        adapterClub = ClubAdapter(context, listClubModel, object :

            OnItemClickListener<ClubModel> {

            override fun onItemClick(item: ClubModel, position: Int, type: Int) {

                if (type == Enum.EnumTypeClick.Phone.value) {

                    if (item.phone!!.isNotEmpty())

                        Utils().openDial(activity!!, "${item.phone}")

                } else if (type == Enum.EnumTypeClick.View.value) {

                    addFragment(ClubInfoScreen.getInstance(item))
                }
            }
        })
        adapterClub!!.setHasStableIds(true)
        frg_football_club_rcv.adapter = adapterClub
        frg_football_club_rcv.setHasFixedSize(true)
        frg_football_club_rcv.setItemViewCacheSize(20)
    }

    private fun loadListClub() {

        showProgress(true)

        BaseRequest().getClubs(object : GetDataListener<ClubModel>{
            override fun onSuccess(list: ArrayList<ClubModel>) {
                listClubModel.clear()
                listClubModel.addAll(list)
                Log.d(Constant().TAG, "club size: ${listClubModel.size}")
                setListClub(listClubModel)
                adapterClub!!.notifyDataSetChanged()

                showProgress(false)
            }

            override fun onSuccess(item: ClubModel) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFail(message: String) {

                showProgress(false)

                Log.d(Constant().TAG, "firebase field fail, message: $message")
            }
        })
    }


    private fun onClick() {

        frg_football_club_fab.setOnClickListener {

            val uIdUser = getUIDUser()
            if (TextUtils.isEmpty(uIdUser)) {
                //non account
                AlertDialog().showDialog(
                    activity!!,
                    Enum.EnumConfirmYes.FeatureNeedLogin.value,
                    object : ConfirmListener {
                        override fun onConfirm(id: Int) {
                            if (id == Enum.EnumConfirmYes.FeatureNeedLogin.value) {
                                addFragment(LoginScreen())
                            }
                        }
                    })
            } else {
                //lấy id cuối cùng của club cuối cùng để cộng thêm 1 khi thêm club
                val listClubs = getListClub()
                if (listClubs.size > 0) {
                    addFragment(AddClubScreen.getInstance(listClubs[listClubs.size - 1].id!!.toInt()))
                } else {
                    addFragment(AddClubScreen.getInstance(0))
                }
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()

        showProgress(false)
    }

}