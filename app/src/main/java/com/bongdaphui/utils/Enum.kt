package com.bongdaphui.utils

class Enum {

    enum class EnumLogin(val value: Int) {
        Facebook(0),
        Google(1),
        Email(2)


    }

    enum class EnumTypeClick(val value: Int) {
        View(1),
        Phone(2),
        JoinClub(3),
        AddSchedule(4)
    }

    enum class EnumPosition(val value: Int, val namePos: String) {
        ThuMon(0, "Thủ Môn"),
        HauVeTrai(1, "Hậu Vệ Trái"),
        TrungVe(2, "Trung Vệ"),
        HauVePhai(3, "Hậu Vệ Phải"),
        TienVePhongNgu(4, "Tiền Vệ Phòng Ngự"),
        TienVeTrungTam(5, "Tiền Vệ Trung Tâm"),
        TienVeTanCong(6, "Tiền Vệ Tấn Công"),
        TienDaoTrai(7, "Tiền Đạo Trái"),
        TienDaoPhai(8, "Tiền Đạo Phải"),
        TienDaoLui(9, "Tiền Đạo Lùi"),
        TrungPhong(10, "Trung Phong")
    }

    enum class EnumTypeField(val value: String) {
        FivePeople("5"),
        SevenPeople("7"),
        ElevenPeople("11")

    }

}