package com.bongdaphui.utils

import android.util.Log
import com.bongdaphui.listener.StepInterface
import com.bongdaphui.model.FbFieldModel
import com.google.firebase.database.FirebaseDatabase

class HardcodeInsertData {

    fun addDataCity(
//        listField: ArrayList<FbFieldModel>,
//        listCity: ArrayList<CityModel>,
        step: Int,
        stepInterface: StepInterface
    ) {

        val databaseReference = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_CITY)


//        val id = "1"
        val listFieldQuan1 = ArrayList<FbFieldModel>()
        val listFieldQuan2 = ArrayList<FbFieldModel>()
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

        val listFieldBinhThanh = ArrayList<FbFieldModel>()
        val listFieldTanBinh = ArrayList<FbFieldModel>()
        val listFieldThuDuc = ArrayList<FbFieldModel>()

        //todo field district 1
        val fieldModelQuan10 = FbFieldModel(
            "0", "GwpjEYwv5XgYPfd1BrRslIOzxuq2", "", "Tao Đàn",
            "0288250891", "Số 1 Huyền Trân Công Chúa, P. Bến Thành, Quận 1, Hồ Chí Minh", "", ""
        )

        val fieldModelQuan11 = FbFieldModel(
            "1", "GwpjEYwv5XgYPfd1BrRslIOzxuq2", "", "Nguyễn Du",
            "02838273865", "116 Nguyễn Du, P. Bến Thành, Quận 1, Hồ Chí Minh", "", ""
        )
        listFieldQuan1.add(fieldModelQuan10)
        listFieldQuan1.add(fieldModelQuan11)

        //todo field  Binh Thanh
        val fieldModelBT1 = FbFieldModel(
            "0", "GwpjEYwv5XgYPfd1BrRslIOzxuq2", "", "Việt Thắng",
            "0916970086", "127 Lê Văn Chí, P. Linh Trung, Quận Bình Thạnh, Hồ Chí Minh", "2", ""
        )
        val fieldModelBT2 = FbFieldModel(
            "0", "GwpjEYwv5XgYPfd1BrRslIOzxuq2", "", "Khiết Tâm",
            "0918097434", "251 Lê Thị Hoa, P. Bình Chiểu, Quận Bình Thạnh, Hồ Chí Minh", "1", "180000"
        )

        listFieldQuanBinhThanh.add(fieldModelBT1)
        listFieldQuanBinhThanh.add(fieldModelBT2)


        //todo field  Thu Duc
        val fieldModelQuanThuDuc1 = FbFieldModel(
            "0", "GwpjEYwv5XgYPfd1BrRslIOzxuq2", "", "Việt Thắng",
            "0916970086", "127 Lê Văn Chí, P. Linh Trung, Quận Thủ Đức, Hồ Chí Minh", "2", ""
        )
        val fieldModelQuanThuDuc2 = FbFieldModel(
            "0", "GwpjEYwv5XgYPfd1BrRslIOzxuq2", "", "Khiết Tâm",
            "0918097434", "251 Lê Thị Hoa, P. Bình Chiểu, Quận Thủ Đức, Hồ Chí Minh", "1", "180000"
        )

        listFieldQuanThuDuc.add(fieldModelQuanThuDuc1)
        listFieldQuanThuDuc.add(fieldModelQuanThuDuc2)


        //todo district
        /*val listDistrict = ArrayList<DistrictModel>()
        val districtModel0 = DistrictModel("0", "Quận 1", null)
        val districtModel1 = DistrictModel("1", "Quận 2", null)
        val districtModel2 = DistrictModel("2", "Quận 3", null)
        val districtModel3 = DistrictModel("3", "Quận 4", null)
        val districtModel4 = DistrictModel("4", "Quận 5", null)
        val districtModel5 = DistrictModel("5", "Quận 6", null)
        val districtModel6 = DistrictModel("6", "Quận 7", null)
        val districtModel7 = DistrictModel("7", "Quận 8", null)
        val districtModel8 = DistrictModel("8", "Quận 9", null)
        val districtModel9 = DistrictModel("9", "Quận 10", null)
        val districtModel10 = DistrictModel("10", "Quận 11", null)
        val districtModel11 = DistrictModel("11", "Quận 12", null)
        val districtModel12 = DistrictModel("12", "Quận Thủ Đức", null)
        val districtModel13 = DistrictModel("13", "Quận Bình Thạnh", null)
        val districtModel14 = DistrictModel("14", "Quận Gò Vấp", null)
        val districtModel15 = DistrictModel("15", "Quận Phú Nhuận", null)
        val districtModel16 = DistrictModel("16", "Quận Tân Phú", null)
        val districtModel17 = DistrictModel("17", "Quận Bình Tân", null)
        val districtModel18 = DistrictModel("18", "Quận Tân Bình", null)
        val districtModel19 = DistrictModel("19", "Huyện Nhà Bè", null)
        val districtModel20 = DistrictModel("20", "Huyện Bình Chánh", null)
        val districtModel21 = DistrictModel("21", "Huyện Hóc Môn", null)
        val districtModel22 = DistrictModel("22", "Huyện Củ Chi", null)
        val districtModel23 = DistrictModel("23", "Huyện Cần Giờ", null)

        listDistrict.add(districtModel0)
        listDistrict.add(districtModel1)
        listDistrict.add(districtModel2)
        listDistrict.add(districtModel3)
        listDistrict.add(districtModel4)
        listDistrict.add(districtModel5)
        listDistrict.add(districtModel6)
        listDistrict.add(districtModel7)
        listDistrict.add(districtModel8)
        listDistrict.add(districtModel9)
        listDistrict.add(districtModel10)
        listDistrict.add(districtModel11)
        listDistrict.add(districtModel12)
        listDistrict.add(districtModel13)
        listDistrict.add(districtModel14)
        listDistrict.add(districtModel15)
        listDistrict.add(districtModel16)
        listDistrict.add(districtModel17)
        listDistrict.add(districtModel18)
        listDistrict.add(districtModel19)
        listDistrict.add(districtModel20)
        listDistrict.add(districtModel21)
        listDistrict.add(districtModel22)
        listDistrict.add(districtModel23)*/


//        val cityModel = CityModel("1", "Quận 2", listDistrict)


