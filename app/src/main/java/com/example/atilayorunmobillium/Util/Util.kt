package com.example.atilayorunmobillium.Util

import android.widget.ImageView
import com.bumptech.glide.Glide

object Util {
    fun ImageView.loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .into(this)
    }
}