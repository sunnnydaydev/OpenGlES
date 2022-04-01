package com.sunnyday.constraintlayout.opengles.utils

import android.util.Log

/**
 * Create by SunnyDay on 16:18 2022/04/01
 */
object MyLog {
    private const val TAG = "MyLog"

    fun d(tag: String?, msg: () -> String) {
        if (null == tag || "" == tag) {
            Log.d(TAG, msg.invoke())
        } else {
            Log.d(tag, msg.invoke())
        }
    }

    fun d(msg: () -> String) {
        d(TAG, msg)
    }
}