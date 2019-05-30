package com.bongdaphui.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
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
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.AcceptListener
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.model.CityModel
import com.bongdaphui.model.CommentModel
import com.bongdaphui.model.DistrictModel
import com.bongdaphui.model.FbFieldModel
import org.json.JSONArray
import org.json.JSONException
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
        return email.matches(Constant().emailRegularExpression.toRegex())
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

    fun getRandomNumberString(): String? {
        // It will generate 3 digit random Number.
        // from 0 to 999
        val rnd = Random()
        val number = rnd.nextInt(999)

        // this will convert any number sequence into 6 character.
        return String.format("%03d", number)
    }

    fun validatePhoneNumber(number: String): Boolean {
        return PhoneNumberUtils.isGlobalPhoneNumber(number) && number.length > 9
    }

    fun showToastInsert(activity: Activity?, isSuccess: Boolean) {

        Toast.makeText(
            activity,
            if (isSuccess) "Thêm thành công" else "Thêm thất bại. Vui lòng thực hiện lại",
            Toast.LENGTH_LONG
        ).show()

        activity!!.onBackPressed()
    }

    fun showToastUpdate(activity: Activity?, isSuccess: Boolean) {

        Toast.makeText(
            activity,
            if (isSuccess) "Cập nhật thông tin thành công" else "Cập nhật thất bại. Vui lòng thực hiện lại",
            Toast.LENGTH_LONG
        ).show()

        activity!!.onBackPressed()
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    fun getFileExtension(activity: Activity?, uri: Uri): String? {

        val contentResolver = activity!!.contentResolver

        val mimeTypeMap = MimeTypeMap.getSingleton()

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))

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
        itemCity: Int,
        spDistrict: Spinner,
        itemDistrict: Int,
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

                if (itemCity.toString() == idCity) {

                    spDistrict.setSelection(itemDistrict)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(Constant().tag, "spinner onNothingSelected")

            }
        }
        spCity.setSelection(itemCity)

        spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                val districtName: String = parent.getItemAtPosition(position) as String

                idDistrict = getIdDistrictFromNameDistrict(idCity, districtName, listCity)

                spInterface.onSelectCity(idCity, idDistrict)

