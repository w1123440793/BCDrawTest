package tencent.jusfoun.cn.bcdrawtest;

import android.graphics.RectF;

/**
 * Author  JUSFOUN
 * CreateDate 2015/10/9.
 * Description
 */
public class CoordTools {

    public boolean checkCoord(float x,float y,RectF rectF){
        if( (Float.compare(x, rectF.left) == 1 || Float.compare(x, rectF.left) == 0 )
                && (Float.compare(x, rectF.right) == -1 || Float.compare(x, rectF.right) == 0 )
                && (Float.compare(y, rectF.top) == 1 || Float.compare(y, rectF.top) == 0 )
                && (Float.compare(y, rectF.bottom) == -1 || Float.compare(y, rectF.bottom) == 0 ) ){
            return true;
        }
        return false;
    }
}
