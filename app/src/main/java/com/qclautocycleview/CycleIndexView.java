package com.qclautocycleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义圆点
 */
public class CycleIndexView extends View {
    private int mViewCount = 5;
    private IdxCircle mIdxCircle;
    private int mCurViewIndex = 0;


    public CycleIndexView(Context context) {
        this(context, null);

    }

    public CycleIndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CycleIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float density = context.getResources().getDisplayMetrics().density;
        mIdxCircle = new IdxCircle(density);

    }

    /*
    * 设置圆点个数
    * */
    public void setViewCount(int count) {
        mViewCount = count;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndexPoint(canvas);
    }

    public void drawIndexPoint(Canvas canvas) {
        final int saveCount = canvas.save();
        for (int i = 0; i < mViewCount; i++) {
            boolean isCurView = (i == mCurViewIndex);
            mIdxCircle.draw(canvas, i, isCurView);
        }
        canvas.restoreToCount(saveCount);

    }

    public int getCycleIdxViewHeight() {
        return mIdxCircle.getHeight();
    }

    public int getCycleIdxViewWidth() {
        return mIdxCircle.getRadius() * 2 * mViewCount + mIdxCircle.getSpace() * (mViewCount - 1);
    }

    //绑定圆点和viewpager的条目
    public void setCurIndex(int idx) {
        mCurViewIndex = idx % mViewCount;
        invalidate();//重绘
    }

    private class IdxCircle {
        private int mAngle = 45;
        private Paint mPaint = new Paint();
        private int mCircleRadius = 5;//设置圆点半径
        private int mSpace = 10;//设置圆点间隔
        private float mDensity = 1;

        public IdxCircle(float density) {
            mDensity = density;
            mCircleRadius = (int) (mCircleRadius * density);
            mSpace = (int) (mSpace * density);

            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setColor(Color.WHITE);
        }

        public void draw(Canvas canvas, int i, boolean isCurPosition) {
            final int saveCount = canvas.save();
            //            final int alpha = isCurPosition ? 5 : 160;
            //            mPaint.setAlpha(alpha);//设置透明度
            if (!isCurPosition) {
                mPaint.setColor(0xffbfbfbf);//设置未选中的点的颜色
            } else {
                mPaint.setColor(0xffffffff);//设置选中的点的颜色
            }

            canvas.translate(mCircleRadius + i * (mSpace + 2 * mCircleRadius), mCircleRadius);

            canvas.drawCircle(0, 0, mCircleRadius, mPaint);

            canvas.restoreToCount(saveCount);
        }

        public int getHeight() {
            return mCircleRadius * 4;//设置圆点布局的高度
        }

        public int getRadius() {
            return mCircleRadius;
        }

        public int getSpace() {
            return mSpace;
        }
    }
}
