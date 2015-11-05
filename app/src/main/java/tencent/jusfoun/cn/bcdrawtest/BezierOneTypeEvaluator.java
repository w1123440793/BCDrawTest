package tencent.jusfoun.cn.bcdrawtest;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Author  JUSFOUN
 * CreateDate 2015/10/19.
 * Description
 */
public class BezierOneTypeEvaluator implements TypeEvaluator<PointF> {

    private PointF[] pointFs;

    public BezierOneTypeEvaluator(PointF... pointfs) {
        this.pointFs = pointfs;
        if (pointFs.length < 2) {
            new IllegalArgumentException("最少两个点");
        }
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {

        //B(t)=p0+(p1-p0)t=(1-t)p0+tp1;
        PointF pointF = new PointF();
        pointF.x = pointFs[0].x + (pointFs[1].x - pointFs[0].x) * fraction;
        pointF.y = pointFs[0].y + (pointFs[1].y - pointFs[0].y) * fraction;
        return pointF;
    }
}
