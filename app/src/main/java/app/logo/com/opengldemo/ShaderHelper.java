package app.logo.com.opengldemo;

import android.opengl.GLES20;

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

    private int complieShader(int type ,String shaderCode) {
        final int shaderObjectId = glCreateShader(type);

        if(shaderObjectId == 0) {
            LogDebug.e(TAG,"create gl create shader fail");
            return 0;
        }


    }

}
