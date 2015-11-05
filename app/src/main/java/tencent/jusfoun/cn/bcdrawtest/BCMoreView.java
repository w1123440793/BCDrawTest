package tencent.jusfoun.cn.bcdrawtest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Author  JUSFOUN
 * CreateDate 2015/9/23.
 * Description
 */
public class BCMoreView extends View {

    private float mRaius;
    private float mCirX, mCirY;
    private Paint paintOne, paintTwo, paintThree;
    private boolean isDestory;
    private Paint cirPaint;

    private Context context;
    private Path path;
    private TextPaint textPaint;
    private ArrayList<RectF> list = new ArrayList<RectF>();
    private ArrayList<String> strings = new ArrayList<String>();

    private CirPoint cirPoint;
    private PointF pointFOne, pointFTwo;
    private ValueAnimator valueAnimatorOne, valueAnimatorTwo;
    private int duration = 1000;
    private boolean fristLog = true;
    private int index = 4;

    private ArrayList<ArrayList<PointF>> lists = new ArrayList<>();
    private ArrayList<PointF> p0 = new ArrayList<>();
    private ArrayList<PointF> p1 = new ArrayList<>();
    private ArrayList<PointF> p2 = new ArrayList<>();
    private ArrayList<PointF> p3 = new ArrayList<>();
    private ArrayList<ValueAnimator> animators = new ArrayList<>();

    private ArrayList<Paint> paintList = new ArrayList<>();
    private float downX, downY, minit, scal = 1;
    private float[] mTran = new float[2];
    private Handler handler = null;
    private PointF[] pointFsOne, pointFsTwo;

    private ArrayList<PointF> p4 = new ArrayList<>();
    private boolean isSacle = false;
    private boolean isMove = false;
    private float minp0X = 0;
    private float minp2X = 0;

    public BCMoreView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public BCMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public BCMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
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
//        cirPaint.setStyle(Paint.Style.FILL);
        cirPaint.setStyle(Paint.Style.STROKE);
        cirPaint.setColor(Color.CYAN);
        cirPaint.setStrokeWidth(10);
        cirPaint.setAntiAlias(true);
//        cirPaint.setAlpha(100);

        path = new Path();

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(20);
        textPaint.setTextAlign(Paint.Align.LEFT);

        isDestory = false;
        cirPoint = new CirPoint();
        pointFsOne = new PointF[3];
        pointFsTwo = new PointF[3];

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//            duration=500;

                for (ValueAnimator v : animators) {
                    if (!v.isStarted() && !v.isRunning()) {

                        v.start();
                    }
                }
            }

        };


    }

    private boolean is = true;

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
//        canvas.scale(scal, scal, mCirX, mCirY);

        for (int i = 0; i < p0.size(); i++) {
            if (i == 0)
                minp0X = p0.get(0).x;
            else
                minp0X = Math.max(minp0X, p0.get(i).x);
            ArrayList<PointF> ps = lists.get(i);
            canvas.drawCircle(p0.get(i).x, p0.get(i).y, mCount, paintList.get(i));
            path.reset();
            path.moveTo(p0.get(i).x, p0.get(i).y);
            if (fristLog) {
                for (int j = 0; j < ps.size(); j++) {
                    path.lineTo(ps.get(j).x, ps.get(j).y);
                    if (j == ps.size() - 1)
                        canvas.drawCircle(ps.get(j).x, ps.get(j).y, 20, paintList.get(i));
                }
            } else {
                if (!isSacle) {
                    int j = a;
                    if (j < ps.size()) {
                        canvas.drawCircle(ps.get(j).x, ps.get(j).y, 20, paintList.get(i));
                    } else {
                        a = 0;
                    }
                    path.quadTo(p1.get(i).x, p1.get(i).y, mCirX, mCirY);
                }
            }
            canvas.drawPath(path, paintOne);
        }

        for (int i = p0.size(); i < p2.size() + p0.size(); i++) {
            int count = i - p0.size();
            if (count == 0)
                minp2X = p2.get(0).x;
            else
                minp2X = Math.min(minp2X, p2.get(count).x);
            ArrayList<PointF> ps = lists.get(i);
            canvas.drawCircle(p2.get(count).x, p2.get(count).y, mCount, paintList.get(i));
            path.reset();
            path.moveTo(mCirX, mCirY);
            if (fristLog) {
                for (int j = 0; j < ps.size(); j++) {

                    path.lineTo(ps.get(j).x, ps.get(j).y);
                    if (j == ps.size() - 1)
                        canvas.drawCircle(ps.get(j).x, ps.get(j).y, 20, paintList.get(i));
                }
            } else {
                if (!isSacle) {
                    int j = a;
                    if (j < ps.size()) {
                        canvas.drawCircle(ps.get(j).x, ps.get(j).y, 20, paintList.get(i));
//                    canvas.drawCircle(pointf.x, pointf.y, 20, paintList.get(i));
                    }
                    path.quadTo(p3.get(count).x, p3.get(count).y, p2.get(count).x, p2.get(count).y);
                }
            }
            canvas.drawPath(path, paintTwo);
        }