//                Log.d(Constant().tag, "district spinner select with idCity: $idCity - idDistrict : $idDistrict")

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(Constant().tag, "spinner onNothingSelected")

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

    fun getNameCityDistrictFromId(context: Context, idCity: String, idDistrict: String?): String {

        var s = ""

        val listCity = getListCity(context)

        for (i in 0 until listCity.size) {

            if (idCity == listCity[i].id) {

                for (j in 0 until (listCity[i].districts!!.size)) {

                    if (idDistrict == listCity[i].districts!![j].id) {

                        s = "${listCity[i].districts!![j].name}, ${listCity[i].name}"
                    }
                }
            }
        }
        return s
    }

    fun openDial(context: Context, phone: String) {

        val intent = Intent(Intent.ACTION_DIAL)

        intent.data = Uri.parse("tel:$phone")

        context.startActivity(intent)
    }

    fun initPositionSpinner(context: Context, sp: Spinner) {
        val listPosition: ArrayList<String> = ArrayList()
        listPosition.add(Enum.EnumPosition.ThuMon.namePos)
        listPosition.add(Enum.EnumPosition.HauVeTrai.namePos)
        listPosition.add(Enum.EnumPosition.TrungVe.namePos)
        listPosition.add(Enum.EnumPosition.HauVePhai.namePos)
        listPosition.add(Enum.EnumPosition.TienVePhongNgu.namePos)
        listPosition.add(Enum.EnumPosition.TienVeTrungTam.namePos)
        listPosition.add(Enum.EnumPosition.TienVeTanCong.namePos)
        listPosition.add(Enum.EnumPosition.TienDaoTrai.namePos)
        listPosition.add(Enum.EnumPosition.TienDaoPhai.namePos)
        listPosition.add(Enum.EnumPosition.TienDaoLui.namePos)
        listPosition.add(Enum.EnumPosition.TrungPhong.namePos)

        val adapter = SpinnerAdapter(context, R.layout.item_spinner, listPosition)

        sp.adapter = adapter
    }

    fun getPosition(pos: Int): String {

        var position = ""


        when (pos) {
            Enum.EnumPosition.ThuMon.value -> position = Enum.EnumPosition.ThuMon.namePos
            Enum.EnumPosition.HauVeTrai.value -> position = Enum.EnumPosition.HauVeTrai.namePos
            Enum.EnumPosition.TrungVe.value -> position = Enum.EnumPosition.TrungVe.namePos
            Enum.EnumPosition.HauVePhai.value -> position = Enum.EnumPosition.HauVePhai.namePos
            Enum.EnumPosition.TienVePhongNgu.value -> position = Enum.EnumPosition.TienVePhongNgu.namePos
            Enum.EnumPosition.TienVeTrungTam.value -> position = Enum.EnumPosition.TienVeTrungTam.namePos
            Enum.EnumPosition.TienVeTanCong.value -> position = Enum.EnumPosition.TienVeTanCong.namePos
            Enum.EnumPosition.TienDaoTrai.value -> position = Enum.EnumPosition.TienDaoTrai.namePos
            Enum.EnumPosition.TienDaoPhai.value -> position = Enum.EnumPosition.TienDaoPhai.namePos
            Enum.EnumPosition.TienDaoLui.value -> position = Enum.EnumPosition.TienDaoLui.namePos
            Enum.EnumPosition.TrungPhong.value -> position = Enum.EnumPosition.TrungPhong.namePos
        }


        return position


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

            Log.d(Constant().tag, "loadJSONFromAsset fail : ${e.message}")

            return null
        }

        return json

    }

    private fun getListCity(context: Context): ArrayList<CityModel> {

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

            Log.d(Constant().tag, "getListCity fail : ${e.message}")
        }

        return listCity
    }

    /*fun getListField(context: Context): ArrayList<FbFieldModel> {

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

                    val idUser = jsonObjectComment.getString("idCaptain")

                    val nameUser = jsonObjectComment.getString("nameUser")

                    val content = jsonObjectComment.getString("content")

                    val commentModel = CommentModel(id, idUser, nameUser, content)

                    listComment.add(commentModel)
                }

                val fbFieldModel = FbFieldModel(
                    id.toLong(), idCity, idDistrict, photoUrl, name, phone, address, amountField, price, lat,
                    lng, countRating, rating
                )

                listField.add(fbFieldModel)

            }
        } catch (e: JSONException) {

            Log.d(Constant().tag, "getListField fail : ${e.message}")
        }

        return listField
    }*/

    fun hiddenRefresh(swipeRefreshLayout: SwipeRefreshLayout?) {

        if (null != swipeRefreshLayout && swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    fun getTypeField(listType: String): String {

        val typeField = StringBuilder()
        if (listType.contains(Enum.EnumTypeField.FivePeople.value)) typeField.append("${Enum.EnumTypeField.FivePeople.value}   ")
        if (listType.contains(Enum.EnumTypeField.SevenPeople.value)) typeField.append("${Enum.EnumTypeField.SevenPeople.value}   ")
        if (listType.contains(Enum.EnumTypeField.ElevenPeople.value)) typeField.append(Enum.EnumTypeField.ElevenPeople.value)

        return typeField.toString()
    }

    fun isNoDistrict(idDistrict: String, isShowDialog: Boolean, context: Context): Boolean {

        var validate = false

        if ("0" == idDistrict) {
            if (isShowDialog) {
                context.let {
                    AlertDialog().showCustomDialog(
                        it,
                        context.resources.getString(R.string.alert),
                        context.resources.getString(R.string.please_choose_district),
                        "",
                        context.resources.getString(R.string.agree),
                        object : AcceptListener {
                            override fun onAccept(message: String) {
                            }
                        }
                    )
                }
            }
            validate = true
        }
        return validate
    }
}