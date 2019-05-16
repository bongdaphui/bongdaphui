package com.bongdaphui.addClub

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import com.bongdaphui.listener.UpdateListener
import com.bongdaphui.model.ClubModel
import com.bongdaphui.utils.*
import com.bongdaphui.utils.Enum
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_add_fc.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddClubScreen : BaseFragment() {

    var idCity: String = ""

    var idDistrict: String = ""

    var idLastFC: Int = 9999

    private var cal = Calendar.getInstance()

    private var filePathUri: Uri? = null

    companion object {

        private const val ID_LAST_FC = "ID_LAST_FC"

        fun getInstance(idFC: Int): AddClubScreen {

            val screen = AddClubScreen()

            val bundle = Bundle()

            bundle.putInt(ID_LAST_FC, idFC)

            screen.arguments = bundle

            return screen
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_fc, container, false)

    }

    override fun onResume() {

        super.onResume()

        showButtonBack(true)

        showFooter(false)

        setTitle(activity!!.resources.getString(R.string.add_fc))
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindView() {

        val bundle = arguments

        if (bundle != null) {

            idLastFC = bundle.getInt(ID_LAST_FC)

        }

        initSpinner()

        initView()

        frg_add_fc_tv_input.setOnClickListener {

            startInsertData()
        }

        frg_add_fc_v_container.setOnTouchListener { _, _ ->

            hideKeyBoard()

            false
        }

    }

    private fun initSpinner() {
        Utils().initSpinnerCity(
            activity!!,
            frg_add_fc_sp_city,
            frg_add_fc_sp_district,
            object :
                BaseSpinnerSelectInterface {
                override fun onSelectCity(_idCity: String, _idDistrict: String) {

                    idCity = _idCity

                    idDistrict = _idDistrict

                    Log.d(Constant().TAG, "spinner onSelectCity with idCity: $idCity - idDistrict : $idDistrict")

                }
            })
    }

    private fun initView() {

        frg_add_fc_v_photo.setOnClickListener {
            checkPermissionStore()
        }

        Utils().editTextTextChange(
            frg_add_fc_et_name_fc,
            frg_add_fc_iv_clear_input_name_fc,
            frg_add_fc_tv_error_input_name_fc
        )

        Utils().editTextTextChange(
            frg_add_fc_et_full_name,
            frg_add_fc_iv_clear_input_full_name,
            frg_add_fc_tv_error_input_full_name
        )

        Utils().editTextTextChange(
            frg_add_fc_et_email,
            frg_add_fc_iv_clear_input_email,
            frg_add_fc_tv_error_input_email
        )

        Utils().editTextTextChange(
            frg_add_fc_et_phone,
            frg_add_fc_iv_clear_input_phone,
            frg_add_fc_tv_error_input_phone
        )

        frg_add_fc_v_input_dob.setOnClickListener {
            DatePickerDialog(
                activity!!,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        Utils().editTextTextChange(
            frg_add_fc_et_address,
            frg_add_fc_iv_clear_input_address,
            frg_add_fc_tv_error_input_address
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
            openPick()
        }
    }

    private fun openPick() {

        val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, RequestCode().pickPhoto)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == RequestCode().readExternalStorage) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                openPick()

            } else {

                AlertDialog().showDialog(
                    activity!!,
                    Enum.EnumConfirmYes.DeniedPermission.value,
                    object : ConfirmListener {
                        override fun onConfirm(id: Int) {

                        }
                    })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RequestCode().pickPhoto && data != null) {

            filePathUri = data.data

            if (null != filePathUri) {

                Glide.with(activity!!).load(filePathUri).into(frg_add_fc_iv_photo)

                frg_add_fc_iv_camera.visibility = View.GONE

                frg_add_fc_tv_error_select_photo.visibility = View.INVISIBLE
            }
        }
    }

    private fun validate(): Boolean {

        var validate = true

        if (frg_add_fc_et_name_fc.text.toString().isEmpty()) {
            frg_add_fc_tv_error_input_name_fc.visibility = View.VISIBLE
            frg_add_fc_et_name_fc.requestFocus()

            validate = false
        }

        /*if (frg_add_fc_et_full_name.text.toString().isEmpty()) {
            frg_add_fc_tv_error_input_full_name.visibility = View.VISIBLE
            frg_add_fc_et_full_name.requestFocus()

            validate = false
        }*/

        /*if (frg_add_fc_et_email.text.toString().isEmpty()) {
            frg_add_fc_tv_error_input_email.visibility = View.VISIBLE

            validate = false
        }*/

        if (!Utils().validatePhoneNumber(frg_add_fc_et_phone.text.toString())) {
            frg_add_fc_tv_error_input_phone.visibility = View.VISIBLE
            frg_add_fc_tv_error_input_phone.text = activity!!.getString(R.string.please_enter_your_phone_valid)
            frg_add_fc_et_phone.requestFocus()

            validate = false
        }

        if (frg_add_fc_et_phone.text.toString().isEmpty()) {
            frg_add_fc_tv_error_input_phone.visibility = View.VISIBLE
            frg_add_fc_tv_error_input_phone.text = activity!!.getString(R.string.please_enter_your_phone)
            frg_add_fc_et_phone.requestFocus()

            validate = false
        }

        if (!Utils().isEmpty(frg_add_fc_et_email.text.toString())
            && !Utils().validateEmail(frg_add_fc_et_email.text.toString())
        ) {

            frg_add_fc_tv_error_input_email.visibility = View.VISIBLE
            frg_add_fc_tv_error_input_email.text =
                activity!!.getString(R.string.email_not_valid)

            frg_add_fc_et_email.requestFocus()

            validate = false

        }

        /*if (frg_add_football_field_et_address.text.toString().isEmpty()) {
            frg_add_football_field_tv_error_input_address.visibility = View.VISIBLE
            frg_add_football_field_et_address.requestFocus()
            validate = false
        }*/


        /*if (frg_add_fc_et_dob.text.toString().isEmpty()) {
            frg_add_fc_tv_error_input_dob.visibility = View.VISIBLE

            validate = false
        }*/

        return validate

    }

    private fun startInsertData() {

        if (validate()) {

            disableItem()
            hideKeyBoard()
            showProgress(true)

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
                        Constant().STORAGE_CLUB + System.currentTimeMillis() + "." + Utils().getFileExtension(
                            activity, filePathUri!!
                        )
                    )

                val selectedFilePath = FilePath.getPath(activity!!, filePathUri!!)

                val file = CompressFile.getCompressedImageFile(File(selectedFilePath), activity!!)

                //val byteImage = Utils().convertImageToByte(activity!!, FilePathUri!!)

                // Adding addOnSuccessListener to second StorageReference.
                //storageReference2nd.putFile(FilePathUri!!)
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

                        /*frg_add_fc_progress.visibility = View.VISIBLE
                        frg_add_fc_progress.progress = Utils().progressTask(it)*/
                    }
            }
        }
    }

    private fun storageData(uri: String) {

        Log.d(Constant().TAG, uri)

        val name = frg_add_fc_et_name_fc.text.toString()
        val caption = frg_add_fc_et_full_name.text.toString()
        val email = frg_add_fc_et_email.text.toString()
        val phone = frg_add_fc_et_phone.text.toString()
        val dob = frg_add_fc_et_dob.text.toString()
        val address: String = if (frg_add_fc_et_address.text.toString().isNotEmpty()) {
            "${frg_add_fc_et_address.text}, ${frg_add_fc_sp_district.selectedItem}, ${frg_add_fc_sp_city.selectedItem}"
        } else {
            "${frg_add_fc_sp_district.selectedItem}, ${frg_add_fc_sp_city.selectedItem}"
        }
        val id = "${idLastFC + 1}"
        var listPlayer = arrayListOf<String>(getUIDUser())

        val clubModel =
            ClubModel(
                id,
                getUIDUser(),
                uri,
                name,
                caption,
                email,
                phone,
                dob,
                address,
                idDistrict,
                idCity,
                "", "", "", "", listPlayer
            )
        BaseRequest().saveOrUpdateClub(clubModel, object : UpdateListener {
            override fun onUpdateSuccess() {
                showProgress(false)
                Utils().alertInsertSuccess(activity)
            }

            override fun onUpdateFail() {
                showProgress(false)
                Utils().alertInsertFail(activity)
            }

        })

    }

    private fun disableItem() {

        frg_add_fc_v_photo.isEnabled = false
        frg_add_fc_et_name_fc.isEnabled = false
        frg_add_fc_et_full_name.isEnabled = false
        frg_add_fc_et_email.isEnabled = false
        frg_add_fc_et_phone.isEnabled = false
        frg_add_fc_iv_dob.isEnabled = false
        frg_add_fc_sp_district.isEnabled = false
        frg_add_fc_sp_city.isEnabled = false
        frg_add_fc_tv_input.isEnabled = false

    }

    private val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateDateInView()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        frg_add_fc_et_dob!!.text = sdf.format(cal.time)
    }

}