package com.assistant.utils

import android.graphics.Bitmap
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette

/**
 * 主色调工具类
 */
object PaletteHelper {

    /**
     * 设置图片主色调
     *
     * @param bitmap
     * @return
     */
    @JvmStatic
    fun getPaletteColor(bitmap: Bitmap?, defaultColor: Int): Int {
        var targetColor = defaultColor
        if (bitmap == null) {
            return targetColor
        }

        Palette.from(bitmap).maximumColorCount(10).generate { palette ->
            val s: Palette.Swatch? = palette?.dominantSwatch //主色调
            targetColor = palette?.dominantSwatch?.rgb ?: defaultColor
        }
        return targetColor
    }
}

//                    val s1: Palette.Swatch? = palette.vibrantSwatch //获取到充满活力的这种色调
//                    val s2: Palette.Swatch? = palette.darkVibrantSwatch //获取充满活力的黑
//                    val s3: Palette.Swatch? = palette.lightVibrantSwatch //获取充满活力的亮
//                    val s4: Palette.Swatch? = palette.mutedSwatch //获取柔和的色调
//                    val s5: Palette.Swatch? = palette.darkMutedSwatch //获取柔和的黑
//                    val s6: Palette.Swatch? = palette.lightMutedSwatch //获取柔和的亮