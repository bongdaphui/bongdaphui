package com.bongdaphui.addPlayer

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.model.ClubModel
import com.bongdaphui.model.UserStickModel
import com.bongdaphui.utils.*
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_join_club.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class JoinClubScreen : BaseFragment() {

    var cal = Calendar.getInstance()

    // Creating URI.
    internal var filePathUri: Uri? = null

    lateinit var clubModel: ClubModel

    companion object {

        private const val CLUB_MODEL = "CLUB_MODEL"

        fun getInstance(clubModel: ClubModel): JoinClubScreen {

            val screen = JoinClubScreen()

            val bundle = Bundle()

            bundle.putSerializable(CLUB_MODEL, clubModel)

            screen.arguments = bundle

            return screen
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_join_club, container, false)

    }

    override fun onResume() {
        super.onResume()

        showButtonBack(true)

        showFooter(false)

        setTitle(activity!!.resources.getString(R.string.join_club))
    }

    override fun onBindView() {

        val bundle = arguments

        if (bundle != null) {

            clubModel = bundle.getSerializable(CLUB_MODEL) as ClubModel

        }

        initView()

        Utils().editTextTextChange(
            frg_join_club_et_height,
            frg_join_club_iv_clear_input_height,
            frg_join_club_tv_error_input_height
        )

        Utils().editTextTextChange(
            frg_join_club_et_weight,
            frg_join_club_iv_clear_input_weight,
            frg_join_club_tv_error_input_weight
        )

        Utils().initPositionSpinner(activity!!, frg_join_club_sp_position)

        Utils().editTextTextChange(
            frg_join_club_et_clothers_number,
            frg_join_club_iv_clear_input_clothers_number,
            frg_join_club_tv_error_input_clothers_number
        )

        onClick()
    }

    private fun onClick() {

        frg_join_club_tv_input.setOnClickListener {

            startValidate()

        }

        frg_join_club_container.setOnTouchListener { _, _ ->

            hideKeyBoard()

            false
        }

    }


    private fun initView() {

        frg_join_club_v_photo.setOnClickListener {

            checkPermissionStore()
        }

        Utils().editTextTextChange(
            frg_join_club_et_full_name,
            frg_join_club_iv_clear_input_full_name,
            frg_join_club_tv_error_input_full_name
        )

        Utils().editTextTextChange(
            frg_join_club_et_email,
            frg_join_club_iv_clear_input_email,
            frg_join_club_tv_error_input_email
        )

        Utils().editTextTextChange(
            frg_join_club_et_phone,
            frg_join_club_iv_clear_input_phone,
            frg_join_club_tv_error_input_phone
        )

        frg_join_club_v_input_dob.setOnClickListener {
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
            frg_join_club_et_address,
            frg_join_club_iv_clear_input_address,
            frg_join_club_tv_error_input_address
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

                Glide.with(activity!!).load(filePathUri).into(frg_join_club_iv_photo)

                frg_join_club_iv_camera.visibility = View.GONE

                frg_join_club_tv_error_select_photo.visibility = View.INVISIBLE

            }
        }
    }

    private fun validate(): Boolean {

        var validate = true

        if (frg_join_club_et_full_name.text.toString().isEmpty()) {
            frg_join_club_tv_error_input_full_name.visibility = View.VISIBLE
            frg_join_club_et_full_name.requestFocus()

            validate = false
        }

        if (frg_join_club_et_email.text.toString().isNotEmpty() && !Utils().validateEmail(frg_join_club_et_email.text.toString())) {

            frg_join_club_tv_error_input_email.visibility = View.VISIBLE
            frg_join_club_tv_error_input_email.text =
                activity!!.getString(R.string.email_not_valid)

            frg_join_club_et_email.requestFocus()

            validate = false

        }

        if (frg_join_club_et_phone.text.toString().isNotEmpty() && !Utils().validatePhoneNumber(frg_join_club_et_phone.text.toString())) {
            frg_join_club_tv_error_input_phone.visibility = View.VISIBLE
            frg_join_club_tv_error_input_phone.text = activity!!.getString(R.string.please_enter_your_phone_valid)
            frg_join_club_et_phone.requestFocus()

            validate = false
        }

        if (frg_join_club_et_phone.text.toString().isEmpty()) {
            frg_join_club_tv_error_input_phone.visibility = View.VISIBLE
            frg_join_club_tv_error_input_phone.text = activity!!.getString(R.string.please_enter_your_phone)
            frg_join_club_et_phone.requestFocus()

            validate = false
        }

        return validate
    }

    private fun startValidate() {

        if (validate()) {

            checkPlayerIsHaveOfClubAndInsertPlayer()

        } else {
            showProgress(false)
        }
    }

    private fun insertPlayer(idPlayer: String) {
        //no image
        if (null == filePathUri) {

            storageData(idPlayer, "")

            //has image
        } else {

            // Assign FirebaseStorage instance to storageReference.
            val storageReference = FirebaseStorage.getInstance().reference

            // Creating second StorageReference.
            val storageReference2nd: StorageReference =
                storageReference.child(
                    Constant().STORAGE_PLAYER + System.currentTimeMillis() + "." + Utils().getFileExtension(
                        activity, filePathUri!!
                    )
                )

            val selectedFilePath = FilePath.getPath(activity!!, filePathUri!!)

            val file = CompressFile.getCompressedImageFile(File(selectedFilePath), activity!!)

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(Uri.fromFile(file))

                .addOnSuccessListener {

                    storageReference2nd.downloadUrl.addOnSuccessListener {

                        storageData(idPlayer, "$it")

                    }

                }
                // If something goes wrong .
                .addOnFailureListener {

                    Utils().alertInsertFail(activity)

                }

                // On progress change upload time.
                .addOnProgressListener {

                    frg_join_club_progress.visibility = View.VISIBLE
                    frg_join_club_progress.progress = Utils().progressTask(it)

                }
        }
    }

    private fun storageData(idPlayer: String, uriPhoto: String) {

        Log.d(Constant().TAG, uriPhoto)

        val name = frg_join_club_et_full_name.text.toString()
        val email = frg_join_club_et_email.text.toString()
        val phone = frg_join_club_et_phone.text.toString()
        val dob = frg_join_club_et_dob.text.toString()
        val address = frg_join_club_et_address.text.toString()
        val height = frg_join_club_et_height.text.toString()
        val weight = frg_join_club_et_weight.text.toString()
        val position = frg_join_club_sp_position.selectedItem.toString()
        val clother_number = frg_join_club_et_clothers_number.text.toString()

        // Assign FirebaseDatabase instance with root database name.
        val databaseReference = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_CLUB)

