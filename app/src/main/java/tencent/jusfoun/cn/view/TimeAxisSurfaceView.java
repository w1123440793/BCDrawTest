package tencent.jusfoun.cn.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import tencent.jusfoun.cn.bcdrawtest.MathHelper;
import tencent.jusfoun.cn.bcdrawtest.R;

/**
 * Author  wangchenchen
 * CreateDate 2016/1/6.
 * Email wcc@jusfoun.com
 * Description
 */
public class TimeAxisSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Bitmap bgBitmap;
    private Matrix bgMatrix;
    private Context context;
    private Paint bgPaint;
    private SurfaceHolder surfaceHolder;
    private Point mCir;
    private Rect bgRect;
    private Bitmap oneBitmap,bitmap1;
    private Bitmap twoBitmap,bitmap2;
    private Bitmap threeBitmap,bitmap3;
    private Matrix oneMatrix,matrix1;
    private Matrix twoMatrix,matrix2;
    private Matrix threeMatrix,matrix3;
    private CustomThread thread;
    private float scaleCount=0f;
    private int trCount=0;
    private Point onePoint,twoPoint,threePoint;
    private PointF pointF;
    public TimeAxisSurfaceView(Context context) {
        super(context);
        initView(context);
    }

    public TimeAxisSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TimeAxisSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        this.context=context;
        surfaceHolder=getHolder();
        surfaceHolder.addCallback(this);
        bgBitmap= BitmapFactory.decodeResource(context.getResources(), R.mipmap.time_axis);
        bgMatrix=new Matrix();
        bgPaint=new Paint();
        bgPaint.setAlpha(255);
        bgRect=new Rect();
        mCir=new Point();
        thread=new CustomThread();
        oneBitmap=BitmapFactory.decodeResource(context.getResources(),R.mipmap.time_icon);
        twoBitmap=BitmapFactory.decodeResource(context.getResources(),R.mipmap.time_icon);
        threeBitmap=BitmapFactory.decodeResource(context.getResources(),R.mipmap.time_icon);
        bitmap1=BitmapFactory.decodeResource(context.getResources(),R.mipmap.legal);
        bitmap2=BitmapFactory.decodeResource(context.getResources(),R.mipmap.legal);
        bitmap3=BitmapFactory.decodeResource(context.getResources(),R.mipmap.legal);
        oneMatrix=new Matrix();
        twoMatrix=new Matrix();
        matrix1=new Matrix();
        matrix2=new Matrix();
        matrix3=new Matrix();
        threeMatrix  =new Matrix();
        onePoint=new Point();
        twoPoint=new Point();
        threePoint=new Point();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCir.x=width/2;
        mCir.y=height/2;
        bgRect.set(0,0,width,height);
        if (thread!=null&&thread.getState()== Thread.State.NEW)
            thread.start();
        else if(thread!=null) {
            thread.resumeThread();
        }
