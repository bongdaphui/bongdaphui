package com.bongdaphui.field

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
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.dialog.TutorialDialog
import com.bongdaphui.listener.*
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.model.TutorialModel
import com.bongdaphui.utils.*
import com.bongdaphui.utils.Enum
import com.google.firebase.firestore.FirebaseFirestore
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

        initWelcomeDialog()

    }

    private fun initWelcomeDialog() {

        val isSeen =
            activity?.let { SharedPreference(it).getValueBoolien(SharedPreference.KeyName.KEY_TUTORIAL.name, false) }

        if (!isSeen!!) {
            BaseRequest().getTutorial(object : GetDataListener<TutorialModel> {
                override fun onSuccess(item: TutorialModel) {

                }

                override fun onFail(message: String) {
                }

                override fun onSuccess(list: ArrayList<TutorialModel>) {

                    context?.let { TutorialDialog().show(it, list) }

                }
            })
        }
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

        BaseRequest().getDataField(FireBasePath().collectionField, object : GetDataListener<FbFieldModel> {
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

//                        Log.d(Constant().tag, "spinner onSelectCity with idCity: $_idCity - idDistrict : $_idDistrict")

                        val fieldListTemp: ArrayList<FbFieldModel> = ArrayList()

                        for (i in 0 until fieldListFull.size) {

                            if (_idCity == fieldListFull[i].idCity!! && ("0" == _idDistrict || _idDistrict == fieldListFull[i].idDistrict!!)) {

                                fieldListTemp.add(fieldListFull[i])
                            }
                        }

                        fieldList.clear()
                        fieldList.addAll(fieldListTemp)

                        fieldList.sortBy { it.idDistrict }

                        showEmptyView(fieldList.size == 0)

                        fieldAdapter.notifyDataSetChanged()
                    }
                })
        }
    }

    private fun onClick() {

        floatingActionButton.setOnClickListener {

            addFragment(AddFieldScreen.getInstance(object : AddDataListener {
                override fun onSuccess() {

                    //field will be check and insert by admin
//                    fieldListFull.clear()
//                    fieldList.clear()
//                    getDataFromCache()
                }
            }))

//            addFieldRequestToField()
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

    // Linh sẽ thực hiện thủ công

    val listFieldRequest: ArrayList<FbFieldModel> = ArrayList()
    private var step = 0

    private fun addFieldRequestToField() {

        showProgress(true)
        if (listFieldRequest.size > 0) {

            //check field
            BaseRequest().getDataField(FireBasePath().collectionField, object : GetDataListener<FbFieldModel> {
                override fun onSuccess(item: FbFieldModel) {
                }

                override fun onSuccess(list: java.util.ArrayList<FbFieldModel>) {

                    for (i in 0 until list.size) {

                        if (list[i].name == listFieldRequest[step].name || list[i].phone == listFieldRequest[step].phone) {

                            //san nay da ton tai => xoa field request
                            BaseRequest().deleteDocument(
                                FireBasePath().collectionRequestField,
                                listFieldRequest[step].id.toString(),
                                object : DeleteDataDataListener {
                                    override fun onFail(message: String) {
                                        Log.d(Constant().tag, "delete field  request fail : $message")

                                    }

                                    override fun onSuccess() {
                                        Log.d(Constant().tag, "delete field request success")

                                        showProgress(false)

                                    }
                                })

                            break
                        }
                    }

                    //add data from field request to field
                    val db = FirebaseFirestore.getInstance()
                        .document("${FireBasePath().collectionField}/${listFieldRequest[step].id}")

                    db.set(listFieldRequest[step])
                        .addOnSuccessListener {
                            Log.d(Constant().tag, "add field request success")

                            step++

                            showProgress(false)

                            //xoa field request
                            /*BaseRequest().deleteDocument(
                                FireBasePath().collectionRequestField,
                                listFieldRequest[step].id.toString(),
                                object : DeleteDataDataListener {
                                    override fun onFail(message: String) {
                                        Log.d(Constant().tag, "delete field  request after add fail : $message")

                                    }

                                    override fun onSuccess() {
                                        Log.d(Constant().tag, "delete field request after add success")

                                        showProgress(false)

                                    }
                                })*/
                        }
                        .addOnFailureListener {

                            Log.d(Constant().tag, "upload field fail : $it")
                        }
                }

                override fun onFail(message: String) {
                    Log.d(Constant().tag, "check field fail : $message")

                }
            })

        } else {

            BaseRequest().getDataField(FireBasePath().collectionRequestField, object : GetDataListener<FbFieldModel> {
                override fun onSuccess(item: FbFieldModel) {

                }

                override fun onSuccess(list: ArrayList<FbFieldModel>) {
                    showProgress(false)

                    listFieldRequest.addAll(list)

                    AlertDialog().showCustomDialog(
                        activity!!,
                        "Get list field request succuess",
                        "size list: ${listFieldRequest.size}",
                        "",
                        "OK",
                        object :
                            AcceptListener {
                            override fun onAccept(message: String) {
                            }
                        })
                }

                override fun onFail(message: String) {
                    Log.d(Constant().tag, "get list field request fail : $message")

                }
            })
        }
    }
}