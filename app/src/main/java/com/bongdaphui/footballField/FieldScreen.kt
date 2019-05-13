package com.bongdaphui.footballField

import android.arch.persistence.room.Room
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.addField.AddFieldScreen
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.dao.AppDatabase
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import kotlinx.android.synthetic.main.fragment_field.*

class FieldScreen : BaseFragment() {

    private lateinit var database: AppDatabase

    private var fieldListFull: ArrayList<FbFieldModel> = ArrayList()

    private var fieldList: ArrayList<FbFieldModel> = ArrayList()

    private lateinit var fieldAdapter: FieldAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_field, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(false)

        showButtonFilter(true)

        showFooter(true)
    }

    override fun onBindView() {

        initListField()

        getDataFromCache()

//        getData()

//        onClick()

    }

    private fun getDataFromCache() {

        database = Room.databaseBuilder(activity!!, AppDatabase::class.java, Constant().roomData)
            .allowMainThreadQueries()
            .build()

        val daoInterface = database.getItemDAO()

        fieldListFull = daoInterface.getItems() as ArrayList<FbFieldModel>
        fieldList.addAll(fieldListFull)

        if (fieldList.size > 0) {

            initSpinnerFieldBox()

        } else {

            getData()
        }
    }

    private fun initListField() {

        fieldAdapter = FieldAdapter(context, fieldList, object :
            OnItemClickListener<FbFieldModel> {

            override fun onItemClick(item: FbFieldModel, position: Int, type: Int) {

                if (type == Enum.EnumTypeClick.Phone.value) {

                    Utils().openDial(activity!!, "${item.phone}")
                }
            }
        })
        frg_field_rcv.setHasFixedSize(true)
        frg_field_rcv.adapter = fieldAdapter

    }

    private fun getData() {

        showProgress(true)

        BaseRequest().getDataField(object : GetDataListener<FbFieldModel> {
            override fun onSuccess(list: ArrayList<FbFieldModel>) {

                fieldListFull.clear()

                fieldList.clear()

                fieldListFull.addAll(list)

                fieldList.addAll(fieldListFull)

                showProgress(false)

                showNoData(false)

                initSpinnerFieldBox()

                deleteCache()

                saveCache(fieldListFull)

            }

            override fun onFail(message: String) {
                showProgress(false)
                showNoData(true)
            }
        })
    }

    private fun saveCache(fieldListFull: ArrayList<FbFieldModel>) {

        val dataListener = database.getItemDAO()

        for (i in 0 until fieldListFull.size) {

            dataListener.insert(fieldListFull[i])

        }
    }

    private fun deleteCache() {

        val dataListener = database.getItemDAO()
        dataListener.deleteTable()

    }


    private fun initSpinnerFieldBox() {

        frg_field_v_spinner.visibility = View.VISIBLE

        Utils().initSpinnerCity(
            activity!!,
            frg_field_sp_city,
            frg_field_sp_district,
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

                        showNoData(false)

                        fieldAdapter.notifyDataSetChanged()

                    } else {

                        showNoData(true)
                    }
                }
            })
    }

    private fun onClick() {

        frg_field_tv_no_data_fab.setOnClickListener {

            addFragment(AddFieldScreen())

        }
    }

    private fun showNoData(isShow: Boolean) {

        if (isShow) {

            frg_field_tv_no_data.visibility = View.VISIBLE
            frg_field_rcv.visibility = View.GONE

        } else {
            frg_field_tv_no_data.visibility = View.GONE
            frg_field_rcv.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        showProgress(false)

    }
}