//        canvas.drawCircle(mCirX, mCirY, mCount, cirPaint);
        canvas.drawText("第一个", mCirX, mCirY, textPaint);

        if (isDrawChildLine) {
            path.reset();
            if (count < p0.size()) {
                path.moveTo(p0.get(count).x, p0.get(count).y);
                path.quadTo(pointChildBc.x, pointChildBc.y, pointFChild.x, pointFChild.y);
            } else {
                path.moveTo(p2.get(count - p0.size()).x, p2.get(count - p0.size()).y);
                path.quadTo(pointChildBc.x, pointChildBc.y, pointFChild.x, pointFChild.y);
            }
            canvas.drawPath(path, paintThree);
            canvas.drawCircle(pointFChild.x, pointFChild.y, mCount, cirPaint);
        }
        canvas.restore();
        is = false;
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

        RectF rectf = new RectF(mCirX - 50, mCirY - 50, mCirX + 50, mCirY + 50);
        list.add(rectf);
        strings.add("第0个");

        float count = (180 - 20) / index;
        for (int i = 0; i < index; i++) {
            float angle = count * i + 130;
            cirPoint = MathHelper.getInstance().getXY(angle, mCirX, mCirY, mRaius * 9);
            PointF pointf = new PointF();
            pointf.x = cirPoint.X;
            pointf.y = cirPoint.Y;
            p0.add(pointf);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setColor(Color.CYAN);
            paint.setStrokeWidth(20);
//            RadialGradient shader=new RadialGradient(p0.get(i).x,p0.get(i).y,mRaius,
//                    new int[]{Color.TRANSPARENT,Color.CYAN},new float[]{0.2f, 1f},Shader.TileMode.REPEAT);
            RadialGradient shader = new RadialGradient(p0.get(i).x, p0.get(i).y, mRaius, Color.TRANSPARENT, Color.CYAN, Shader.TileMode.REPEAT);
            paint.setShader(shader);
            paint.setAntiAlias(true);
//            paint.setAlpha(100);
            paintList.add(paint);

            rectf = new RectF(pointf.x - 50, pointf.y - 50, pointf.x + 50, pointf.y + 50);
            list.add(rectf);
            strings.add("第" + (i + 1) + "个");
        }

        for (int i = 0; i < index; i++) {
            float angle = count * i + 310;
            if (angle > 360)
                angle -= 360;
            cirPoint = MathHelper.getInstance().getXY(angle, mCirX, mCirY, mRaius * 9);
            PointF pointf = new PointF();
            pointf.x = cirPoint.X;
            pointf.y = cirPoint.Y;
            p2.add(pointf);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.CYAN);
            paint.setStrokeWidth(3);
            paint.setAntiAlias(true);
            paint.setAlpha(100);
            paintList.add(paint);
            rectf = new RectF(pointf.x - 50, pointf.y - 50, pointf.x + 50, pointf.y + 50);
            list.add(rectf);
            strings.add("第" + (i + p0.size() + 1) + "个");
        }

        for (PointF pointf : p0) {
            cirPoint = MathHelper.getInstance().getBezierQuad(pointf.x - mCirX, pointf.y - mCirY, mCirX, mCirY, mRaius * 5.5f);
            PointF p = new PointF();
            p.x = cirPoint.X;
            p.y = cirPoint.Y;
            p1.add(p);
        }

        for (PointF pointf : p2) {
            cirPoint = MathHelper.getInstance().getBezierQuad(pointf.x - mCirX, pointf.y - mCirY, mCirX, mCirY, mRaius * 5.5f);
            PointF p = new PointF();
            p.x = cirPoint.X;
            p.y = cirPoint.Y;
            p3.add(p);
        }

        RadialGradient shader = new RadialGradient(mCirX, mCirY, mRaius, Color.TRANSPARENT, Color.CYAN, Shader.TileMode.REPEAT);
        cirPaint.setShader(shader);
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

    private boolean isDrawChildLine = false;
    private PointF pointFChild = new PointF();
    private PointF pointChildBc = new PointF();
    private int count = 0;

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
                if (isMove) {
                    isMove = false;
                    break;
                }
                float x = 0;
