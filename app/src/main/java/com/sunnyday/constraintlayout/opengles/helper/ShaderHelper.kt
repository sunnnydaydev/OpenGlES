package com.sunnyday.constraintlayout.opengles.helper

import android.opengl.GLES20.GL_FRAGMENT_SHADER
import android.opengl.GLES20.GL_VERTEX_SHADER

/**
 * Create by SunnyDay on 17:26 2022/03/30
 */
class ShaderHelper {
    companion object {
        private const val TAG = "ShaderHelper"

        /**
         * 编译顶点着色器
         * @param shaderCode 顶点着色器代码
         * */
        fun compileVertexShader(shaderCode: String): Int {
            return compileShader(GL_VERTEX_SHADER, shaderCode)
        }

        /**
         *编译片段着色器
         * @param shaderCode 片段着色器代码
         * */
        fun compileFragmentShader(shaderCode: String): Int {
            return compileShader(GL_FRAGMENT_SHADER, shaderCode)
        }

        /**
         * 编译着色器
         * @param type 着色器类型
         * @param shaderCode 着色器代码
         * */
        private fun compileShader(type: Int, shaderCode: String): Int {
            return 0
        }
    }
}