package com.bongdaphui.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.support.v4.content.ContextCompat
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import com.bongdaphui.R
import com.bongdaphui.addField.SpinnerAdapter
import com.bongdaphui.base.BaseApplication
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.model.CityModel
import com.bongdaphui.model.CommentModel
import com.bongdaphui.model.DistrictModel
import com.bongdaphui.model.FbFieldModel
import com.google.firebase.storage.UploadTask
import org.json.JSONArray
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.util.*
import kotlin.collections.ArrayList

class Utils {

    fun isEmpty(str: String?): Boolean {

        return str == null || str.isEmpty()
    }

    fun validateEmail(email: String): Boolean {
        return email.matches(Constant().EMAIL_REGULAR_EXPRESSION.toRegex())
    }

    fun getDrawable(resourceID: Int): Drawable? {
        return BaseApplication().getContext()?.let { ContextCompat.getDrawable(it, resourceID) }
    }

    fun editTextTextChange(editText: EditText, ivClear: ImageView, tvError: TextView) {

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            @SuppressLint("NewApi")
            override fun afterTextChanged(s: Editable) {

                ivClear.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE

                tvError.visibility = if (s.isNotEmpty()) View.INVISIBLE else View.VISIBLE

            }
        })

        ivClear.setOnClickListener { editText.setText("") }
    }

    @SuppressLint("SetTextI18n")
    fun showDatePicker(texbox: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            BaseApplication().getActiveActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                texbox.text = "$dayOfMonth/$monthOfYear/$year"
            },
            year,
            month,
            day
        )

        dpd.show()
    }

    fun getSharedPreferenceKey(): String {
        val pInfo: PackageInfo
        try {
            pInfo = BaseApplication().getContext()!!
                .packageManager
                .getPackageInfo(
                    BaseApplication().getContext()!!.packageName, 0
                )
            return Constant().sharePreferenceName + "." + pInfo.packageName

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Constant().sharePreferenceName
    }

    fun checkPermisstionPhoto(activity: Activity?) {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    RequestCode().readExternalStorage
                )
            }
        } else if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            choosePhoto(activity)
        }
    }

    private fun choosePhoto(activity: Activity?) {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        activity!!.startActivityForResult(Intent.createChooser(intent, "Select Picture"), RequestCode().pickPhoto)
    }

    fun getRandomNumberString(): String? {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        val rnd = Random()
        val number = rnd.nextInt(999999)

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number)
    }

    fun validatePhoneNumber(number: String): Boolean {
        return PhoneNumberUtils.isGlobalPhoneNumber(number)
    }

    fun alertInsertFail(activity: Activity?) {

        Toast.makeText(activity, "Thêm thất bại. Bạn vui lòng thực hiện lại.", Toast.LENGTH_LONG).show()

        activity!!.onBackPressed()
    }

    fun alertUpdateFail(activity: Activity?) {

        Toast.makeText(activity, "Cập nhật thất bại. Bạn vui lòng thực hiện lại.", Toast.LENGTH_LONG).show()

        activity!!.onBackPressed()
    }

    fun alertInsertSuccess(activity: Activity?) {

        Toast.makeText(activity, "Thêm thành công", Toast.LENGTH_LONG).show()

        activity!!.onBackPressed()

    }

    fun alertUpdateSuccess(activity: Activity?) {

        Toast.makeText(activity, "Cập nhật thành công", Toast.LENGTH_LONG).show()

        activity!!.onBackPressed()

    }

    fun convertImageToByte(activity: Activity, uri: Uri): ByteArray? {
        var data: ByteArray? = null
        try {
            val cr = activity.baseContext.contentResolver

            val inputStream = cr.openInputStream(uri)


            val bitmap = BitmapFactory.decodeStream(inputStream)


            Log.d(Constant().TAG, "realImage.width before  ${bitmap.width}")
            Log.d(Constant().TAG, "realImage.height before ${bitmap.height}")

//            val bitmapScale = scaleDown(bitmap, Constant().sizeImage, false)

            val baos = ByteArrayOutputStream()

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

//            val mb = (baos.size() / 1024f) / 1024f

//            Log.d(Constant().TAG, "before $mb : ${baos.size()}")


//            if (baos.size() > Constant().sizeImage) {
//
//                bitmap.compress(Bitmap.CompressFormat.JPEG, (Constant().sizeImage * 100) / baos.size(), baos)
//            }

            val mb1 = (baos.size() / 1024f) / 1024f

            Log.d(Constant().TAG, "after mb: $mb1 -  byte: ${baos.size()} ")

            data = baos.toByteArray()


        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return data
    }

    private fun scaleDown(realImage: Bitmap, maxImageSize: Float, filter: Boolean): Bitmap {

        Log.d(Constant().TAG, "realImage.width  ${realImage.width}")
        Log.d(Constant().TAG, "realImage.height  ${realImage.height}")


        val ratio = Math.min(maxImageSize / realImage.width, maxImageSize / realImage.height)

        Log.d(Constant().TAG, "ratio  $ratio")

        val width = Math.round(ratio * realImage.width)

        Log.d(Constant().TAG, "width  $width")

        val height = Math.round(ratio * realImage.height)

        Log.d(Constant().TAG, "height  $height")

        return Bitmap.createScaledBitmap(realImage, width, height, filter)
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    fun getFileExtension(activity: Activity?, uri: Uri): String? {

        val contentResolver = activity!!.contentResolver

        val mimeTypeMap = MimeTypeMap.getSingleton()

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))

    }

    fun progressTask(it: UploadTask.TaskSnapshot): Int {
        return ((100.0 * it.bytesTransferred) / it.totalByteCount).toInt()
    }

    fun getDrawable(context: Context, resourceID: Int): Drawable? {
        return ContextCompat.getDrawable(context, resourceID)
    }

    private fun isStringNumeric(str: String): Boolean {
        return !isEmpty(str) && str.matches("^[0-9,.]*$".toRegex())
    }

    private fun convertToDouble(number: String): Double {
        if (isEmpty(number)) {
            return 0.0
        }
        val theLocale =
            Locale.US//Locale.getDefault();//Hung Ngo: remove default Locale (which depend on device, using Locale.US
        val numberFormat = DecimalFormat.getInstance(theLocale)
        val theNumber: Number
        try {
            theNumber = numberFormat.parse(number)
            return theNumber.toDouble()
        } catch (e: ParseException) {
            val valueWithDot = number.replace(",".toRegex(), ".")
            try {
                return java.lang.Double.valueOf(valueWithDot)
            } catch (e2: NumberFormatException) {
                return 0.0
            }

        }
    }

    private fun formatMoney(pattern: String, money: Double): String {
        /* this code is formatting with local locale */
        //        DecimalFormat formatter = new DecimalFormat(pattern);
        //        return formatter.format(money);
        /* this code is formatting with local locale */

        /* this code is formatting with US locale */
        val nf = NumberFormat.getNumberInstance(Locale.US)
        val formatter = nf as DecimalFormat
        formatter.applyPattern(pattern)
        return formatter.format(money)
        /* this code is formatting with US locale */
    }

    fun formatMoney(pattern: String, money: String): String {
        return if (isStringNumeric(money)) {
            formatMoney(pattern, convertToDouble(money))
        } else {
            "0"
        }
    }

    fun initSpinnerCity(
        context: Context,
        spCity: Spinner,
        spDistrict: Spinner,
        spInterface: BaseSpinnerSelectInterface
    ) {

        val listCity = getListCity(context)
        //init City
        val listCityName = ArrayList<String>()
        var idCity = ""
        var idDistrict: String

        for (i in 0 until listCity.size) {

            val ciTyModel = listCity[i]

            listCityName.add(ciTyModel.name!!)
        }

        val spCityAdapter = SpinnerAdapter(context, R.layout.item_spinner, listCityName)
        spCity.adapter = spCityAdapter

        spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                val cityName: String = parent.getItemAtPosition(position) as String

                idCity = getIdCityFromNameCity(cityName, listCity)

                val listDistrictName = getListDistrictNameFromIdCity(listCity, idCity)

                val spDistrictAdapter = SpinnerAdapter(context, R.layout.item_spinner, listDistrictName)

                spDistrict.adapter = spDistrictAdapter

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(Constant().TAG, "spinner onNothingSelected")

            }
        }

        spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                val districtName: String = parent.getItemAtPosition(position) as String

                idDistrict = getIdDistrictFromNameDistrict(idCity, districtName, listCity)

                spInterface.onSelectCity(idCity, idDistrict)

