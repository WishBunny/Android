package com.wish.bunny.util

import android.content.Context
import android.content.SharedPreferences

/**
    작성자: 엄상은
    처리 내용: Shared Preferences 객체 초기화
*/
class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        // defValue는 값이 없을 경우 대체되어 나올 값
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun removeKeyWithValue(key: String) {
        prefs.edit().remove(key).apply()
    }
}