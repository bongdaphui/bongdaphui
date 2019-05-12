package com.bongdaphui.footballClub

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.addClub.AddClubScreen
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.clubInfo.ClubInfoScreen
import com.bongdaphui.listener.FireBaseSuccessListener
import com.bongdaphui.listener.ItemClickInterface
import com.bongdaphui.model.ClubModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.fragment_club.*


class ClubScreen : BaseFragment() {

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

//        initAdapter()
//
//        loadListClub()

        onClick()

    }

    private fun initAdapter() {

        adapterClub = ClubAdapter(context, listClubModel, object :

            ItemClickInterface<ClubModel> {

            override fun OncItemlick(item: ClubModel, position: Int, type: Int) {

                if (type == Enum.EnumTypeClick.Phone.value) {

                    if (item.phone!!.isNotEmpty())

                        Utils().openDial(activity!!, "${item.phone}")

                } else if (type == Enum.EnumTypeClick.View.value) {

                    addFragment(ClubInfoScreen.getInstance(item))
                }
            }
        })

        frg_football_club_rcv.adapter = adapterClub
    }

    private fun loadListClub() {

        /*if (getListField().size > 0) {

            setData()

        } else {
*/
        BaseRequest().loadData(activity!!, Constant().DATABASE_CLUB, object :
            FireBaseSuccessListener {
            override fun onSuccess(data: DataSnapshot) {

                if (data.exists()) {

                    listClubModel.clear()

                    for (i in data.children) {

                        val club = i.getValue<ClubModel>(ClubModel::class.java)

                        listClubModel.add(club!!)

                    }

                    Log.d(Constant().TAG, "club size: ${listClubModel.size}")

                    //set list field to global
                    val clubFull: ArrayList<ClubModel> = ArrayList()
                    clubFull.addAll(listClubModel)
                    setListClub(clubFull)

                    setData()

                }
            }

            override fun onFail(message: String) {

                Log.d(Constant().TAG, "firebase field fail, message: $message")
            }
        })
//        }
    }

    private fun setData() {

//        listClubModel = getListClub()

        adapterClub!!.notifyDataSetChanged()
    }

    private fun onClick() {

        frg_football_club_fab.setOnClickListener {

            //lấy id cuối cùng của club cuối cùng để cộng thêm 1 khi thêm club
            addFragment(AddClubScreen.getInstance(getListClub()[getListClub().size - 1].id!!.toInt()))

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        showProgress(false)
    }

}