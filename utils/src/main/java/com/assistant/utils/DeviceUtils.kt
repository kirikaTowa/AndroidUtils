package com.assistant.utils

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Enumeration
import java.util.Locale
import java.util.UUID

object DeviceUtils {

        val isDeviceRooted: Boolean
            get() {
                val su: String = "su"
                val locations: Array<String> = arrayOf(
                    "/system/bin/",
                    "/system/xbin/",
                    "/sbin/",
                    "/system/sd/xbin/",
                    "/system/bin/failsafe/",
                    "/data/local/xbin/",
                    "/data/local/bin/",
                    "/data/local/",
                    "/system/sbin/",
                    "/usr/bin/",
                    "/vendor/bin/"
                )
                for (location: String in locations) {
                    if (File(location + su).exists()) {
                        return true
                    }
                }
                return false
            }

        /**
         * Return whether ADB is enabled.
         *
         * @return `true`: yes<br></br>`false`: no
         */
        @get:RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        val isAdbEnabled: Boolean
            get() = Settings.Secure.getInt(
                UtilsBridge.app.contentResolver,
                Settings.Global.ADB_ENABLED, 0
            ) > 0

        /**
         * Return the version name of device's system.
         *
         * @return the version name of device's system
         */
        val sDKVersionName: String
            get() {
                return Build.VERSION.RELEASE
            }

        /**
         * Return version code of device's system.
         *
         * @return version code of device's system
         */
        val sDKVersionCode: Int
            get() {
                return Build.VERSION.SDK_INT
            }

