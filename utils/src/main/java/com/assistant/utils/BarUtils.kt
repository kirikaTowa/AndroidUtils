package com.assistant.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi

class BarUtils private constructor() {
    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {
        ///////////////////////////////////////////////////////////////////////////
        // status bar
        ///////////////////////////////////////////////////////////////////////////
        private const val TAG_STATUS_BAR = "TAG_STATUS_BAR"
        private const val TAG_OFFSET = "TAG_OFFSET"
        private const val KEY_OFFSET = -123

        /**
         * Return the status bar's height.
         *
         * @return the status bar's height
         */
        val statusBarHeight: Int
            get() {
                val resources = Resources.getSystem()
                val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
                return resources.getDimensionPixelSize(resourceId)
            }

        /**
         * Set the status bar's visibility.
         *
         * @param activity  The activity.
         * @param isVisible True to set status bar visible, false otherwise.
         */
        fun setStatusBarVisibility(
            activity: Activity,
            isVisible: Boolean
        ) {
            setStatusBarVisibility(activity.window, isVisible)
        }

        /**
         * Set the status bar's visibility.
         *
         * @param window    The window.
         * @param isVisible True to set status bar visible, false otherwise.
         */
        fun setStatusBarVisibility(
            window: Window,
            isVisible: Boolean
        ) {
            if (isVisible) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                showStatusBarView(window)
                addMarginTopEqualStatusBarHeight(window)
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                hideStatusBarView(window)
                subtractMarginTopEqualStatusBarHeight(window)
            }
        }

        /**
         * Return whether the status bar is visible.
         *
         * @param activity The activity.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isStatusBarVisible(activity: Activity): Boolean {
            val flags = activity.window.attributes.flags
            return flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0
        }

        /**
         * Set the status bar's light mode.
         *
         * @param activity    The activity.
         * @param isLightMode True to set status bar light mode, false otherwise.
         */
        fun setStatusBarLightMode(
            activity: Activity,
            isLightMode: Boolean
        ) {
            setStatusBarLightMode(activity.window, isLightMode)
        }

        /**
         * Set the status bar's light mode.
         *
         * @param window      The window.
         * @param isLightMode True to set status bar light mode, false otherwise.
         */
        fun setStatusBarLightMode(
            window: Window,
            isLightMode: Boolean
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val decorView = window.decorView
                var vis = decorView.systemUiVisibility
                vis = if (isLightMode) {
                    vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
                decorView.systemUiVisibility = vis
            }
        }

