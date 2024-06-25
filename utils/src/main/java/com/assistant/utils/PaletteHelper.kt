package com.assistant.utils

import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.palette.graphics.Palette
/**
 * 主色调工具类
 */
object PaletteHelper {

    /**
     * 设置图片主色调
     */
    @JvmStatic
    fun setPaletteView(imageView: ImageView, bitmap: Bitmap?, @ColorRes defaultColor: Int) {
        if (bitmap == null) {
            imageView.setBackgroundColor(imageView.resources.getColor(defaultColor))
        } else {
            Palette.from(bitmap).maximumColorCount(10).generate { palette ->
                val s: Palette.Swatch? = palette?.dominantSwatch //主色调
                if (s != null) {
                    imageView.setBackgroundColor(s.rgb)
                } else {
                    imageView.setBackgroundColor(imageView.resources.getColor(defaultColor))
                }
            }
        }
    }

    /**
     * 设置图片主色调
     */
    @JvmStatic
    fun setPaletteViewGuadual(imageView: ImageView, bitmap: Bitmap?, @ColorRes defaultColor: Int) {
        if (bitmap == null) {
            imageView.setBackgroundColor(imageView.resources.getColor(defaultColor))
        } else {
            Palette.from(bitmap).maximumColorCount(10).generate { palette ->
                val s: Palette.Swatch? = palette?.dominantSwatch //主色调
                if (s != null) {
                    val colors =
                        intArrayOf(s.rgb, imageView.resources.getColor(com.assistant.resources.R.color.transparent))
                    val drawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors)
                    drawable.gradientType = GradientDrawable.LINEAR_GRADIENT;
                    imageView.background = drawable
                } else {
                    val colors =
                        intArrayOf(defaultColor, imageView.resources.getColor(com.assistant.resources.R.color.transparent))
                    val drawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors)
                    drawable.gradientType = GradientDrawable.LINEAR_GRADIENT;
                    imageView.background = drawable
                }
            }
        }
    }
}

//                    val s1: Palette.Swatch? = palette.vibrantSwatch //获取到充满活力的这种色调
//                    val s2: Palette.Swatch? = palette.darkVibrantSwatch //获取充满活力的黑
//                    val s3: Palette.Swatch? = palette.lightVibrantSwatch //获取充满活力的亮
//                    val s4: Palette.Swatch? = palette.mutedSwatch //获取柔和的色调
//                    val s5: Palette.Swatch? = palette.darkMutedSwatch //获取柔和的黑
//                    val s6: Palette.Swatch? = palette.lightMutedSwatch //获取柔和的亮