package com.assistant.utils

import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.view.Window
import androidx.annotation.IntRange

object BrightnessUtils {

        val isAutoBrightnessEnabled: Boolean
            get() = try {
                val mode = Settings.System.getInt(
                    UtilsBridge.app.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE
                )
                mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
                false
            }

        /**
         * Enable or disable automatic brightness mode.
         *
         * Must hold `<uses-permission android:name="android.permission.WRITE_SETTINGS" />`
         *
         * @param enabled True to enabled, false otherwise.
         * @return `true`: success<br></br>`false`: fail
         */
        fun setAutoBrightnessEnabled(enabled: Boolean): Boolean {
            return Settings.System.putInt(
                UtilsBridge.app.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                if (enabled) Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC else Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            )
        }

        /**
         * 获取屏幕亮度
         *
         * @return 屏幕亮度 0-255
         */
        val brightness: Int
            get() {
                return try {
                    Settings.System.getInt(
                        UtilsBridge.app.contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS
                    )
                } catch (e: SettingNotFoundException) {
                    e.printStackTrace()
                    0
                }
            }

        /**
         * 设置屏幕亮度
         *
         * 需添加权限 `<uses-permission android:name="android.permission.WRITE_SETTINGS" />`
         * 并得到授权
         *
         * @param brightness 亮度值
         */
        fun setBrightness(@IntRange(from = 0, to = 255) brightness: Int): Boolean {
            val resolver = UtilsBridge.app.contentResolver
            val b = Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
            resolver.notifyChange(Settings.System.getUriFor("screen_brightness"), null)
            return b
        }

        /**
         * 设置窗口亮度
         *
         * @param window     窗口
         * @param brightness 亮度值
         */
        fun setWindowBrightness(
            window: Window,
            @IntRange(from = 0, to = 255) brightness: Int
        ) {
            val lp = window.attributes
            lp.screenBrightness = brightness / 255f
            window.attributes = lp
        }

        /**
         * 获取窗口亮度
         *
         * @param window 窗口
         * @return 屏幕亮度 0-255
         */
        fun getWindowBrightness(window: Window): Int {
            val lp = window.attributes
            val brightness = lp.screenBrightness.toInt()
            return if (brightness < 0) brightness else (brightness * 255).toInt()
        }
}