//                if (event.getX()<mCirX)
//                     x= event.getX() - mTran[0]+x1+x3;
//                else if (event.getX()>mCirX)
//                    x= event.getX() - mTran[0]-x2+x4;
//                else
                x = event.getX() - mTran[0];
                float y = event.getY() - mTran[1];
                int count = isInRect(x, y);
                if (count >= 0) {
                    Toast.makeText(context, strings.get(count), Toast.LENGTH_SHORT).show();
                    if (count != 0) {
                        count -= 1;
                        isDrawChildLine = true;
                        if (count < p0.size()) {
                            cirPoint = MathHelper.getInstance().getXY(160, p0.get(count).x, p0.get(count).y, 9 * mRaius);
                            pointFChild.x = cirPoint.X;
                            pointFChild.y = cirPoint.Y;
                            cirPoint = MathHelper.getInstance().getBezierQuad(pointFChild.x - p0.get(count).x, pointFChild.y - p0.get(count).y
                                    , p0.get(count).x, p0.get(count).y, 5f * mRaius);
                            pointChildBc.x = cirPoint.X;
                            pointChildBc.y = cirPoint.Y;
                            BCMoreView.this.count = count;
                        } else {
                            cirPoint = MathHelper.getInstance().getXY(330, p2.get(count - p0.size()).x, p2.get(count - p0.size()).y, 9 * mRaius);
                            pointFChild.x = cirPoint.X;
                            pointFChild.y = cirPoint.Y;
                            cirPoint = MathHelper.getInstance().getBezierQuad(pointFChild.x - p2.get(count - p0.size()).x, pointFChild.y - p2.get(count - p0.size()).y
                                    , p2.get(count - p0.size()).x, p2.get(count - p0.size()).y, 5f * mRaius);
                            pointChildBc.x = cirPoint.X;
                            pointChildBc.y = cirPoint.Y;
                            BCMoreView.this.count = count;
                        }
                    }
                }
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
    private int a = 0;
    private PointF pointf;

    public void start() {

        animators.clear();
        lists.clear();
        fristLog = true;
        duration = 1000;

        PointF cir = new PointF(mCirX, mCirY);
        for (int i = 0; i < p0.size(); i++) {
            PointF[] ps = new PointF[3];
            ps[0] = p0.get(i);
            ps[1] = p1.get(i);
            ps[2] = cir;
            final ArrayList<PointF> p = new ArrayList<>();
            ValueAnimator valueAnimator = new ValueAnimator().ofObject(new BezierTypeEvaluator(ps), new PointF());
            valueAnimator.setDuration(1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    pointf = (PointF) animation.getAnimatedValue();
                    PointF point = new PointF(pointf.x, pointf.y);
                    if (pointf.x == mCirX)
                        fristLog = false;
                    if (fristLog) {
                        if (!p.contains(point))
                            p.add(point);
                    }
                    postInvalidate();
                }
            });