//                Log.d(Constant().TAG, "district spinner select with idCity: $idCity - idDistrict : $idDistrict")

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(Constant().TAG, "spinner onNothingSelected")

            }
        }
    }

    fun getListDistrictNameFromIdCity(listCity: ArrayList<CityModel>, idCity: String): ArrayList<String> {

        val listDistrictName = ArrayList<String>()

        for (i in 0 until listCity.size) {

            if (idCity == listCity[i].id) {

                for (j in 0 until listCity[i].districts!!.size) {

                    listDistrictName.add(listCity[i].districts!![j].name!!)
                }
            }
        }

        return listDistrictName
    }

    fun getIdCityFromNameCity(name: String, listCity: ArrayList<CityModel>): String {

        var idCity = ""

        for (i in 0 until listCity.size) {

            if (name == listCity[i].name) {

                idCity = listCity[i].id!!
            }
        }

        return idCity
    }

    fun getIdDistrictFromNameDistrict(idCity: String, nameDistrict: String, listCity: ArrayList<CityModel>): String {

        var idDistrict = ""

        for (i in 0 until listCity.size) {

            if (idCity == listCity[i].id) {

                for (j in 0 until listCity[i].districts!!.size) {

                    if (nameDistrict == listCity[i].districts!![j].name) {

                        idDistrict = listCity[i].districts!![j].id!!
                    }
                }
            }
        }

        return idDistrict
    }

    fun openDial(context: Context, phone: String) {

        val intent = Intent(Intent.ACTION_DIAL)

        intent.data = Uri.parse("tel:$phone")

        context.startActivity(intent)
    }

    fun initPositionSpinner(context: Context, sp: Spinner) {
        val listPosition: ArrayList<String> = ArrayList()
        listPosition.add(Enum.EnumPosition.GK.namePos)
        listPosition.add(Enum.EnumPosition.DF.namePos)
        listPosition.add(Enum.EnumPosition.MF.namePos)
        listPosition.add(Enum.EnumPosition.SK.namePos)

        val adapter = SpinnerAdapter(context, R.layout.item_spinner, listPosition)

        sp.adapter = adapter
    }

    private fun loadJSONFromAsset(context: Context, fileName: String): String? {

        val json: String

        try {
            val `is` = context.assets.open(fileName)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.forName("UTF-8"))

        } catch (e: IOException) {

            Log.d(Constant().TAG, "loadJSONFromAsset fail : ${e.message}")

            return null
        }

        return json

    }

     fun getListCity(context: Context): ArrayList<CityModel> {

        val listCity = ArrayList<CityModel>()

        try {
            val jsonArrayCity = JSONArray(loadJSONFromAsset(context, "json_city.json"))

            for (i in 0 until jsonArrayCity.length()) {

                val jsonObjectCity = jsonArrayCity.getJSONObject(i)

                val idCity = jsonObjectCity.getString("id")

                val nameCity = jsonObjectCity.getString("name")

                val jsonArrayDistrict = JSONArray(jsonObjectCity.getString("districts"))

                val listDistrict = ArrayList<DistrictModel>()

                for (j in 0 until jsonArrayDistrict.length()) {

                    val jsonObjectDistrict = jsonArrayDistrict.getJSONObject(j)

                    val idDistrict = jsonObjectDistrict.getString("id")

                    val nameDistrict = jsonObjectDistrict.getString("name")

                    val districtModel = DistrictModel(idDistrict, nameDistrict)

                    listDistrict.add(districtModel)
                }

                val cityModel = CityModel(idCity, nameCity, listDistrict)

                listCity.add(cityModel)

            }
        } catch (e: JSONException) {

            Log.d(Constant().TAG, "getListCity fail : ${e.message}")
        }

        return listCity
    }

    fun getListField(context: Context): ArrayList<FbFieldModel> {

        val listField = ArrayList<FbFieldModel>()

        try {
            val jsonArrayField = JSONArray(loadJSONFromAsset(context, "json_field.json"))

            for (i in 0 until jsonArrayField.length()) {

                val jsonObjectField = jsonArrayField.getJSONObject(i)

                val id = jsonObjectField.getString("id")
                val idCity = jsonObjectField.getString("idCity")
                val idDistrict = jsonObjectField.getString("idDistrict")
                val photoUrl = jsonObjectField.getString("photoUrl")
                val name = jsonObjectField.getString("name")
                val phone = jsonObjectField.getString("phone")
                val address = jsonObjectField.getString("address")
                val amountField = jsonObjectField.getString("amountField")
                val price = jsonObjectField.getString("price")
                val lat = jsonObjectField.getString("lat")
                val lng = jsonObjectField.getString("lng")
                val countRating = jsonObjectField.getString("countRating")
                val rating = jsonObjectField.getString("rating")


                val jsonArrayComment = JSONArray(jsonObjectField.getString("comment"))

                val listComment = ArrayList<CommentModel>()

                for (j in 0 until jsonArrayComment.length()) {

                    val jsonObjectComment = jsonArrayComment.getJSONObject(i)

                    val id = jsonObjectComment.getString("id")

                    val idUser = jsonObjectComment.getString("idUser")

                    val nameUser = jsonObjectComment.getString("nameUser")

                    val content = jsonObjectComment.getString("content")

                    val commentModel = CommentModel(id, idUser, nameUser, content)

                    listComment.add(commentModel)
                }

                val fbFieldModel = FbFieldModel(
                    id.toLong(), idCity, idDistrict, photoUrl, name, phone, address, amountField, price, lat,
                    lng, countRating, rating, listComment
                )

                listField.add(fbFieldModel)

            }
        } catch (e: JSONException) {

            Log.d(Constant().TAG, "getListField fail : ${e.message}")
        }

        return listField
    }
}