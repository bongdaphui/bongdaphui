package com.bongdaphui.footballField

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.bongdaphui.R
import com.bongdaphui.addField.AddFieldScreen
import com.bongdaphui.addField.SpinnerAdapter
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import kotlinx.android.synthetic.main.fragment_field.*


class FieldScreen : BaseFragment() {

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

        getData()

//        onClick()

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

        if (getListField().size > 0) {

            initSpinnerFieldBox()

        } else {

            showProgress(true)

            BaseRequest().getDataField(object : GetDataListener<FbFieldModel> {
                override fun onSuccess(list: ArrayList<FbFieldModel>) {

                    fieldList.addAll(list)

                    setListField(list)

                    showProgress(false)

                    showNoData(false)

                    initSpinnerFieldBox()

                }

                override fun onFail(message: String) {
                    showProgress(false)
                    showNoData(true)
                }
            })
        }
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

                    for (i in 0 until getListField().size) {

                        if (_idCity == getListField()[i].idCity!! && _idDistrict == getListField()[i].idDistrict!!) {

                            fieldListTemp.add(getListField()[i])
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