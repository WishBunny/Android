package com.wish.bunny.alarm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.wish.bunny.R

class AlarmReceiver() : BroadcastReceiver() {

    private lateinit var manager: NotificationManager
    private lateinit var builder: NotificationCompat.Builder

    companion object{
        const val CHANNEL_ID = "channel"
        const val CHANNEL_NAME = "channel1"
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Log.d("AlarmReceiver", "알림 채널 생성 전")
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )

        Log.d("AlarmReceiver", "NotificationCompat.Builder 생성 전")
        builder = NotificationCompat.Builder(context, CHANNEL_ID)

        val intent2 = Intent(context, AlarmService::class.java)
        val requestCode = intent?.extras!!.getInt("alarm_rqCode")
        val title = intent.extras?.getString("alarm_content")
        Log.d("전달받은 제목", title ?: "null")

        Log.d("AlarmReceiver", "PendingIntent 생성 전")
        val pendingIntent = if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
            PendingIntent.getActivity(context,requestCode,intent2,PendingIntent.FLAG_IMMUTABLE);
        }else {
            PendingIntent.getActivity(context,requestCode,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Log.d("AlarmReceiver", "알람 트리거: $title, requestCode: $requestCode")

        val notification = builder.setContentTitle("오늘 마감인 위시리스트가 있어요")
            .setContentText(title)
            .setSmallIcon(R.drawable.bg_friend_common_button)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        Log.d("AlarmReceiver", "notify 호출 전")
        try {
            manager.notify(1, notification)
        } catch (e:Exception){
            e.printStackTrace()
        }
    }
}