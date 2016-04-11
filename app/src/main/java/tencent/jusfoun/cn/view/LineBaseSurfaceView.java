package tencent.jusfoun.cn.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.widget.Toast;

import java.util.ArrayList;

import tencent.jusfoun.cn.bcdrawtest.BezierOneTypeEvaluator;
import tencent.jusfoun.cn.bcdrawtest.DataModel;
import tencent.jusfoun.cn.bcdrawtest.MathHelper;
import tencent.jusfoun.cn.bcdrawtest.R;

/**
 * Author  wangchenchen
 * CreateDate 2015/12/29.
 * Email wcc@jusfoun.com
 * Description
 */
public class LineBaseSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private int width, height;
    protected Context context;
    protected PointF mCir;
    protected float mRaius;
    protected Paint mCirPaint, mLeftPaint, mRightPaint, mCirAnimPaint;

    protected boolean enableTouch, childAnimStop, threadStop;

    protected ArrayList<PointF> leftLines = new ArrayList<>();
    protected ArrayList<PointF> rightLines = new ArrayList<>();
    protected ArrayList<Paint> leftPaints = new ArrayList<>();
    protected ArrayList<Paint> rightPaints = new ArrayList<>();

    protected ArrayList<ValueAnimator> leftAnims = new ArrayList<>();
    protected ArrayList<ValueAnimator> rightAnims = new ArrayList<>();
    protected ArrayList<ArrayList<PointF>> leftPoints = new ArrayList<>();
    protected ArrayList<ArrayList<PointF>> rightPoints = new ArrayList<>();

    protected ArrayList<String> leftInfo = new ArrayList<>();
    protected ArrayList<String> rightInfo = new ArrayList<>();
    protected ArrayList<RectF> leftRects = new ArrayList<>();
    protected ArrayList<RectF> rightRects = new ArrayList<>();
    protected ArrayList<TextPaint> childPaints = new ArrayList<>();

    protected float leftAngles[], rightAngles[], mTran[], childAngles[];
    protected Path path;
    protected boolean isAnimStop = false, doublePointer, isMove, isDrawChildLine;
    protected int leftCount = 7, rightCount = 7, clickCount = -1, lastClickCount = -1, leftChildCount, rightChildCount;
    protected float downX, downY, initX, initY;

    protected ArrayList<PointF> childList = new ArrayList<>();
    protected ArrayList<RectF> childRect = new ArrayList<>();
    protected ArrayList<ArrayList<PointF>> childPoint = new ArrayList<>();
    protected TextPaint leftPaint, rightPaint, cirPaint;
    protected ValueAnimator anim;

    protected float mRaiusCount = 40;
    protected int index = 0, childIndex = 0;
    protected VelocityTracker mVelocityTracker;
    protected float scaleCount = 1;
    protected StaticLayout mStaticLayout;

    protected Bitmap mCirBitmap, mBlueArrow, mBlueLight, mRedArrow, mRedLight;
    protected Matrix mCirMatrix, mRedArrowMatrix, mRedLightMatrix, mBlueArrowMatrix, mBlueLightMatrix, mCirMatrixChild;
    protected PointF leftArrowAngle[], rightArrowAngle[], childArrowAngle[];
    protected ArrayList<StaticLayout> leftStaticLayout = new ArrayList<>();
    protected ArrayList<StaticLayout> rightStaticLayout = new ArrayList<>();

    protected Paint paint;
    protected int maxWidth = 60;
    protected ArrayList<String> alphaList = new ArrayList<String>();
    protected ArrayList<String> startWidthList = new ArrayList<String>();
    protected boolean putChildAnim = false;

    protected int count = 0, lastIndex;
    private CustomDrawThread thread;

    public LineBaseSurfaceView(Context context) {
        super(context);
        initView(context);
    }

    public LineBaseSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LineBaseSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        this.context = context;
        mCirPaint = new Paint();
        mCirPaint.setColor(Color.parseColor("#FF7200"));
        mCirPaint.setStrokeWidth(2);
        mCirPaint.setStyle(Paint.Style.FILL);
        mCirPaint.setAlpha(255);

        mCirAnimPaint = new Paint();
        mCirAnimPaint.setColor(Color.YELLOW);
        mCirAnimPaint.setStrokeWidth(2);
        mCirAnimPaint.setStyle(Paint.Style.STROKE);
        mCirAnimPaint.setAlpha(255);

        mLeftPaint = new Paint();
        mLeftPaint.setColor(Color.RED);
        mLeftPaint.setStrokeWidth(2);
        mLeftPaint.setStyle(Paint.Style.FILL);

        mRightPaint = new Paint();
        mRightPaint.setColor(Color.BLUE);
        mRightPaint.setStrokeWidth(2);
        mRightPaint.setStyle(Paint.Style.FILL);

        path = new Path();
        mCir = new PointF();

        leftAngles = new float[leftCount];
        rightAngles = new float[rightCount];
        mTran = new float[]{0, 0};

        anim = ValueAnimator.ofInt(0, 100);
        anim.setDuration(1000);
        mVelocityTracker = VelocityTracker.obtain();

        leftPaint = new TextPaint();
        leftPaint.setAlpha(255);
        leftPaint.setStyle(Paint.Style.FILL);
        leftPaint.setTextAlign(Paint.Align.RIGHT);
        leftPaint.setColor(Color.WHITE);
        leftPaint.setTextSize(25);

        rightPaint = new TextPaint();
        rightPaint.setAlpha(255);
        rightPaint.setStyle(Paint.Style.FILL);
        rightPaint.setTextAlign(Paint.Align.LEFT);
        rightPaint.setColor(Color.WHITE);
        rightPaint.setTextSize(25);

        cirPaint = new TextPaint();
        cirPaint.setAlpha(255);
        cirPaint.setStyle(Paint.Style.FILL);
        cirPaint.setTextAlign(Paint.Align.CENTER);
        cirPaint.setColor(Color.WHITE);
        cirPaint.setTextSize(25);

        mCirBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.cir);
        mBlueArrow = BitmapFactory.decodeResource(context.getResources(), R.mipmap.blue_arrow);
        mRedArrow = BitmapFactory.decodeResource(context.getResources(), R.mipmap.red_arrow);
        mBlueLight = BitmapFactory.decodeResource(context.getResources(), R.mipmap.blue_light);
        mRedLight = BitmapFactory.decodeResource(context.getResources(), R.mipmap.red_light);

        mCirMatrix = new Matrix();
        mRedArrowMatrix = new Matrix();
        mRedLightMatrix = new Matrix();
        mBlueArrowMatrix = new Matrix();
        mBlueLightMatrix = new Matrix();
        mCirMatrixChild = new Matrix();

        paint = new Paint();
        paint.setColor(Color.RED);//此处颜色可以改为自己喜欢的
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.FILL);
        alphaList.add("255");//圆心的不透明度
        startWidthList.add("0");
        thread = new CustomDrawThread();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!enableTouch)
            return true;
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
                touchUp(event);
                break;
        }
        return true;
    }

    public void translate(MotionEvent event) {
        if (doublePointer) {
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
//                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
    }

    private float minit = 0;

    float lastScale = 1;
    private float x1, x2, x3;
    private PointF pointf = new PointF();

    private void scale(MotionEvent event) {
        doublePointer = true;
        mVelocityTracker.addMovement(event);
        mVelocityTracker.computeCurrentVelocity(2000);
        isMove = true;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_UP:
                lastScale = scaleCount;
                break;
            case MotionEvent.ACTION_MOVE:
                float toScal = getXYLength(event);
                x2 = toScal;
                x3 = x2 / x1;
                float v = mVelocityTracker.getXVelocity();
                if (Math.abs(v) < 5)
                    break;
                scaleCount = toScal / minit - 1 + lastScale;
                int i = 0;
                for (PointF pointF : leftLines) {
                    float x = pointF.x - mCir.x;
                    float y = pointF.y - mCir.y;
                    x = x * x3 + mCir.x;
                    y = y * x3 + mCir.y;
                    leftRects.get(i).set(x - 50, y - 50, x + 50, y + 50);
                    i++;
                }
                i = 0;
                for (PointF pointF : rightLines) {
                    float x = pointF.x - mCir.x;
                    float y = pointF.y - mCir.y;
                    x = x * x3 + mCir.x;
                    y = y * x3 + mCir.y;
                    rightRects.get(i).set(x - 50, y - 50, x + 50, y + 50);
                    i++;
                }

                i = 0;
                for (PointF pointF : childList) {
                    float x = pointF.x - mCir.x;
                    float y = pointF.y - mCir.y;
                    x = x * x3 + mCir.x;
                    y = y * x3 + mCir.y;
                    childRect.get(i).set(x - 50, y - 50, x + 50, y + 50);
                    i++;
                }
//                postInvalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                if (event.getPointerCount() == 2) {
                    minit = getXYLength(event);
                    x1 = minit;
                }
                break;
        }
    }

    public void touchUp(MotionEvent event) {

        if (doublePointer)
            doublePointer = false;
        if (isMove) {
            isMove = false;
            return;
        }
        float x = event.getX() - mTran[0];
        float y = event.getY() - mTran[1];
        int i = 0;
        int clickCount = isInRect(x, y, leftRects);
        if (clickCount >= 0) {
            Toast.makeText(context, leftInfo.get(clickCount), Toast.LENGTH_SHORT).show();
        } else {
            clickCount = isInRect(x, y, rightRects);
            if (clickCount >= 0) {
                Toast.makeText(context, rightInfo.get(clickCount), Toast.LENGTH_SHORT).show();
                clickCount += leftCount;
            }
        }

        if (clickCount >= 0) {
            this.clickCount = clickCount;
            scaleCount = 1;
            lastScale = 1;
            for (PointF pointF : leftLines) {
                leftRects.get(i).set(pointF.x - 30, pointF.y - 50, pointF.x + 80, pointF.y + 50);
                i++;
            }
            i = 0;
            for (PointF pointF : rightLines) {
                rightRects.get(i).set(pointF.x - 30, pointF.y - 50, pointF.x + 80, pointF.y + 50);
                i++;
            }
        }
        if (this.clickCount != -1 && this.clickCount == lastClickCount) {
            int count = isInRect(x, y, childRect);
            if (count >= 0) {
                Toast.makeText(context, "你点的子节点", Toast.LENGTH_SHORT).show();
                i = 0;
                for (PointF pointF : childList) {
                    childRect.get(i).set(pointF.x - 50, pointF.y - 50, pointF.x + 50, pointF.y + 50);
                    i++;
                }
            } else if (clickCount != -1) {
                if (isDrawChildLine)
                    putChildAnim(this.clickCount);
                else
                    clickChildAnim();
            }
            return;
        }
        if (clickCount != -1 && clickCount != lastClickCount && lastClickCount != -1) {
            if (isDrawChildLine)
                putChildAnim(lastClickCount);
            else
                clickChildAnim();
            return;
        }
        if (clickCount != -1) {
            if (potListener != null)
                potListener.onClickPot();
            clickChildAnim();
        }
        lastClickCount = clickCount;
    }

    public float getXYLength(MotionEvent event) {

        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    protected int isInRect(float x, float y, ArrayList<RectF> list) {

        int count = 0;
        for (RectF rectF : list) {
            if (rectF.contains(x, y))
                return count;
            count++;
        }
        return -1;
    }

    public void refresh(DataModel dataModel) {

        if (dataModel == null) {
            //测试
            leftCount = 7;
            rightCount = 7;
        } else {
            leftCount = dataModel.getItemOneList().size();
            rightCount = dataModel.getItemTowList().size();
        }

        leftArrowAngle = new PointF[leftCount];
        rightArrowAngle = new PointF[rightCount];
        mStaticLayout = new StaticLayout("这是个中心点", cirPaint, 75, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        if (leftCount == 0) {
            //TODO:投资人为零
        }
        if (rightCount == 0) {
            //TODO:投资公司为零
        }

        float leftAngle = 90.0f / (leftCount - 1);
        float rightAngle = 90.0f / (rightCount - 1);
        leftLines.clear();
        leftPaints.clear();
        rightLines.clear();
        rightPaints.clear();
        leftRects.clear();
        rightRects.clear();
        leftInfo.clear();
        rightInfo.clear();

        for (int i = 0; i < leftCount; i++) {
            PointF pointf = new PointF();
            leftAngles[i] = leftAngle * i + 135;
            pointf.x = MathHelper.getInstance().getPointF(leftAngle * i + 135, mCir.x, mCir.y, mRaius).x;
            pointf.y = MathHelper.getInstance().getPointF(leftAngle * i + 135, mCir.x, mCir.y, mRaius).y;
            leftLines.add(pointf);

            PointF arrow = new PointF();
            arrow.x = MathHelper.getInstance().getPointF(leftAngle * i + 135, mCir.x, mCir.y, mRaius * 0.3f).x;
            arrow.y = MathHelper.getInstance().getPointF(leftAngle * i + 135, mCir.x, mCir.y, mRaius * 0.3f).y;
            leftArrowAngle[i] = arrow;

            RectF rect = new RectF(pointf.x - 50, pointf.y - 50, pointf.x + 50, pointf.y + 50);
            leftRects.add(rect);

            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#FF2F2F"));
            paint.setStrokeWidth(2);
            paint.setAlpha(255);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setTextSize(20);
            paint.setTextAlign(Paint.Align.RIGHT);
            leftPaints.add(paint);

            leftInfo.add("左边第" + i + "个点");
            StaticLayout staticLayout = new StaticLayout(leftInfo.get(i), cirPaint, 75, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            leftStaticLayout.add(staticLayout);
        }

        for (int i = 0; i < rightCount; i++) {
            PointF pointf = new PointF();
            rightAngles[i] = rightAngle * i + 315 >= 360 ? (rightAngle * i + 315 - 360) : rightAngle * i + 315;
            pointf.x = MathHelper.getInstance().getPointF(rightAngle * i + 315, mCir.x, mCir.y, mRaius).x;
            pointf.y = MathHelper.getInstance().getPointF(rightAngle * i + 315, mCir.x, mCir.y, mRaius).y;
            rightLines.add(pointf);

            PointF arrow = new PointF();
            arrow.x = MathHelper.getInstance().getPointF(rightAngle * i + 315, mCir.x, mCir.y, mRaius * 0.8f).x;
            arrow.y = MathHelper.getInstance().getPointF(rightAngle * i + 315, mCir.x, mCir.y, mRaius * 0.8f).y;
            rightArrowAngle[i] = arrow;

            RectF rect = new RectF(pointf.x - 50, pointf.y - 50, pointf.x + 50, pointf.y + 50);
            rightRects.add(rect);

            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#01B9FF"));
            paint.setStrokeWidth(2);
            paint.setAlpha(255);
            paint.setAntiAlias(true);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setTextSize(20);
            rightPaints.add(paint);

            rightInfo.add("右边第" + i + "个点");

            StaticLayout staticLayout = new StaticLayout(rightInfo.get(i), cirPaint, 75, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            rightStaticLayout.add(staticLayout);
        }

        startAnim();
    }

    public void startAnim() {
        leftAnims.clear();
        leftPoints.clear();
        rightAnims.clear();
        rightPoints.clear();
        isAnimStop = false;
        enableTouch = false;
        for (int i = 0; i < leftCount; i++) {
            ArrayList<PointF> list = new ArrayList<>();
            ValueAnimator valu = getAnim(mCir, leftLines.get(i), list);
            leftAnims.add(valu);
            leftPoints.add(list);
            valu.start();
        }

        for (int i = 0; i < rightCount; i++) {
            ArrayList<PointF> list = new ArrayList<>();
            ValueAnimator valu = getAnim(mCir, rightLines.get(i), list);
            rightAnims.add(valu);
            rightPoints.add(list);
            valu.start();
        }
    }

    public ValueAnimator getAnim(PointF startPoint, PointF endPoint, final ArrayList<PointF> list) {
        PointF[] linePoints = new PointF[2];
        linePoints[0] = startPoint;
        linePoints[1] = endPoint;
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new BezierOneTypeEvaluator(linePoints), new PointF());
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (1000 <= animation.getCurrentPlayTime()) {
                    if (isDrawChildLine)
                        childAnimStop = true;
                    else
                        isAnimStop = true;
                }
                PointF p = (PointF) animation.getAnimatedValue();
                PointF p1 = new PointF(p.x, p.y);
                if (!list.contains(p1))
                    list.add(p1);
//                postInvalidate();
            }
        });
        return valueAnimator;
    }

    //加model变成子动画
    public void clickChildAnim() {
        childRect.clear();
        childList.clear();
        childPaints.clear();
        leftChildCount = 1;
        rightChildCount = 1;
        childAngles = new float[leftChildCount + rightChildCount];
        childArrowAngle = new PointF[leftChildCount + rightChildCount];
        enableTouch = false;
        count = 0;

        lastIndex = 0;
        childAngles = new float[2];
        float angle = 90 / (leftChildCount + rightChildCount - 1);
        if (clickCount < leftCount) {
            for (int i = 0; i < leftChildCount + rightChildCount; i++) {
                childAngles[leftChildCount + rightChildCount - i - 1] = 135 + angle * i;
                TextPaint paint = new TextPaint();
                paint.setStyle(Paint.Style.FILL);
                paint.setTextAlign(Paint.Align.RIGHT);
                paint.setColor(Color.WHITE);
                paint.setTextSize(25);
                childPaints.add(paint);

            }

        } else {
            for (int i = 0; i < leftChildCount + rightChildCount; i++) {
                childAngles[leftChildCount + rightChildCount - i - 1]
                        = 315 + angle * i >= 360 ? 315 + angle * i - 360 : 315 + angle * i;
                TextPaint paint = new TextPaint();
                paint.setStyle(Paint.Style.FILL);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.WHITE);
                paint.setTextSize(25);
                childPaints.add(paint);
            }
        }


        anim.removeAllUpdateListeners();
        anim.setDuration(1000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int index = (Integer) animation.getAnimatedValue();
                if (index >= 100) {
                    childAnimStop = true;
                    isDrawChildLine = true;
                    lastIndex = 0;
                    if (clickCount < leftCount) {
                        leftRects.get(clickCount).set(leftLines.get(clickCount).x - 50, leftLines.get(clickCount).y - 50
                                , leftLines.get(clickCount).x + 50, leftLines.get(clickCount).y + 50);
                        for (int i = 0; i < leftChildCount + rightChildCount; i++) {
                            PointF pointf = new PointF();
                            pointf.x = MathHelper.getInstance().getPointF(childAngles[i]
                                    , leftLines.get(clickCount).x, leftLines.get(clickCount).y, mRaius * 0.8f).x;
                            pointf.y = MathHelper.getInstance().getPointF(childAngles[i]
                                    , leftLines.get(clickCount).x, leftLines.get(clickCount).y, mRaius * 0.8f).y;
                            if (!childList.contains(pointf))
                                childList.add(pointf);

                            RectF rectf = new RectF(pointf.x - 100, pointf.y - 100, pointf.x + 100, pointf.y + 100);
                            if (!childRect.contains(rectf)) {
                                childRect.add(rectf);
                            }

                            PointF point = new PointF();
                            if (i < leftChildCount) {
                                point.x = MathHelper.getInstance().getPointF(childAngles[i]
                                        , leftLines.get(clickCount).x, leftLines.get(clickCount).y, mRaius * 0.4f).x;
                                point.y = MathHelper.getInstance().getPointF(childAngles[i]
                                        , leftLines.get(clickCount).x, leftLines.get(clickCount).y, mRaius * 0.4f).y;
                            } else {
                                point.x = MathHelper.getInstance().getPointF(childAngles[i]
                                        , leftLines.get(clickCount).x, leftLines.get(clickCount).y, mRaius * 0.4f).x;
                                point.y = MathHelper.getInstance().getPointF(childAngles[i]
                                        , leftLines.get(clickCount).x, leftLines.get(clickCount).y, mRaius * 0.4f).y;
                            }
                            childArrowAngle[i] = point;
                        }
                    } else {
                        rightRects.get(clickCount - leftCount).set(rightLines.get(clickCount - leftCount).x - 50, rightLines.get(clickCount - leftCount).y - 50
                                , rightLines.get(clickCount - leftCount).x + 50, rightLines.get(clickCount - leftCount).y + 50);
                        for (int i = 0; i < leftChildCount + rightChildCount; i++) {
                            PointF pointf = new PointF();
                            pointf.x = MathHelper.getInstance().getPointF(childAngles[i]
                                    , rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, mRaius * 0.8f).x;
                            pointf.y = MathHelper.getInstance().getPointF(childAngles[i]
                                    , rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, mRaius * 0.8f).y;
                            if (!childList.contains(pointf))
                                childList.add(pointf);

                            RectF rectf = new RectF(pointf.x - 50, pointf.y - 50, pointf.x + 50, pointf.y + 50);
                            if (!childRect.contains(rectf)) {
                                childRect.add(rectf);
                            }

                            PointF point = new PointF();
                            if (i < leftChildCount) {
                                point.x = MathHelper.getInstance().getPointF(childAngles[i]
                                        , rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, mRaius * 0.4f).x;
                                point.y = MathHelper.getInstance().getPointF(childAngles[i]
                                        , rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, mRaius * 0.4f).y;
                            } else {
                                point.x = MathHelper.getInstance().getPointF(childAngles[i]
                                        , rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, mRaius * 0.6f).x;
                                point.y = MathHelper.getInstance().getPointF(childAngles[i]
                                        , rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, mRaius * 0.6f).y;
                            }
                            childArrowAngle[i] = point;
                        }
                    }
                    childAnim();
                } else {

                    if (clickCount < leftCount && clickCount >= 0) {
                        //根据角度，算出偏移值，根据sin,cos值正负会自己获取是增是减
                        float argAngle = (float) (Math.PI * leftAngles[clickCount] / 180f);
                        leftLines.get(clickCount).x += Math.cos(argAngle) * (index - lastIndex) * mRaius * 0.02f;
                        leftLines.get(clickCount).y += Math.sin(argAngle) * (index - lastIndex) * mRaius * 0.02f;
                        mTran[0] = (float) (-(mCir.x + mRaius) * index * Math.cos(argAngle) / 100);
                        mTran[1] = (float) (-(mCir.y + mRaius * 2) * index * Math.sin(argAngle) / 100);
                    } else if (clickCount >= 0) {
                        float argAngle = (float) (Math.PI * rightAngles[clickCount - leftCount] / 180f);
                        rightLines.get(clickCount - leftCount).x += Math.cos(argAngle) * (index - lastIndex) * mRaius * 0.02f;
                        rightLines.get(clickCount - leftCount).y += Math.sin(argAngle) * (index - lastIndex) * mRaius * 0.02f;
                        mTran[0] = (float) (-(mCir.x + mRaius) * index * Math.cos(argAngle) / 100);
                        mTran[1] = (float) (-(mCir.y + mRaius * 2) * index * Math.sin(argAngle) / 100);
                    }
                    count++;
                    lastIndex = index;
//                    postInvalidate();
                }
            }
        });
        anim.start();
    }

    private void childAnim() {
        if (!childAnimStop)
            return;
        childAnimStop = false;
        childPoint.clear();
        for (int i = 0; i < childList.size(); i++) {
            PointF point = childList.get(i);
            ArrayList<PointF> list = new ArrayList<>();
            ValueAnimator valu = null;
            if (clickCount < leftCount)
                valu = getAnim(leftLines.get(clickCount), point, list);
            else
                valu = getAnim(rightLines.get(clickCount - leftCount), point, list);
            valu.setDuration(1000);
            if (!childPoint.contains(list))
                childPoint.add(list);
            valu.start();
        }
    }


    private void putChildAnim(final int click) {
        putChildAnim = true;
        childRect.clear();
        childList.clear();
        enableTouch = false;
        lastIndex = 0;
        anim.removeAllUpdateListeners();
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int index = (Integer) animation.getAnimatedValue();
                if (index >= 100) {
                    if (click < leftCount) {
                        leftRects.get(click).set(leftLines.get(click).x - 50, leftLines.get(click).y - 50
                                , leftLines.get(click).x + 50, leftLines.get(click).y + 50);
                    } else {
                        rightRects.get(click - leftCount).set(rightLines.get(click - leftCount).x - 50, rightLines.get(click - leftCount).y - 50
                                , rightLines.get(click - leftCount).x + 50, rightLines.get(click - leftCount).y + 50);
                    }
                    putChildAnim = false;
                    if (clickCount != click && clickCount != -1 && click != -1)
                        clickChildAnim();
                    lastIndex = 0;
                    return;
                }
                if (click < leftCount && click >= 0) {
                    //根据角度，算出偏移值，根据sin,cos值正负会自己获取是增是减
                    float argAngle = (float) (Math.PI * leftAngles[click] / 180f);
                    mTran[0] = (float) -((mCir.x + mRaius) * (100 - index) * Math.cos(argAngle) / 100);
                    mTran[1] = (float) -((mCir.y + mRaius) * (100 - index) * Math.sin(argAngle) / 100);
                    leftLines.get(click).x -= Math.cos(argAngle) * (index - lastIndex) * mRaius * 0.02f;
                    leftLines.get(click).y -= Math.sin(argAngle) * (index - lastIndex) * mRaius * 0.02f;
//                    ArrayList<PointF> list=leftPoints.get(click);
//                    list.add(new PointF(leftLines.get(click).x,leftLines.get(click).y));
                } else if (click >= 0) {
                    float argAngle = (float) (Math.PI * rightAngles[click - leftCount] / 180f);
                    mTran[0] = (float) -((mCir.x + mRaius) * (100 - index) * Math.cos(argAngle) / 100);
                    mTran[1] = (float) -((mCir.y + mRaius) * (100 - index) * Math.sin(argAngle) / 100);
                    rightLines.get(click - leftCount).x -= Math.cos(argAngle) * (index - lastIndex) * mRaius * 0.02f;
                    rightLines.get(click - leftCount).y -= Math.sin(argAngle) * (index - lastIndex) * mRaius * 0.02f;
//                    ArrayList<PointF> list=rightPoints.get(click - leftCount);
//                    list.add(new PointF(rightLines.get(click - leftCount).x,rightLines.get(click - leftCount).y));
                }
                lastIndex = index;
            }
        });
