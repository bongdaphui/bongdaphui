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
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.utils.*
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_add_football_field.*
import java.io.File


class AddFieldScreen : BaseFragment() {

    private var idCity: String = ""
    private var idDistrict: String = ""

    private var filePathUri: Uri? = null

    private var storagePath = "football_field_image/"

    /*companion object {

        const val LIST_CITY_MODEL = "LIST_CITY_MODEL"

        const val LIST_DISTRICT_MODEL = "LIST_DISTRICT_MODEL"

        fun getInstance(

            listCityModel: ArrayList<CityModel>,

            listDistrictModel: ArrayList<DistrictModel>

        ): AddFieldScreen {

            val screen = AddFieldScreen()

            val bundle = Bundle()

            bundle.putSerializable(LIST_CITY_MODEL, listCityModel)

            bundle.putSerializable(LIST_DISTRICT_MODEL, listDistrictModel)

            screen.arguments = bundle

            return screen
        }
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_football_field, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        showButtonBack(true)

        showTitle(true)

        showFilter(false)

        showButtonFilter(false)

        setTitle(activity!!.resources.getString(R.string.add_football_field))

        showFooter(false)

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindView() {

        /*val bundle = arguments

        if (bundle != null) {

            listCityModel = bundle.getSerializable(LIST_CITY_MODEL) as ArrayList<CityModel>

            listDistrictModelFull = bundle.getSerializable(LIST_DISTRICT_MODEL) as ArrayList<DistrictModel>

        }*/

        initSpinner()

        initView()

