package com.assistant.constants

import android.Manifest.permission
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.StringDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@SuppressLint("InlinedApi")
object PermissionConstant {
    const val CALENDAR = "CALENDAR"
    const val CAMERA = "CAMERA"
    const val CONTACTS = "CONTACTS"
    const val LOCATION = "LOCATION"
    const val MICROPHONE = "MICROPHONE"
    const val PHONE = "PHONE"
    const val SENSORS = "SENSORS"
    const val SMS = "SMS"
    const val STORAGE = "STORAGE"
    const val ACTIVITY_RECOGNITION = "ACTIVITY_RECOGNITION"
    private val GROUP_CALENDAR = arrayOf<String?>(
        permission.READ_CALENDAR, permission.WRITE_CALENDAR
    )
    private val GROUP_CAMERA = arrayOf<String?>(
        permission.CAMERA
    )
    private val GROUP_CONTACTS = arrayOf<String?>(
        permission.READ_CONTACTS, permission.WRITE_CONTACTS, permission.GET_ACCOUNTS
    )
    private val GROUP_LOCATION = arrayOf<String?>(
        permission.ACCESS_FINE_LOCATION,
        permission.ACCESS_COARSE_LOCATION,
        permission.ACCESS_BACKGROUND_LOCATION
    )
    private val GROUP_MICROPHONE = arrayOf<String?>(
        permission.RECORD_AUDIO
    )
    private val GROUP_PHONE = arrayOf<String?>(
        permission.READ_PHONE_STATE, permission.READ_PHONE_NUMBERS, permission.CALL_PHONE,
        permission.READ_CALL_LOG, permission.WRITE_CALL_LOG, permission.ADD_VOICEMAIL,
        permission.USE_SIP, permission.PROCESS_OUTGOING_CALLS, permission.ANSWER_PHONE_CALLS
    )
    private val GROUP_PHONE_BELOW_O = arrayOf<String?>(
        permission.READ_PHONE_STATE, permission.READ_PHONE_NUMBERS, permission.CALL_PHONE,
        permission.READ_CALL_LOG, permission.WRITE_CALL_LOG, permission.ADD_VOICEMAIL,
        permission.USE_SIP, permission.PROCESS_OUTGOING_CALLS
    )
    private val GROUP_SENSORS = arrayOf<String?>(
        permission.BODY_SENSORS
    )
    private val GROUP_SMS = arrayOf<String?>(
        permission.SEND_SMS, permission.RECEIVE_SMS, permission.READ_SMS,
        permission.RECEIVE_WAP_PUSH, permission.RECEIVE_MMS
    )
    private val GROUP_STORAGE = arrayOf<String?>(
        permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE
    )
    private val GROUP_ACTIVITY_RECOGNITION = arrayOf<String?>(
        permission.ACTIVITY_RECOGNITION
    )

    fun getPermissions(@PermissionGroup permission: String?): Array<String?> {
        if (permission == null) return arrayOfNulls(0)
        when (permission) {
            CALENDAR -> return GROUP_CALENDAR
            CAMERA -> return GROUP_CAMERA
            CONTACTS -> return GROUP_CONTACTS
            LOCATION -> return GROUP_LOCATION
            MICROPHONE -> return GROUP_MICROPHONE
            PHONE -> return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                GROUP_PHONE_BELOW_O
            } else {
                GROUP_PHONE
            }

            SENSORS -> return GROUP_SENSORS
            SMS -> return GROUP_SMS
            STORAGE -> return GROUP_STORAGE
            ACTIVITY_RECOGNITION -> return GROUP_ACTIVITY_RECOGNITION
        }
        return arrayOf(permission)
    }

    @StringDef(CALENDAR, CAMERA, CONTACTS, LOCATION, MICROPHONE, PHONE, SENSORS, SMS, STORAGE)
    @Retention(
        RetentionPolicy.SOURCE
    )
    annotation class PermissionGroup
}
