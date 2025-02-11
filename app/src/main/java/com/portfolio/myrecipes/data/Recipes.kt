package com.portfolio.myrecipes.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipes(
    val name :String,
    val time : String,
    val serving : String,
    val ingredient : String,
    val step : String,
    val photo : String
) : Parcelable
