package br.com.jpm.netflixrmk.model

import androidx.annotation.DrawableRes

data class Movie(
    val id: Int,
    val coverUrl: String,
    val title: String = "",
    val des: String = "",
    val cast: String = ""
)
