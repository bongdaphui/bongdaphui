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
        JoinClub(3)
    }

    enum class EnumPosition(val value: Int, val namePos: String) {
        GK(0, "Thủ Môn"),
        DF(1, "Hậu Vệ"),
        MF(2, "Tiền Vệ"),
        SK(3, "Tiền Đạo")
    }

    enum class EnumConfirmYes(val value: Int) {
        Logout(0),
        DeniedPermission(1),
        RequestJoinClubFail(2),
        RequestJoinClubSuccess(3),

    }

}