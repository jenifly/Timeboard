package com.jenifly.timeboard.view.timelyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jenifly.timeboard.view.timelyView.animation.TimelyEvaluator;
import com.jenifly.timeboard.view.timelyView.model.NumberUtils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.util.Property;

public class TimelyView extends View {
    private static final Property<TimelyView, float[][]> CONTROL_POINTS_PROPERTY = new Property<TimelyView, float[][]>(float[][].class, "controlPoints") {
        @Override
        public float[][] get(TimelyView object) {
            return object.getControlPoints();
        }

        @Override
        public void set(TimelyView object, float[][] value) {
            object.setControlPoints(value);
        }
    };

    private Paint mPaint = null;
    private Path mPath = null;
    private float[][] controlPoints = null;
    private int figure = 0,figureRange = 9;

    public TimelyView(Context context) {
        super(context);
        init();
    }

    public TimelyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimelyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public float[][] getControlPoints() {
        return controlPoints;
    }

    public void setControlPoints(float[][] controlPoints) {
        this.controlPoints = controlPoints;
        invalidate();
    }

    public void setColor(int color){
        mPaint.setColor(color);
        invalidate();
    }

    public int getLimit(){
        return figureRange;
    }

    public void setView(int figure,int figureRange) {
        this.figure = figure;
        this.figureRange = figureRange;
    }

    public void setLimit(int limit){
        this.figureRange = limit;
    }

    public int getFigure() {
        return figure;
    }

    public void Add(int duration) {
        float[][] startPoints = NumberUtils.getControlPointsFor(figure);
        if(figure >= figureRange){
            figure = 0;
        }else ++figure;
        float[][] endPoints = NumberUtils.getControlPointsFor(figure);
        ObjectAnimator objectAnimator =ObjectAnimator.ofObject(this, CONTROL_POINTS_PROPERTY, new TimelyEvaluator(), startPoints, endPoints);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (controlPoints == null)
            controlPoints = NumberUtils.getControlPointsFor(figure);
        int length = controlPoints.length;
        float minDimen = getMeasuredHeight();
        int adjust_X = (int)(minDimen * 0.24f);
        int adjust_Y = (int)(minDimen * 0.05f);
        mPath.reset();
        mPath.moveTo(minDimen * controlPoints[0][0] - adjust_X, minDimen * controlPoints[0][1] - adjust_Y);
        for (int i = 1; i < length; i += 3) {
            mPath.cubicTo(minDimen * controlPoints[i][0] - adjust_X, minDimen * controlPoints[i][1] - adjust_Y,
                          minDimen * controlPoints[i + 1][0] - adjust_X, minDimen * controlPoints[i + 1][1] - adjust_Y,
                          minDimen * controlPoints[i + 2][0] - adjust_X, minDimen * controlPoints[i + 2][1] - adjust_Y);
        }
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        int width =(int)(height * 0.64f);
        setMeasuredDimension(width, height);
    }

    private void init() {
        if(mPaint == null){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(5.0f);
        mPaint.setStyle(Paint.Style.STROKE);
        }
        if(mPath == null)
           mPath = new Path();
    }
}
