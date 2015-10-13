package tencent.jusfoun.cn.bcdrawtest;

import android.graphics.Paint;

/**
 * Author  JUSFOUN
 * CreateDate 2015/9/18.
 * Description
 */
public class MathHelper {

    private static MathHelper mathHelper;
    public static MathHelper getInstance(){
        if (mathHelper==null)
            mathHelper=new MathHelper();
        return mathHelper;
    }

    private CirPoint cirPoint=new CirPoint();
    /**
     * 根据圆心坐标与角度计算圆心射线与圆交点坐标
     * @param angle 角度
     * @param X X坐标
     * @param Y Y坐标
     * @param radius 圆半径
     * @return 点坐标
     */
    public CirPoint getXY(float angle,float X,float Y,float radius){

        float arcAngle= (float) (Math.PI*(angle/180.0f));
        if (Float.compare(arcAngle,0.0f)<0){
            cirPoint.X=cirPoint.Y=0;
        }else if (Float.compare(angle,0.0f)==0){
            cirPoint.X=X+radius;
            cirPoint.Y=Y;
        }else if (Float.compare(angle,90.0f)==0){
            cirPoint.X=X;
            cirPoint.Y=Y+radius;
        }else if (Float.compare(angle,180.0f)==0){
            cirPoint.X=X-radius;
            cirPoint.Y=Y;
        }else if (Float.compare(angle,270.0f)==0){
            cirPoint.X=X;
            cirPoint.Y=Y-radius;
        }else if (Float.compare(angle,0.0f)>0
                &&Float.compare(angle,90.0f)<0){
            cirPoint.X= (float) (X+Math.cos(arcAngle)*radius);
            cirPoint.Y= (float) (Y+Math.sin(arcAngle)*radius);

        }else if (Float.compare(angle,90.0f)>0
                &&Float.compare(angle,180.0f)<0){
            angle=180-angle;
            arcAngle=(float) (Math.PI*(angle/180.0f));
            cirPoint.X= (float) (X-Math.cos(arcAngle)*radius);
            cirPoint.Y= (float) (Y+Math.sin(arcAngle) * radius);
        }else if (Float.compare(angle,180.0f)>0
                &&Float.compare(angle,270.0f)<0){
            angle-=180;
            arcAngle=(float) (Math.PI*(angle/180.0f));
            cirPoint.X= (float) (X-Math.cos(arcAngle)*radius);
            cirPoint.Y= (float) (Y-Math.sin(arcAngle)*radius);
        }else if (Float.compare(angle,270.0f)>0
                &&Float.compare(angle,360.0f)<0){
            angle=360-angle;
            arcAngle=(float) (Math.PI*(angle/180.0f));
            cirPoint.X= (float) (X+Math.cos(arcAngle)*radius);
            cirPoint.Y= (float) (Y-Math.sin(arcAngle)*radius);
        }

        return cirPoint;
    }

    /**
     * 获取三次贝塞尔曲线第二个转折点坐标
     * @param x1 起点X坐标
     * @param y1 起点Y坐标
     * @param x2 目标点X坐标
     * @param y2 目标点Y坐标
     * @param mCirX 圆心X坐标
     * @param mCirY 圆心Y坐标
     * @param mRaius 半径
     * @return 贝塞尔曲线第二个转折点
     */
    public CirPoint getBezierCubic(float x1, float y1, float x2, float y2,
                                   float mCirX, float mCirY, float mRaius){

        float lengthX=x2-mCirX;
        float lengthY=y2-mCirY;
//        float arcAngle= (float) Math.atan2(Math.abs(lengthY),Math.abs(lengthX));
        float arcAngle= (float) Math.atan2(Math.abs(y2-mCirY),Math.abs(x2-mCirX));
        float arcAngleCir= (float) Math.atan2(Math.abs(y1-mCirY),Math.abs(x1-mCirX));

        if (arcAngle>arcAngleCir){
            arcAngle+=Math.PI*(10f/180f);
        }else if (arcAngle<arcAngleCir)
        {
            arcAngle-=Math.PI*(10f/180f);
        }

        if (lengthX>0)
            cirPoint.X= (float) (mRaius*Math.cos(arcAngle))+mCirX;
        else
            cirPoint.X= mCirX-(float) (mRaius*Math.cos(arcAngle));
        if (lengthY>0)
            cirPoint.Y= (float) (mRaius*Math.sin(arcAngle))+mCirY;
        else
            cirPoint.Y= mCirY-(float) (mRaius*Math.sin(arcAngle));

        return cirPoint;
    }

    /**
     * 获取同方向二次贝塞尔曲线转折点
     * @param x 目标点X坐标
     * @param y 目标点Y坐标
     * @param mCirX 原点X坐标
     * @param mCirY  原点Y坐标
     * @param mRaius 半径
     * @return 转折点坐标
     */
    public CirPoint getBezierQuad(float x,float y,float mCirX, float mCirY,
                                  float mRaius){

        float arcAngle= (float) Math.atan2(Math.abs(y),Math.abs(x));

//        if (x>0)
            arcAngle+=Math.PI*(30f/180f);
//        else
//            arcAngle-=Math.PI*(30f/180f);
        if (x>0)
            cirPoint.X= (float) (mRaius*Math.cos(arcAngle))+mCirX;
        else
            cirPoint.X= mCirX-(float) (mRaius*Math.cos(arcAngle));
        if (y>0)
            cirPoint.Y= (float) (mRaius*Math.sin(arcAngle))+mCirY;
        else
            cirPoint.Y= mCirY-(float) (mRaius*Math.sin(arcAngle));
        return cirPoint;
    }
}