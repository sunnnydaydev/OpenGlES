# 前言

- 检测当前设备是否支持OpenGLES
- GLSurfaceView
- GLSurfaceView.Renderer


# 检测当前设备是否支持OpenGLES

```kotlin
    /**
 * 检测当前设备是否支持OpenGLES 2.0
 * */
private fun isSupportES2(context: Context): Boolean {
    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val configurationInfo = am.deviceConfigurationInfo
    return configurationInfo.reqGlEsVersion > 0x20000
}
```

# GLSurfaceView初始化

初始化OpenGL主要是这几步： 检查openGL版本 -> 创建GLSurfaceView实例 -> 配置OpenGL -> 处理生命周期

```kotlin
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

```

######  GLSurfaceView

OpenGL初始化过程中会有一些基本的操作如:配置显示设备（display）、在后台线程中渲染。

渲染是在显示设备中一个称为“surface”的特定区域完成的，有时也被称为视口（viewport）

GLSurfaceView是一个特殊的类，用来初始化OpenGL， GLSurfaceView对OpenGL的初始化操作进行了包装。我们只需使用GLSurfaceView的相关api即可。

在安卓中Activity有很多生命周期，通常用户切换界面过程中需要进行资源的暂停、释放、等操作。OpenGL资源同样也是需要处理的。GLSurfaceView同样对OpenGL的资源释放、暂停做了封装。方便我们处理Activity的生命周期。



GLSurfaceView实际上为自己创建了一个Window，并在视图层次（View Hierarchy）上穿了个“洞”让底层的OpenGL surface显示出来。与普通的安卓view区别是GLSurfaceView无动画、变形特效，因为GLSurfaceView是window的一部分。

```kotlin
//GLSurfaceView主要是基于SurfaceView进行了扩展。
//不是一个普通的安卓view
public class GLSurfaceView extends SurfaceView implements SurfaceHolder.Callback2 {
    
}
public class SurfaceView extends MockView {
    
}
```



Android4.0提供了一个纹理视图TextureView，他可以渲染OpenGl而不用创建单独的window或者打洞，这就意味着这个视图像一个常规的窗口一样可以被操作。有动画和变形特效，但是TextureView内部未内置OpenGL的初始化操作，所以想要操作有两种方式：

1、执行自定义的OpenGL初始化，并在TextureView上运行。

2、吧GLSurfaceView的源码搬来，适配到TextureView上。

```kotlin
//安卓中的普通view
public class TextureView extends View {
    
}
```

# GLSurfaceView.Renderer

```kotlin

/**
 * Create by SunnyDay on 20:26 2021/12/26
 */
class OpenGlRender : GLSurfaceView.Renderer {
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
```

#  End





