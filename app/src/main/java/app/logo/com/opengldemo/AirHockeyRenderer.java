package app.logo.com.opengldemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.logging.SocketHandler;

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
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private static final String U_COLOR = "u_Color";
    private int uColorLocation;
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;


    private Context mContext;

    private String vertexShaderCode;
    private String fragmentShaderCode;



    private FloatBuffer vertexData = null;
    private float[] tableVerticesWithTriangles = {
            // Triangle 1
            -0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f,

            //Trianle 2
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,

            //Line 1
            -0.5f, 0f,
            0.5f, 0f,

            //Line 2
            0f, -0.25f,
            0f, 0.25f
    };

    public AirHockeyRenderer(Context context) {

        mContext = context;
        initData();
    }

    private void initData() {
        //开辟本地内存，并且把坐标数组传递到本地内存中,保存在floatBuffer中
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT).
                order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
        vertexData.position(0);

        //获取shader glsl代码
        vertexShaderCode = TextResourceReader.readTextFileFromResource(mContext,R.raw.simple_vertex_shader);
        fragmentShaderCode = TextResourceReader.readTextFileFromResource(mContext,R.raw.simple_fragment_shader);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogDebug.e(TAG, "onSurfaceCreated......");
        GLES20.glClearColor(0.0f, 0.0f, 0, 0);
        int vertexShader = ShaderHelper.complieVertexShader(vertexShaderCode);
        int framentShader = ShaderHelper.complieFragmentShader(fragmentShaderCode);
        int programId = ShaderHelper.linkProgram(vertexShader,framentShader);
        //告诉Pengl 在绘制任何东西在屏幕上的时候要使用这里定义的程序
        ShaderHelper.validateProgram(programId);
        GLES20.glUseProgram(programId);

        //查找u_color 和a_Position位置
        uColorLocation = GLES20.glGetUniformLocation(programId,U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(programId,A_POSITION);

        //告诉OPengl去找到a_Position对应的数据

        GLES20.glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GLES20.GL_FLOAT,
                false,0,vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

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
        //绘制顶点

        //初始一个白色桌面
        GLES20.glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,6);

        //绘制中心分割线
        GLES20.glUniform4f(uColorLocation,1.0f,0f,0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES,6,2);

        //绘制木槌代表的第一个点
        GLES20.glUniform4f(uColorLocation,0.0f,0f,1.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);

        //绘制木槌代表的第二个点
        GLES20.glUniform4f(uColorLocation,1.0f,0f,0.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);


    }
}
