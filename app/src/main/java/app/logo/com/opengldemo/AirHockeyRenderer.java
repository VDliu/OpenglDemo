package app.logo.com.opengldemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import app.logo.com.opengldemo.util.LogDebug;
import app.logo.com.opengldemo.util.MatrixHelper;
import app.logo.com.opengldemo.util.TextResourceReader;


/**
 * Created by Administrator on 2018/9/2 0002.
 * GL10 is not used in es2.0 ,we ues GLES2.0 static api to draw
 */

class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static String TAG = "AirHockeyRenderer";

    //static section
    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private static final String A_COLOR = "a_Color";
    private int aColorLocation;
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private static final String U_MATRIX = "u_Matrix";
    private int uMatrixLocation;


    private Context mContext;

    private String vertexShaderCode;
    private String fragmentShaderCode;


    private FloatBuffer vertexData = null;
    private float[] tableVerticesWithTriangles = {
            // define the desktop vertex：x y r g b
            //前两个点为坐标，后面三个点代表顶点颜色
            0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

            //Line 1
            -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.5f, 0.0f, 1.0f, 0.0f, 0.0f,

            //point 1
            0.0f, -0.4f, 0.0f, 0.0f, 1.0f,
            //point 2
            0.0f, 0.4f, 1.0f, 0.0f, 0.0f
    };

    // matrix define section, vertec (clip) = ProjectMatrix * ModelMatrix * vertex(model)

    //存储生成的正交投影矩阵，之后会传递给u_matrix
    private final float[] projectionMatrix = new float[16];

    //模型矩阵，将物体移至视景体中
    private final float[] modelMatrix = new float[16];



    public AirHockeyRenderer(Context context) {

        mContext = context;
        initData();
    }

    private void initData() {
        //开辟本地内存，并且把坐标数组传递到本地内存中,保存在floatBuffer中
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT).
                order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);

        //获取shader glsl代码
        vertexShaderCode = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        fragmentShaderCode = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0, 0);

        int vertexShader = ShaderHelper.complieVertexShader(vertexShaderCode);
        int framentShader = ShaderHelper.complieFragmentShader(fragmentShaderCode);

        int programId = ShaderHelper.linkProgram(vertexShader, framentShader);
        //告诉Pengl 在绘制任何东西在屏幕上的时候要使用这里定义的程序
        ShaderHelper.validateProgram(programId);
        GLES20.glUseProgram(programId);

        //查找u_color 和a_Position位置
        aColorLocation = GLES20.glGetAttribLocation(programId, A_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION);
        uMatrixLocation = GLES20.glGetUniformLocation(programId, U_MATRIX);

        //告诉OPengl去找到a_Position对应的数据
        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        LogDebug.e(TAG, "width =" + width + ",height = " + height);
        GLES20.glViewport(0, 0, width, height);

       /* final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if (width > height) {
            //横屏时
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f);
        }*/
        //生成透视投影矩阵
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / height, 1f, 10f);

        //生成模型矩阵
        Matrix.setIdentityM(modelMatrix,0);
        //将vertex坐标的z坐标平移到-2处
        Matrix.translateM(modelMatrix,0,0f,0f,-3f);
        //旋转桌子,
        Matrix.rotateM(modelMatrix,0,-60f,1f,0f,0f);

        //投影矩阵和模型矩阵相乘得到的矩阵传入着色器中

        final float temp[] = new float[16];
        Matrix.multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清空屏幕，会使用glClearColor定义的颜色填充整个屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        //初始一个白色桌面
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        //绘制中心分割线
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        //绘制木槌代表的第一个点
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        //绘制木槌代表的第二个点
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);

    }
}
