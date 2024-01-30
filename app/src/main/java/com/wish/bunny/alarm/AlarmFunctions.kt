package com.wish.bunny.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
작성자:  이혜연
처리 내용: AlarmFunctions 구현
 */
class AlarmFunctions(private val context: Context) {

    private lateinit var pendingIntent: PendingIntent

    fun callAlarm(time : String, alarm_code : Int, content : String){

        Log.d("전달한 내용",content)

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val receiverIntent = Intent(context, AlarmReceiver::class.java)
        receiverIntent.apply {
            putExtra("alarm_rqCode", alarm_code)
            putExtra("alarm_content", content)
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context,
                alarm_code,
                receiverIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                alarm_code,
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd H:mm:ss")
        var datetime = Date()
        try {
            datetime = dateFormat.parse(time) as Date
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val calendar = Calendar.getInstance()
        calendar.time = datetime

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

    }

    fun cancelAlarm(alarm_code: Int) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(context, alarm_code, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(
                context,
                alarm_code,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        alarmManager.cancel(pendingIntent)
    }
}