//        bgMatrix.postTranslate()
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.setPauseThread();
    }

    class CustomThread extends Thread{
        private boolean isStopThread;
        private boolean isPauseThread;
        private Canvas canvas=null;
        public void setThreadStop(){
            this.isStopThread=true;
        }
        public void setPauseThread(){
            this.isPauseThread=true;
        }
        public void resumeThread(){
            synchronized (this) {
                isPauseThread = false;
                notify();
            }
        }
        @Override
        public void run() {
            while (!isStopThread){
                if (!isPauseThread){
                    try {
//                        if (scaleCount>=2.5f){
//                            scaleCount=0.5f;
//                        }else {
//                            scaleCount+=0.1f;
//                        }
                        if (trCount>=1560){
                            trCount=60;
                            scaleCount=0.0f;
                        }else {
                            trCount+=1;
                            scaleCount+=0.001f;
                        }
                        synchronized (surfaceHolder){
                            canvas=surfaceHolder.lockCanvas();
                            onDraw(canvas);
                        }
//                        Thread.sleep(50);
                    }catch (Exception e){

                    }finally {
                        if (canvas!=null){
                            synchronized (surfaceHolder){
                                surfaceHolder.unlockCanvasAndPost(canvas);
                                canvas=null;
                            }
                        }
                    }
                }else {
                    try {
                        synchronized (surfaceHolder){
                            canvas=surfaceHolder.lockCanvas();
                            onDraw(canvas);
                            if (canvas!=null) {
                                surfaceHolder.unlockCanvasAndPost(canvas);
                                canvas = null;
                            }
                        }
                        synchronized (this) {
                            wait();
                        }
                    }catch (Exception e){

                    }
                }
            }
        }

        private void onDraw(Canvas canvas){
            canvas.drawColor(Color.BLACK);
            canvas.save();
            canvas.drawBitmap(bgBitmap, null, bgRect, bgPaint);

            float count=0;

            if (trCount>460&&trCount<1460) {
                canvas.save();
                threeMatrix.setScale(scaleCount - 0.4f, scaleCount - 0.4f, threeBitmap.getWidth() / 2, threeBitmap.getHeight() / 2);
                matrix3.setScale(scaleCount - 0.4f, scaleCount - 0.4f, bitmap3.getWidth() / 2, bitmap3.getHeight());
                pointF = MathHelper.getInstance().getPoint((mCir.x), (int) ((trCount-400)*(scaleCount-0.4f)), 49,scaleCount);
                threeMatrix.postTranslate(pointF.x - threeBitmap.getWidth() / 2, pointF.y - threeBitmap.getHeight() / 2);
                matrix3.postTranslate(pointF.x - bitmap3.getWidth() / 2, pointF.y - threeBitmap.getHeight() * (scaleCount - 0.4f) / 2 - bitmap3.getHeight());
                canvas.drawBitmap(threeBitmap, threeMatrix, bgPaint);
                canvas.drawBitmap(bitmap3, matrix3, bgPaint);
                canvas.restore();
            }

            if (trCount>260&&trCount<1260) {
                canvas.save();
                oneMatrix.setScale(scaleCount - 0.2f, scaleCount - 0.2f, oneBitmap.getWidth() / 2, oneBitmap.getHeight() / 2);
                matrix1.setScale(scaleCount-0.2f, scaleCount-0.2f, bitmap1.getWidth() / 2, bitmap1.getHeight());
                pointF = MathHelper.getInstance().getPoint( (mCir.x), (int) ( (trCount-200)*(scaleCount-0.2f)), 131,scaleCount);
//                Log.e("rect", "left==" + (pointF.x - bitmap1.getWidth() / 2 * (scaleCount-0.6f))
//                        + "\ntop==" + (pointF.y - bitmap1.getHeight() / 2 * (scaleCount-0.6f))
//                        +"\nright=="+(pointF.x + bitmap1.getWidth() / 2 * (scaleCount-0.6f))
//                        +"\nbottom=="+pointF.y );
                count-=pointF.y;
                Log.e("count",count+"");
                oneMatrix.postTranslate(pointF.x - oneBitmap.getWidth() / 2, pointF.y - oneBitmap.getHeight() / 2);
                matrix1.postTranslate(pointF.x - bitmap1.getWidth() / 2, pointF.y - oneBitmap.getHeight() * (scaleCount-0.2f) / 2 - bitmap1.getHeight());
                canvas.drawBitmap(oneBitmap, oneMatrix, bgPaint);
                canvas.drawBitmap(bitmap1, matrix1, bgPaint);
                canvas.restore();
            }

            if (trCount<1060) {
                canvas.save();
                twoMatrix.setScale(scaleCount, scaleCount, twoBitmap.getWidth() / 2, twoBitmap.getHeight() / 2);
                matrix2.setScale(scaleCount, scaleCount , bitmap2.getWidth() / 2, bitmap2.getHeight());
                pointF = MathHelper.getInstance().getPoint(mCir.x, (int) (trCount*scaleCount), 90,scaleCount);
                count=pointF.y;
                twoMatrix.postTranslate(pointF.x - twoBitmap.getWidth() / 2, pointF.y - twoBitmap.getHeight() / 2);
                matrix2.postTranslate(pointF.x - bitmap2.getWidth() / 2, pointF.y - twoBitmap.getHeight() * (scaleCount ) / 2 - bitmap2.getHeight());
                canvas.drawBitmap(twoBitmap, twoMatrix, bgPaint);
                canvas.drawBitmap(bitmap2, matrix2, bgPaint);
                canvas.restore();
            }
            canvas.restore();
        }
    }
}
