package tencent.jusfoun.cn.bcdrawtest;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Author  JUSFOUN
 * CreateDate 2015/10/10.
 * Description
 */
public class BeziersTypeEvaluator implements TypeEvaluator<ArrayList<PointF>> {

    private PointF[] pointFs=null;
    private PointF pointF;
    private ArrayList<PointF> list;
    public BeziersTypeEvaluator(PointF...pointFs){

        this.pointFs=pointFs;
        list=new ArrayList<PointF>();
        pointF=new PointF();
        if (pointFs.length<3){
            new IllegalArgumentException("最少为二次曲线");
        }
    }
    @Override
    public ArrayList<PointF> evaluate(float fraction, ArrayList<PointF> startValue, ArrayList<PointF> endValue) {
        float t=fraction;
        float one_t=1.0f-t;
        if (pointFs.length==3){
            pointF.x= (float) (pointFs[0].x*Math.pow(one_t,2)+2*pointFs[1].x*t*one_t+pointFs[2].x*Math.pow(t,2));
            pointF.y= (float) (pointFs[0].y*Math.pow(one_t,2)+2*pointFs[1].y*t*one_t+pointFs[2].y*Math.pow(t,2));
        }
        list.add(pointF);
        return list;
    }
}
