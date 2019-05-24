package com.bongdaphui.footballField

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.addField.AddFieldScreen
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.AddDataListener
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import kotlinx.android.synthetic.main.frg_field.*
import kotlinx.android.synthetic.main.view_empty.*

class FieldScreen : BaseFragment() {

    private var fieldListFull: ArrayList<FbFieldModel> = ArrayList()

    private var fieldList: ArrayList<FbFieldModel> = ArrayList()

    private lateinit var fieldAdapter: FieldAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_field, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(false)

        showFooter(true)
    }

    override fun onBindView() {

        initListField()

        getDataFromCache()

        refreshData()

        onClick()

    }

    private fun getDataFromCache() {

        fieldListFull = getDatabase().getFieldDAO().getItems() as ArrayList<FbFieldModel>

        fieldList.addAll(fieldListFull)

        if (fieldList.size > 0) {

            initSpinnerFieldBox()

        } else {

            getData()
        }
    }

    private fun initListField() {
        val isLoggedUser = !TextUtils.isEmpty(getUIDUser())
        fieldAdapter = FieldAdapter(context, fieldList, isLoggedUser, object :
            OnItemClickListener<FbFieldModel> {

            override fun onItemClick(item: FbFieldModel, position: Int, type: Int) {

                if (type == Enum.EnumTypeClick.Phone.value) {
                    if (isLoggedUser) {
                        Utils().openDial(activity!!, "${item.phone}")
                    } else {
                        addFragment(LoginScreen())
                    }

                }
            }
        })
        frg_field_rcv.setHasFixedSize(true)
        frg_field_rcv.setItemViewCacheSize(20)
        fieldAdapter.setHasStableIds(true)
        frg_field_rcv.adapter = fieldAdapter

    }

    private fun getData() {

        showProgress(true)

        BaseRequest().getDataField(object : GetDataListener<FbFieldModel> {
            override fun onSuccess(item: FbFieldModel) {
            }

            override fun onSuccess(list: ArrayList<FbFieldModel>) {

                Utils().hiddenRefresh(frg_field_refresh_view)

                fieldListFull.clear()

                fieldList.clear()

                fieldListFull.addAll(list)

                fieldList.addAll(fieldListFull)

                showProgress(false)

                showEmptyView(false)

                initSpinnerFieldBox()

                saveCache(fieldListFull)

            }

            override fun onFail(message: String) {
                Utils().hiddenRefresh(frg_field_refresh_view)
                showProgress(false)
                showEmptyView(true)
            }
        })
    }

    private fun saveCache(fieldListFull: ArrayList<FbFieldModel>) {

//        val dataListener = database.getFieldDAO()

        for (i in 0 until fieldListFull.size) {

            getDatabase().getFieldDAO().insert(fieldListFull[i])

        }
    }

    private fun initSpinnerFieldBox() {

        if (isAdded) {

            frg_field_cv_spinner.visibility = View.VISIBLE

            Utils().initSpinnerCity(
                activity!!,
                frg_field_sp_city, 0,
                frg_field_sp_district, 0,
                object :
                    BaseSpinnerSelectInterface {
                    override fun onSelectCity(_idCity: String, _idDistrict: String) {

                        Log.d(Constant().TAG, "spinner onSelectCity with idCity: $_idCity - idDistrict : $_idDistrict")

                        val fieldListTemp: ArrayList<FbFieldModel> = ArrayList()

                        for (i in 0 until fieldListFull.size) {

                            if (_idCity == fieldListFull[i].idCity!! && _idDistrict == fieldListFull[i].idDistrict!!) {

                                fieldListTemp.add(fieldListFull[i])
                            }
                        }

                        fieldList.clear()
                        fieldList.addAll(fieldListTemp)

                        Log.d(Constant().TAG, "fieldList sort size: ${fieldList.size}")

                        if (fieldList.size > 0) {

                            showEmptyView(false)

                            fieldAdapter.notifyDataSetChanged()

                        } else {

                            showEmptyView(true)
                        }
                    }
                })
        }
    }

    private fun onClick() {

        frg_field_fab.setOnClickListener {

            addFragment(AddFieldScreen.getInstance(object : AddDataListener {
                override fun onSuccess() {

                        //field will be check and insert by admin
//                    fieldListFull.clear()
//                    fieldList.clear()
//                    getDataFromCache()
                }
            }))
        }
    }

    private fun showEmptyView(isShow: Boolean) {

        if (isAdded) {

            view_empty?.visibility = if (isShow) View.VISIBLE else View.GONE

            frg_field_rcv?.visibility = if (isShow) View.GONE else View.VISIBLE

        }
    }

    private fun refreshData() {

        frg_field_refresh_view.setOnRefreshListener {

            frg_field_cv_spinner.visibility = View.GONE

            view_empty.visibility = View.GONE

            frg_field_rcv.visibility = View.GONE

            getData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        showProgress(false)

    }
}