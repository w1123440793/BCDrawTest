package tencent.jusfoun.cn.bcdrawtest;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

/**
 * Author  JUSFOUN
 * CreateDate 2015/9/18.
 * Description
 */
public class MathHelper {

    private static MathHelper mathHelper;

    public static MathHelper getInstance() {
        if (mathHelper == null)
            mathHelper = new MathHelper();
        return mathHelper;
    }

    private CirPoint cirPoint = new CirPoint();
    private PointF pointF = new PointF();

    /**
     * 根据圆心坐标与角度计算圆心射线与圆交点坐标
     *
     * @param angle  角度
     * @param X      X坐标
     * @param Y      Y坐标
     * @param radius 圆半径
     * @return 点坐标
     */
    public CirPoint getXY(float angle, float X, float Y, float radius) {

        float arcAngle = (float) (Math.PI * (angle / 180.0f));
        if (Float.compare(arcAngle, 0.0f) < 0) {
            cirPoint.X = cirPoint.Y = 0;
        } else if (Float.compare(angle, 0.0f) == 0) {
            cirPoint.X = X + radius;
            cirPoint.Y = Y;
        } else if (Float.compare(angle, 90.0f) == 0) {
            cirPoint.X = X;
            cirPoint.Y = Y + radius;
        } else if (Float.compare(angle, 180.0f) == 0) {
            cirPoint.X = X - radius;
            cirPoint.Y = Y;
        } else if (Float.compare(angle, 270.0f) == 0) {
            cirPoint.X = X;
            cirPoint.Y = Y - radius;
        } else if (Float.compare(angle, 0.0f) > 0
                && Float.compare(angle, 90.0f) < 0) {
            cirPoint.X = (float) (X + Math.cos(arcAngle) * radius);
            cirPoint.Y = (float) (Y + Math.sin(arcAngle) * radius);

        } else if (Float.compare(angle, 90.0f) > 0
                && Float.compare(angle, 180.0f) < 0) {
            angle = 180 - angle;
            arcAngle = (float) (Math.PI * (angle / 180.0f));
            cirPoint.X = (float) (X - Math.cos(arcAngle) * radius);
            cirPoint.Y = (float) (Y + Math.sin(arcAngle) * radius);
        } else if (Float.compare(angle, 180.0f) > 0
                && Float.compare(angle, 270.0f) < 0) {
            angle -= 180;
            arcAngle = (float) (Math.PI * (angle / 180.0f));
            cirPoint.X = (float) (X - Math.cos(arcAngle) * radius);
            cirPoint.Y = (float) (Y - Math.sin(arcAngle) * radius);
        } else if (Float.compare(angle, 270.0f) > 0
                && Float.compare(angle, 360.0f) < 0) {
            angle = 360 - angle;
            arcAngle = (float) (Math.PI * (angle / 180.0f));
            cirPoint.X = (float) (X + Math.cos(arcAngle) * radius);
            cirPoint.Y = (float) (Y - Math.sin(arcAngle) * radius);
        }
        return cirPoint;
    }

