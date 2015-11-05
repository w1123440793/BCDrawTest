package tencent.jusfoun.cn.bcdrawtest;

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
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Author  JUSFOUN
 * CreateDate 2015/10/27.
 * Description
 */
public class LineBaseView extends View {

    protected Context context;
    protected PointF mCir;
    protected float mRaius;
    protected Paint mCirPaint, mLeftPaint, mRightPaint, mCirAnimPaint;

    protected boolean enableTouch, childAnimStop, threadStop;
    protected Thread thread;

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


    public LineBaseView(Context context) {
        super(context);
        initView(context);
    }

    public LineBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LineBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        this.context = context;
        mCirPaint = new Paint();
        mCirPaint.setColor(Color.parseColor("#FF7200"));
        mCirPaint.setStrokeWidth(2);
        mCirPaint.setStyle(Paint.Style.FILL);

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

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mTran[0], mTran[1]);
        canvas.scale(scaleCount, scaleCount, mCir.x, mCir.y);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRaius = Math.min(w, h) / 2 * 0.9f;
        mCir.x = w / 2;
        mCir.y = h / 2;

        maxWidth = (int) (mRaius * 0.4f);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        threadStop = true;
        stopThread();
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }

        if (mCirBitmap != null) {
            mCirBitmap.recycle();
            mCirBitmap = null;
        }

        if (mRedArrow != null) {
            mRedArrow.recycle();
            mRedArrow = null;
        }

        if (mRedLight != null) {
            mRedLight.recycle();
            mRedLight = null;
        }

        if (mBlueArrow != null) {
            mBlueArrow.recycle();
            mBlueArrow = null;
        }

        if (mBlueLight != null) {
            mBlueLight.recycle();
            mBlueLight = null;
        }
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
                postInvalidate();
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
                postInvalidate();
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
    }

    private void stopThread() {
        if (thread != null && thread.isInterrupted()) {
            thread.interrupt();
            thread = null;
            System.gc();
        }
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
}
