package tencent.jusfoun.cn.bcdrawtest;

import android.app.Activity;
import android.os.Bundle;

import tencent.jusfoun.cn.view.LineBaseSurfaceView;

/**
 * Author  wangchenchen
 * CreateDate 2015/12/30.
 * Email wcc@jusfoun.com
 * Description
 */
public class LineSurfaceActivity extends Activity {

    private LineBaseSurfaceView surfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        surfaceView= (LineBaseSurfaceView) findViewById(R.id.surfaceview);
    }
}