    /**
     * 根据圆心坐标与角度计算圆心射线与圆交点坐标
     *
     * @param angle  角度
     * @param X      X坐标
     * @param Y      Y坐标
     * @param radius 圆半径
     * @return 点坐标
     */
    public PointF getPointF(float angle, float X, float Y, float radius) {

        if (angle >= 360)
            angle -= 360;
        float arcAngle = (float) (Math.PI * (angle / 180.0f));
        if (Float.compare(arcAngle, 0.0f) < 0) {
            pointF.x = pointF.y = 0;
        } else if (Float.compare(angle, 0.0f) == 0) {
            pointF.x = X + radius;
            pointF.y = Y;
        } else if (Float.compare(angle, 90.0f) == 0) {
            pointF.x = X;
            pointF.y = Y + radius;
        } else if (Float.compare(angle, 180.0f) == 0) {
            pointF.x = X - radius;
            pointF.y = Y;
        } else if (Float.compare(angle, 270.0f) == 0) {
            pointF.x = X;
            pointF.y = Y - radius;
        } else if (Float.compare(angle, 0.0f) > 0
                && Float.compare(angle, 90.0f) < 0) {
            pointF.x = (float) (X + Math.cos(arcAngle) * radius);
            pointF.y = (float) (Y + Math.sin(arcAngle) * radius);

        } else if (Float.compare(angle, 90.0f) > 0
                && Float.compare(angle, 180.0f) < 0) {
            angle = 180 - angle;
            arcAngle = (float) (Math.PI * (angle / 180.0f));
            pointF.x = (float) (X - Math.cos(arcAngle) * radius);
            pointF.y = (float) (Y + Math.sin(arcAngle) * radius);
        } else if (Float.compare(angle, 180.0f) > 0
                && Float.compare(angle, 270.0f) < 0) {
            angle -= 180;
            arcAngle = (float) (Math.PI * (angle / 180.0f));
            pointF.x = (float) (X - Math.cos(arcAngle) * radius);
            pointF.y = (float) (Y - Math.sin(arcAngle) * radius);
        } else if (Float.compare(angle, 270.0f) > 0
                && Float.compare(angle, 360.0f) < 0) {
            angle = 360 - angle;
            arcAngle = (float) (Math.PI * (angle / 180.0f));
            pointF.x = (float) (X + Math.cos(arcAngle) * radius);
            pointF.y = (float) (Y - Math.sin(arcAngle) * radius);
        }

        return pointF;
    }

    /**
     * 获取三次贝塞尔曲线第二个转折点坐标
     *
     * @param x1     起点X坐标
     * @param y1     起点Y坐标
     * @param x2     目标点X坐标
     * @param y2     目标点Y坐标
     * @param mCirX  圆心X坐标
     * @param mCirY  圆心Y坐标
     * @param mRaius 半径
     * @return 贝塞尔曲线第二个转折点
     */
    public CirPoint getBezierCubic(float x1, float y1, float x2, float y2,
                                   float mCirX, float mCirY, float mRaius) {

        float lengthX = x2 - mCirX;
        float lengthY = y2 - mCirY;
//        float arcAngle= (float) Math.atan2(Math.abs(lengthY),Math.abs(lengthX));
        float arcAngle = (float) Math.atan2(Math.abs(y2 - mCirY), Math.abs(x2 - mCirX));
        float arcAngleCir = (float) Math.atan2(Math.abs(y1 - mCirY), Math.abs(x1 - mCirX));

        if (arcAngle > arcAngleCir) {
            arcAngle += Math.PI * (10f / 180f);
        } else if (arcAngle < arcAngleCir) {
            arcAngle -= Math.PI * (10f / 180f);
        }

        if (lengthX > 0)
            cirPoint.X = (float) (mRaius * Math.cos(arcAngle)) + mCirX;
        else
            cirPoint.X = mCirX - (float) (mRaius * Math.cos(arcAngle));
        if (lengthY > 0)
            cirPoint.Y = (float) (mRaius * Math.sin(arcAngle)) + mCirY;
        else
            cirPoint.Y = mCirY - (float) (mRaius * Math.sin(arcAngle));

        return cirPoint;
    }

