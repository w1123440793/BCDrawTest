package tencent.jusfoun.cn.bcdrawtest;

import android.app.Activity;
import android.os.Bundle;

import tencent.jusfoun.cn.view.TimeAxisSurfaceView;

/**
 * Author  wangchenchen
 * CreateDate 2016/1/6.
 * Email wcc@jusfoun.com
 * Description
 */
public class TimeAxisActivity extends Activity {

    private TimeAxisSurfaceView timeAxisSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_axis);
        timeAxisSurfaceView= (TimeAxisSurfaceView) findViewById(R.id.time_axis);
    }
}
