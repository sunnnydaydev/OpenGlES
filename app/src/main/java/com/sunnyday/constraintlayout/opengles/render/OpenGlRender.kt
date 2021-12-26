package com.sunnyday.constraintlayout.opengles.render

import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Create by SunnyDay on 20:26 2021/12/26
 */
class OpenGlRender : GLSurfaceView.Renderer {
    override fun onDrawFrame(gl: GL10?) {
       gl?.glClear(GL_COLOR_BUFFER_BIT)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
       gl?.glViewport(0,0,width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        gl?.glClearColor(1f, 0f, 0f, 0f)
    }
}