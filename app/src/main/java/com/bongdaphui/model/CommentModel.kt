package com.bongdaphui.model

import java.io.Serializable

data class CommentModel(
    val id: String? = "",
    val idUser: String? = "",
    val nameUser: String? = "",
    val photoUser: String? = "",
    val content: String? = "",
    val time: String? = ""
) : Serializable