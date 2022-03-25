package com.sunnyday.constraintlayout.opengles.render

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Create by SunnyDay on 20:26 2021/12/26
 */
class AirHockeyRender : GLSurfaceView.Renderer {
    /**
     * 当绘制一帧时这个方法会被GLSurfaceView调用。
     * ps：在这个方法中一定要绘制些东西，即使是一个清空屏幕的操作，
     * 因为这个方法返回后，渲染缓冲区会被交换并显示到屏幕上，如果
     * 什么都没有画，可能会看到屏幕闪烁效果。
     * */
    override fun onDrawFrame(gl: GL10?) {
        // 清空屏幕所有颜色，
       glClear(GL_COLOR_BUFFER_BIT)
    }

    /**
     * Surface被创建后，每次Surface尺寸变化时GLSurfaceView会调用这个方法。
     * 如：横竖屏切换时Surface尺寸就会变化。
     * */
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        /**
         * 设置视口（Viewport）尺寸，也即告诉OpenGL渲染view的大小。
         * */
       glViewport(0,0,width, height)
    }
    /**
     * 当Surface被创建时候，GLSurfaceView会调用这个方法。
     * ps：应用第一次运行时、当设备被唤醒或者用户从其他activity切回来时
     * 方法也可能会被调用。
     * */
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        /**
         * 设置清空屏幕用的颜色，参数：red、green、blue、alpha
         * [0,1]代表颜色强度。如下屏幕清空会显示红色。
         * */
        glClearColor(1f, 0f, 0f, 0f)
    }
}