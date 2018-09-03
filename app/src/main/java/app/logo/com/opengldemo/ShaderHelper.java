package app.logo.com.opengldemo;

import android.opengl.GLES20;
import android.util.Log;

import app.logo.com.opengldemo.util.LogDebug;

/**
 * Created by Administrator on 2018/9/2 0002.
 */

public class ShaderHelper {
    private final static String TAG = "ShaderHelper";

    public static int complieVertexShader(String shaderCode) {
        return complieShader(GLES20.GL_VERTEX_SHADER,shaderCode);
    }

    public static int complieFragmentShader(String shaderCode) {
        return complieShader(GLES20.GL_FRAGMENT_SHADER,shaderCode);
    }

    private static int complieShader(int type ,String shaderCode) {
        //创建一个着色器对象，
        final int shaderObjectId = GLES20.glCreateShader(type);

        if(shaderObjectId == 0) {
            LogDebug.e(TAG,"create gl create shader fail");
            return 0;
        }

        //上传着色器源代码
        GLES20.glShaderSource(shaderObjectId,shaderCode);
        //编译代码
        GLES20.glCompileShader(shaderObjectId);
        //取出编译状态
        final int[] compleStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId,GLES20.GL_COMPILE_STATUS,compleStatus,0);

        String complieInfo = " Result of compling source:\n"
                + shaderCode+ "\n" + GLES20.glGetShaderInfoLog(shaderObjectId);
        LogDebug.d(TAG,complieInfo);
        //如果编译失败 删除之前创建的着色器对象
        if(compleStatus[0] == 0){
            GLES20.glDeleteShader(shaderObjectId);
            Log.e(TAG,"fatal::create shader object failed ！");
            return 0;
        }

        return  shaderObjectId;

    }

    public static int linkProgram(int vertexShaderId ,int fragmentShaderId){
        //创建一个程序对象
        final int programId = GLES20.glCreateProgram();
        if(programId == 0){
            Log.e(TAG,"create program object failed");
            return 0;
        }

        //给程序对象附上着色器
        GLES20.glAttachShader(programId,vertexShaderId);
        GLES20.glAttachShader(programId,fragmentShaderId);

        //链接程序
        GLES20.glLinkProgram(programId);

        //检查链接是否成功
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programId,GLES20.GL_LINK_STATUS,linkStatus,0);
        String linkInfo = "Result of linking program:\n" +
                GLES20.glGetProgramInfoLog(programId);
        LogDebug.d(TAG,linkInfo);

        if(linkStatus[0] == 0){
            GLES20.glDeleteProgram(programId);
            Log.e(TAG,"fatal::link program failed!!");
        }

        return programId;
    }

    public static boolean validateProgram(int programObjectId){
        GLES20.glValidateProgram(programObjectId);
        final int validateStatus[] = new int[1];
        GLES20.glGetProgramiv(programObjectId,GLES20.GL_VALIDATE_STATUS,validateStatus,0);
        LogDebug.d(TAG,"Result of validating program:\n" + "validateProgram ="+ validateStatus[0]+
                GLES20.glGetProgramInfoLog(programObjectId));

        return validateStatus[0] !=0;
    }

}