        /**
         * Is the status bar light mode.
         *
         * @param activity The activity.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isStatusBarLightMode(activity: Activity): Boolean {
            return isStatusBarLightMode(activity.window)
        }

        /**
         * Is the status bar light mode.
         *
         * @param window The window.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isStatusBarLightMode(window: Window): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val decorView = window.decorView
                val vis = decorView.systemUiVisibility
                return vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR != 0
            }
            return false
        }

        /**
         * Add the top margin size equals status bar's height for view.
         *
         * @param view The view.
         */
        fun addMarginTopEqualStatusBarHeight(view: View) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
            view.tag = TAG_OFFSET
            val haveSetOffset = view.getTag(KEY_OFFSET)
            if (haveSetOffset != null && haveSetOffset as Boolean) return
            val layoutParams = view.layoutParams as MarginLayoutParams
            layoutParams.setMargins(
                layoutParams.leftMargin,
                layoutParams.topMargin + statusBarHeight,
                layoutParams.rightMargin,
                layoutParams.bottomMargin
            )
            view.setTag(KEY_OFFSET, true)
        }



        private fun addMarginTopEqualStatusBarHeight(window: Window) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
            val withTag = window.decorView.findViewWithTag<View>(TAG_OFFSET)
                ?: return
            addMarginTopEqualStatusBarHeight(withTag)
        }

        private fun subtractMarginTopEqualStatusBarHeight(window: Window) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
            val withTag = window.decorView.findViewWithTag<View>(TAG_OFFSET)
                ?: return
            subtractMarginTopEqualStatusBarHeight(withTag)
        }

        fun subtractMarginTopEqualStatusBarHeight(view: View) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
            val haveSetOffset = view.getTag(KEY_OFFSET)
            if (haveSetOffset == null || !(haveSetOffset as Boolean)) return
            val layoutParams = view.layoutParams as MarginLayoutParams
            layoutParams.setMargins(
                layoutParams.leftMargin,
                layoutParams.topMargin - BarUtils.statusBarHeight,
                layoutParams.rightMargin,
                layoutParams.bottomMargin
            )
            view.setTag(KEY_OFFSET, false)
        }

        private fun hideStatusBarView(activity: Activity) {
            hideStatusBarView(activity.window)
        }

        private fun hideStatusBarView(window: Window) {
            val decorView = window.decorView as ViewGroup
            val fakeStatusBarView = decorView.findViewWithTag<View>(TAG_STATUS_BAR)
                ?: return
            fakeStatusBarView.visibility = View.GONE
        }

        private fun showStatusBarView(window: Window) {
            val decorView = window.decorView as ViewGroup
            val fakeStatusBarView = decorView.findViewWithTag<View>(TAG_STATUS_BAR)
                ?: return
            fakeStatusBarView.visibility = View.VISIBLE
        }

        private fun createStatusBarView(
            context: Context,
            color: Int
        ): View {
            val statusBarView = View(context)
            statusBarView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight
            )
            statusBarView.setBackgroundColor(color)
            statusBarView.tag = TAG_STATUS_BAR
            return statusBarView
        }

        fun transparentStatusBar(activity: Activity) {
            transparentStatusBar(activity.window)
        }

        fun transparentStatusBar(window: Window) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                val option =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                val vis = window.decorView.systemUiVisibility
                window.decorView.systemUiVisibility = option or vis
                window.statusBarColor = Color.TRANSPARENT
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }




        val navBarHeight: Int
            get() {
                val res = Resources.getSystem()
                val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
                return if (resourceId != 0) {
                    res.getDimensionPixelSize(resourceId)
                } else {
                    0
                }
            }

        /**
         * Set the navigation bar's visibility.
         *
         * @param activity  The activity.
         * @param isVisible True to set navigation bar visible, false otherwise.
         */
        fun setNavBarVisibility(activity: Activity, isVisible: Boolean) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
            setNavBarVisibility(activity.window, isVisible)
        }

        /**
         * Set the navigation bar's visibility.
         *
         * @param window    The window.
         * @param isVisible True to set navigation bar visible, false otherwise.
         */
        fun setNavBarVisibility(window: Window, isVisible: Boolean) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
            val decorView = window.decorView as ViewGroup
            var i = 0
            val count = decorView.childCount
            while (i < count) {
                val child = decorView.getChildAt(i)
                val id = child.id
                if (id != View.NO_ID) {
                    val resourceEntryName = getResNameById(id)
                    if ("navigationBarBackground" == resourceEntryName) {
                        child.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                    }
                }
                i++
            }
            val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            if (isVisible) {
                decorView.systemUiVisibility = decorView.systemUiVisibility and uiOptions.inv()
            } else {
                decorView.systemUiVisibility = decorView.systemUiVisibility or uiOptions
            }
        }

        /**
         * Return whether the navigation bar visible.
         *
         * Call it in onWindowFocusChanged will get right result.
         *
         * @param activity The activity.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isNavBarVisible(activity: Activity): Boolean {
            return isNavBarVisible(activity.window)
        }

        /**
         * Return whether the navigation bar visible.
         *
         * Call it in onWindowFocusChanged will get right result.
         *
         * @param window The window.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isNavBarVisible(window: Window): Boolean {
            var isVisible = false
            val decorView = window.decorView as ViewGroup
            var i = 0
            val count = decorView.childCount
            while (i < count) {
                val child = decorView.getChildAt(i)
                val id = child.id
                if (id != View.NO_ID) {
                    val resourceEntryName = getResNameById(id)
                    if ("navigationBarBackground" == resourceEntryName && child.visibility == View.VISIBLE) {
                        isVisible = true
                        break
                    }
                }
                i++
            }
            if (isVisible) {
                // 对于三星手机，android10以下非OneUI2的版本，比如 s8，note8 等设备上，
                // 导航栏显示存在bug："当用户隐藏导航栏时显示输入法的时候导航栏会跟随显示"，会导致隐藏输入法之后判断错误
                // 这个问题在 OneUI 2 & android 10 版本已修复
                if (UtilsBridge.isSamsung && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    try {
                        return Settings.Global.getInt(
                            UtilsBridge.app.contentResolver,
                            "navigationbar_hide_bar_enabled"
                        ) == 0
                    } catch (ignore: Exception) {
                    }
                }
                val visibility = decorView.systemUiVisibility
                isVisible = visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0
            }
            return isVisible
        }

        private fun getResNameById(id: Int): String {
            return try {
                UtilsBridge.app.resources.getResourceEntryName(id)
            } catch (ignore: Exception) {
                ""
            }
        }

        /**
         * Set the navigation bar's color.
         *
         * @param activity The activity.
         * @param color    The navigation bar's color.
         */
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun setNavBarColor(activity: Activity, @ColorInt color: Int) {
            setNavBarColor(activity.window, color)
        }

        /**
         * Set the navigation bar's color.
         *
         * @param window The window.
         * @param color  The navigation bar's color.
         */
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun setNavBarColor(window: Window, @ColorInt color: Int) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.navigationBarColor = color
        }

        /**
         * Return the color of navigation bar.
         *
         * @param activity The activity.
         * @return the color of navigation bar
         */
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun getNavBarColor(activity: Activity): Int {
            return getNavBarColor(activity.window)
        }

        /**
         * Return the color of navigation bar.
         *
         * @param window The window.
         * @return the color of navigation bar
         */
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun getNavBarColor(window: Window): Int {
            return window.navigationBarColor
        }

        /**
         * Return whether the navigation bar visible.
         *
         * @return `true`: yes<br></br>`false`: no
         */
        val isSupportNavBar: Boolean
            get() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    val wm = UtilsBridge.app
                        .getSystemService(Context.WINDOW_SERVICE) as WindowManager
                        ?: return false
                    val display = wm.defaultDisplay
                    val size = Point()
                    val realSize = Point()
                    display.getSize(size)
                    display.getRealSize(realSize)
                    return realSize.y != size.y || realSize.x != size.x
                }
                val menu = ViewConfiguration.get(UtilsBridge.app).hasPermanentMenuKey()
                val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
                return !menu && !back
            }

        /**
         * Set the nav bar's light mode.
         *
         * @param activity    The activity.
         * @param isLightMode True to set nav bar light mode, false otherwise.
         */
        fun setNavBarLightMode(
            activity: Activity,
            isLightMode: Boolean
        ) {
            setNavBarLightMode(activity.window, isLightMode)
        }

        /**
         * Set the nav bar's light mode.
         *
         * @param window      The window.
         * @param isLightMode True to set nav bar light mode, false otherwise.
         */
        fun setNavBarLightMode(
            window: Window,
            isLightMode: Boolean
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val decorView = window.decorView
                var vis = decorView.systemUiVisibility
                vis = if (isLightMode) {
                    vis or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                } else {
                    vis and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
                }
                decorView.systemUiVisibility = vis
            }
        }

        /**
         * Is the nav bar light mode.
         *
         * @param activity The activity.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isNavBarLightMode(activity: Activity): Boolean {
            return isNavBarLightMode(activity.window)
        }

        /**
         * Is the nav bar light mode.
         *
         * @param window The window.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isNavBarLightMode(window: Window): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val decorView = window.decorView
                val vis = decorView.systemUiVisibility
                return vis and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR != 0
            }
            return false
        }

        fun transparentNavBar(activity: Activity) {
            transparentNavBar(activity.window)
        }

        fun transparentNavBar(window: Window) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.navigationBarColor = Color.TRANSPARENT
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (window.attributes.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION == 0) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                }
            }
            val decorView = window.decorView
            val vis = decorView.systemUiVisibility
            val option =
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = vis or option
        }
    }
}