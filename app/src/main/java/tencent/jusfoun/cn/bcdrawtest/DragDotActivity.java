package tencent.jusfoun.cn.bcdrawtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import tencent.jusfoun.cn.model.DragDotModel;
import tencent.jusfoun.cn.view.DragDotSurfaceView;

/**
 * Author  wangchenchen
 * CreateDate 2015/12/21.
 * Email wcc@jusfoun.com
 * Description
 */
public class DragDotActivity extends Activity {
    private DragDotSurfaceView dragDotSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragdot);
        dragDotSurfaceView = (DragDotSurfaceView) findViewById(R.id.dragdot);

        dragDotSurfaceView.setOnClickListener(new DragDotSurfaceView.OnClickListener() {
            @Override
            public void onClick(DragDotModel model,boolean isDotLongPress) {

                Log.e("onclick",isDotLongPress+"");
                if (isDotLongPress) {
                    if (model.isShow()) {
                        model.setIsShow(false);
                    } else {
                        model.setIsShow(true);
                    }
                }else {
                    if (model.isCheck())
                        model.setIsCheck(false);
                    else
                        model.setIsCheck(true);
                    Toast.makeText(getApplication(), "你点的" + model.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
