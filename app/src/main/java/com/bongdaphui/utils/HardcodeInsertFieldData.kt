package com.bongdaphui.utils

import android.util.Log
import com.bongdaphui.listener.StepInterface
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.model.FieldModel1
import com.google.firebase.database.FirebaseDatabase

class HardcodeInsertFieldData {

    fun addDataCity(
        listFootballField: ArrayList<FieldModel1>,
//        listField: ArrayList<FbFieldModel>,
//        listCity: ArrayList<CityModel>,
        step: Int,
        stepInterface: StepInterface
    ) {

        val databaseReference = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_FIELD)


//        val id = "1"
        val listField = ArrayList<FbFieldModel>()
        /*val listFieldQuan2 = ArrayList<FbFieldModel>()
        val listFieldQuan3 = ArrayList<FbFieldModel>()
        val listFieldQuan4 = ArrayList<FbFieldModel>()
        val listFieldQuan5 = ArrayList<FbFieldModel>()
        val listFieldQuan6 = ArrayList<FbFieldModel>()
        val listFieldQuan7 = ArrayList<FbFieldModel>()
        val listFieldQuan8 = ArrayList<FbFieldModel>()
        val listFieldQuan9 = ArrayList<FbFieldModel>()
        val listFieldQuan10 = ArrayList<FbFieldModel>()
        val listFieldQuan11 = ArrayList<FbFieldModel>()
        val listFieldQuan12 = ArrayList<FbFieldModel>()
        val listFieldQuanThuDuc = ArrayList<FbFieldModel>()
        val listFieldQuanBinhThanh = ArrayList<FbFieldModel>()
        val listFieldQuanGoVap = ArrayList<FbFieldModel>()
        val listFieldQuanPhuNhuan = ArrayList<FbFieldModel>()
        val listFieldQuanTanPhu = ArrayList<FbFieldModel>()
        val listFieldQuanBinhTan = ArrayList<FbFieldModel>()
        val listFieldQuanTanBinh = ArrayList<FbFieldModel>()
        val listFieldQuanNhaBe = ArrayList<FbFieldModel>()
        val listFieldQuanBinhChanh = ArrayList<FbFieldModel>()
        val listFieldQuanHocMon = ArrayList<FbFieldModel>()
        val listFieldQuanCuChi = ArrayList<FbFieldModel>()
        val listFieldQuanCanGio = ArrayList<FbFieldModel>()
        val listFieldTanBinh = ArrayList<FbFieldModel>()
        val listFieldThuDuc = ArrayList<FbFieldModel>()*/

        //todo field district 1
        /* val district_1_0 = FbFieldModel(
             "0", "0", "0", "", "Tao Đàn",
             "0288250891", "Số 1 Huyền Trân Công Chúa, P. Bến Thành, Quận 1, Hồ Chí Minh",
             "", "", "", "", "", "", null
         )

         val district_1_1 = FbFieldModel(
             "1", "0", "0", "", "Nguyễn Du",
             "02838273865", "116 Nguyễn Du, P. Bến Thành, Quận 1, Hồ Chí Minh",
             "", "", "", "", "", "", null
         )
         listFieldQuan.add(district_1_1)
         listFieldQuan.add(district_1_0)*/

        //todo field  Binh Thanh

        /*for (i in 0 until listFootballField.size) {
            if ("-LdhhIhH4N7cj66Tu2MU" == listFootballField[i].idDistrict) {

                val field = FbFieldModel(

                    // todo NGUY HIEM nho cong them size cua danh sach hien tai de TANG ID CUA SAN khong bi ghi de -> mat du lieu
                    //TODO KIEM TRA ID CITY VA ID DISTRICT

                    "$step", "0", "13", "", "${listFootballField[i].name}",
                    "${listFootballField[i].phone}", "${listFootballField[i].address}",
                    "${listFootballField[i].amount_field}", "${listFootballField[i].price}",
                    "", "", "", "", null
                )
                listField.add(field)
            }
        }*/

        //todo field  Tan Binh

        /*for (i in 0 until listFootballField.size) {
            if ("-LdhhIuJ2dxiKnwWV_s_" == listFootballField[i].idDistrict) {
                val field = FbFieldModel(

                    // todo NGUY HIEM nho cong them size cua danh sach hien tai de TANG ID CUA SAN khong bi ghi de -> mat du lieu
                    //TODO KIEM TRA ID CITY VA ID DISTRICT

                    "${step + 4}", "0", "18", "", "${listFootballField[i].name}",
                    "${listFootballField[i].phone}", "${listFootballField[i].address}",
                    "${listFootballField[i].amount_field}", "${listFootballField[i].price}",
                    "", "", "", "", null
                )
                listField.add(field)
            }
        }*/

        //todo field  Thu Duc

        for (i in 0 until listFootballField.size) {
            if ("-LdhhIHirzeiQkWKwKig" == listFootballField[i].id_district) {
                val field = FbFieldModel(

                    // todo NGUY HIEM nho cong them size cua danh sach hien tai de TANG ID CUA SAN khong bi ghi de -> mat du lieu
                    //TODO KIEM TRA ID CITY VA ID DISTRICT
                    "${step + 13}", "0", "12", "", "${listFootballField[i].name}",
                    "${listFootballField[i].phone}", "${listFootballField[i].address}",
                    "${listFootballField[i].amount_field}", "${listFootballField[i].price}",
                    "", "", "", "", null
                )
                listField.add(field)
            }
        }

        //todo start

        Log.d(Constant().TAG, "list add size ${listField.size}")


        //todo khong thay doi trong vong for
        for (i in 0 until listField.size) {

            if (step == i) {

                databaseReference
//                    .child("districts")
//                    .child("${list[i].idDistrict}").child("fields")

                    // todo NGUY HIEM nho cong them size cua danh sach hien tai de khong bi ghi de -> mat du lieu

                    .child("${step + 13}").setValue(listField[i])

                    .addOnCompleteListener {

                        Log.d(
                            Constant().TAG,
                            "add field success ${listField[i].name} - con lai: ${listField.size - (step+1)} lan them"
                        )

                        stepInterface.onStep(step)

                    }
                    .addOnFailureListener {

                        Log.d(Constant().TAG, "add field fail : $it")

                    }
            }
        }
    }
}