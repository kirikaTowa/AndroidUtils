package com.assistant.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class CalendarReminderUtils {
    private static String CALENDER_URL = "content://com.android.calendar/calendars";
    private static String CALENDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALENDER_REMINDER_URL = "content://com.android.calendar/reminders";

    private static String CALENDARS_NAME = "boohee";
    private static String CALENDARS_ACCOUNT_NAME = "BOOHEE@boohee.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.android.boohee";
    private static String CALENDARS_DISPLAY_NAME = "BOOHEE账户";

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     * 获取账户成功返回账户id，否则返回-1
     */
    private static int checkAndAddCalendarAccount(Context context) {
        int oldId = checkCalendarAccount(context);
        if (oldId >= 0) {
            return oldId;
        } else {
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    private static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALENDER_URL), null, null, null, null);
        try {
            if (userCursor == null) { //查询返回空值
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) { //存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(getSaveValue(userCursor.getColumnIndex(CalendarContract.Calendars._ID)));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    /**
     * 添加日历账户，账户创建成功则返回账户id，否则返回-1
     */

    private static long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALENDER_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    /**
     * 添加日历事件
     */

    public static Boolean addCalendarEvent(Context context, String title, String description, long reminderTime, int previousDate) {
        try {
            if (context == null) {
                return false;
            }
            int calId = checkAndAddCalendarAccount(context); //获取日历账户的id
            if (calId < 0) { //获取账户id失败直接返回，添加日历事件失败
                return false;
            }
            long targetTime = transformTargetTime(reminderTime);
            //添加日历事件
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(targetTime);//设置开始时间
            long start = mCalendar.getTime().getTime();
            mCalendar.setTimeInMillis(start + 10 * 60 * 1000);//设置终止时间，开始时间加10分钟
            long end = mCalendar.getTime().getTime();
            ContentValues event = new ContentValues();
            event.put("title", title);
            event.put("description", description);
            event.put("calendar_id", calId); //插入账户的id
            event.put(CalendarContract.Events.DTSTART, start);
            event.put(CalendarContract.Events.DTEND, end);
            event.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒
            String location = String.valueOf(TimeZone.getDefault().getID());
            event.put(CalendarContract.Events.EVENT_TIMEZONE, location);//这个是时区，必须有
            Uri newEvent = context.getContentResolver().insert(Uri.parse(CALENDER_EVENT_URL), event); //添加事件
            if (newEvent == null) { //添加日历事件失败直接返回
                return false;
            }

            //事件提醒的设定
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
            values.put(CalendarContract.Reminders.MINUTES, previousDate * 24 * 60);// 提前previousDate天有提醒
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            Uri uri = context.getContentResolver().insert(Uri.parse(CALENDER_REMINDER_URL), values);
            if (uri == null) { //添加事件提醒失败直接返回
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }


    private static long transformTargetTime(long reminderTime) {
        Date date = new Date(reminderTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (hours < 8) {
            date.setTime(date.getTime() + (8 - hours) * 60 * 60 * 1000); // Add the difference in hours
        }

        return date.getTime();
    }

    /**
     * 检查日历事件
     *
     * @param context
     * @param title
     */
    public static boolean checkCalendarEvent(Context context, String title, String description, long startTime, long endTime) {
        if (context == null) {
            return false;
        }
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null);
        try {
            if (eventCursor == null) { //查询返回空值
                return false;
            }
            if (eventCursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                String eventTitle = "";
                String eventDescription = "";
                long eventStartTime;
                long eventEndTime;
                while (eventCursor.moveToNext()) {
                    eventTitle = eventCursor.getString(getSaveValue(eventCursor.getColumnIndex("title")));
                    eventDescription = eventCursor.getString(getSaveValue(eventCursor.getColumnIndex("description")));
                    eventStartTime = Long.parseLong(eventCursor.getString(getSaveValue(eventCursor.getColumnIndex("dtstart"))));
                    eventEndTime = Long.parseLong(eventCursor.getString(getSaveValue(eventCursor.getColumnIndex("dtend"))));
                    if ((title != null && title.equals(eventTitle)) && (description != null && description.equals(eventDescription)) && (startTime == eventStartTime) && (endTime == eventEndTime)) {
                        return true;
                    }
                }
            }
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
        return false;
    }


    /**
     * 检查日历事件
     *
     * @param context
     * @param description
     */
    public static boolean checkCalendarEventDescription(Context context, String description) {
        if (context == null) {
            return false;
        }
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null);
        try {
            if (eventCursor == null) { //查询返回空值
                return false;
            }
            if (eventCursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                String eventDescription = "";
                while (eventCursor.moveToNext()) {
                    eventDescription = eventCursor.getString(getSaveValue(eventCursor.getColumnIndex("description")));

                    if ((description != null && description.equals(eventDescription))) {
                        return true;
                    }
                }
            }
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
        return false;
    }

    public static Boolean judgeAddStatus(Context context, String title, String description, long reminderTime, int previousDate) {
        if (CalendarReminderUtils.checkCalendarEventDescription(context, description)) {
            return true;
        } else {
            return addCalendarEvent(context, title, description, reminderTime, previousDate);
        }
    }

    public static Boolean judgeDeleteStatus(Context context, String describe) {
        if (!CalendarReminderUtils.checkCalendarEventDescription(context, describe)) {
            return true;
        } else {
            return deleteCalendarEventDescribe(context, describe);
        }
    }

    /**
     * 删除日历事件
     */
    public static void deleteCalendarEvent(Context context, String title) {
        if (context == null) {
            return;
        }
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null);
        try {
            if (eventCursor == null) { //查询返回空值
                return;
            }
            if (eventCursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                    String eventTitle = eventCursor.getString(getSaveValue(eventCursor.getColumnIndex("title")));
                    if (!TextUtils.isEmpty(title) && title.equals(eventTitle)) {
                        int id = eventCursor.getInt(getSaveValue(eventCursor.getColumnIndex(CalendarContract.Calendars._ID)));//取得id
                        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALENDER_EVENT_URL), id);
                        int rows = context.getContentResolver().delete(deleteUri, null, null);
                        if (rows == -1) { //事件删除失败
                            return;
                        }
                    }
                }
            }
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
    }

    /**
     * 删除日历事件
     */
    public static Boolean deleteCalendarEventDescribe(Context context, String description) {
        if (context == null) {
            return false;
        }
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null);
        try {
            if (eventCursor == null) { //查询返回空值
                return false;
            }
            if (eventCursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的description一样的项
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                    String eventDescription = eventCursor.getString(getSaveValue(eventCursor.getColumnIndex("description")));
                    if (!TextUtils.isEmpty(description) && description.equals(eventDescription)) {
                        int id = eventCursor.getInt(getSaveValue(eventCursor.getColumnIndex(CalendarContract.Calendars._ID)));//取得id
                        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALENDER_EVENT_URL), id);
                        int rows = context.getContentResolver().delete(deleteUri, null, null);
                        if (rows == -1) { //事件删除失败
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
        return false;
    }

    private static int getSaveValue(int value) {
        if (value < 0) {
            return 0;
        } else {
            return value;
        }
    }
}