//        val memberId = databaseReference.push().key

//
//        // Adding image upload id s child element into databaseReference.
//        databaseReference.child(clubModel.id!!).child(Constant().CHILD_PLAYERS).child(idPlayer).setValue(playersModel)
//
//            .addOnCompleteListener {
//
//                showProgress(false)
//                Utils().alertInsertSuccess(activity)
//                hideKeyBoard()
//
//            }
//            .addOnFailureListener {
//
//                showProgress(false)
//                Utils().alertInsertFail(activity)
//
//            }
    }

    private fun disableItem() {

        frg_join_club_v_photo.isEnabled = false
        frg_join_club_et_full_name.isEnabled = false
        frg_join_club_et_email.isEnabled = false
        frg_join_club_et_phone.isEnabled = false
        frg_join_club_iv_dob.isEnabled = false
        frg_join_club_et_address.isEnabled = false
        frg_join_club_et_height.isEnabled = false
        frg_join_club_et_weight.isEnabled = false
        frg_join_club_sp_position.isEnabled = false
        frg_join_club_et_clothers_number.isEnabled = false
        frg_join_club_tv_input.isEnabled = false

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
        frg_join_club_et_dob!!.text = sdf.format(cal.time)
    }

    private fun checkPlayerIsHaveOfClubAndInsertPlayer() {

        disableItem()
        hideKeyBoard()
        showProgress(true)

        //if don't have player

        if (clubModel.players.size == 0) {

            insertPlayer("0")

            //if have player => get id of last player ++
        } else {

            insertPlayer("${getIdLastPlayer().toInt() + 1}")

            Log.d(Constant().TAG, "club have player, start add with id ${getIdLastPlayer().toInt() + 1}")
        }

        /*  val rootRef = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_CLUB)
              .child(clubModel.id!!).child(Constant().CHILD_PLAYERS)

          rootRef.addValueEventListener(object : ValueEventListener {

              override fun onDataChange(dataSnapshot: DataSnapshot) {

                  if (dataSnapshot.exists()) {

                      //if have player => get id of last player ++
                      insertPlayer("${getIdLastPlayer().toInt() + 1}")

                      Log.d(Constant().TAG, "club have player, start add with id ${getIdLastPlayer().toInt() + 1}")


                  } else {

                      //if don't have player
                      insertPlayer("0")
                      Log.d(Constant().TAG, "club no player, start add with id 0")

                  }

              }

              override fun onCancelled(databaseError: DatabaseError) {
                  Utils().alertInsertFail(activity)
              }
          })*/
    }

    fun getIdLastPlayer(): String {

        for (i in 0 until clubModel.players.size) {

            if (i == clubModel.players.size - 1) {
                val userStickModel = Gson().fromJson(clubModel.players[i],UserStickModel::class.java)
                return userStickModel.id

            }
        }

        return ""
    }
}