package com.sunnyday.constraintlayout.opengles.render

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import com.sunnyday.constraintlayout.opengles.R
import com.sunnyday.constraintlayout.opengles.helper.ShaderHelper
import com.sunnyday.constraintlayout.opengles.utils.LoggerConfig
import com.sunnyday.constraintlayout.opengles.utils.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Create by SunnyDay on 20:26 2021/12/26
 */
class AirHockeyRender(private val mContext: Context) : GLSurfaceView.Renderer {
    // 矩形就是两个三角形，注意逆时针顺序定义。
    private val tableVerticesWithTriangle = floatArrayOf(
        //第一个三角形
        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,
        // 第二个三角形
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,
        // 中间的分割线
        -0.5f, 0f,
        0.5f, 0f,
        // 两木追顶点
        0f, -0.25f,
        0f, 0.25f
    )

    //定义分配本地堆数据，这样着色器就可以读取本地的数据了。
    private var vertexData = ByteBuffer
        .allocateDirect(tableVerticesWithTriangle.size * BYTE_PER_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(tableVerticesWithTriangle) //吧内存从java堆赋值到本地堆

    private var program = 0
    private var uColorLocation: Int = 0
    private var aPositionLocation: Int = 0

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2

        // 每个float变量所占字节数
        private const val BYTE_PER_FLOAT = 4
        private const val U_COLOR = "u_Color"
        private const val A_POSITION = "a_Position"
    }

    init {
        //矩形顶点属性数组
        val tableVertices = floatArrayOf(
            0f, 0f,
            0f, 14f,
            9f, 14f,
            9f, 0f
        )
    }

    /**
     * 当绘制一帧时这个方法会被GLSurfaceView调用。
     * ps：在这个方法中一定要绘制些东西，即使是一个清空屏幕的操作，
     * 因为这个方法返回后，渲染缓冲区会被交换并显示到屏幕上，如果
     * 什么都没有画，可能会看到屏幕闪烁效果。
     * */
    override fun onDrawFrame(gl: GL10?) {
        // 清空屏幕所有颜色，
        glClear(GL_COLOR_BUFFER_BIT)
        // 绘制桌子
        glUniform4f(uColorLocation, 1f, 1f, 1f, 1f)
        glDrawArrays(GL_TRIANGLES, 0, 6)
        //绘制分割线
        glUniform4f(uColorLocation, 1f, 0f, 0f, 1f)
        glDrawArrays(GL_LINES, 6, 2)
        //木追绘制为点
        glUniform4f(uColorLocation, 0f, 0f, 1f, 1f)
        glDrawArrays(GL_POINTS, 9, 1)
    }

    /**
     * Surface被创建后，每次Surface尺寸变化时GLSurfaceView会调用这个方法。
     * 如：横竖屏切换时Surface尺寸就会变化。
     * */
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        /**
         * 设置视口（Viewport）尺寸，也即告诉OpenGL渲染view的大小。
         * */
        glViewport(0, 0, width, height)
    }

    /**
     * 当Surface被创建时候，GLSurfaceView会调用这个方法。
     * ps：应用第一次运行时、当设备被唤醒或者用户从其他activity切回来时
     * 方法也可能会被调用。
     * */
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        /**
         * 设置清空屏幕用的颜色，参数：red、green、blue、alpha
         * [0,1]代表颜色强度。如下屏幕清空会显示黑色。
         * */
        glClearColor(0f, 0f, 0f, 0f)
        // 读取、编译、获取着色器
        val vertexShaderSource =
            TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader)
        val fragmentShaderSource =
            TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader)
        val vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource)
        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource)
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader)

        // 验证程序
        if (LoggerConfig.isOpenLogger) {
            ShaderHelper.validateProgram(program)
        }
        // 使用程序
        glUseProgram(program)
        //获取uniform位置
        uColorLocation = glGetUniformLocation(program, U_COLOR)
        aPositionLocation = glGetAttribLocation(program, A_POSITION)

        //关联属性与顶点数据数组
        vertexData.position(0)
        glVertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GL_FLOAT,
            false,
            0,
            vertexData
        )
        //使能够顶点数组
        glEnableVertexAttribArray(aPositionLocation)
    }
}