        /*for (i in 0 until listField.size) {
            if ("-LdhhIhH4N7cj66Tu2MU" == listField[i].idDistrict) {
                val fieldModel = FbFieldModel(
                    "$i", listField[i].idUser, "", listField[i].name,
                    listField[i].phone, listField[i].address, listField[i].amount_field, listField[i].price_field
                )

                listFieldBinhThanh.add(fieldModel)
            } else if ("-LdhhIHirzeiQkWKwKig" == listField[i].idDistrict) {
                val fieldModel = FbFieldModel(
                    "$i", listField[i].idUser, "", listField[i].name,
                    listField[i].phone, listField[i].address, listField[i].amount_field, listField[i].price_field
                )

                listFieldThuDuc.add(fieldModel)
            } else if ("-LdhhIuJ2dxiKnwWV_s_" == listField[i].idDistrict) {
                val fieldModel = FbFieldModel(
                    "$i", listField[i].idUser, "", listField[i].name,
                    listField[i].phone, listField[i].address, listField[i].amount_field, listField[i].price_field
                )

                listFieldTanBinh.add(fieldModel)
            }
        }

        val listHCM = ArrayList<DistrictModel>()
        val listHN = ArrayList<DistrictModel>()
        val listDN = ArrayList<DistrictModel>()

        for (i in 0 until listCity.size) {

            if ("0" == listCity[i].id) {

                var districtModel: DistrictModel

                for (j in 0 until listCity[i].districts!!.size) {

                    if ("14" == listCity[i].districts!![j].id) {

                        districtModel = DistrictModel("$j", listCity[i].districts!![j].name, listFieldBinhThanh)

                    } else if ("12" == listCity[i].districts!![j].id) {

                        districtModel = DistrictModel("$j", listCity[i].districts!![j].name, listFieldThuDuc)

                    } else if ("15" == listCity[i].districts!![j].id) {

                        districtModel = DistrictModel("$j", listCity[i].districts!![j].name, listFieldTanBinh)

                    } else {

                        districtModel = DistrictModel("$j", listCity[i].districts!![j].name, ArrayList())
                    }

                    listHCM.add(districtModel)
                }
            } else if ("1" == listCity[i].id) {

                var districtModel: DistrictModel

                for (j in 0 until listCity[i].districts!!.size) {


                    districtModel = DistrictModel("$j", listCity[i].districts!![j].name, ArrayList())

                    listHN.add(districtModel)
                }
            } else if ("2" == listCity[i].id) {

                var districtModel: DistrictModel

                for (j in 0 until listCity[i].districts!!.size) {

                    districtModel = DistrictModel("$j", listCity[i].districts!![j].name, ArrayList())

                    listDN.add(districtModel)
                }
            }
        }


        val cityModel1 = CityModel("0", "Hồ Chí Minh", listHCM)
        val cityModel2 = CityModel("1", "Hà Nội", listHN)
        val cityModel3 = CityModel("2", "Đà Nẵng", listDN)

        val listCity = ArrayList<CityModel>()

        listCity.add(cityModel1)
        listCity.add(cityModel2)
        listCity.add(cityModel3)*/

//        Log.d(Constant().TAG, "size: ${listDistrict.size} - step:  $step")

//        for (i in 0 until listDistrict.size) { // todo add district
        for (i in 0 until listFieldQuanThuDuc.size) { // todo add field to district, remember change id of district

            if (step == i) {

//                databaseReference.child("0").child("districts").child("$step").setValue(listDistrict[i]) // todo add district
                databaseReference.child("0").child("districts")
                    .child("13").child("fields").child("$step").setValue(listFieldQuanThuDuc[i]) //todo add field

                    .addOnCompleteListener {

//                        Log.d(Constant().TAG, "success ${listDistrict[i].name}")

                        stepInterface.onStep(step)

                    }
                    .addOnFailureListener {

                        Log.d(Constant().TAG, "fail")

                    }
            }
        }
    }
}