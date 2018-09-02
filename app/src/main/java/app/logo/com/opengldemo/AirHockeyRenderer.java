package app.logo.com.opengldemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import app.logo.com.opengldemo.util.LogDebug;
import app.logo.com.opengldemo.util.TextResourceReader;


/**
 * Created by Administrator on 2018/9/2 0002.
 * GL10 is not used in es2.0 ,we ues GLES2.0 static api to draw
 */

class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static String TAG = "AirHockeyRenderer";

    //static section
    private static final int POSITION_COMPONENT = 2;
    private static final int BYTES_PER_FLOAT = 4;


    private Context mContext;

    private String vertexShaderCode;
    private String fragmentShaderCode;


    private FloatBuffer vertexData = null;
    private float[] tableVerticesWithTriangles = {
            // Triangle 1
            0f, 0f,
            9f, 14f,
            0f, 14f,

            //Trianle 2
            0f, 0f,
            9f, 0f,
            9f, 14f,

            //Line 1
            0f, 7f,
            9f, 7f,

            //Line 2
            4.5f, 2f,
            4.5f, 12f
    };

    public AirHockeyRenderer(Context context) {
        mContext = context;
    }

    //开辟本地内存，并且把坐标数组传递到本地内存中,保存在floatBuffer中
    private void initData() {
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT).
                order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);

        //获取shader glsl代码
        vertexShaderCode = TextResourceReader.readTextFileFromResource(mContext,R.raw.simple_vertex_shader);
        fragmentShaderCode = TextResourceReader.readTextFileFromResource(mContext,R.raw.simple_fragment_shader);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogDebug.e(TAG, "onSurfaceCreated......");
        GLES20.glClearColor(1.0f, 1.0f, 0, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        LogDebug.e(TAG, "width =" + width + ",height = " + height);
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        LogDebug.e(TAG, "onDrawFrame......");
        //清空屏幕，会使用glClearColor定义的颜色填充整个屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

    }
}