    /**
     * 获取同方向二次贝塞尔曲线转折点
     *
     * @param x      目标点X坐标
     * @param y      目标点Y坐标
     * @param mCirX  原点X坐标
     * @param mCirY  原点Y坐标
     * @param mRaius 半径
     * @return 转折点坐标
     */
    public CirPoint getBezierQuad(float x, float y, float mCirX, float mCirY,
                                  float mRaius) {

        float arcAngle = 0;
        if (y == 0) {
            if (x > 0)
                arcAngle = 180;
            else
                arcAngle = 0;
        } else
            arcAngle = (float) Math.atan2(Math.abs(y), Math.abs(x));

//        if (x>0)
        arcAngle += Math.PI * (20f / 180f);
//        else
//            arcAngle-=Math.PI*(30f/180f);
        if (x > 0)
            cirPoint.X = (float) (mRaius * Math.cos(arcAngle)) + mCirX;
        else
            cirPoint.X = mCirX - (float) (mRaius * Math.cos(arcAngle));
        if (y > 0)
            cirPoint.Y = (float) (mRaius * Math.sin(arcAngle)) + mCirY;
        else
            cirPoint.Y = mCirY - (float) (mRaius * Math.sin(arcAngle));
        return cirPoint;
    }

    /**
     * 另一种贝塞尔曲线算法，已半径比分计算，只更改半径上Y坐标
     *
     * @param x      目标点X坐标
     * @param y      目标点Y坐标
     * @param mCirX  原点X坐标
     * @param mCirY  原点Y坐标
     * @param mRaius 半径
     * @return 转折点坐标
     */
    public CirPoint getBezierQuadOther(float x, float y, float mCirX, float mCirY,
                                       float mRaius) {
        float arcAngle = 0;
        if (y == 0) {
            cirPoint.X = mCirX;
            if (x > 0) {
                cirPoint.Y += y;
            } else {
                cirPoint.Y -= y;
            }
            return cirPoint;
        } else
            arcAngle = (float) Math.atan2(Math.abs(y), Math.abs(x));
        if (x > 0)
            cirPoint.X = (float) (mRaius * Math.cos(arcAngle)) + mCirX;
        else
            cirPoint.X = (float) (mCirX - mRaius * Math.cos(arcAngle));
        if (y > 0) {
            cirPoint.Y = (float) (mRaius * Math.sin(arcAngle)) + mCirY + 100;
        } else {
            cirPoint.Y = mCirY - (float) (mRaius * Math.sin(arcAngle)) - 100;
        }
        return cirPoint;
    }

    public PointF getXy(float x, float x1, float x2,
                        float y, float y1, float y2, float t) {
        PointF pointF = new PointF();
        float one_t = 1 - t;
        pointF.x = (float) (x * Math.pow(one_t, 2) + 2 * x1 * t * one_t + x2 * Math.pow(t, 2));
        pointF.y = (float) (y * Math.pow(one_t, 2) + 2 * y1 * t * one_t + y2 * Math.pow(t, 2));
        return pointF;

    }

    public void getDotLocation(Point[] dotLocations, int width, int height){

        int w=width/4;
        int h=height/3;
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 4 ; j++) {
                Point point=new Point();
                point.x=w*j+50;
                point.y=h*i+50;
                dotLocations[j+i*4]=point;
            }
        }
    }

    public float getAngel(Point A,Point B){
        float angel=-1;
        float c= (float) Math.sqrt((A.x-B.x)*(A.x-B.x)
                +(A.y-B.y)*(A.y-B.y));
        float a=B.x-A.x;
        float b=B.y-A.y;
        if (b==0&&a>=0){
            angel=0;
        }else if (b==0&&a<0){
            angel=180;
        }else if (a==0&&b>=0){
            angel=90;
        }else if (a==0&&b<0){
            angel=270;
        }else if (b<0){
            angel= (float) (Math.PI+Math.asin(a/c)+90*Math.PI/180);
        }else if (b>0){
            angel= (float) (90*Math.PI/180-Math.asin(a/c));
        }
        return (float) (angel*180/Math.PI);
    }

    public int getXYLength(Point A,Point B){
        return (int) Math.sqrt((A.x-B.x)*(A.x-B.x)
                +(A.y-B.y)*(A.y-B.y));
    }

    public PointF getPoint(int x,int y,float angel,float scale){
        double arc=  (Math.PI * ((angel-90) / 180.0f));
        pointF.y=y;
        pointF.x= (float) (x-Math.tan(arc)*(y-60));
        return pointF;
    }
}
