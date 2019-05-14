package com.bongdaphui.updateAccount

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.UpdateUserListener
import com.bongdaphui.model.UserModel
import com.bongdaphui.utils.*
import com.bongdaphui.utils.Enum
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_update_account.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class UpdateAccountScreen : BaseFragment() {

    var cal = Calendar.getInstance()

    // Creating URI.
    private var filePathUri: Uri? = null
    private var userModel: UserModel? = null

    companion object {

        private const val USER_MODEL = "USER_MODEL"

        fun getInstance(

            userModel: UserModel

        ): UpdateAccountScreen {

            val screen = UpdateAccountScreen()

            val bundle = Bundle()

            bundle.putSerializable(USER_MODEL, userModel)

            screen.arguments = bundle

            return screen
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_update_account, container, false)

    }

    override fun onResume() {
        super.onResume()

        showButtonBack(true)

        showFooter(false)

        setTitle(activity!!.resources.getString(R.string.update_info))
    }

    override fun onBindView() {

        val bundle = arguments

        if (bundle != null) {

            userModel = bundle.getSerializable(USER_MODEL) as UserModel?

        }
        Utils().initPositionSpinner(activity!!, frg_update_account_sp_position)

        fillData(userModel!!)

        initView()

        frg_update_account_tv_input.setOnClickListener {

            startInsertData()

        }

        frg_update_account_container.setOnTouchListener { _, _ ->

            hideKeyBoard()

            false
        }
    }

    private fun initView() {

        frg_update_account_v_photo.setOnClickListener {

            checkPermissionStore()
        }

        Utils().editTextTextChange(
            frg_update_account_et_full_name,
            frg_update_account_iv_clear_input_full_name,
            frg_update_account_tv_error_input_full_name
        )

        Utils().editTextTextChange(
            frg_update_account_et_email,
            frg_update_account_iv_clear_input_email,
            frg_update_account_tv_error_input_email
        )

        Utils().editTextTextChange(
            frg_update_account_et_phone,
            frg_update_account_iv_clear_input_phone,
            frg_update_account_tv_error_input_phone
        )

        frg_update_account_v_input_dob.setOnClickListener {
            DatePickerDialog(
                activity!!,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

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

                Glide.with(activity!!).load(filePathUri).into(frg_update_account_iv_photo)

                frg_update_account_iv_camera.visibility = View.GONE

                frg_update_account_tv_error_select_photo.visibility = View.INVISIBLE

            }
        }
    }

    private fun validate(): Boolean {

        var validate = true

        if (!Utils().isEmpty(frg_update_account_et_email.text.toString())
            && !Utils().validateEmail(frg_update_account_et_email.text.toString())
        ) {

            frg_update_account_tv_error_input_email.visibility = View.VISIBLE
            frg_update_account_tv_error_input_email.text =
                activity!!.getString(R.string.email_not_valid)

            frg_update_account_et_email.requestFocus()

            validate = false

        }

        if (!Utils().isEmpty(frg_update_account_et_phone.text.toString())
            && !Utils().validatePhoneNumber(frg_update_account_et_phone.text.toString())
        ) {
            frg_update_account_tv_error_input_phone.visibility = View.VISIBLE
            frg_update_account_tv_error_input_phone.text = activity!!.getString(R.string.please_enter_your_phone_valid)
            frg_update_account_et_phone.requestFocus()

            validate = false
        }

        return validate
    }

    private fun startInsertData() {

        if (validate()) {

            disableItem()

            showProgress(true)

            //no image
            if (null == filePathUri) {

                if (userModel!!.photoUrl!!.isEmpty()) {

                    storageData("")
                } else {
                    storageData(userModel!!.photoUrl!!)
                }

                //has image
            } else {

                //remove Image old if have
                if (!Utils().isEmpty(userModel!!.photoUrl!!)) {
                    BaseRequest().removeImage(userModel!!.photoUrl!!)
                }

                // Assign FirebaseStorage instance to storageReference.
                val storageReference = FirebaseStorage.getInstance().reference

                // Creating second StorageReference.
                val storageReference2nd: StorageReference =
                    storageReference.child(
                        Constant().STORAGE_USER + System.currentTimeMillis() + "." + Utils().getFileExtension(
                            activity, filePathUri!!
                        )
                    )

                val selectedFilePath = FilePath.getPath(activity!!, filePathUri!!)

                val file = CompressFile.getCompressedImageFile(File(selectedFilePath), activity!!)

                // Adding addOnSuccessListener to second StorageReference.
                storageReference2nd.putFile(Uri.fromFile(file))

                    .addOnSuccessListener {

                        storageReference2nd.downloadUrl.addOnSuccessListener {

                            storageData("$it")

                        }

                    }
                    // If something goes wrong .
                    .addOnFailureListener {

                        showProgress(false)

                        Utils().alertUpdateFail(activity)

                    }

                    // On progress change upload time.
                    .addOnProgressListener {

                        //                        frg_update_account_progress.visibility = View.VISIBLE
//                        frg_update_account_progress.progress = Utils().progressTask(it)
                    }
            }
        }
    }

    private fun storageData(uriPhoto: String) {

        Log.d(Constant().TAG, uriPhoto)

        val name = frg_update_account_et_full_name.text.toString()
        val email = frg_update_account_et_email.text.toString()
        val phone = frg_update_account_et_phone.text.toString()
        val dob = frg_update_account_et_dob.text.toString()
        val height = frg_update_account_et_height.text.toString()
        val weight = frg_update_account_et_weight.text.toString()
        val position = frg_update_account_sp_position.selectedItem.toString()

        val userModel = UserModel(getUIDUser(), uriPhoto, name, email, phone, dob, height, weight, position)

        BaseRequest().saveOrUpdateUser(userModel, object : UpdateUserListener {
            override fun onUpdateSuccess() {
                //cache data
                getDatabase().getUserDAO().insert(userModel)

                showProgress(false)

                Utils().alertUpdateSuccess(activity)
//                updateUserOnAccountScreen(userModel)
            }

            override fun onUpdateFail() {

                showProgress(false)

                Utils().alertInsertFail(activity)

            }

        })
    }

    private fun disableItem() {

        frg_update_account_v_photo.isEnabled = false
        frg_update_account_et_full_name.isEnabled = false
        frg_update_account_et_email.isEnabled = false
        frg_update_account_et_phone.isEnabled = false
        frg_update_account_iv_dob.isEnabled = false
        frg_update_account_tv_input.isEnabled = false

    }

    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateDateInView()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        frg_update_account_et_dob!!.text = sdf.format(cal.time)
    }

    @SuppressLint("CheckResult")
    private fun fillData(userModel: UserModel) {

        Glide.with(context!!).load(
            if (Utils().isEmpty(userModel.photoUrl))
                Utils().getDrawable(context!!, R.drawable.ic_profile) else userModel.photoUrl
        ).into(frg_update_account_iv_photo)

        frg_update_account_et_full_name.text = userModel.name!!.toEditable()
        frg_update_account_et_email.text = userModel.email!!.toEditable()
        frg_update_account_et_phone.text = userModel.phone!!.toEditable()
        frg_update_account_et_dob.text = userModel.dob!!.toEditable()
        frg_update_account_et_height.text = userModel.height!!.toEditable()
        frg_update_account_et_weight.text = userModel.weight!!.toEditable()

        when {
            userModel.position == Enum.EnumPosition.GK.namePos -> frg_update_account_sp_position.setSelection(Enum.EnumPosition.GK.value)
            userModel.position == Enum.EnumPosition.DF.namePos -> frg_update_account_sp_position.setSelection(Enum.EnumPosition.DF.value)
            userModel.position == Enum.EnumPosition.MF.namePos -> frg_update_account_sp_position.setSelection(Enum.EnumPosition.MF.value)
            userModel.position == Enum.EnumPosition.SK.namePos -> frg_update_account_sp_position.setSelection(Enum.EnumPosition.SK.value)
        }
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}