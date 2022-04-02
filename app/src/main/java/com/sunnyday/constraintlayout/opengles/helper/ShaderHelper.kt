package com.sunnyday.constraintlayout.opengles.helper

import android.opengl.GLES20.*
import com.sunnyday.constraintlayout.opengles.utils.LoggerConfig
import com.sunnyday.constraintlayout.opengles.utils.MyLog

/**
 * Create by SunnyDay on 17:26 2022/03/30
 */
class ShaderHelper {

    companion object {
        //编译状态
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
            val compileStatus = IntArray(1)
            //创建着色器对象。返回的int值代表OpenGl对象的引用。返回0代表创建对象失败。
            val shaderObjectId = glCreateShader(type)
            // print log to android platform.
            if (LoggerConfig.isOpenLogger) {
                MyLog.d {
                    "Result of compiling source: \n $shaderCode \n ${
                        glGetShaderInfoLog(
                            shaderObjectId
                        )
                    }"
                }
            }
            if (compileStatus[0] == 0) {
                glDeleteShader(shaderObjectId)
                MyLog.d {
                    "Compilation of shader failed."
                }
                return 0
            }
            //把着色器源码上传给着色器对象
            glShaderSource(shaderObjectId, shaderCode)
            //编译着色器
            glCompileShader(shaderObjectId)
            //取出编译状态
            glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0)
            return shaderObjectId
        }

        /**
         * 链接OpenGl的程序：可以在多个程序中使用同一个着色器。
         * */
        fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
            val linkStatus = IntArray(1)
            val programObjectId = glCreateProgram()
            // print log to android platform.
            if (LoggerConfig.isOpenLogger) {
                MyLog.d {
                    "Result of linking program: \n ${glGetShaderInfoLog(programObjectId)}"
                }
            }
            if (linkStatus[0] == 0) {
                glDeleteProgram(programObjectId)
                MyLog.d {
                    "Linking of program failed."
                }
                return 0
            }
            //附上着色器
            glAttachShader(programObjectId, vertexShaderId)
            glAttachShader(programObjectId, fragmentShaderId)
            glGetProgramiv(programObjectId, GL_LINK_STATUS,linkStatus, 0)
            return programObjectId
        }
    }
}