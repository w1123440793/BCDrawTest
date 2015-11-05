package tencent.jusfoun.cn.bcdrawtest;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/4.
 * Description
 */
public class VerticalMarquee extends LinearLayout {
    public VerticalMarquee(Context context) {
        super(context);
    }

    public VerticalMarquee(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalMarquee(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
