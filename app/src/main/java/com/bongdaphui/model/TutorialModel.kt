package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class TutorialModel(
    val id: String? = "",
    val photo: String? = "",
    val title: String? = "",
    val content: String? = ""
) : Serializable