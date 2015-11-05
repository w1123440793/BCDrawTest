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
import android.text.BoringLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Author  JUSFOUN
 * CreateDate 2015/10/19.
 * Description
 */
public class LineDrawView extends LineBaseView {

    private static final String TAG = "LineDrawView";
    public LineDrawView(Context context) {
        super(context);
    }

    public LineDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public LineDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        refresh(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        //绘制主图
        for (int i = 0; i < leftCount; i++) {
            path.reset();
            path.moveTo(mCir.x, mCir.y);
            ArrayList<PointF> list = leftPoints.get(i);
            if (isAnimStop) {
                path.lineTo(leftLines.get(i).x, leftLines.get(i).y);
                canvas.drawPath(path, leftPaints.get(i));
                float arc = leftAngles[i] / 180f;
                if (clickCount!=i){
                    canvas.save();
                    canvas.rotate(leftAngles[i] + 180, (leftLines.get(i).x), (leftLines.get(i).y + 12));
                    canvas.drawText(leftInfo.get(i), (leftLines.get(i).x - 80), (leftLines.get(i).y + 12), leftPaint);
                    canvas.restore();
                }
                if (!isDrawChildLine&&clickCount==i) {
                    canvas.save();
                    canvas.rotate(leftAngles[i] + 180, (leftLines.get(i).x), (leftLines.get(i).y + 12));
                    canvas.drawText(leftInfo.get(i), (leftLines.get(i).x - 80), (leftLines.get(i).y + 12), leftPaint);
                    canvas.restore();
                }
                if (mCirBitmap != null && !mCirBitmap.isRecycled()) {
                    mCirMatrix.setScale(mRaiusCount / 50f, mRaiusCount / 50f, mCirBitmap.getWidth() / 2, mCirBitmap.getHeight() / 2);
                    mCirMatrix.postTranslate(mCir.x - mCirBitmap.getWidth() / 2, mCir.y - mCirBitmap.getHeight() / 2);
                    canvas.drawBitmap(mCirBitmap, mCirMatrix, null);
                }

                for (int j = 0; j < alphaList.size(); j++) {
                    int alpha = Integer.parseInt(alphaList.get(j));
                    int startWidth = Integer.parseInt(startWidthList.get(j));
                    paint.setAlpha(alpha);
                    canvas.drawCircle(mCir.x, mCir.y, startWidth, paint);
                    Log.e(TAG,startWidth+"");
                    //同心圆扩散
                    if ( alpha > 0 && startWidth < maxWidth)
                    {
                        alphaList.set(j, (alpha-2)+"");
                        startWidthList.set(j, (startWidth+1)+"");
                    }
                }
                if (Integer.parseInt(startWidthList.get(startWidthList.size() - 1)) == (50+maxWidth / 5)) {
                    alphaList.add("255");
                    startWidthList.add("50");
                }
                //同心圆数量达到3个，删除最外层圆
                if(startWidthList.size()==3)
                {
                    startWidthList.remove(0);
                    alphaList.remove(0);
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
                if (clickCount-leftCount!=i) {
                    canvas.save();
                    canvas.rotate(rightAngles[i], rightLines.get(i).x, rightLines.get(i).y + 12);
                    canvas.drawText(rightInfo.get(i), rightLines.get(i).x + 80, rightLines.get(i).y + 12, rightPaint);
                    canvas.restore();
                }

                if (!isDrawChildLine&&clickCount-leftCount==i) {
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
                                if (i<leftChildCount)
                                    canvas.drawCircle(list.get(j).x, list.get(j).y, 30, leftPaints.get(i));
                                else
                                    canvas.drawCircle(list.get(j).x, list.get(j).y, 30, rightPaints.get(i));
                                list.remove(j);
                                if (j - 1 >= 0)
                                    list.remove(j - 1);
                                if(j-2>=0)
                                    list.remove(j-2);
                            }
                        }
                        if (i<leftChildCount)
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
                            canvas.rotate(childAngles[i]+180, childList.get(i).x, childList.get(i).y);
                            canvas.drawText("第" + i + "个点", childList.get(i).x -80, childList.get(i).y, childPaints.get(i));
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
                            canvas.drawCircle(leftLines.get(clickCount).x, leftLines.get(clickCount).y, 50, mCirPaint);
                            if (mCirBitmap != null && !mCirBitmap.isRecycled()) {
                                mCirMatrixChild.setScale(mRaiusCount / 50f, mRaiusCount / 50f, mCirBitmap.getWidth() / 2, mCirBitmap.getHeight() / 2);
                                mCirMatrixChild.postTranslate(leftLines.get(clickCount).x - mCirBitmap.getWidth() / 2
                                        , leftLines.get(clickCount).y - mCirBitmap.getHeight() / 2);
                                canvas.drawBitmap(mCirBitmap, mCirMatrixChild, null);
                            }

                            canvas.save();
                            canvas.translate(leftLines.get(clickCount).x, leftLines.get(clickCount).y-30);
                            leftStaticLayout.get(clickCount).draw(canvas);
                            canvas.restore();

                        } else {
                            canvas.drawCircle(rightLines.get(clickCount -leftCount).x, rightLines.get(clickCount - leftCount).y, 50, mCirPaint);
                            if (mCirBitmap != null && !mCirBitmap.isRecycled()) {
                                mCirMatrixChild.setScale(mRaiusCount / 50f, mRaiusCount / 50f, mCirBitmap.getWidth() / 2, mCirBitmap.getHeight() / 2);
                                mCirMatrixChild.postTranslate(rightLines.get(clickCount - leftCount).x - mCirBitmap.getWidth() / 2
                                        , rightLines.get(clickCount - leftCount).y - mCirBitmap.getHeight() / 2);
                                canvas.drawBitmap(mCirBitmap, mCirMatrixChild, null);
                            }

                            canvas.save();
                            canvas.translate(rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y-30);
                            rightStaticLayout.get(clickCount - leftCount).draw(canvas);
                            canvas.restore();
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
                            if (i<leftChildCount) {
                                canvas.drawPath(path, leftPaints.get(i));
                                if (j == list.size() - 1) {
                                    canvas.drawCircle(list.get(j).x, list.get(j).y, 30, leftPaints.get(i));
                                }
                            }
                            else {
                                canvas.drawPath(path, rightPaints.get(i));
                                if (j == list.size() - 1) {
                                    canvas.drawCircle(list.get(j).x, list.get(j).y, 30, rightPaints.get(i));
                                }
                            }

                        } else {
                            path.moveTo(rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y);
                            path.lineTo(list.get(j).x, list.get(j).y);
                            if (i<leftChildCount) {
                                canvas.drawPath(path, leftPaints.get(i));
                                if (j == list.size() - 1) {
                                    canvas.drawCircle(list.get(j).x, list.get(j).y, 30, leftPaints.get(i));
                                }
                            }
                            else {
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
        mStaticLayout = new StaticLayout("九次方财富资", cirPaint, 75, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
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
            StaticLayout staticLayout=new StaticLayout(leftInfo.get(i),cirPaint, 75, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
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

            StaticLayout staticLayout=new StaticLayout(rightInfo.get(i),cirPaint, 75, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            rightStaticLayout.add(staticLayout);
        }

        startAnim();
    }

    @Override
    public void touchUp(MotionEvent event) {
        super.touchUp(event);
        if (doublePointer)
            doublePointer=false;
        if (isMove) {
            isMove = false;
            return;
        }
        float x = event.getX() - mTran[0];
        float y = event.getY() - mTran[1];
        int i=0;
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
            LineDrawView.this.clickCount = clickCount;
            scaleCount=1;
            lastScale=1;
            for(PointF pointF:leftLines){
                leftRects.get(i).set(pointF.x-30,pointF.y-50,pointF.x+80,pointF.y+50);
                i++;
            }
            i=0;
            for (PointF pointF:rightLines){
                rightRects.get(i).set(pointF.x-30,pointF.y-50,pointF.x+80,pointF.y+50);
                i++;
            }
        }
        if (LineDrawView.this.clickCount != -1 && LineDrawView.this.clickCount == lastClickCount) {
            int count = isInRect(x, y, childRect);
            if (count >= 0) {
                Toast.makeText(context, "你点的子节点", Toast.LENGTH_SHORT).show();
                i=0;
                for(PointF pointF:childList){
                    childRect.get(i).set(pointF.x-50,pointF.y-50,pointF.x+50,pointF.y+50);
                    i++;
                }
            } else if (clickCount!=-1){
                if (isDrawChildLine)
                    putChildAnim(LineDrawView.this.clickCount);
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

        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!threadStop) {
                        if (mRaiusCount >= 60) {
                            mRaiusCount = 30;
                        } else
                            mRaiusCount++;
                        index++;
                        childIndex++;
                        try {
                            Thread.sleep(50);
                            postInvalidate();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        if (!thread.isAlive())
            thread.start();
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
                postInvalidate();
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
                TextPaint paint=new TextPaint();
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
                TextPaint paint=new TextPaint();
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
                    postInvalidate();
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
                } else if (click >= 0) {
                    float argAngle = (float) (Math.PI * rightAngles[click - leftCount] / 180f);
                    mTran[0] = (float) -((mCir.x + mRaius) * (100 - index) * Math.cos(argAngle) / 100);
                    mTran[1] = (float) -((mCir.y + mRaius) * (100 - index) * Math.sin(argAngle) / 100);
                    rightLines.get(click - leftCount).x -= Math.cos(argAngle) * (index - lastIndex) * mRaius * 0.02f;
                    rightLines.get(click - leftCount).y -= Math.sin(argAngle) * (index - lastIndex) * mRaius * 0.02f;
                }
                lastIndex = index;
            }
        });
        postInvalidate();
    }

    public static interface OnClickPotListener {
        public void onClickPot();
    }

    private OnClickPotListener potListener;

    public void setClickPot(OnClickPotListener listener) {
        potListener = listener;
    }
}