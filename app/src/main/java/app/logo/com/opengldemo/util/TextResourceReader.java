package app.logo.com.opengldemo.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2018/9/2 0002.
 */

public class TextResourceReader {

    public static String readTextFileFromResource(Context context, int resource) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = context.getResources().openRawResource(resource);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                sb.append(nextLine);
                sb.append("\n");
            }

        } catch (Exception e) {
            throw new RuntimeException("get shader code fail reource id =" + resource);
        }
        return sb.toString();
    }
}
