package tencent.jusfoun.cn.bcdrawtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Author  JUSFOUN
 * CreateDate 2015/10/22.
 * Description
 */
public class ImageScaleView extends View {

    private Bitmap bitmap;
    private Matrix matrix;
    private Context context;
    private PointF mCir;
    private float count=0;
    private Thread thread;

    private Paint paint;
    private int maxWidth = 255;
    private boolean isStarting = false;
    private List<String> alphaList = new ArrayList<String>();
    private List<String> startWidthList = new ArrayList<String>();
    public ImageScaleView(Context context) {
        super(context);
        init(context);
    }

    public ImageScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        this.context=context;
        matrix=new Matrix();
        bitmap= BitmapFactory.decodeResource(context.getResources(),R.mipmap.cir);
        mCir=new PointF();

        paint = new Paint();
        paint.setColor(Color.RED);//此处颜色可以改为自己喜欢的
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.FILL);
        alphaList.add("255");//圆心的不透明度
        startWidthList.add("0");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCir.x=w/2;
        mCir.y=h/2;
        start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        for (int i = 0; i < alphaList.size(); i++) {
            int alpha = Integer.parseInt(alphaList.get(i));
            int startWidth = Integer.parseInt(startWidthList.get(i));
            paint.setAlpha(alpha);
            canvas.drawCircle(mCir.x, mCir.y, startWidth,paint);
            //同心圆扩散
            if ( alpha > 0 && startWidth < maxWidth)
            {
                alphaList.set(i, (alpha-2)+"");
                startWidthList.set(i, (startWidth+1)+"");
            }
        }
        if (Integer.parseInt(startWidthList.get(startWidthList.size() - 1)) == maxWidth / 5) {
            alphaList.add("255");
            startWidthList.add("0");
        }
        //同心圆数量达到6个，删除最外层圆
        if(startWidthList.size()==3)
        {
            startWidthList.remove(0);
            alphaList.remove(0);
        }


//        matrix.setScale(count / 50, count / 50, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
//        matrix.postTranslate(mCir.x - bitmap.getWidth() / 2, mCir.y-bitmap.getHeight() / 2);
//        canvas.drawBitmap(bitmap,matrix,null);
        canvas.restore();
    }

    public void start(){
        if (thread==null){
            thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        if (count>50)
                            count=0;
                        else
                            count++;
                        try {
                            Thread.sleep(20);
                            postInvalidate();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            if (!thread.isAlive())
                thread.start();
        }
    }
}
