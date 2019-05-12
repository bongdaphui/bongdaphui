package com.bongdaphui.footballField

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bongdaphui.R
import com.bongdaphui.addField.AddFieldScreen
import com.bongdaphui.addField.SpinnerAdapter
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.FireBaseSuccessListener
import com.bongdaphui.listener.ItemClickInterface
import com.bongdaphui.model.DistrictModel
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.fragment_football_field.*
import kotlinx.android.synthetic.main.view_bottom_filter.*


class FieldScreen : BaseFragment() {

    private var fbFieldList: ArrayList<FbFieldModel> = ArrayList()

    private var fieldAdapter: FieldAdapter? = null

    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    private var listCityName: ArrayList<String> = ArrayList()

    private lateinit var districtAdapter: SpinnerAdapter

    private var listDistrictModel: ArrayList<DistrictModel> = ArrayList()

    private var listDistrictString: ArrayList<String> = ArrayList()

    private var idCity: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_football_field, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        showButtonBack(false)

        showTitle(false)

        showFilter(true)

        showButtonFilter(true)

        showFooter(true)
    }

    override fun onBindView() {

        loadListField()

        intBottomSheet()

//        initViewFilter()

        onClick()

        initListField()

        Log.d(Constant().TAG, "${Utils().getListCity(activity!!).size}")

    }

    private fun initListField() {

        fieldAdapter = FieldAdapter(context, fbFieldList, object :
            ItemClickInterface<FbFieldModel> {

            override fun OncItemlick(item: FbFieldModel, position: Int, type: Int) {

                if (type == Enum.EnumTypeClick.Phone.value) {

                    Utils().openDial(activity!!, "${item.phone}")
                }
            }
        })

        fragment_football_field_rcv_field.adapter = fieldAdapter

    }

    private fun loadListField() {

        if (getListField().size > 0) {

            initSpinnerFieldBox()

        } else {

            BaseRequest().loadData(activity!!, Constant().DATABASE_FIELD, object :
                FireBaseSuccessListener {
                override fun onSuccess(data: DataSnapshot) {

                    if (data.exists()) {

                        fbFieldList.clear()

                        for (i in data.children) {

                            val fieldModel = i.getValue<FbFieldModel>(FbFieldModel::class.java)

                            fbFieldList.add(fieldModel!!)

                        }
                        Log.d(Constant().TAG, "field size: ${fbFieldList.size}")

                        //set list field to global
                        val fieldFull: ArrayList<FbFieldModel> = ArrayList()
                        fieldFull.addAll(fbFieldList)
                        setListField(fieldFull)

                        initSpinnerFieldBox()

                    }
                }

                override fun onFail(message: String) {

                    Log.d(Constant().TAG, "firebase field fail, message: $message")
                }
            })
        }
    }

    private fun initSpinnerFieldBox() {

        initSpinnerBox(object : SpinnerSelectInterface {

            override fun onSelect(cityId: String) {

                idCity = cityId

                loadListField(idCity)
            }
        })
    }

    private fun loadListField(idCity: String) {

        listDistrictModel.clear()

        listDistrictString.clear()

        for (i in 0 until getListCity().size) {

            if (idCity == getListCity()[i].id) {

                for (j in 0 until getListCity()[i].districts!!.size) {

                    listDistrictModel.add(getListCity()[i].districts!![j])

                    listDistrictString.add(getListCity()[i].districts!![j].name!!)
                }
                break
            }
        }
        districtAdapter.notifyDataSetChanged()

        Log.d(Constant().TAG, "list field full: ${getListField().size}")

        //load list field follow city

        starFilter(idCity, "")

    }

    private fun onClick() {

        view_bottom_filter_tv_filter.setOnClickListener {

            val idDistrict = Utils().getIdDistrictFromNameDistrict(
                idCity,
                view_bottom_filter_sp_district.selectedItem.toString(),
                getListCity()
            )

            starFilter(idCity, idDistrict)

        }

        fragment_football_field_fab.setOnClickListener {

            addFragment(AddFieldScreen())

        }

        view_bottom_filter_tv_full.setOnClickListener {

            starFilter(idCity, "")

            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }


    private fun intBottomSheet() {

        sheetBehavior = BottomSheetBehavior.from<LinearLayout>(view_bottom_filter)

        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        districtAdapter = SpinnerAdapter(activity!!, R.layout.item_spinner, listDistrictString)

        view_bottom_filter_sp_district.adapter = districtAdapter
    }

    fun openFilter() {

        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

    }

    private fun starFilter(idCity: String, idDistrict: String) {

        fbFieldList.clear()

        for (i in 0 until getListField().size) {

            //no filter follow district
            if (Utils().isEmpty(idDistrict)) {

                if (idCity == getListField()[i].idCity) {

                    val fbFieldModel = getListField()[i]

                    fbFieldList.add(fbFieldModel)
                }

                // filter follow district
            } else {

                if (idCity == getListField()[i].idCity && idDistrict == getListField()[i].idDistrict) {

                    val fbFieldModel = getListField()[i]

                    fbFieldList.add(fbFieldModel)
                }
            }
        }

        Log.d(Constant().TAG, "size field after filter : ${fbFieldList.size}")

        if (fbFieldList.size > 0) {

            showNoData(false)

            fieldAdapter!!.notifyDataSetChanged()

        } else {

            showNoData(true)
        }

        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

    }

    private fun showNoData(isShow: Boolean) {

        if (isShow) {

            fragment_football_field_tv_no_data.visibility = View.VISIBLE
            fragment_football_field_rcv_field.visibility = View.GONE

        } else {
            fragment_football_field_tv_no_data.visibility = View.GONE
            fragment_football_field_rcv_field.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        showProgress(false)

        showButtonFilter(false)

        showTitle(true)

        showFilter(false)

    }
}