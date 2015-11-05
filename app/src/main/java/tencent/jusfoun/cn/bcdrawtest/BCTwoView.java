package tencent.jusfoun.cn.bcdrawtest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Author  JUSFOUN
 * CreateDate 2015/9/23.
 * Description
 */
public class BCTwoView extends View {

    private float mRaius;
    private float mCirX, mCirY;
    private float dotOne[] = new float[]{200, 100};
    private float dotTwo[] = new float[]{600, 150};
    private float dotThree[] = new float[]{550, 800};

    private float qOne[] = new float[]{150, 500};
    private float qTwo[] = new float[]{550, 550};
    private float qThree[] = new float[]{640, 640};
    private Paint paintOne, paintTwo, paintThree;
    private boolean isDestory;
    private Paint cirPaint;
    private Context context;
    private Path path;
    private TextPaint textPaint;
    private ArrayList<RectF> list = new ArrayList<RectF>();
    private ArrayList<String> strings = new ArrayList<String>();
    private CirPoint cirPoint;
    private CoordTools coordTools;
    private PointF pointFOne, pointFTwo;
    private ValueAnimator valueAnimatorOne, valueAnimatorTwo;
    private int duration = 1000;
    private boolean fristLog = true;
    private ArrayList<PointF> pointFslist = new ArrayList<>();
    private ArrayList<PointF> pFL = new ArrayList<>();

