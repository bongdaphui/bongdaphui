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
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.AcceptListener
import com.bongdaphui.listener.AddDataListener
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.utils.*
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.frg_add_field.*
import java.io.File
import java.util.*


class AddFieldScreen : BaseFragment() {

    private var idCity: String = ""
    private var idDistrict: String = ""

    private var filePathUri: Uri? = null

    companion object {

        private var addDataListener: AddDataListener? = null

        fun getInstance(addDataListener: AddDataListener): AddFieldScreen {

            this.addDataListener = addDataListener

            return AddFieldScreen()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_add_field, container, false)

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
            frg_add_field_sp_city, 0,
            frg_add_field_sp_district, 0,
            object :
                BaseSpinnerSelectInterface {
                override fun onSelectCity(_idCity: String, _idDistrict: String) {

                    idCity = _idCity
                    idDistrict = _idDistrict
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

            startInsertField()
        }
    }

    private fun validate(): Boolean {

        var validate = true

        if (TextUtils.isEmpty(frg_add_field_et_name.text.toString())) {
            frg_add_field_tv_error_input_name.visibility = View.VISIBLE
            frg_add_field_et_name.requestFocus()
            validate = false
        }

        if (TextUtils.isEmpty(frg_add_field_et_phone_1.text.toString()) && TextUtils.isEmpty(frg_add_field_et_phone_2.text.toString())) {
            frg_add_field_tv_error_input_phone_1.visibility = View.VISIBLE
            frg_add_field_tv_error_input_phone_1.text =
                activity!!.getString(R.string.please_enter_phone_field)
            frg_add_field_et_phone_1.requestFocus()
            validate = false
        }

        if (!TextUtils.isEmpty(frg_add_field_et_phone_1.text.toString()) && !Utils().validatePhoneNumber(
                frg_add_field_et_phone_1.text.toString()
            )
        ) {
            frg_add_field_tv_error_input_phone_1.visibility = View.VISIBLE
            frg_add_field_tv_error_input_phone_1.text =
                activity!!.getString(R.string.please_enter_your_phone_valid)
            frg_add_field_et_phone_1.requestFocus()

            validate = false
        }

        if (!TextUtils.isEmpty(frg_add_field_et_phone_2.text.toString()) && !Utils().validatePhoneNumber(
                frg_add_field_et_phone_2.text.toString()
            )
        ) {
            frg_add_field_tv_error_input_phone_2.visibility = View.VISIBLE
            frg_add_field_tv_error_input_phone_2.text =
                activity!!.getString(R.string.please_enter_your_phone_valid)
            frg_add_field_et_phone_2.requestFocus()

            validate = false
        }

        if (TextUtils.isEmpty(frg_add_field_et_address.text.toString())) {
            frg_add_field_tv_error_input_address.visibility = View.VISIBLE
            frg_add_field_et_address.requestFocus()
            validate = false
        }

        if (activity?.let { Utils().isNoDistrict(idDistrict, false, it) }!!) {
            frg_add_field_tv_error_input_city_district.visibility = View.VISIBLE
            validate = false
        }

        return validate
    }

    private fun enableItem(isDisable: Boolean) {

        frg_add_field_et_name.isEnabled = isDisable
        frg_add_field_et_phone_1.isEnabled = isDisable
        frg_add_field_et_phone_2.isEnabled = isDisable
        frg_add_field_et_address.isEnabled = isDisable
        frg_add_field_sp_district.isEnabled = isDisable
        frg_add_field_sp_city.isEnabled = isDisable
        frg_add_field_et_count_field.isEnabled = isDisable
        frg_add_field_et_price_field_min.isEnabled = isDisable
        frg_add_field_et_price_field_max.isEnabled = isDisable

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
                    FireBasePath().storageField + System.currentTimeMillis() + "." + Utils().getFileExtension(
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

                    showAlertAddField(activity!!.resources.getString(R.string.add_field_fail))
                }

                // On progress change upload time.
                .addOnProgressListener {

                    //frg_add_field_progress.progress = Utils().progressTask(it)
                }
        }
    }

    private fun setData(uriPhoto: String) {

        val name = frg_add_field_et_name.text.toString()
        var phone1 = frg_add_field_et_phone_1.text.toString()
        var phone2 = frg_add_field_et_phone_2.text.toString()

        if (TextUtils.isEmpty(phone1) && !TextUtils.isEmpty(phone2)) {
            phone1 = phone2
            phone2 = ""
        }
        val address =
            frg_add_field_et_address.text.toString() +
                    ", " + frg_add_field_sp_district.selectedItem.toString() +
                    ", " + frg_add_field_sp_city.selectedItem.toString()

        val amountField = frg_add_field_et_count_field.text.toString()
        val priceFieldMin = frg_add_field_et_price_field_min.text.toString()
        val priceFieldMax = frg_add_field_et_price_field_max.text.toString()

        val id = Calendar.getInstance().timeInMillis

        val fieldModel = FbFieldModel(
            id,
            idCity,
            idDistrict,
            uriPhoto,
            name,
            phone1,
            phone2,
            address,
            amountField,
            priceFieldMin,
            priceFieldMax,
            "",
            "",
            "",
            ""
        )

        //add data to field request
        val db =
            FirebaseFirestore.getInstance().document("${FireBasePath().collectionRequestField}/${fieldModel.id}")

        db.set(fieldModel)
            .addOnSuccessListener {

                //field will be check and insert by admin
                //cache data
//                getDatabase().getFieldDAO().insert(fieldModel)
//                addDataListener?.onSuccess()

                showAlertAddField(activity!!.resources.getString(R.string.add_field_success))
            }
            .addOnFailureListener {

                Log.d(Constant().tag, "upload field fail : $it")

                showAlertAddField(activity!!.resources.getString(R.string.add_field_fail))
            }
    }

    private fun showAlertAddField(message: String) {

        showProgress(false)

        enableItem(true)

        activity?.let { it ->
            AlertDialog().showCustomDialog(
                it,
                activity!!.resources.getString(R.string.alert),
                message,
                "",
                activity!!.resources.getString(R.string.close),
                object : AcceptListener {
                    override fun onAccept(message: String) {
                    }
                }
            )
        }
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
            frg_add_field_et_phone_1,
            frg_add_field_iv_clear_input_phone_1,
            frg_add_field_tv_error_input_phone_1
        )

        Utils().editTextTextChange(
            frg_add_field_et_phone_2,
            frg_add_field_iv_clear_input_phone_2,
            frg_add_field_tv_error_input_phone_2
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
            frg_add_field_et_price_field_min,
            frg_add_field_iv_clear_input_price_field_min,
            frg_add_field_tv_error_input_price
        )

        Utils().editTextTextChange(
            frg_add_field_et_price_field_max,
            frg_add_field_iv_clear_input_price_field_max,
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
                Constant().cameraWriteExternalPermission,
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