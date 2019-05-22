package com.bongdaphui.clubUpdate

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.AddDataListener
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.listener.ConfirmListener
import com.bongdaphui.listener.UpdateListener
import com.bongdaphui.model.ClubModel
import com.bongdaphui.model.UserStickModel
import com.bongdaphui.utils.*
import com.bongdaphui.utils.Enum
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import kotlinx.android.synthetic.main.frg_update_club.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class UpdateClubScreen : BaseFragment() {

    var idCity: String = ""

    var idDistrict: String = ""

    private var cal = Calendar.getInstance()

    private var filePathUri: Uri? = null

    companion object {

        private var clubModel: ClubModel? = null

        private var addDataListener: AddDataListener? = null

        fun getInstance(model: ClubModel, listener: AddDataListener): UpdateClubScreen {

            clubModel = model

            addDataListener = listener

            return UpdateClubScreen()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_update_club, container, false)
    }

    override fun onResume() {

        super.onResume()

        showHeader(true)

        showButtonBack(true)

        showFooter(false)

        clubModel?.name?.let { setTitle(it) }
    }

    override fun onBindView() {

        initSpinner()

        initView()

        frg_update_club_tv_input.setOnClickListener {

            startInsertData()
        }

        frg_update_club_v_container.setOnTouchListener { _, _ ->

            hideKeyBoard()

            false
        }
    }

    private fun initSpinner() {

        Utils().initSpinnerCity(
            activity!!,
            frg_update_club_sp_city,
            frg_update_club_sp_district,
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

        frg_update_club_v_photo.setOnClickListener {
            checkPermissionStore()
        }

        Utils().editTextTextChange(
            frg_update_club_et_name_fc,
            frg_update_club_iv_clear_input_name_fc,
            frg_update_club_tv_error_input_name_fc
        )

        Utils().editTextTextChange(
            frg_update_club_et_full_name,
            frg_update_club_iv_clear_input_full_name,
            frg_update_club_tv_error_input_full_name
        )

        Utils().editTextTextChange(
            frg_update_club_et_email,
            frg_update_club_iv_clear_input_email,
            frg_update_club_tv_error_input_email
        )

        Utils().editTextTextChange(
            frg_update_club_et_phone,
            frg_update_club_iv_clear_input_phone,
            frg_update_club_tv_error_input_phone
        )

        frg_update_club_v_input_dob.setOnClickListener {
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
            frg_update_club_et_address,
            frg_update_club_iv_clear_input_address,
            frg_update_club_tv_error_input_address
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

                Glide.with(activity!!).load(filePathUri).into(frg_update_club_iv_photo)

                frg_update_club_iv_camera.visibility = View.GONE

                frg_update_club_tv_error_select_photo.visibility = View.INVISIBLE
            }
        }
    }

    private fun validate(): Boolean {

        var validate = true

        if (frg_update_club_et_name_fc.text.toString().isEmpty()) {
            frg_update_club_tv_error_input_name_fc.visibility = View.VISIBLE
            frg_update_club_et_name_fc.requestFocus()

            validate = false
        }

        if (!Utils().validatePhoneNumber(frg_update_club_et_phone.text.toString())) {
            frg_update_club_tv_error_input_phone.visibility = View.VISIBLE
            frg_update_club_tv_error_input_phone.text = activity!!.getString(R.string.please_enter_your_phone_valid)
            frg_update_club_et_phone.requestFocus()

            validate = false
        }

        if (frg_update_club_et_phone.text.toString().isEmpty()) {
            frg_update_club_tv_error_input_phone.visibility = View.VISIBLE
            frg_update_club_tv_error_input_phone.text = activity!!.getString(R.string.please_enter_your_phone)
            frg_update_club_et_phone.requestFocus()

            validate = false
        }

        if (!Utils().isEmpty(frg_update_club_et_email.text.toString())
            && !Utils().validateEmail(frg_update_club_et_email.text.toString())
        ) {

            frg_update_club_tv_error_input_email.visibility = View.VISIBLE
            frg_update_club_tv_error_input_email.text =
                activity!!.getString(R.string.email_not_valid)

            frg_update_club_et_email.requestFocus()

            validate = false

        }

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

                        /*frg_update_club_progress.visibility = View.VISIBLE
                        frg_update_club_progress.progress = Utils().progressTask(it)*/
                    }
            }
        }
    }

    private fun storageData(uri: String) {

        Log.d(Constant().TAG, uri)

        val name = frg_update_club_et_name_fc.text.toString()
        val caption = frg_update_club_et_full_name.text.toString()
        val email = frg_update_club_et_email.text.toString()
        val phone = frg_update_club_et_phone.text.toString()
        val dob = frg_update_club_et_dob.text.toString()
        val address: String = if (frg_update_club_et_address.text.toString().isNotEmpty()) {
            "${frg_update_club_et_address.text}, ${frg_update_club_sp_district.selectedItem}, ${frg_update_club_sp_city.selectedItem}"
        } else {
            "${frg_update_club_sp_district.selectedItem}, ${frg_update_club_sp_city.selectedItem}"
        }

        val currentTime = Calendar.getInstance().timeInMillis

        val id = "${Utils().getRandomNumberString()}$currentTime"

        val userCurrent = getDatabase().getUserDAO().getItems()

        val userStickModel =
            UserStickModel(userCurrent.id, userCurrent.photoUrl, userCurrent.name, userCurrent.position)

        val listPlayer = arrayListOf(Gson().toJson(userStickModel))

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

                addDataListener?.onSuccess()

                showProgress(false)

                Utils().alertInsertSuccess(activity)

            }

            override fun onUpdateFail(err: String) {
                showProgress(false)
                Utils().alertInsertFail(activity)
            }
        })
    }

    private fun disableItem() {

        frg_update_club_v_photo.isEnabled = false
        frg_update_club_et_name_fc.isEnabled = false
        frg_update_club_et_full_name.isEnabled = false
        frg_update_club_et_email.isEnabled = false
        frg_update_club_et_phone.isEnabled = false
        frg_update_club_iv_dob.isEnabled = false
        frg_update_club_sp_district.isEnabled = false
        frg_update_club_sp_city.isEnabled = false
        frg_update_club_tv_input.isEnabled = false

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
        frg_update_club_et_dob!!.text = sdf.format(cal.time)
    }
}