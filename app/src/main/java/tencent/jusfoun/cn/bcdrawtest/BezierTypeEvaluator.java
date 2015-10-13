package tencent.jusfoun.cn.bcdrawtest;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Author  JUSFOUN
 * CreateDate 2015/10/9.
 * Description
 */
public class BezierTypeEvaluator implements TypeEvaluator<PointF> {

    private PointF[] pointFs=null;
    private PointF pointF;

    public BezierTypeEvaluator(PointF ...pointFs){
        this.pointFs=pointFs;
        pointF=new PointF();
        if (pointFs.length<3){
            new IllegalArgumentException("最少为二次曲线");
        }
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {

        // B(t) = P0 * (1-t)^2 + 2 * P1 * t * (1-t) + P2 * t^2

        float t=fraction;
        float one_t=1.0f-t;
        if (pointFs.length==3){
            pointF.x= (float) (pointFs[0].x*Math.pow(one_t,2)+2*pointFs[1].x*t*one_t+pointFs[2].x*Math.pow(t,2));
            pointF.y= (float) (pointFs[0].y*Math.pow(one_t,2)+2*pointFs[1].y*t*one_t+pointFs[2].y*Math.pow(t,2));
        }
        return pointF;
    }
}
