package com.enclave.recipeproject.util

import android.view.View
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.enclave.recipeproject.R
import com.enclave.recipeproject.model.RecipeType
import java.io.File

@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean?) {
    isVisible?.let {
        view.visibility = if (it) View.VISIBLE else View.GONE
    }
}

@BindingAdapter("android:recipeImg")
fun setImg(image: ImageView, path: String?) {
    path?.let {
        val scale = when (image.scaleType) {
            ImageView.ScaleType.CENTER_CROP -> CenterCrop()
            ImageView.ScaleType.FIT_CENTER -> FitCenter()
            ImageView.ScaleType.CENTER_INSIDE -> CenterInside()
            else -> null
        }
        if (!URLUtil.isValidUrl(path)) { // image from local
            Glide.with(image.context)
                .load(File(path))
                .placeholder(R.drawable.image_holder)
                .transform(scale)
                .into(image)
        } else {
            Glide.with(image.context)
                .load(path)
                .placeholder(R.drawable.image_holder)
                .transform(scale)
                .into(image)
        }
    }
}

@BindingAdapter("typeId", "listRecipeType", requireAll = true)
fun bindTypeName(textView: TextView, typeId: Int?, listRecipeType: List<RecipeType>?) {
    if (typeId != null && listRecipeType != null)
        textView.text = listRecipeType.find { it.id == typeId }?.title ?: ""
}