        onClick()

    }

    private fun initSpinner() {
        Utils().initSpinnerCity(
            activity!!,
            getListCity(),
            frg_add_football_field_sp_city,
            frg_add_football_field_sp_district,
            object :
                BaseSpinnerSelectInterface {
                override fun onSelectCity(cityId: String, districtId: String) {

                    idCity = cityId

                    idDistrict = districtId

                    Log.d(Constant().TAG, "spinner onSelectCity with idCity: $idCity - idDistrict : $idDistrict")

                }
            })

        hideKeyBoard()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun onClick() {

        frg_add_football_field_tv_input.setOnClickListener {

            startInsertData()
        }

        frg_add_football_field_container.setOnTouchListener { _, _ ->

            hideKeyBoard()

            false
        }
    }

    private fun startInsertData() {

        if (validate()) {

            disableItem()

            //no image
            if (null == filePathUri) {

                storageData("")

                //has image
            } else {

                // Assign FirebaseStorage instance to storageReference.
                val storageReference = FirebaseStorage.getInstance().reference

                // Creating second StorageReference.
                val storageReference2nd: StorageReference =
                    storageReference.child(
                        storagePath + System.currentTimeMillis() + "." + Utils().getFileExtension(
                            activity,
                            filePathUri!!
                        )
                    )

                val selectedFilePath = FilePath.getPath(activity!!, filePathUri!!)

                val file = CompressFile.getCompressedImageFile(File(selectedFilePath), activity!!)

                storageReference2nd.putFile(Uri.fromFile(file))

                    .addOnSuccessListener {

                        storageReference2nd.downloadUrl.addOnSuccessListener {

                            storageData("$it")
                        }

                    }
                    // If something goes wrong .
                    .addOnFailureListener {

                        Utils().alertInsertFail(activity)

                    }

                    // On progress change upload time.
                    .addOnProgressListener {

                        frg_add_football_field_progress.visibility = View.VISIBLE
                        frg_add_football_field_progress.progress = Utils().progressTask(it)
                    }
            }
        }
    }

    private fun storageData(uriPhoto: String) {

        Log.d(Constant().TAG, uriPhoto)

        val name = frg_add_football_field_et_name.text.toString()

        val phone = frg_add_football_field_et_phone.text.toString()

        val address =
            frg_add_football_field_et_address.text.toString() +
                    ", " + frg_add_football_field_sp_district.selectedItem.toString() +
                    ", " + frg_add_football_field_sp_city.selectedItem.toString()

        val amountField = frg_add_football_field_et_count_field.text.toString()

        val priceField = frg_add_football_field_et_price_field.text.toString()

        val dataReference = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_CITY)

//        val fbFieldId = dataReference.push().key

        /*val footballFieldModel1 = FbFieldModel(
            fbFieldId,
            "-Ldhdzv3rIqReHz010jj",
            "-LdhhBIAlKbs1tOF3xt0",
            getUIDUser(Constant().KEY_LOGIN_UID_USER),
            uriPhoto,
            "C1",
            "0909060202",
            "Phan Chu Trinh, phường 12, Bình Thạnh, Hồ Chí Minh",
            "5",
            "300000"
        )*/

//        dataReference.child(fbFieldId!!).setValue(footballFieldModel)

        var idOfField = ""

        for (i in 0 until getListCity().size) {

            if (idCity == getListCity()[i].id) {

                for (j in 0 until getListCity()[i].districts!!.size) {

                    /*if (idDistrict == getListCity()[i].districts!![j].id) {

                        idOfField = if (null == getListCity()[i].districts!![j].fields) {

                            "0"

                        } else {

                            "${getListCity()[i].districts!![j].fields!!.size}"
                        }
                    }*/
                }
            }
        }

        val footballFieldModel = FbFieldModel(
            idOfField,
            idCity,
            idDistrict,
//            getUIDUser(Constant().KEY_LOGIN_UID_USER),
            getUIDUser(),
            uriPhoto,
            name,
            phone,
            address,
            amountField,
            priceField
        )

        Log.d(Constant().TAG, "field of idCity:$idCity, idDistrict:$idDistrict : $idOfField")

        dataReference.child(idCity).child("districts")
            .child(idDistrict).child("fields").child(idOfField).setValue(footballFieldModel)

            .addOnCompleteListener {

                Utils().alertInsertSuccess(activity)

                hideKeyBoard()

            }
            .addOnFailureListener {

                Utils().alertInsertFail(activity)

            }
    }


    private fun disableItem() {

        frg_add_football_field_et_name.isEnabled = false
        frg_add_football_field_et_phone.isEnabled = false
        frg_add_football_field_et_address.isEnabled = false
        frg_add_football_field_sp_district.isEnabled = false
        frg_add_football_field_sp_city.isEnabled = false
        frg_add_football_field_et_count_field.isEnabled = false
        frg_add_football_field_et_price_field.isEnabled = false

    }

    private fun initView() {

        frg_add_football_field_v_photo.setOnClickListener {
            checkPermissionStore()
        }

        Utils().editTextTextChange(
            frg_add_football_field_et_name,
            frg_add_football_field_iv_clear_input_name,
            frg_add_football_field_tv_error_input_name
        )

        Utils().editTextTextChange(
            frg_add_football_field_et_phone,
            frg_add_football_field_iv_clear_input_phone,
            frg_add_football_field_tv_error_input_phone
        )

        Utils().editTextTextChange(
            frg_add_football_field_et_address,
            frg_add_football_field_iv_clear_input_address,
            frg_add_football_field_tv_error_input_address
        )

        Utils().editTextTextChange(
            frg_add_football_field_et_count_field,
            frg_add_football_field_iv_clear_input_count_field,
            frg_add_football_field_tv_error_input_count_field
        )

        Utils().editTextTextChange(
            frg_add_football_field_et_price_field,
            frg_add_football_field_iv_clear_input_price_field,
            frg_add_football_field_tv_error_input_height
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

                Glide.with(activity!!).load(filePathUri).into(frg_add_football_field_iv_photo)

                frg_add_football_field_iv_camera.visibility = View.GONE

                frg_add_football_field_tv_error_select_photo.visibility = View.INVISIBLE

            }
        }
    }

    private fun validate(): Boolean {

        var validate = true

        if (frg_add_football_field_et_name.text.toString().isEmpty()) {
            frg_add_football_field_tv_error_input_name.visibility = View.VISIBLE
            frg_add_football_field_et_name.requestFocus()
            validate = false
        }

        if (frg_add_football_field_et_phone.text.toString().isEmpty()) {
            frg_add_football_field_tv_error_input_phone.visibility = View.VISIBLE
            frg_add_football_field_tv_error_input_phone.text =
                activity!!.getString(R.string.please_enter_your_phone)
            frg_add_football_field_et_phone.requestFocus()
            validate = false
        }

        if (!Utils().validatePhoneNumber(frg_add_football_field_et_phone.text.toString())) {
            frg_add_football_field_tv_error_input_phone.visibility = View.VISIBLE
            frg_add_football_field_tv_error_input_phone.text =
                activity!!.getString(R.string.please_enter_your_phone_valid)
            frg_add_football_field_et_phone.requestFocus()

            validate = false
        }

        if (frg_add_football_field_et_address.text.toString().isEmpty()) {
            frg_add_football_field_tv_error_input_address.visibility = View.VISIBLE
            frg_add_football_field_et_address.requestFocus()
            validate = false
        }

        return validate
    }

}