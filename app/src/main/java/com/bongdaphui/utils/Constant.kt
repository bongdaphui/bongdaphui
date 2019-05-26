package com.bongdaphui.utils

import android.Manifest
import com.bongdaphui.BuildConfig

class Constant {

    val TAG = "linhnt"

    val roomData = "db_bongdaphui"

    val collectionPathField = if (BuildConfig.DEBUG) "dev_fields" else "fields"

    val userPathField = if (BuildConfig.DEBUG) "dev_users" else "users"

    val clubPathField = if (BuildConfig.DEBUG) "dev_clubs" else "clubs"

    val requestJoinPathField = if (BuildConfig.DEBUG) "dev_requests" else "requests"

    val schedulePlayerPathField = if (BuildConfig.DEBUG) "dev_schedule_player" else "schedule_player"

    val scheduleClubPathField = if (BuildConfig.DEBUG) "dev_schedule_club" else "schedule_club"

    val pathStorageField = if (BuildConfig.DEBUG) "dev/field_image/" else "field_image/"

    var pathStorageUser = if (BuildConfig.DEBUG) "dev/user_image/" else "user_image/"

    val emailRegularExpression =
        "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    // Root Database Name for Firebase Database.

    var DATABASE_CITY = "data_city"

    var DATABASE_CLUB = "data_club"

    var DATABASE_FIELD = "data_field"

    var DATABASE_USER = "data_user"

    var DATABASE_JOIN_CLUB = "data_join_club"

    var CHILD_PLAYERS = "players"

    var STORAGE_CLUB = if (BuildConfig.DEBUG) "dev/club_image/" else "club_image/"

    var STORAGE_PLAYER = if (BuildConfig.DEBUG) "dev/player_image/" else "player_image/"

    val sharePreferenceName = "sharePrefenceFile"

    val KEY_SHARED_PREFERENCES = "football.com.manager"

    val KEY_LOGIN_UID_USER = "uid"
    val KEY_LOGIN_TYPE_USER = "type"

    val LENGTH_PASS_WORD = 6

    val sizeImage = 100F //0.1 MB

    val CAMERA_WRITE_EXTERNAL_PERMISSIONS =
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val CALL_PHONE =
        arrayOf(Manifest.permission.CALL_PHONE)

    val ONE_DECIMAL_FORMAT = "#,###,###,###.#"


}