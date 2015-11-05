package tencent.jusfoun.cn.bcdrawtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Author  JUSFOUN
 * CreateDate 2015/9/18.
 * Description
 */
public class BCView extends View {

    public static final int TRAN = 0;
    public static final int SCAL = 1;
    private float cirX, cirY;
    private Paint cirPaint;
    private float cirR = 20;
    private Path linePath;
    private Paint linePaint;
    private int index = 10;
    private RectF rectF;
    private float startAngle = 0;
    private float angles[] = new float[index];
    private float anglesMax[] = new float[index * 2];
    private float cirXs[], cirxs[];
    private float cirYs[], cirys[];
    private float bcXs[], bcYs[];
    private float qXs[], qYs[];
    private float mRadius;
    private CirPoint cirPoint;
    private float mTran[] = new float[2];
    private float minit;
    private float scal = 1;
    private boolean mIsDisallowIntercept;
    private int action;
    private ArrayList<RectF> list;

    private float downX, downY;
    private CoordTools coordTools;
    private Context context;

    public BCView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public BCView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public BCView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public void initView() {
        cirPaint = new Paint();
        cirPaint.setStyle(Paint.Style.FILL);
        cirPaint.setColor(Color.BLUE);
        rectF = new RectF();

        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setAntiAlias(true);

        linePath = new Path();

        cirXs = new float[index];
        cirYs = new float[index];

        cirxs = new float[index * 2];
        cirys = new float[index * 2];

        qXs = new float[index];
        qYs = new float[index];

        bcXs = new float[index * 2];
        bcYs = new float[index * 2];

        float angle = 360 / index;
        for (int i = 0; i < angles.length; i++) {
            angles[i] = startAngle + angle * i;
            if (angles[i] > 360)
                angles[i] -= 360;
        }

        for (int i = 0; i < anglesMax.length; i++) {
            anglesMax[i] = startAngle + angle * i / 2;
            if (anglesMax[i] > 360)
                anglesMax[i] -= 360;
        }

        mTran[0] = 0;
        mTran[1] = 0;
        list = new ArrayList<RectF>();
        coordTools = new CoordTools();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cirX = w / 2;
        cirY = h / 2;
        if (cirX < cirY) {
            mRadius = (float) (cirX * 0.9);
        } else {
            mRadius = (float) (cirY * 0.9);

        }

        for (int i = 0; i < index; i++) {
            cirPoint = MathHelper.getInstance().getXY(angles[i], cirX, cirY, mRadius * 0.5f);
            cirXs[i] = cirPoint.X;
            cirYs[i] = cirPoint.Y;
            RectF rectF = new RectF();
            rectF.set(cirXs[i], cirYs[i], cirXs[i] + 200, cirYs[i] + 200);
            list.add(rectF);
        }

        for (int i = 0; i < index * 2; i++) {
            cirPoint = MathHelper.getInstance().getXY(anglesMax[i], cirX, cirY, mRadius);
            cirxs[i] = cirPoint.X;
            cirys[i] = cirPoint.Y;
            RectF rectF = new RectF();
            rectF.set(cirxs[i], cirys[i], cirxs[i] + 50, cirys[i] + 50);
            list.add(rectF);
        }

        for (int i = 0; i < index; i++) {
            cirPoint = MathHelper.getInstance().getXY(angles[i], cirX, cirY, mRadius * 0.7f);
            qXs[i] = cirPoint.X;
            qYs[i] = cirPoint.Y;
        }

        for (int i = 0; i < cirxs.length; i++) {
            cirPoint = MathHelper.getInstance().getBezierCubic(cirXs[i / 2], cirYs[i / 2], cirxs[i], cirys[i], cirX, cirY, mRadius * 0.6f);
            if (cirPoint != null) {
                bcXs[i] = cirPoint.X;
                bcYs[i] = cirPoint.Y;
            }
        }

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
                int count = 0;
                for (RectF rectF : list) {
                    if (coordTools.checkCoord(event.getX(), event.getY(), rectF)) {
                        break;
                    }
                    count++;
                }
                if (count < index)
                    Toast.makeText(context, "你点的第一层第" + count + "个", Toast.LENGTH_SHORT).show();
                else if (count < 30)
                    Toast.makeText(context, "你点击的第二层第" + (count - index) + "个", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
//        if (mode == TRAN) {
        if (mTran[0] < -cirX)
            mTran[0] = -cirX;
        if (mTran[0] > cirX)
            mTran[0] = cirX;
        if (mTran[1] < -cirY)
            mTran[1] = -cirY;
        if (mTran[1] > cirY)
            mTran[1] = cirY;
        canvas.translate(mTran[0], mTran[1]);
//        } else if (mode == SCAL)
        canvas.save();
        canvas.scale(scal, scal, cirX, cirY);
        canvas.drawCircle(cirX, cirY, cirR, cirPaint);
        cirPaint.setTextSize(20);
        for (int i = 0; i < index; i++) {
            list.get(i).offset(mTran[0], mTran[1]);
            linePath.reset();
            linePath.moveTo(cirX, (cirY + cirR));
            if (cirXs[i] > cirX)
                linePath.quadTo((cirX + 5), (cirY + cirR + 100), cirXs[i], cirYs[i]);
            else
                linePath.quadTo((cirX - 5), (cirY + cirR + 100), cirXs[i], cirYs[i]);
            if (i == 6 || i == 7) {
                linePath.moveTo(cirXs[i], cirYs[i]);

                linePath.cubicTo(qXs[i], qYs[i], bcXs[i * 2], bcYs[i * 2]
                        , cirxs[i * 2], cirys[i * 2]);
                linePath.moveTo(cirXs[i], cirYs[i]);
                linePath.cubicTo(qXs[i], qYs[i], bcXs[i * 2 + 1], bcYs[i * 2 + 1]
                        , cirxs[i * 2 + 1], cirys[i * 2 + 1]);

                canvas.save();
                canvas.rotate(anglesMax[i * 2], cirxs[i * 2], cirys[i * 2]);
                cirPaint.getFontMetrics();
                canvas.drawText("hahahahahahahahha" + i, cirxs[i * 2], cirys[i * 2], cirPaint);

//                rectF.set(cirxs[i * 2] - 100, cirys[i * 2] - 100, cirxs[i * 2] + 100, cirys[i * 2] + 100);
//                rectF.
                canvas.restore();
                canvas.save();
                canvas.rotate(anglesMax[i * 2 + 1], cirxs[i * 2 + 1], cirys[i * 2 + 1]);
                canvas.drawText("hahahahahahahahha" + i, cirxs[i * 2 + 1], cirys[i * 2 + 1], cirPaint);
                canvas.restore();
            } else {
                canvas.save();
                canvas.rotate(angles[i], cirXs[i], cirYs[i]);
                canvas.drawText("hahahahahahahahha" + i, cirXs[i], cirYs[i], cirPaint);
                canvas.restore();
            }
            canvas.restore();
            canvas.drawPath(linePath, linePaint);
            cirPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(cirXs[i], cirYs[i], 10, cirPaint);
            if (i == 6 || i == 7) {
                canvas.drawCircle(cirxs[i * 2], cirys[i * 2], 10, cirPaint);
                canvas.drawCircle(cirxs[i * 2 + 1], cirys[i * 2 + 1], 10, cirPaint);
            }
        }
        canvas.restore();
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
                float toScal = getXYLength(event);
                if (Float.compare(minit, 0f) == 0)
                    break;
                scal = toScal / minit;
                Log.e("scal", scal + "");
                postInvalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                if (event.getPointerCount() == 2) {
                    minit = getXYLength(event);
                }
                break;
        }
    }

    public float getXYLength(MotionEvent event) {

        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

}
