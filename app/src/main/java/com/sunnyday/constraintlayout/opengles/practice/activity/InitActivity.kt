package com.sunnyday.constraintlayout.opengles.practice.activity

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sunnyday.constraintlayout.opengles.R
import com.sunnyday.constraintlayout.opengles.practice.render.InitRender
import com.sunnyday.constraintlayout.opengles.render.AirHockeyRender

class InitActivity : AppCompatActivity() {
    private lateinit var glSurfaceView: GLSurfaceView
    /**
     * 标记是否已经启用渲染器。方便处理生命周期问题。
     * */
    private var rendererSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //1、创建GLSurfaceView实例
        glSurfaceView = GLSurfaceView(this)
        val isSupportES2 = isSupportES2(applicationContext)
        if (isSupportES2) {
            //2、配置OpenGL
            glSurfaceView.setEGLContextClientVersion(2)
            glSurfaceView.setRenderer(InitRender(applicationContext))
            rendererSet = true
        } else {
            Toast.makeText(applicationContext, "This device does not support OpenGL ES 2.0 !", Toast.LENGTH_SHORT).show()
            return
        }
        setContentView(glSurfaceView)
    }

    /**
     * 处理生命周期
     * */
    override fun onPause() {
        super.onPause()
        if (rendererSet) glSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (rendererSet) glSurfaceView.onResume()
    }

    /**
     * 检测当前设备是否支持OpenGLES 2.0
     * */
    private fun isSupportES2(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = am.deviceConfigurationInfo
        return configurationInfo.reqGlEsVersion > 0x20000
    }
}