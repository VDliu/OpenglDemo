package app.logo.com.opengldemo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import app.logo.com.opengldemo.util.LogDebug;


/**
 * Created by Administrator on 2018/9/2 0002.
 * GL10 is not used in es2.0 ,we ues GLES2.0 static api to draw
 */

class FirstOpenGLProjectRenderer implements GLSurfaceView.Renderer {
    private static String TAG = "FirstOpenGLProjectRenderer";

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogDebug.e(TAG, "onSurfaceCreated......");
        GLES20.glClearColor(1.0f, 0, 0, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        LogDebug.e(TAG, "width =" + width + ",height = " + height);
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        LogDebug.e(TAG, "onDrawFrame" +
                "......");
        //清空屏幕，会使用glClearColor定义的颜色填充整个屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

    }
}
