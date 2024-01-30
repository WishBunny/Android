package com.wish.bunny.alarm

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
작성자:  이혜연
처리 내용: AlarmService 구현
 */
class AlarmService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("실행 실패");
    }

}
