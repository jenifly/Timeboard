package com.jenifly.timeboard.view;

/**
 * Created by Jenifly on 2017/3/12.
 */

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import com.jenifly.timeboard.cache.Cache;
import com.jenifly.timeboard.theme.Theme;

@SuppressLint("AppCompatCustomView")
public class JEditText extends EditText {

    private final float DIMEN_1_DP  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
    private final float DIMEN_2_DP  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
    private final float DIMEN_8_DP  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

    private final int COLOR_HINT;

    // Unselected line
    private Paint unselectedLinePaint;
    private Path unselectedLine;
    private int unselectedColor;

    // Highlighted line
    private Paint highlightedLinePaint;
    private Path highlightedLine;
    private float highlightedLineThickness;
    private int highlightedColor;

    // Common line fields
    private PathEffect lineEffect;
    private float lineHeight;
    private AnimatorSet lineAnimation;
    private float lineLeftX, lineRightX;

    private int lineColor = Color.RED;


    public JEditText(Context context) {
        this(context, null);
    }

    public JEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        COLOR_HINT = getCurrentHintTextColor();
        init(context, attrs);
    }

    public JEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        COLOR_HINT = getCurrentHintTextColor();
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        setBackgroundDrawable(null);

        // Initialize the unselected line
        unselectedLinePaint = new Paint();
        unselectedLinePaint.setStyle(Paint.Style.STROKE);
        unselectedLine = new Path();

        // Initialize the highlighted line
        highlightedLinePaint = new Paint();
        highlightedLinePaint.setStyle(Paint.Style.STROKE);
        highlightedLine = new Path();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                lineLeftX = 0;
                lineRightX = getWidth();
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        lineHeight = getHeight() - DIMEN_8_DP;

        if (isFocused()) {
            highlightedLineThickness = DIMEN_2_DP;
            highlightedColor =  Cache.theme.getBGCOLOR_DARK();
        } else {
            highlightedLineThickness = DIMEN_1_DP;
            highlightedColor = Color.TRANSPARENT;
        }
        if (isEnabled()) {
            lineEffect = null;
            unselectedColor = COLOR_HINT;
        } else {
            unselectedColor = getTextColors().getColorForState(new int[]{ -android.R.attr.state_enabled }, 0);
        }


        // Draw the unselected line
        unselectedLinePaint.setColor(unselectedColor);
        unselectedLinePaint.setStrokeWidth(DIMEN_1_DP);
        unselectedLinePaint.setPathEffect(lineEffect);
        unselectedLine.reset();
        unselectedLine.moveTo(getScrollX()+DIMEN_2_DP, lineHeight);
        unselectedLine.lineTo(getScrollX() + getWidth()-DIMEN_2_DP, lineHeight);
        canvas.drawPath(unselectedLine, unselectedLinePaint);

        // Draw the highlighted line
        highlightedLinePaint.setColor(highlightedColor);
        highlightedLinePaint.setStrokeWidth(highlightedLineThickness);
        highlightedLinePaint.setPathEffect(lineEffect);
        highlightedLine.reset();
        highlightedLine.moveTo(getScrollX()+DIMEN_2_DP + lineLeftX, lineHeight);
        highlightedLine.lineTo(getScrollX() + (lineAnimation != null && lineAnimation.isRunning() ? lineRightX : getWidth()) - DIMEN_2_DP, lineHeight);
        canvas.drawPath(highlightedLine, highlightedLinePaint);

    }

    public void setHighLightedLineColor(int color) {
        this.lineColor = color;
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!isFocused()) {
                        float x = event.getX();
                        lineAnimation = createLineAnimation(x, x, 0, getWidth());
                        lineAnimation.start();
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }




    private AnimatorSet createLineAnimation(float startA, float startB, float targetA, float targetB) {
        ValueAnimator leftAnim = ValueAnimator.ofFloat(startA, targetA);
        leftAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lineLeftX = (Float) animation.getAnimatedValue();
            }
        });
        ValueAnimator rightAnim = ValueAnimator.ofFloat(startB, targetB);
        rightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lineRightX = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(leftAnim, rightAnim);
        return set;
    }

}