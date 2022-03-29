package com.sunnyday.constraintlayout.opengles.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException

/**
 * Create by SunnyDay on 12:06 2022/03/29
 */
class TextResourceReader {
    companion object {
        /**
         * 从资源中读取文本
         * @param context Application Context
         * @param resId  resource id
         * */
        fun readTextFileFromResource(context: Context, resId: Int): String {
            val sb = StringBuilder()
            try {
                val inputStream = context.resources.openRawResource(resId)
                val inputStreamReader = InputStreamReader(inputStream)
                val br = BufferedReader(inputStreamReader)
                var nextLines = br.readLine()
                while (nextLines != null) {
                    sb.append(nextLines)
                    sb.append("\n")
                    nextLines = br.readLine()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw RuntimeException("resource which id is $resId not fount ")
            }
            return sb.toString()
        }
    }
}