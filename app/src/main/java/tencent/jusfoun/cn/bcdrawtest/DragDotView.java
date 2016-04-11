package tencent.jusfoun.cn.bcdrawtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.VelocityTracker;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import tencent.jusfoun.cn.model.DragDotModel;

/**
 * Author  wangchenchen
 * CreateDate 2015/12/22.
 * Email wcc@jusfoun.com
 * Description
 */
public class DragDotView extends View {

    private Context context;
    private Paint cirPaint;
    private List<DragDotModel> dots = new ArrayList<>();
    private Point mTranslate,firstDown,lastDown;
    private Point[] dotLocation = new Point[12];
    private int clickCount;
    private boolean isMove = false;
    private VelocityTracker velocityTracker;
    private boolean dotMove =false;
    public DragDotView(Context context) {
        super(context);
        initView(context);
    }

    public DragDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DragDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        this.context = context;

        cirPaint = new Paint();
        cirPaint.setColor(Color.YELLOW);
        cirPaint.setStrokeWidth(1);
        cirPaint.setAntiAlias(true);
        cirPaint.setStyle(Paint.Style.FILL);
        cirPaint.setAlpha(255);

        mTranslate = new Point(0, 0);
        firstDown=new Point();
        lastDown=new Point();
        velocityTracker=VelocityTracker.obtain();
        for (int i = 0; i <12; i++) {
            DragDotModel model=new DragDotModel();
            model.setName("第"+(i+1)+"个");
            model.setIsShow(true);
            dots.add(model);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        MathHelper.getInstance().getDotLocation(dotLocation, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawColor(Color.BLACK);
        canvas.restore();
        for (int i = 0; i < dotLocation.length; i++) {
            Point point = dotLocation[i];
            canvas.save();
            canvas.translate(mTranslate.x, mTranslate.y);
            if (i != 0) {
                if (dots.get(i-1).isShow()&& dots.get(i).isShow()) {
                    Point lastPoint = dotLocation[i - 1];
                    canvas.drawLine(lastPoint.x, lastPoint.y, point.x, point.y, cirPaint);
                }
            }
            if (dots.get(i).isShow())
                canvas.drawCircle(point.x, point.y, 50, cirPaint);
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        velocityTracker.computeCurrentVelocity(1000);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = true;
                dotMove =false;
                lastDown.x= (int) event.getX();
                lastDown.y= (int) event.getY();
                clickCount = getClickCount((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                int velocity=getXYVelocity(velocityTracker);
                if (Math.abs(velocityTracker.getXVelocity())<5
                        &&Math.abs(velocityTracker.getYVelocity())<5)
                    return true;
                dotMove =true;
                int x= (int) event.getX();
                int y= (int) event.getY();
                if (clickCount != -1) {
                    dotLocation[clickCount].x += x-lastDown.x;
                    dotLocation[clickCount].y += y-lastDown.y;
                }else {
                    mTranslate.x+= x-lastDown.x;
                    mTranslate.y+= y-lastDown.y;
                }
                lastDown.x= (int) event.getX();
                lastDown.y= (int) event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (!dotMove &&clickCount!=-1){
                    if (onClickListener!=null)
                        onClickListener.onClick(dots.get(clickCount));
                }
                break;
        }
        return true;
    }

    public int getClickCount(int x, int y) {
        int count = 0;
        x-=mTranslate.x;
        y-=mTranslate.y;
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

    public int getXYLength(MotionEvent event){
        int x= (int) (event.getX()-firstDown.x);
        int y= (int) (event.getY()-firstDown.y);
        return (int) Math.sqrt(x*x+y*y);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        public void onClick(DragDotModel model);
    }

}
