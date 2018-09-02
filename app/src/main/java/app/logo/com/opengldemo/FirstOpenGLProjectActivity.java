package app.logo.com.opengldemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class FirstOpenGLProjectActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glSurfaceView = findViewById(R.id.gls);
        if (checkIsSurpportES2()) {
            glSurfaceView.setRenderer(new FirstOpenGLProjectRenderer());
            renderSet = true;
        } else {
            Toast.makeText(this, "this device is not surppot opengles 2.0", Toast.LENGTH_LONG);
        }
    }

    private boolean checkIsSurpportES2() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        return supportEs2;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (renderSet) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (renderSet) {
            glSurfaceView.onPause();
        }

    }
}
