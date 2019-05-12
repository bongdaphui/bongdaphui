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
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.CommentModel
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_field.*


class FieldScreen : BaseFragment() {

    private var fieldListFull: ArrayList<FbFieldModel> = ArrayList()

    private var fieldList: ArrayList<FbFieldModel> = ArrayList()

    private lateinit var fieldAdapter: FieldAdapter

    private lateinit var spAdapter: SpinnerAdapter


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

//        loadListField()

//        intBottomSheet()

//        initViewFilter()

//        onClick()

    }

    /*fun getListOfPlaces() : ArrayList<FbFieldModel> {
        Log.d("TAG", "Before attaching the listener!");
        val fields = ArrayList<FbFieldModel>()
        val db = FirebaseFirestore.getInstance().collection("fields")

        db.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "Inside onComplete function!")

                for (document in task.result) {
                    val fieldModel = document.data
                    fields.add(fieldModel)
                }
            }
        }
        Log.d("TAG", "After attaching the listener!");
        return fields
    }*/

    private fun getData() {
        showProgress(true)
        val db = FirebaseFirestore.getInstance().collection("fields")

        db.orderBy("name").get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val fbFieldModel = FbFieldModel(
                        document.data["id"] as Long?,
                        document.data["idCity"] as String?,
                        document.data["idDistrict"] as String?,
                        document.data["photoUrl"] as String?,
                        document.data["name"] as String?,
                        document.data["phone"] as String?,
                        document.data["address"] as String?,
                        document.data["amountField"] as String?,
                        document.data["price"] as String?,
                        document.data["lat"] as String?,
                        document.data["lng"] as String?,
                        document.data["countRating"] as String?,
                        document.data["rating"] as String?,
                        document.data["comment"] as ArrayList<CommentModel>?
                    )

                    fieldListFull.add(fbFieldModel)
                    fieldList.add(fbFieldModel)

//                    Log.d(Constant().TAG, "${document.id} => ${document.data}")
                }

                Log.d(Constant().TAG, "field size: ${fieldList.size}")

                if (fieldList.size > 0) {

                    fieldAdapter.notifyDataSetChanged()

                    showProgress(false)

                    showNoData(false)

                    initSpinnerFieldBox()

                } else {
                    showProgress(false)
                    showNoData(true)
                }

            }
            .addOnFailureListener { exception ->
                Log.d(Constant().TAG, "Error getting documents: ", exception)
                showProgress(false)
                showNoData(true)

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

    private fun initSpinnerFieldBox() {

        val listCity = Utils().getListCity(activity!!)

        val listCityName = ArrayList<String>()

        for (i in 0 until listCity.size) {

            val cityModel = listCity[i]

            listCityName.add(cityModel.name!!)
        }
        spAdapter = SpinnerAdapter(activity!!, R.layout.item_spinner, listCityName)

        frg_field_v_spinner.visibility = View.VISIBLE

        frg_field_sp_city.adapter = spAdapter

        frg_field_sp_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                val nameCity: String = parent.getItemAtPosition(position) as String

                val idCity = Utils().getIdCityFromNameCity(nameCity, listCity)

                val fieldListTemp: ArrayList<FbFieldModel> = ArrayList()

                for (i in 0 until fieldListFull.size) {

                    if (idCity == fieldListFull[i].idCity!!) {

                        fieldListTemp.add(fieldListFull[i])
                    }
                }
                Log.d(Constant().TAG, "fieldListTemp size: ${fieldListTemp.size}")

                fieldList.clear()
                fieldList.addAll(fieldListTemp)
                Log.d(Constant().TAG, "fieldList sort size: ${fieldList.size}")

                if (fieldList.size > 0) {

                    showNoData(false)

                    spAdapter.notifyDataSetChanged()

                } else {

                    showNoData(true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(Constant().TAG, "onNothingSelected")

            }
        }
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