        /**
         * Return the android id of device.
         *
         * @return the android id of device
         */
        @get:SuppressLint("HardwareIds")
        val androidID: String
            get() {
                val id: String? = Settings.Secure.getString(
                    UtilsBridge.app.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
                if (("9774d56d682e549c" == id)) return ""
                return if (id == null) "" else id
            }

        //    /**
        //     * Return the MAC address.
        //     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
        //     * {@code <uses-permission android:name="android.permission.INTERNET" />},
        //     * {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
        //     *
        //     * @return the MAC address
        //     */
        //    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, CHANGE_WIFI_STATE})
        //    public static String getMacAddress() {
        //        String macAddress = getMacAddress((String[]) null);
        //        if (!TextUtils.isEmpty(macAddress) || getWifiEnabled()) return macAddress;
        //        setWifiEnabled(true);
        //        setWifiEnabled(false);
        //        return getMacAddress((String[]) null);
        //    }
        val wifiEnabled: Boolean
            get() {
                @SuppressLint("WifiManagerLeak") val manager: WifiManager? =
                    UtilsBridge.app.getSystemService(
                        Context.WIFI_SERVICE
                    ) as WifiManager
                if (manager == null) return false
                return manager.isWifiEnabled
            }

        /**
         * Enable or disable wifi.
         *
         * Must hold `<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />`
         *
         * @param enabled True to enabled, false otherwise.
         */
        @RequiresPermission(permission.CHANGE_WIFI_STATE)
        private fun setWifiEnabled(enabled: Boolean) {
            @SuppressLint("WifiManagerLeak") val manager: WifiManager =
                UtilsBridge.app.getSystemService(
                    Context.WIFI_SERVICE
                ) as WifiManager ?: return
            if (enabled == manager.isWifiEnabled) return
            manager.isWifiEnabled = enabled
        }

        //    /**
        //     * Return the MAC address.
        //     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
        //     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
        //     *
        //     * @return the MAC address
        //     */
        //    @RequiresPermission(allOf = {ACCESS_WIFI_STATE})
        //    public static String getMacAddress(final String... excepts) {
        //        String macAddress = getMacAddressByNetworkInterface();
        //        if (isAddressNotInExcepts(macAddress, excepts)) {
        //            return macAddress;
        //        }
        //        macAddress = getMacAddressByInetAddress();
        //        if (isAddressNotInExcepts(macAddress, excepts)) {
        //            return macAddress;
        //        }
        //        macAddress = getMacAddressByWifiInfo();
        //        if (isAddressNotInExcepts(macAddress, excepts)) {
        //            return macAddress;
        //        }
        //        macAddress = getMacAddressByFile();
        //        if (isAddressNotInExcepts(macAddress, excepts)) {
        //            return macAddress;
        //        }
        //        return "";
        //    }
        private fun isAddressNotInExcepts(address: String, vararg excepts: String): Boolean {
            if (TextUtils.isEmpty(address)) {
                return false
            }
            if (("02:00:00:00:00:00" == address)) {
                return false
            }
            if (excepts == null || excepts.size == 0) {
                return true
            }
            for (filter: String? in excepts) {
                if (filter != null && (filter == address)) {
                    return false
                }
            }
            return true
        }

        @RequiresPermission(permission.ACCESS_WIFI_STATE)
        private fun getMacAddressByWifiInfo(): String {
            try {
                val wifi: WifiManager? = UtilsBridge.app
                    .applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                if (wifi != null) {
                    val info: WifiInfo? = wifi.connectionInfo
                    if (info != null) {
                        @SuppressLint("HardwareIds") val macAddress: String = info.macAddress
                        if (!TextUtils.isEmpty(macAddress)) {
                            return macAddress
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return "02:00:00:00:00:00"
        }

        private fun getMacAddressByNetworkInterface(): String {
            try {
                val nis: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
                while (nis.hasMoreElements()) {
                    val ni: NetworkInterface? = nis.nextElement()
                    if (ni == null || !ni.name.equals("wlan0", ignoreCase = true)) continue
                    val macBytes: ByteArray? = ni.hardwareAddress
                    if (macBytes != null && macBytes.size > 0) {
                        val sb: StringBuilder = StringBuilder()
                        for (b: Byte in macBytes) {
                            sb.append(String.format("%02x:", b))
                        }
                        return sb.substring(0, sb.length - 1)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return "02:00:00:00:00:00"
        }

        private fun getMacAddressByInetAddress(): String {
            try {
                val inetAddress: InetAddress? = getInetAddress()
                if (inetAddress != null) {
                    val ni: NetworkInterface? = NetworkInterface.getByInetAddress(inetAddress)
                    if (ni != null) {
                        val macBytes: ByteArray? = ni.hardwareAddress
                        if (macBytes != null && macBytes.size > 0) {
                            val sb: StringBuilder = StringBuilder()
                            for (b: Byte in macBytes) {
                                sb.append(String.format("%02x:", b))
                            }
                            return sb.substring(0, sb.length - 1)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return "02:00:00:00:00:00"
        }

        private fun getInetAddress(): InetAddress? {
            try {
                val nis: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
                while (nis.hasMoreElements()) {
                    val ni: NetworkInterface = nis.nextElement()
                    // To prevent phone of xiaomi return "10.0.2.15"
                    if (!ni.isUp) continue
                    val addresses: Enumeration<InetAddress> = ni.inetAddresses
                    while (addresses.hasMoreElements()) {
                        val inetAddress: InetAddress = addresses.nextElement()
                        if (!inetAddress.isLoopbackAddress) {
                            val hostAddress: String = inetAddress.hostAddress
                            if (hostAddress.indexOf(':') < 0) return inetAddress
                        }
                    }
                }
            } catch (e: SocketException) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * Return the manufacturer of the product/hardware.
         *
         * e.g. Xiaomi
         *
         * @return the manufacturer of the product/hardware
         */
        fun getManufacturer(): String {
            return Build.MANUFACTURER
        }

        /**
         * Return the model of device.
         *
         * e.g. MI2SC
         *
         * @return the model of device
         */
        fun getModel(): String {
            var model: String? = Build.MODEL
            if (model != null) {
                model = model.trim { it <= ' ' }.replace("\\s*".toRegex(), "")
            } else {
                model = ""
            }
            return model
        }

        /**
         * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
         * element in the list.
         *
         * @return an ordered list of ABIs supported by this device
         */
        fun getABIs(): Array<String> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return Build.SUPPORTED_ABIS
            } else {
                if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                    return arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
                }
                return arrayOf(Build.CPU_ABI)
            }
        }

        /**
         * Return whether device is tablet.
         *
         * @return `true`: yes<br></br>`false`: no
         */
        fun isTablet(): Boolean {
            return ((Resources.getSystem().configuration.screenLayout
                    and Configuration.SCREENLAYOUT_SIZE_MASK)
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE)
        }

        /**
         * Return whether device is emulator.
         *
         * @return `true`: yes<br></br>`false`: no
         */
        fun isEmulator(): Boolean {
            val checkProperty: Boolean = (Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.lowercase(Locale.getDefault()).contains("vbox")
                    || Build.FINGERPRINT.lowercase(Locale.getDefault()).contains("test-keys")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                    || ("google_sdk" == Build.PRODUCT))
            if (checkProperty) return true
            var operatorName: String = ""
            val tm: TelephonyManager? =
                UtilsBridge.app.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (tm != null) {
                val name: String? = tm.networkOperatorName
                if (name != null) {
                    operatorName = name
                }
            }
            val checkOperatorName: Boolean =
                (operatorName.lowercase(Locale.getDefault()) == "android")
            if (checkOperatorName) return true
            val url: String = "tel:" + "123456"
            val intent: Intent = Intent()
            intent.data = Uri.parse(url)
            intent.action = Intent.ACTION_DIAL
            val checkDial: Boolean =
                intent.resolveActivity(UtilsBridge.app.packageManager) == null
            if (checkDial) return true
            if (isEmulatorByCpu()) return true

//        boolean checkDebuggerConnected = Debug.isDebuggerConnected();
//        if (checkDebuggerConnected) return true;
            return false
        }

        /**
         * Returns whether is emulator by check cpu info.
         * by function of [.readCpuInfo], obtain the device cpu information.
         * then compare whether it is intel or amd (because intel and amd are generally not mobile phone cpu), to determine whether it is a real mobile phone
         *
         * @return `true`: yes<br></br>`false`: no
         */
        private fun isEmulatorByCpu(): Boolean {
            val cpuInfo: String = readCpuInfo()
            return cpuInfo.contains("intel") || cpuInfo.contains("amd")
        }

        /**
         * Return Cpu information
         *
         * @return Cpu info
         */
        private fun readCpuInfo(): String {
            var result: String = ""
            try {
                val args: Array<String> = arrayOf("/system/bin/cat", "/proc/cpuinfo")
                val cmd: ProcessBuilder = ProcessBuilder(*args)
                val process: Process = cmd.start()
                val sb: StringBuilder = StringBuilder()
                var readLine: String?
                val responseReader: BufferedReader =
                    BufferedReader(InputStreamReader(process.inputStream, "utf-8"))
                while ((responseReader.readLine().also { readLine = it }) != null) {
                    sb.append(readLine)
                }
                responseReader.close()
                result = sb.toString().lowercase(Locale.getDefault())
            } catch (ignored: IOException) {
            }
            return result
        }

        /**
         * Whether user has enabled development settings.
         *
         * @return whether user has enabled development settings.
         */
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        fun isDevelopmentSettingsEnabled(): Boolean {
            return Settings.Global.getInt(
                UtilsBridge.app.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
            ) > 0
        }

        private fun getUdid(prefix: String, id: String): String {
            if ((id == "")) {
                return prefix + UUID.randomUUID().toString().replace("-", "")
            }
            return prefix + UUID.nameUUIDFromBytes(id.toByteArray()).toString().replace("-", "")
        }
}