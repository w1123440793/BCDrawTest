package tencent.jusfoun.cn.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

import java.util.ArrayList;
import java.util.List;

import tencent.jusfoun.cn.bcdrawtest.MathHelper;
import tencent.jusfoun.cn.bcdrawtest.R;
import tencent.jusfoun.cn.model.DragDotModel;

/**
 * Author  wangchenchen
 * CreateDate 2015/12/21.
 * Email wcc@jusfoun.com
 * Description
 */
public class DragDotSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Context context;
    private SurfaceHolder surfaceHolder;
    private Paint cirPaint;
    private List<DragDotModel> dots = new ArrayList<>();
    private Point mTranslate, firstDown, lastDown;
    private Point[] dotLocation = new Point[120];
    private int clickCount;
    private Custom custom;
    private boolean isMove = false;
    private VelocityTracker velocityTracker;
    private boolean dotMove = false, isDoublePoint;
    private float scale = 1, initScale = 1, lastScale;
    private Point cir;
    private Runnable mLongPressRunnable;
    private boolean isDotLongPress = false, dotLongPressChange;
    private Paint normalPaint, clickPaint, longClickPaint, closePaint, emptyPaint;
    private TextPaint textPaint;

    private Bitmap mBlueArrow;
    private Matrix mBlueArrowMatrix;
    private List<Point> downPoints = new ArrayList<>();
    private int width, height;
    private boolean isPointMove = false;

    private int mCir = 50;

    public DragDotSurfaceView(Context context) {
        super(context);
        initView(context);
    }

    public DragDotSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DragDotSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        clickCount=-1;

        cirPaint = new Paint();
        cirPaint.setColor(Color.YELLOW);
        cirPaint.setStrokeWidth(3);
        cirPaint.setAntiAlias(true);
        cirPaint.setStyle(Paint.Style.FILL);
        cirPaint.setAlpha(255);

        mTranslate = new Point(0, 0);
        custom = new Custom();
        firstDown = new Point();
        lastDown = new Point();
        velocityTracker = VelocityTracker.obtain();
        for (int i = 0; i < 120; i++) {
            DragDotModel model = new DragDotModel();
            model.setName("第" + (i + 1) + "个");
            model.setIsShow(true);
            dots.add(model);
        }

        cir = new Point();
        mLongPressRunnable = new Runnable() {
            @Override
            public void run() {
                if (clickCount != -1)
                    pressLongClick();
            }
        };

        normalPaint = new Paint();
        normalPaint.setColor(Color.YELLOW);
        normalPaint.setStrokeWidth(3);
        normalPaint.setAntiAlias(true);
        normalPaint.setStyle(Paint.Style.FILL);
        normalPaint.setAlpha(255);

        clickPaint = new Paint();
        clickPaint.setColor(Color.RED);
        clickPaint.setStrokeWidth(3);
        clickPaint.setAntiAlias(true);
        clickPaint.setStyle(Paint.Style.FILL);
        clickPaint.setAlpha(255);

        emptyPaint = new Paint();
        emptyPaint.setColor(Color.RED);
        emptyPaint.setStrokeWidth(3);
        emptyPaint.setFilterBitmap(true);
        emptyPaint.setAntiAlias(true);
        emptyPaint.setStyle(Paint.Style.STROKE);
        emptyPaint.setAlpha(255);

        longClickPaint = new Paint();
        longClickPaint.setColor(Color.BLUE);
        longClickPaint.setStrokeWidth(3);
        longClickPaint.setAntiAlias(true);
        longClickPaint.setStyle(Paint.Style.FILL);
        longClickPaint.setAlpha(255);

        closePaint = new Paint();
        closePaint.setColor(Color.CYAN);
        closePaint.setStrokeWidth(3);
        closePaint.setAntiAlias(true);
        closePaint.setStyle(Paint.Style.FILL);
        closePaint.setAlpha(255);

        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setStrokeWidth(3);
        textPaint.setTextSize(20);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAlpha(255);

        mBlueArrow = BitmapFactory.decodeResource(context.getResources(), R.mipmap.blue_arrow);
        mBlueArrowMatrix = new Matrix();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("surface", "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("surface", "surfaceChanged");
        cir.x = width / 2;
        cir.y = height / 2;
        this.width = width;
        this.height = height;
        Log.e("thread", custom.getState() + "surfaceChanged1");
        if (custom != null && custom.getState() == Thread.State.NEW) {
            MathHelper.getInstance().getDotLocation(dotLocation, width, height);
            custom.start();
        } else {
            custom.onRessumeThread();
        }
        Log.e("thread", custom.getState() + "surfaceChanged2");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("surface", "surfaceDestroyed");
        custom.setIsPauseThread(true);
        Log.e("thread", custom.getState() + "surfaceDestroyed");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        custom.setIsSotpThread(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        velocityTracker.computeCurrentVelocity(1000);
        switch (event.getPointerCount()) {
            case 1:
                singlePoint(event);
                break;
            default:
                removeCallbacks(mLongPressRunnable);
                isDoublePoint = true;
                morePoint(event);
//            case 2:
//                scale(event);
//                break;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                isDoublePoint = false;
                custom.setIsPauseThread(true);
                break;
        }
        return true;
    }

    /**
     * 多点触控
     *
     * @param event
     */
    private void morePoint(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (custom != null)
                    custom.onRessumeThread();
                downPoints.clear();
                int count = event.getPointerCount();
                for (int i = 0; i < count; i++) {

                    Point point = new Point((int) event.getX(i), (int) event.getY(i));
                    downPoints.add(point);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int x = (int) event.getX(i);
                    int y = (int) event.getY(i);
                    int lastx = downPoints.get(i).x;
                    int lasty = downPoints.get(i).y;
                    int index = getClickCount(x, y);
                    int lastIndex = getClickCount(lastx, lasty);
                    if (index != -1 && lastIndex != -1) {
                        dotLocation[index].x += x - lastx;
                        dotLocation[index].y += y - lasty;
                    } else if (index != -1 && lastIndex == -1) {

                    }
                    downPoints.get(i).x = x;
                    downPoints.get(i).y = y;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                custom.setIsPauseThread(true);
                break;
        }
    }

    /**
     * 单指操作
     *
     * @param event
     */
    public void singlePoint(MotionEvent event) {
        if (isDoublePoint)
            return;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCir=50;
                dotLongPressChange = false;
                isMove = true;
                dotMove = false;
                lastDown.x = (int) event.getX();
                lastDown.y = (int) event.getY();
                if (custom != null)
                    custom.onRessumeThread();
                if (custom.isInterrupted()) {
                    Log.e("thread", "run");
                }
                clickCount = getClickCount((int) event.getX(), (int) event.getY());
                if (clickCount != -1) {
                    isPointMove = true;
                    postDelayed(mLongPressRunnable, 1000);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - lastDown.x) < 20
                        && Math.abs(event.getY() - lastDown.y) < 20)
                    break;
                removeCallbacks(mLongPressRunnable);
                dotMove = true;
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (clickCount != -1) {
//                    isPointMove = true;
                    dotLocation[clickCount].x += x - lastDown.x;
                    dotLocation[clickCount].y += y - lastDown.y;
                } else {
                    if (dotLocation.length < 2) {
                        mTranslate.x = 0;
                        mTranslate.y = 0;
                    }
                    int x1 = mTranslate.x + x - lastDown.x;
                    int y1 = mTranslate.y + y - lastDown.y;
                    if (x1 > -dotLocation[dotLocation.length - 1].x
                            && x1 < width) {
                        mTranslate.x = x1;
                    }
                    if (y1 > -dotLocation[dotLocation.length - 1].y
                            && y1 < height) {
                        mTranslate.y = y1;
                    }
                }
                lastDown.x = (int) event.getX();
                lastDown.y = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                isPointMove = false;
                removeCallbacks(mLongPressRunnable);
                if (!dotMove && clickCount != -1 && !dotLongPressChange) {
                    if (onClickListener != null)
                        onClickListener.onClick(dots.get(clickCount), isDotLongPress);
                }
                isDoublePoint = false;
                break;
        }
    }

    private float lastLength, nowLength;
    private float scaleCount;
    private Point doubleDownOne = new Point();
    private Point doubleDownTwo = new Point();

    /**
     * 放大
     *
     * @param event
     */
    private void scale(MotionEvent event) {
        isDoublePoint = true;
        isMove = true;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (custom != null)
                    custom.onRessumeThread();
                scale = initScale;
                lastScale = initScale;
                doubleDownOne.x = (int) event.getX(0);
                doubleDownOne.y = (int) event.getY(0);
                doubleDownTwo.x = (int) event.getX(1);
                doubleDownTwo.y = (int) event.getY(1);
                lastLength = getXYLength(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX(0) - doubleDownOne.x) < 10
                        && Math.abs(event.getY(0) - doubleDownOne.y) < 10
                        && Math.abs(event.getX(1) - doubleDownTwo.x) < 10
                        && Math.abs(event.getY(1) - doubleDownTwo.y) < 10)
                    break;
                Log.e("velocity", velocityTracker.getXVelocity() + "");
                nowLength = getXYLength(event);
                scaleCount = nowLength / lastLength;
                if (scaleCount < 1f && scale < 0.9f)
                    break;
                scale = scaleCount - 1 + initScale;
                for (Point point : dotLocation) {
                    float x = (point.x - cir.x) / lastScale;
                    float y = (point.y - cir.y) / lastScale;
                    point.x = (int) (x * scale + cir.x);
                    point.y = (int) (y * scale + cir.y);
                }
                lastScale = scale;
                doubleDownOne.x = (int) event.getX(0);
                doubleDownOne.y = (int) event.getY(0);
                doubleDownTwo.x = (int) event.getX(1);
                doubleDownTwo.y = (int) event.getY(1);
                Log.e("scale", scale + "");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                initScale = scale;
                custom.setIsPauseThread(true);
                break;

        }
    }

    public int getClickCount(int x, int y) {
        int count = 0;
        x -= mTranslate.x;
        y -= mTranslate.y;
        Rect rect = new Rect(x - 50, y - 50, x + 50, y + 50);
        for (Point point : dotLocation) {
            int pointx = point.x;
            int pointy = point.y;
            if (rect.contains(pointx, pointy))
                return count;
            count++;
        }
        return -1;
    }

    public int getXYVelocity(VelocityTracker velocityTracker) {

        int x = (int) velocityTracker.getXVelocity();
        int y = (int) velocityTracker.getYVelocity();
        return (int) Math.sqrt(x * x + y * y);
    }

    public float getXYLength(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    class Custom extends Thread {
        private boolean isSotpThread = false;
        private boolean isPauseThread = false;
        private Canvas canvas = null;

        @Override
        public void run() {
            while (!isSotpThread) {
                if (!isPauseThread) {

                    try {
                        synchronized (surfaceHolder) {
                            canvas = surfaceHolder.lockCanvas();
                            if (isPointMove && mCir != 100) {
                                mCir += 10;
                                Thread.sleep(15);
                            }
                            onDraw(canvas);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (canvas != null)
                            surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                } else {
                    try {
                        synchronized (this) {
                            synchronized (surfaceHolder) {
                                isPointMove = false;
                                canvas = surfaceHolder.lockCanvas();
                                if (!isPointMove && mCir != 50) {
                                    mCir -= 10;
                                    Thread.sleep(150);
                                }
                                onDraw(canvas);
                                surfaceHolder.unlockCanvasAndPost(canvas);
                            }
                            Log.e("thread",""+mCir);
                            if (mCir==50) {
                                clickCount = -1;
                                wait();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void onRessumeThread() {
            synchronized (this) {
                isPauseThread = false;
                notify();
                Log.e("thread", custom.getState() + "onRessumeThread");
            }
        }

        public void onDraw(Canvas canvas) {
            canvas.save();
            canvas.drawColor(Color.BLACK);
            canvas.restore();
            for (int i = 0; i < dotLocation.length; i++) {
                Point point = dotLocation[i];
                canvas.save();
//                canvas.scale(scale, scale, cir.x, cir.y);
                canvas.translate(mTranslate.x, mTranslate.y);
                if (i != 0) {
                    if (dots.get(i - 1).isShow() && dots.get(i).isShow()) {
//                        if (isPointMove && (i == clickCount || i - 1 == clickCount)) {
//
//                        } else {
                            Point lastPoint = dotLocation[i - 1];
                            float width1 = textPaint.measureText("股东，投资") + 10;
                            float width2 = textPaint.measureText("股东，投资") + 30;
                            float angel = MathHelper.getInstance().getAngel(dotLocation[i - 1], dotLocation[i]);

                            int cirx = (dotLocation[i - 1].x + dotLocation[i].x) / 2;
                            int ciry = (dotLocation[i - 1].y + dotLocation[i].y) / 2;
                            int onex = (int) (cirx - width1 * Math.cos(angel * Math.PI / 180) / 2);
                            int oney = (int) (ciry - width1 * Math.sin(angel * Math.PI / 180) / 2);

                            int twox = (int) (cirx + width2 * Math.cos(angel * Math.PI / 180) / 2);
                            int twoy = (int) (ciry + width2 * Math.sin(angel * Math.PI / 180) / 2);

                            if (MathHelper.getInstance()
                                    .getXYLength(dotLocation[i - 1], dotLocation[i]) > 260) {
                                canvas.drawLine(dotLocation[i - 1].x, dotLocation[i - 1].y, onex, oney, cirPaint);
                                canvas.drawLine(twox, twoy, dotLocation[i].x, dotLocation[i].y, cirPaint);
                                int x = dotLocation[i - 1].x + (dotLocation[i].x - dotLocation[i - 1].x) / 2;
                                int y = dotLocation[i - 1].y + (dotLocation[i].y - dotLocation[i - 1].y) / 2;
                                canvas.save();
                                if ((dotLocation[i].x - dotLocation[i - 1].x) > 0) {
                                    canvas.rotate(angel, (x), (y + 10));
                                    canvas.drawText("股东，投资", (x), (y + 10), textPaint);
                                } else {
                                    canvas.rotate(angel, (x), (y - 10));
                                    canvas.drawText("股东，投资", (x), (y - 10), textPaint);
                                }

                                canvas.restore();
                                if (mBlueArrow != null && !mBlueArrow.isRecycled()) {
                                    mBlueArrowMatrix.setRotate(angel, mBlueArrow.getWidth() / 2, mBlueArrow.getHeight() / 2);
                                    mBlueArrowMatrix.postTranslate((float) (dotLocation[i].x - 60 * Math.cos(angel * Math.PI / 180) - mBlueArrow.getWidth() / 2)
                                            , (float) (dotLocation[i].y - 60 * Math.sin(angel * Math.PI / 180) - mBlueArrow.getHeight() / 2));
                                    canvas.drawBitmap(mBlueArrow, mBlueArrowMatrix, null);
                                }
                            } else {
                                canvas.drawLine(lastPoint.x, lastPoint.y, point.x, point.y, cirPaint);
                            }
//                        }
                    }
                }
                if (dots.get(i).isShow()) {
                    if (dots.get(i).isCheck()) {
                        if (isDotLongPress)
                            canvas.drawCircle(point.x, point.y, 50, longClickPaint);
                        else
                            canvas.drawCircle(point.x, point.y, 50, clickPaint);
                    } else {
                        if ( i == clickCount&&mCir!=50) {
                            Log.e("ondraw",""+mCir);
                            canvas.drawCircle(point.x, point.y, mCir + 25*((float)mCir/100), emptyPaint);
                            canvas.drawCircle(point.x, point.y, mCir + 50*((float)mCir/100), emptyPaint);
                            canvas.drawCircle(point.x, point.y, mCir, clickPaint);
                        } else
                            canvas.drawCircle(point.x, point.y, 50, normalPaint);
                    }
                } else {
                    canvas.drawCircle(point.x, point.y, 50, closePaint);
                }
                canvas.restore();
            }
            if (!isMove)
                isPauseThread = true;

        }

        public boolean isSotpThread() {
            return isSotpThread;
        }

        public void setIsSotpThread(boolean isSotpThread) {
            this.isSotpThread = isSotpThread;
        }

        public void setIsPauseThread(boolean isPauseThread) {
            this.isPauseThread = isPauseThread;
        }
    }

    public void pressLongClick() {
        if (!isDotLongPress) {
            isDotLongPress = true;
            for (DragDotModel model : dots) {
                model.setIsCheck(true);
            }
        } else {
            isDotLongPress = false;
            for (DragDotModel model : dots) {
                model.setIsCheck(false);
            }
        }

        dotLongPressChange = true;
        Log.e("isdot", isDotLongPress + "");
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        public void onClick(DragDotModel model, boolean isDotLongPress);
    }
}
