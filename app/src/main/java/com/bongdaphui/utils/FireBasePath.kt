package com.bongdaphui.utils

import com.bongdaphui.BuildConfig

class FireBasePath {

    val collectionUser = if (BuildConfig.DEBUG) "dev_users" else "users"

    val collectionTutorial = "tutorial"

    val collectionField = "fields"

    val collectionRequestField = if (BuildConfig.DEBUG) "dev_fields_request" else "fields_request"

    val collectionSchedulePlayer = if (BuildConfig.DEBUG) "dev_schedule_player" else "schedule_player"

    val collectionScheduleClub = if (BuildConfig.DEBUG) "dev_schedule_club" else "schedule_club"

    val collectionClub = if (BuildConfig.DEBUG) "dev_clubs" else "clubs"

    val collectionRequestJoinClub = if (BuildConfig.DEBUG) "dev_requests" else "requests"

    var storageUser = if (BuildConfig.DEBUG) "dev/user_image/" else "user_image/"

    val storageField = if (BuildConfig.DEBUG) "dev/field_image/" else "field_image/"

    var storageClub = if (BuildConfig.DEBUG) "dev/club_image/" else "club_image/"

    var storageTutorial = "tutorial_image/"


}