package com.bongdaphui.model

import java.io.Serializable

data class CommentModel(
    val id: String? = "",
    val idUser: String? = "",
    val nameUser: String? = "",
    val content: String? = ""
) : Serializable