//        postInvalidate();
    }

    public static interface OnClickPotListener {
        public void onClickPot();
    }

    private OnClickPotListener potListener;

    public void setClickPot(OnClickPotListener listener) {
        potListener = listener;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("surface", "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("surface", "surfaceChanged");
        this.width = width;
        this.height = height;
        mCir.x = width / 2;
        mCir.y = height / 2;
        mRaius = Math.min(width, height) / 2 * 0.9f;
        Log.e("thread", thread.getState() + "");
        maxWidth = (int) (mRaius * 0.4f);
        if (thread != null && thread.getState() == Thread.State.NEW) {
            refresh(null);
            thread.start();
        } else {
            thread.onThreadResume();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("surface", "surfaceDestroyed");
        thread.setThreadIsPause(true);
    }

    class CustomDrawThread extends Thread {
        private boolean threadIsClose=false, threadIsPause=false;
        private Canvas canvas=null;
        public void onThreadResume() {
            synchronized (this) {
                threadIsPause = false;
                notify();
            }
        }

        public void setThreadIsClose(boolean threadIsClose) {
            this.threadIsClose = threadIsClose;
        }

        public void setThreadIsPause(boolean threadIsPause) {
            this.threadIsPause = threadIsPause;
        }

        @Override
        public void run() {
            super.run();
            while (!threadIsClose) {
                if (!threadIsPause) {
                    try {
                        if (mRaiusCount >= 60) {
                            mRaiusCount = 30;
                        } else
                            mRaiusCount++;
                        index++;
                        childIndex++;
                        synchronized (surfaceHolder) {
                            canvas = surfaceHolder.lockCanvas();
                            onDraw(canvas);
                        }
                        Thread.sleep(30);
                    } catch (Exception e) {

                    } finally {
                        if (canvas != null) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                } else {
                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void onDraw(Canvas canvas) {
            canvas.save();
            canvas.drawColor(Color.parseColor("#1B1E29"));
            //绘制主图
            canvas.translate(mTran[0], mTran[1]);
            canvas.scale(scaleCount, scaleCount, mCir.x, mCir.y);
            for (int i = 0; i < leftCount; i++) {
                path.reset();
                path.moveTo(mCir.x, mCir.y);
                ArrayList<PointF> list = leftPoints.get(i);
                if (isAnimStop) {
                    path.lineTo(leftLines.get(i).x, leftLines.get(i).y);
                    canvas.drawPath(path, leftPaints.get(i));
                    float arc = leftAngles[i] / 180f;
                    if (clickCount != i) {
                        canvas.save();
                        canvas.rotate(leftAngles[i] + 180, (leftLines.get(i).x), (leftLines.get(i).y + 12));
                        canvas.drawText(leftInfo.get(i), (leftLines.get(i).x - 80), (leftLines.get(i).y + 12), leftPaint);
                        canvas.restore();
                    }
                    if (!isDrawChildLine && clickCount == i) {
                        canvas.save();
                        canvas.rotate(leftAngles[i] + 180, (leftLines.get(i).x), (leftLines.get(i).y + 12));
                        canvas.drawText(leftInfo.get(i), (leftLines.get(i).x - 80), (leftLines.get(i).y + 12), leftPaint);
                        canvas.restore();
                    }

                    if (mRedArrow != null && !mRedArrow.isRecycled()) {
                        mRedArrowMatrix.setRotate(leftAngles[i] + 180, mRedArrow.getWidth() / 2, mRedArrow.getHeight() / 2);
                        mRedArrowMatrix.postTranslate((leftArrowAngle[i].x - mRedArrow.getWidth() / 2)
                                , (leftArrowAngle[i].y - mRedArrow.getHeight() / 2));
                        canvas.drawBitmap(mRedArrow, mRedArrowMatrix, null);
                    }


                    enableTouch = true;
                    int j = index / 2;
                    if (j < list.size()) {
                        if (mRedLight != null && !mRedLight.isRecycled()) {
                            mRedLightMatrix.setRotate(leftAngles[i] + 180, mRedLight.getWidth() / 2, mRedLight.getHeight() / 2);
                            mRedLightMatrix.postTranslate(list.get(list.size() - j - 1).x - mRedLight.getWidth() / 2
                                    , list.get(list.size() - j - 1).y - mRedLight.getHeight() / 2);
                            canvas.drawBitmap(mRedLight, mRedLightMatrix, null);
                        }
                    } else
                        index = 0;
                    canvas.drawCircle(leftLines.get(i).x, leftLines.get(i).y, 30, leftPaints.get(i));
                } else {
                    for (int j = 0; j < list.size(); j++) {
                        path.lineTo(list.get(j).x, list.get(j).y);
                        canvas.drawPath(path, leftPaints.get(i));
                        if (j == list.size() - 1) {
                            canvas.drawCircle(list.get(j).x, list.get(j).y, 30, leftPaints.get(i));
                        }
                    }
                }
                path.close();
            }

            for (int i = 0; i < rightCount; i++) {
                path.reset();
                path.moveTo(mCir.x, mCir.y);
                ArrayList<PointF> list = rightPoints.get(i);
                if (isAnimStop) {
                    path.lineTo(rightLines.get(i).x, rightLines.get(i).y);
                    canvas.drawPath(path, rightPaints.get(i));
                    if (clickCount - leftCount != i) {
                        canvas.save();
                        canvas.rotate(rightAngles[i], rightLines.get(i).x, rightLines.get(i).y + 12);
                        canvas.drawText(rightInfo.get(i), rightLines.get(i).x + 80, rightLines.get(i).y + 12, rightPaint);
                        canvas.restore();
                    }

                    if (!isDrawChildLine && clickCount - leftCount == i) {
                        canvas.save();
                        canvas.rotate(rightAngles[i], rightLines.get(i).x, rightLines.get(i).y + 12);
                        canvas.drawText(rightInfo.get(i), rightLines.get(i).x + 80, rightLines.get(i).y + 12, rightPaint);
                        canvas.restore();
                    }

                    if (mBlueArrow != null && !mBlueArrow.isRecycled()) {
                        mBlueArrowMatrix.setRotate(rightAngles[i], mBlueArrow.getWidth() / 2, mBlueArrow.getHeight() / 2);
                        mBlueArrowMatrix.postTranslate((rightArrowAngle[i].x - mBlueArrow.getWidth() / 2)
                                , (rightArrowAngle[i].y - mBlueArrow.getHeight() / 2));
                        canvas.drawBitmap(mBlueArrow, mBlueArrowMatrix, null);
                    }
                    int j = index / 2;
                    if (j < list.size()) {
                        if (mBlueLight != null && !mBlueArrow.isRecycled()) {
                            mBlueLightMatrix.setRotate(rightAngles[i], mBlueLight.getWidth() / 2, mBlueLight.getHeight() / 2);
                            mBlueLightMatrix.postTranslate(list.get(j).x - mBlueLight.getWidth() / 2
                                    , list.get(j).y - mBlueLight.getHeight() / 2);
                            canvas.drawBitmap(mBlueLight, mBlueLightMatrix, null);
                        }
                    } else
                        index = 0;
                    canvas.drawCircle(rightLines.get(i).x, rightLines.get(i).y, 30, rightPaints.get(i));
                } else {
                    for (int j = 0; j < list.size(); j++) {
                        path.lineTo(list.get(j).x, list.get(j).y);
                        canvas.drawPath(path, rightPaints.get(i));
                        if (j == list.size() - 1) {
                            canvas.drawCircle(list.get(j).x, list.get(j).y, 30, rightPaints.get(i));
                        }
                    }
                }
                path.close();
            }

            if (mCirBitmap != null && !mCirBitmap.isRecycled()) {
                mCirMatrix.setScale(mRaiusCount / 50f, mRaiusCount / 50f, mCirBitmap.getWidth() / 2, mCirBitmap.getHeight() / 2);
                mCirMatrix.postTranslate(mCir.x - mCirBitmap.getWidth() / 2, mCir.y - mCirBitmap.getHeight() / 2);
                canvas.drawBitmap(mCirBitmap, mCirMatrix, null);
            }

            canvas.drawCircle(mCir.x, mCir.y, 50, mCirPaint);
            canvas.save();
            canvas.translate(mCir.x, mCir.y - 30);
            mStaticLayout.draw(canvas);
            canvas.restore();

            //绘制子节点
            if (isDrawChildLine) {
                if (childAnimStop) {
                    if (putChildAnim) {
                        for (int i = 0; i < childPoint.size(); i++) {
                            ArrayList<PointF> list = childPoint.get(i);
                            if (list.size() < 1) {
                                putChildAnim = false;
                                isDrawChildLine = false;
                                anim.start();
                            }
                            path.reset();
                            if (lastClickCount < leftCount)
                                path.moveTo(leftLines.get(lastClickCount).x, leftLines.get(lastClickCount).y);
                            else
                                path.moveTo(rightLines.get(lastClickCount - leftCount).x, rightLines.get(lastClickCount - leftCount).y);
                            for (int j = 0; j < list.size(); j++) {
                                path.lineTo(list.get(j).x, list.get(j).y);
                                if (j == list.size() - 1) {
                                    if (i < leftChildCount)
                                        canvas.drawCircle(list.get(j).x, list.get(j).y, 30, leftPaints.get(i));
                                    else
                                        canvas.drawCircle(list.get(j).x, list.get(j).y, 30, rightPaints.get(i));
                                    list.remove(j);
                                    if (j - 1 >= 0)
                                        list.remove(j - 1);
                                    if (j - 2 >= 0)
                                        list.remove(j - 2);
                                }
                            }
                            if (i < leftChildCount)
                                canvas.drawPath(path, leftPaints.get(i));
                            else {
                                canvas.drawPath(path, rightPaints.get(i));
                            }
                        }
                    } else {
                        enableTouch = true;
                        for (int i = 0; i < leftChildCount + rightChildCount; i++) {
                            ArrayList<PointF> list = childPoint.get(i);
                            int j = childIndex;
                            path.reset();
                            if (clickCount < leftCount) {
                                path.moveTo(leftLines.get(clickCount).x, leftLines.get(clickCount).y);
                                path.lineTo(childList.get(i).x, childList.get(i).y);
                                canvas.save();
                                canvas.rotate(childAngles[i] + 180, childList.get(i).x, childList.get(i).y);
                                canvas.drawText("第" + i + "个点", childList.get(i).x - 80, childList.get(i).y, childPaints.get(i));
                                canvas.restore();

                            } else {
                                path.moveTo(rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y);
                                path.lineTo(childList.get(i).x, childList.get(i).y);
                                canvas.save();
                                canvas.rotate(childAngles[i], childList.get(i).x, childList.get(i).y);
                                canvas.drawText("第" + i + "个点", childList.get(i).x + 80, childList.get(i).y, childPaints.get(i));
                                canvas.restore();
                            }
                            if (i < leftChildCount) {
                                canvas.drawPath(path, leftPaints.get(i));

                                if (mRedArrow != null && !mRedArrow.isRecycled()) {
                                    mRedArrowMatrix.setRotate(childAngles[i] + 180, mRedArrow.getWidth() / 2, mRedArrow.getHeight() / 2);
                                    mRedArrowMatrix.postTranslate((childArrowAngle[i].x - mRedArrow.getWidth() / 2)
                                            , (childArrowAngle[i].y - mRedArrow.getHeight() / 2));
                                    canvas.drawBitmap(mRedArrow, mRedArrowMatrix, null);
                                }
                                if (j < list.size()) {
                                    if (mRedLight != null && !mRedLight.isRecycled()) {
                                        mRedLightMatrix.setRotate(childAngles[i] + 180, mRedLight.getWidth() / 2, mRedLight.getHeight() / 2);
                                        mRedLightMatrix.postTranslate(list.get(list.size() - j - 1).x - mRedLight.getWidth() / 2
                                                , list.get(list.size() - j - 1).y - mRedLight.getHeight() / 2);
                                        canvas.drawBitmap(mRedLight, mRedLightMatrix, null);
                                    }
                                } else {
                                    childIndex = 0;
                                }
                                canvas.drawCircle(childList.get(i).x, childList.get(i).y, 30, leftPaints.get(i));
                            } else {
                                canvas.drawPath(path, rightPaints.get(i));
                                if (mBlueArrow != null && !mBlueArrow.isRecycled()) {
                                    mBlueArrowMatrix.setRotate(childAngles[i], mBlueArrow.getWidth() / 2, mBlueArrow.getHeight() / 2);
                                    mBlueArrowMatrix.postTranslate((childArrowAngle[i].x - mBlueArrow.getWidth() / 2)
                                            , (childArrowAngle[i].y - mBlueArrow.getHeight() / 2));
                                    canvas.drawBitmap(mBlueArrow, mBlueArrowMatrix, null);
                                }
                                if (j < list.size()) {
                                    if (mBlueLight != null && !mBlueArrow.isRecycled()) {
                                        mBlueLightMatrix.setRotate(childAngles[i], mBlueLight.getWidth() / 2, mBlueLight.getHeight() / 2);
                                        mBlueLightMatrix.postTranslate(list.get(j).x - mBlueLight.getWidth() / 2
                                                , list.get(j).y - mBlueLight.getHeight() / 2);
                                        canvas.drawBitmap(mBlueLight, mBlueLightMatrix, null);
                                    }
                                } else
                                    childIndex = 0;
                                canvas.drawCircle(childList.get(i).x, childList.get(i).y, 30, rightPaints.get(i));
                            }
                            path.close();

                        }
                        for (int i = 0; i < leftChildCount + rightChildCount; i++) {
                            if (clickCount < leftCount) {
                                canvas.save();
                                canvas.translate(leftLines.get(clickCount).x, leftLines.get(clickCount).y - 30);
                                leftStaticLayout.get(clickCount).draw(canvas);
                                canvas.restore();

                            } else {
                                canvas.save();
                                canvas.translate(rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y - 30);
                                rightStaticLayout.get(clickCount - leftCount).draw(canvas);
                                canvas.restore();
                            }
                        }

                        if (clickCount < leftCount) {
                            if (mCirBitmap != null && !mCirBitmap.isRecycled()) {
                                canvas.drawCircle(leftLines.get(clickCount).x, leftLines.get(clickCount).y, 50, mCirPaint);
                                mCirMatrixChild.setScale(mRaiusCount / 50f, mRaiusCount / 50f, mCirBitmap.getWidth() / 2, mCirBitmap.getHeight() / 2);
                                mCirMatrixChild.postTranslate(leftLines.get(clickCount).x - mCirBitmap.getWidth() / 2
                                        , leftLines.get(clickCount).y - mCirBitmap.getHeight() / 2);
                                canvas.drawBitmap(mCirBitmap, mCirMatrixChild, null);
                            }
                        } else {
                            canvas.drawCircle(rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, 50, mCirPaint);
                            if (mCirBitmap != null && !mCirBitmap.isRecycled()) {
                                mCirMatrixChild.setScale(mRaiusCount / 50f, mRaiusCount / 50f, mCirBitmap.getWidth() / 2, mCirBitmap.getHeight() / 2);
                                mCirMatrixChild.postTranslate(rightLines.get(clickCount - leftCount).x - mCirBitmap.getWidth() / 2
                                        , rightLines.get(clickCount - leftCount).y - mCirBitmap.getHeight() / 2);
                                canvas.drawBitmap(mCirBitmap, mCirMatrixChild, null);
                            }
                        }
                    }
                } else {
                    enableTouch = false;
                    for (int i = 0; i < childPoint.size(); i++) {
                        ArrayList<PointF> list = childPoint.get(i);
                        for (int j = 0; j < list.size(); j++) {
                            path.reset();
                            if (clickCount < leftCount) {
                                path.moveTo(leftLines.get(clickCount).x, leftLines.get(clickCount).y);
                                path.lineTo(list.get(j).x, list.get(j).y);
                                if (i < leftChildCount) {
                                    canvas.drawPath(path, leftPaints.get(i));
                                    if (j == list.size() - 1) {
                                        canvas.drawCircle(list.get(j).x, list.get(j).y, 30, leftPaints.get(i));
                                    }
                                } else {
                                    canvas.drawPath(path, rightPaints.get(i));
                                    if (j == list.size() - 1) {
                                        canvas.drawCircle(list.get(j).x, list.get(j).y, 30, rightPaints.get(i));
                                    }
                                }

                            } else {
                                path.moveTo(rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y);
                                path.lineTo(list.get(j).x, list.get(j).y);
                                if (i < leftChildCount) {
                                    canvas.drawPath(path, leftPaints.get(i));
                                    if (j == list.size() - 1) {
                                        canvas.drawCircle(list.get(j).x, list.get(j).y, 30, leftPaints.get(i));
                                    }
                                } else {
                                    canvas.drawPath(path, rightPaints.get(i));
                                    if (j == list.size() - 1) {
                                        canvas.drawCircle(list.get(j).x, list.get(j).y, 30, rightPaints.get(i));
                                    }
                                }

                            }
                            path.close();
                        }
                    }
                }
            }

            canvas.restore();
        }
    }
}
