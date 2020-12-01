package com.enclave.recipeproject.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipe(
    var id: Int? = null,
    var name: String,
    var step: String,
    var typeId: Int,
    var ingredients: String,
    var img: String
) : Parcelable