    private float downX, downY, minit, scal = 1;
    private float[] mTran = new float[2];

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            duration=500;
            if (valueAnimatorOne != null) {
                if (!valueAnimatorOne.isStarted() && !valueAnimatorOne.isRunning()) {
//                    if (!fristLog)
//                        valueAnimatorOne.setDuration(duration);
                    valueAnimatorOne.start();
                }
            }
            if (valueAnimatorTwo != null) {
                if (!valueAnimatorTwo.isStarted() && !valueAnimatorTwo.isRunning()) {
//                    if (!fristLog)
//                        valueAnimatorTwo.setDuration(duration);
                    valueAnimatorTwo.start();
                }
            }
        }
    };

    private PointF[] pointFsOne, pointFsTwo;

    public BCTwoView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public BCTwoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public BCTwoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public void initView() {

        paintOne = new Paint();
        paintOne.setStyle(Paint.Style.STROKE);
        paintOne.setColor(Color.BLUE);
        paintOne.setPathEffect(new DashPathEffect(new float[]{4, 8, 5, 10}, 1));
        paintOne.setStrokeWidth(3);
        paintOne.setAntiAlias(true);
        paintOne.setAlpha(255);

        paintTwo = new Paint();
        paintTwo.setStyle(Paint.Style.STROKE);
        paintTwo.setColor(Color.RED);
        paintTwo.setStrokeWidth(3);
        paintTwo.setAntiAlias(true);
        paintTwo.setAlpha(255);

        paintThree = new Paint();
        paintThree.setStyle(Paint.Style.STROKE);
        paintThree.setColor(Color.DKGRAY);
        paintThree.setStrokeWidth(3);
        paintThree.setAntiAlias(true);
        paintThree.setAlpha(255);

        cirPaint = new Paint();
        cirPaint.setStyle(Paint.Style.FILL);
//        cirPaint.setStyle(Paint.Style.FILL);
        cirPaint.setColor(Color.CYAN);
        cirPaint.setStrokeWidth(3);
        cirPaint.setAntiAlias(true);
        cirPaint.setAlpha(100);

        path = new Path();
        for (int i = 0; i < 4; i++) {
            strings.add("你点的第" + (i + 1) + "个点");
        }

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(20);
        textPaint.setTextAlign(Paint.Align.LEFT);

        isDestory = false;
        cirPoint = new CirPoint();
        coordTools = new CoordTools();
        pointFsOne = new PointF[3];
        pointFsTwo = new PointF[3];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
//        cirPaint.setAlpha(mAlp);
        if (mTran[0] < -mCirX)
            mTran[0] = -mCirX;
        if (mTran[0] > mCirX)
            mTran[0] = mCirX;
        if (mTran[1] < -mCirY)
            mTran[1] = -mCirY;
        if (mTran[1] > mCirY)
            mTran[1] = mCirY;
        canvas.translate(mTran[0], mTran[1]);
//        } else if (mode == SCAL)
        canvas.scale(scal, scal, mCirX, mCirY);
        path.reset();
//        path.quadTo(qOne[0], qOne[1], dotOne[0], dotOne[1]);
        path.moveTo(dotOne[0], dotOne[1]);
        if (fristLog) {
//            if (pointFOne != null) {
//                if (mCirX!=0&&mCirY!=0) {
//                    if (pointFslist.size()>2){
            for (int i = 0; i < pointFslist.size(); i++) {
                path.lineTo(pointFslist.get(i).x, pointFslist.get(i).y);
                Log.e("size", pointFslist.size() + "");
            }
//                    }
            if (pointFOne != null) {
                PointF p = new PointF(pointFOne.x, pointFOne.y);
                if (!pointFslist.contains(p))
                    pointFslist.add(p);
//                    path.rQuadTo(qOne[0], qOne[1], mCirX, mCirY);
            }
//            }
        } else {

            path.quadTo(qOne[0], qOne[1], mCirX, mCirY);
        }
        canvas.drawPath(path, paintOne);

        canvas.drawCircle(dotOne[0], dotOne[1], mCount, cirPaint);
        path.reset();
        path.moveTo(mCirX, mCirY);
        if (fristLog) {
            if (pointFTwo != null) {
                if (pointFTwo.x != 0 && pointFTwo.y != 0) {
                    if (pFL.size() > 2) {
                        for (int i = 0; i < pFL.size(); i++) {
                            path.lineTo(pFL.get(i).x, pFL.get(i).y);
                        }
                    }
                    PointF p = new PointF(pointFTwo.x, pointFTwo.y);
                    if (!pFL.contains(p))
                        pFL.add(p);
//                    path.rQuadTo(qOne[0], qOne[1], mCirX, mCirY);
                }
            }
        } else {
            path.quadTo(qTwo[0], qTwo[1], dotTwo[0], dotTwo[1]);
        }
        canvas.drawPath(path, paintTwo);
        canvas.drawCircle(dotTwo[0], dotTwo[1], mCount, cirPaint);

        path.reset();
        path.moveTo(mCirX, mCirY);
        path.quadTo(qThree[0], qThree[1], dotThree[0], dotThree[1]);
        canvas.drawPath(path, paintThree);
        canvas.drawCircle(dotThree[0], dotThree[1], mCount, cirPaint);

        canvas.drawCircle(mCirX, mCirY, mCount, cirPaint);

        canvas.drawText("第一个", mCirX, mCirY, textPaint);
        canvas.drawText("第二个", dotOne[0], dotOne[1], textPaint);
        canvas.drawText("第三个", dotTwo[0], dotTwo[1], textPaint);
        canvas.drawText("第四个", dotThree[0], dotThree[1], textPaint);

        if (pointFOne != null) {
            canvas.drawCircle(pointFOne.x, pointFOne.y, 10, cirPaint);
        }

        if (pointFTwo != null)
            canvas.drawCircle(pointFTwo.x, pointFTwo.y, 10, cirPaint);
        canvas.restore();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCirX = w / 2;
        mCirY = h / 2;
        if (mCirX > mCirY) {
            mRaius = (float) (mCirY * 0.1);
        } else {
            mRaius = (float) (mCirX * 0.1);
        }
        cirPoint = MathHelper.getInstance().getBezierQuad(dotOne[0] - mCirX, dotOne[1] - mCirY, mCirX, mCirY, mRaius * 3f);
        qOne[0] = cirPoint.X;
        qOne[1] = cirPoint.Y;
        cirPoint = MathHelper.getInstance().getBezierQuad(dotTwo[0] - mCirX, dotTwo[1] - mCirY, mCirX, mCirY, mRaius * 3f);
        qTwo[0] = cirPoint.X;
        qTwo[1] = cirPoint.Y;
        cirPoint = MathHelper.getInstance().getBezierQuad(dotThree[0] - mCirX, dotThree[1] - mCirY, mCirX, mCirY, mRaius * 3f);
        qThree[0] = cirPoint.X;
        qThree[1] = cirPoint.Y;
        RectF cir = new RectF();
        RectF one = new RectF();
        RectF two = new RectF();
        RectF three = new RectF();
        cir.set(mCirX - 100, mCirY - 100, mCirX + 100, mCirY + 100);
        one.set(dotOne[0] - 50, dotOne[1] - 50, dotOne[0] + 50, dotOne[1] + 50);
        two.set(dotTwo[0] - 50, dotTwo[1] - 50, dotTwo[0] + 50, dotTwo[1] + 50);
        three.set(dotThree[0] - 50, dotThree[1] - 50, dotThree[0] + 50, dotThree[1] + 50);
        list.add(cir);
        list.add(one);
        list.add(two);
        list.add(three);

        PointF pointF1 = new PointF();
        pointF1.x = mCirX;
        pointF1.y = mCirY;
        pointFsOne[2] = pointF1;
        pointFsTwo[0] = pointF1;

        PointF pointF2 = new PointF();
        pointF2.x = qOne[0];
        pointF2.y = qOne[1];
        pointFsOne[1] = pointF2;

        PointF pointF3 = new PointF();
        pointF3.x = dotOne[0];
        pointF3.y = dotOne[1];
        pointFsOne[0] = pointF3;


        PointF pointF5 = new PointF();
        pointF5.x = qTwo[0];
        pointF5.y = qTwo[1];
        pointFsTwo[1] = pointF5;

        PointF pointF6 = new PointF();
        pointF6.x = dotTwo[0];
        pointF6.y = dotTwo[1];
        pointFsTwo[2] = pointF6;

        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isDestory = true;
        stopThread();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getPointerCount()) {
            case 1:
                translate(event);
                break;
            case 2:
                scale(event);
                break;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                float x = event.getX() - mTran[0];
                float y = event.getY() - mTran[1];
                int count = isInRect(x, y);
                if (count >= 0)
                    Toast.makeText(context, strings.get(count), Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    private int isInRect(float x, float y) {

        int count = 0;
        for (RectF rectF : list) {
            if (rectF.contains(x, y))
                return count;
//            if(coordTools.checkCoord(x,y,rectF)){
//                return count;
//            }
            count++;
        }
        return -1;
    }

    private Thread thread;
    private float mCount = 0;
    private int mAlp = 255;

    public void start() {

        fristLog = true;
        duration = 1000;
        valueAnimatorOne = ValueAnimator.ofObject(new BezierTypeEvaluator(pointFsOne), new PointF());
        valueAnimatorOne.setDuration(duration);
        valueAnimatorOne.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointFOne = (PointF) animation.getAnimatedValue();
                if (pointFOne.x == mCirX) {
                    fristLog = false;
                }
                postInvalidate();
            }
        });
        valueAnimatorTwo = ValueAnimator.ofObject(new BezierTypeEvaluator(pointFsTwo), new PointF());
        valueAnimatorTwo.setDuration(duration);
        valueAnimatorTwo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointFTwo = (PointF) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!isDestory) {
                        handler.sendMessage(new Message());
                        if (mCount < mRaius) {
                            mCount += 1f;
                            mAlp *= (1 - mCount / mRaius);
                        } else {
                            mCount = 0;
                            mAlp = 255;
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        postInvalidate();
                    }
                }
            });
        }
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    private void stopThread() {
        if (thread != null && thread.isInterrupted()) {
            thread.interrupt();
            thread = null;
            System.gc();
        }
    }

    private void getArc(float x, float y, float bcXY[]) {
        if (bcXY.length < 2)
            return;
        float arcAngle = (float) Math.atan2(Math.abs(y), Math.abs(x));
//        if (arcAngle+Math.PI*(30f/180f)>Math.PI/2){
//            arcAngle-=Math.PI-arcAngle;
//        }
//        else
        if (x > 0)
            arcAngle += Math.PI * (30f / 180f);
        else
            arcAngle -= Math.PI * (30f / 180f);
//        if (arcAngle>Math.PI/2)
//            arcAngle-=Math.PI*(30f/180f);
//        else if (arcAngle<=Math.PI/2)
//            arcAngle+=Math.PI*(30f/180f);
        if (x > 0)
            bcXY[0] = (float) (mRaius * 3 * Math.cos(arcAngle)) + mCirX;
        else
            bcXY[0] = mCirX - (float) (mRaius * 3 * Math.cos(arcAngle));
        if (y > 0)
            bcXY[1] = (float) (mRaius * 3 * Math.sin(arcAngle)) + mCirY;
        else
            bcXY[1] = mCirY - (float) (mRaius * 3 * Math.sin(arcAngle));
    }

    public void translate(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mTran[0] = mTran[0] + event.getX() - downX;
                mTran[1] = mTran[1] + event.getY() - downY;
                downX = event.getX();
                downY = event.getY();
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
    }

    public void scale(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float toScal = getInitLength(event);
                if (Float.compare(minit, 0f) == 0)
                    break;
                scal = toScal / minit;
                Log.e("scal", scal + "");
                postInvalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    minit = getInitLength(event);
                }
                break;
        }
    }

    public float getInitLength(MotionEvent event) {

        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

}