//            ArrayList<PointF> p=new ArrayList<>();
//            for (PointF pi:pppp){
//                p.add(pi);
//            }
            lists.add(p);
            animators.add(valueAnimator);
        }

        for (int i = 0; i < p2.size(); i++) {
            PointF[] ps = new PointF[3];
            ps[2] = p2.get(i);
            ps[1] = p3.get(i);
            ps[0] = cir;
            final ArrayList<PointF> p = new ArrayList<>();
            ValueAnimator valueAnimator = new ValueAnimator().ofObject(new BezierTypeEvaluator(ps), new PointF());
            valueAnimator.setDuration(1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    pointf = (PointF) animation.getAnimatedValue();
                    PointF point = new PointF(pointf.x, pointf.y);
                    if (!p.contains(point))
                        p.add(point);
                    postInvalidate();
                }
            });
            lists.add(p);
            animators.add(valueAnimator);
        }
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
                        if (fristLog)
                            handler.sendMessage(new Message());
                        if (mCount < mRaius) {
                            mCount += 1f;
                            mAlp *= (1 - (float) mCount / mRaius);
//                            Log.e("malp",mAlp+"");
                        } else {
                            mCount = 0;
                            mAlp = 255;
                        }
                        a++;
                        try {
                            Thread.sleep(20);
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

    private float initX = 0, initY = 0;

    public void translate(MotionEvent event) {
        if (doublePointer) {
            doublePointer = false;
            return;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                initX = downX;
                initY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                if ((Math.abs(event.getX() - initX)) < 5 && (Math.abs(event.getY() - initY) < 5))
                    isMove = false;
                else
                    isMove = true;
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


    /**
     * 双指放大，同时放大或者缩小
     */

    /**
     * 双指放大，左指左移向左放大，右指右移向右放大
     */
    private float x1 = 0, x2 = 0, x3 = 0, x4 = 0;
    private boolean fristPointInLeft = true;
    private boolean doublePointer = false;
    private boolean isScale = true;
    private boolean isShowToast = true;

    public void scale(MotionEvent event) {
        isMove = true;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_UP:
//                for(PointF pointF:p0){
//                    pointF.x*=scal;
//                }
//                for(PointF pointF:p2){
//                    pointF.x*=scal;
//                }
                mTran[0] = 0;
                mTran[1] = 0;
                x3 = 0;
                x4 = 0;
                start();
                isSacle = false;
                postInvalidate();
//                x1=0;
//                x2=0;
//                x3=0;
//                x4=0;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isSacle)
                    break;
                float toScal = getInitLength(event);
//                if (Float.compare(minit, 0f) == 0)
//                    break;
//                scal = toScal / minit;
                int i = 0;
                for (PointF pointF : p0) {
                    float x = pointF.x;
                    if (fristPointInLeft)
                        x += (event.getX(0) + x3 - x1);
                    else
                        x += (event.getX(1) + x4 - x1);
                    if (x >= mCirX) {
                        if (isShowToast) {
                            Toast.makeText(context, "左边不能再缩小了！！！", Toast.LENGTH_SHORT).show();
                            isShowToast = false;
                        }
                        isSacle = false;
                    } else {
                        pointF.x = x;
                        list.get(i + 1).set(pointF.x - 100, pointF.y - 100, pointF.x + 100, pointF.y + 100);
                        i++;
                    }
                }
                i = 0;
                for (PointF pointF : p2) {
                    float x = pointF.x;
                    if (fristPointInLeft)
                        x += (event.getX(1) + x4 - x2);
                    else
                        x += (event.getX(0) + x3 - x2);
                    if (x <= mCirX) {
                        if (isShowToast)
                            Toast.makeText(context, "右边不能再缩小了！！！", Toast.LENGTH_SHORT).show();
                        isSacle = false;
                    } else {
                        pointF.x = x;
                        list.get(i + 1 + p0.size()).set(pointF.x - 100, pointF.y - 100, pointF.x + 100, pointF.y + 100);
                        i++;
                    }
                }
                if (fristPointInLeft) {
                    x3 = x1 - event.getX(0);
                    x4 = x2 - event.getX(1);
                } else {
                    x3 = x2 - event.getX(0);
                    x4 = x1 - event.getX(1);
                }
                postInvalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                isSacle = true;
                isShowToast = true;
                if (event.getPointerCount() == 2) {
                    doublePointer = true;
                    if (event.getX(0) > mCirX) {
                        x2 = event.getX(0);
                        x1 = event.getX(1);
                        fristPointInLeft = false;
                        Log.e("eventx", event.toString());
                    } else {
                        x1 = event.getX(0);
                        x2 = event.getX(1);
                        fristPointInLeft = true;
                    }
//                    minit = getXYLength(event);
                    isSacle = true;
                    postInvalidate();
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
