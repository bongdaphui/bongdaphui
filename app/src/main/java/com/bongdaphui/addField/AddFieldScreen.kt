package com.bongdaphui.addField

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.listener.ConfirmListener
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.utils.*
import com.bongdaphui.utils.Enum
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_add_field.*
import java.io.File
import java.util.*


class AddFieldScreen : BaseFragment() {

    private var idCity: String = ""
    private var idDistrict: String = ""

    private var filePathUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_field, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        showButtonBack(true)

        showTitle(true)

        setTitle(activity!!.resources.getString(R.string.add_football_field))

        showFooter(false)

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindView() {

        initSpinner()

        onClick()

        initView()
    }

    private fun initSpinner() {
        Utils().initSpinnerCity(
            activity!!,
            frg_add_field_sp_city,
            frg_add_field_sp_district,
            object :
                BaseSpinnerSelectInterface {
                override fun onSelectCity(_idCity: String, _idDistrict: String) {

                    idCity = _idCity

                    idDistrict = _idDistrict

                    Log.d(Constant().TAG, "spinner onSelectCity with idCity: $idCity - idDistrict : $idDistrict")

                }
            })

        hideKeyBoard()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun onClick() {

        frg_add_field_container.setOnTouchListener { _, _ ->

            hideKeyBoard()

            false
        }

        frg_add_field_tv_input.setOnClickListener {

            checkFieldAndGetIdForField()
        }
    }

    private fun checkFieldAndGetIdForField() {

        if (validate()) {

            enableItem(false)

            showProgress(true)

            BaseRequest().getDataField(object : GetDataListener<FbFieldModel> {
                override fun onSuccess(item: FbFieldModel) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onSuccess(list: ArrayList<FbFieldModel>) {

                    for (i in 0 until list.size) {

                        if (list[i].name == frg_add_field_et_name.text.toString() || list[i].phone == frg_add_field_et_phone.text.toString()) {

                            showAlertFieldIsAvailable()

                            break
                        }
                    }
                    startInsertField()
                }

                override fun onFail(message: String) {

                    showAlertAddFail()
                }
            })
        }
    }

    private fun validate(): Boolean {

        var validate = true

        if (frg_add_field_et_name.text.toString().isEmpty()) {
            frg_add_field_tv_error_input_name.visibility = View.VISIBLE
            frg_add_field_et_name.requestFocus()
            validate = false
        }

        if (frg_add_field_et_phone.text.toString().isEmpty()) {
            frg_add_field_tv_error_input_phone.visibility = View.VISIBLE
            frg_add_field_tv_error_input_phone.text =
                activity!!.getString(R.string.please_enter_your_phone)
            frg_add_field_et_phone.requestFocus()
            validate = false
        }

        if (!Utils().validatePhoneNumber(frg_add_field_et_phone.text.toString())) {
            frg_add_field_tv_error_input_phone.visibility = View.VISIBLE
            frg_add_field_tv_error_input_phone.text =
                activity!!.getString(R.string.please_enter_your_phone_valid)
            frg_add_field_et_phone.requestFocus()

            validate = false
        }

        if (frg_add_field_et_address.text.toString().isEmpty()) {
            frg_add_field_tv_error_input_address.visibility = View.VISIBLE
            frg_add_field_et_address.requestFocus()
            validate = false
        }

        return validate
    }

    private fun enableItem(isDisable: Boolean) {

        frg_add_field_et_name.isEnabled = isDisable
        frg_add_field_et_phone.isEnabled = isDisable
        frg_add_field_et_address.isEnabled = isDisable
        frg_add_field_sp_district.isEnabled = isDisable
        frg_add_field_sp_city.isEnabled = isDisable
        frg_add_field_et_count_field.isEnabled = isDisable
        frg_add_field_et_price_field.isEnabled = isDisable

    }

    private fun startInsertField() {

        //no image
        if (null == filePathUri) {

            setData("")

            //has image
        } else {

            // Assign FirebaseStorage instance to storageReference.
            val storageReference = FirebaseStorage.getInstance().reference

            // Creating second StorageReference.
            val storageReference2nd: StorageReference =
                storageReference.child(
                    Constant().pathStorageField + System.currentTimeMillis() + "." + Utils().getFileExtension(
                        activity,
                        filePathUri!!
                    )
                )

            val selectedFilePath = FilePath.getPath(activity!!, filePathUri!!)

            val file = CompressFile.getCompressedImageFile(File(selectedFilePath), activity!!)

            storageReference2nd.putFile(Uri.fromFile(file))

                .addOnSuccessListener {

                    storageReference2nd.downloadUrl.addOnSuccessListener {

                        setData("$it")
                    }
                }
                // If something goes wrong .
                .addOnFailureListener {

                    showAlertAddFail()

                }

                // On progress change upload time.
                .addOnProgressListener {

                    //                    frg_add_field_progress.visibility = View.GONE
                    frg_add_field_progress.progress = Utils().progressTask(it)
                }
        }
    }

    private fun setData(uriPhoto: String) {

        val name = frg_add_field_et_name.text.toString()
        val phone = frg_add_field_et_phone.text.toString()
        val address =
            frg_add_field_et_address.text.toString() +
                    ", " + frg_add_field_sp_district.selectedItem.toString() +
                    ", " + frg_add_field_sp_city.selectedItem.toString()

        val amountField = frg_add_field_et_count_field.text.toString()
        val priceField = frg_add_field_et_price_field.text.toString()

        val idField = Calendar.getInstance().timeInMillis

        val fieldModel = FbFieldModel(
            idField,
            idCity,
            idDistrict,
            uriPhoto,
            name,
            phone,
            address,
            amountField,
            priceField, "", "", "", ""
        )

        val db = FirebaseFirestore.getInstance().document("${Constant().collectionPathField}/$idField")

        db.set(fieldModel)
            .addOnSuccessListener {

                Log.d(Constant().TAG, "upload field success")

                //cache data

                getDatabase().getFieldDAO().insert(fieldModel)

                showAlertAddSuccess()
            }
            .addOnFailureListener {

                Log.d(Constant().TAG, "upload field fail : $it")

                showAlertAddFail()
            }
    }

    private fun showAlertFieldIsAvailable() {

        showProgress(false)

        enableItem(true)

        AlertDialog().showDialog(activity!!, Enum.EnumConfirmYes.FieldIsAvailable.value, object : ConfirmListener {
            override fun onConfirm(id: Int) {

            }
        })
    }

    private fun showAlertAddFail() {

        showProgress(false)

        enableItem(true)

        AlertDialog().showDialog(activity!!, Enum.EnumConfirmYes.AddFieldFail.value, object : ConfirmListener {
            override fun onConfirm(id: Int) {

            }
        })
    }

    private fun showAlertAddSuccess() {

        showProgress(false)

        AlertDialog().showDialog(activity!!, Enum.EnumConfirmYes.AddFieldSuccess.value, object : ConfirmListener {
            override fun onConfirm(id: Int) {

                enableItem(true)

                frg_add_field_et_name.text.clear()
                frg_add_field_et_phone.text.clear()
                frg_add_field_et_address.text.clear()
                frg_add_field_et_count_field.text.clear()
                frg_add_field_et_price_field.text.clear()
            }
        })
    }

    private fun initView() {

        frg_add_field_v_photo.setOnClickListener {
            checkPermissionStore()
        }

        Utils().editTextTextChange(
            frg_add_field_et_name,
            frg_add_field_iv_clear_input_name,
            frg_add_field_tv_error_input_name
        )

        Utils().editTextTextChange(
            frg_add_field_et_phone,
            frg_add_field_iv_clear_input_phone,
            frg_add_field_tv_error_input_phone
        )

        Utils().editTextTextChange(
            frg_add_field_et_address,
            frg_add_field_iv_clear_input_address,
            frg_add_field_tv_error_input_address
        )

        Utils().editTextTextChange(
            frg_add_field_et_count_field,
            frg_add_field_iv_clear_input_count_field,
            frg_add_field_tv_error_input_count_field
        )

        Utils().editTextTextChange(
            frg_add_field_et_price_field,
            frg_add_field_iv_clear_input_price_field,
            frg_add_field_tv_error_input_price
        )
    }

    private fun checkPermissionStore() {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                Constant().CAMERA_WRITE_EXTERNAL_PERMISSIONS,
                RequestCode().readExternalStorage
            )
        } else if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RequestCode().pickPhoto)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RequestCode().pickPhoto && data != null) {

            filePathUri = data.data

            if (null != filePathUri) {

                Glide.with(activity!!).load(filePathUri).into(frg_add_field_iv_photo)

                frg_add_field_iv_camera.visibility = View.GONE

                frg_add_field_tv_error_select_photo.visibility = View.INVISIBLE

            }
        }
    }
}