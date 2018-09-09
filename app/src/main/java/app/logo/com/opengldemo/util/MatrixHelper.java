package app.logo.com.opengldemo.util;

/**
 * Created by Administrator on 2018/9/9 0009.
 * 自定义投影矩阵
 */

public class MatrixHelper {
    /**
     * construct a perspective matrix like below:
     * <p>
     * a/aspect       0        0        0
     * 0              a        0        0
     * 0              0  (f+n)/(f -n)   -(2*f*n / f-n)
     * 0              0        -1       0
     * <p>
     * 矩阵按列顺序存储在数组中
     */
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float near, float far) {

        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((far + near) / (far - near));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2 * far * near) / (far - near));
        m[15] = 0f;

    }

}
