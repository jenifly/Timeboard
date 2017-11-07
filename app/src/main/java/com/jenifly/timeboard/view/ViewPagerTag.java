package com.jenifly.timeboard.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.jenifly.timeboard.cache.Cache;
import com.jenifly.timeboard.theme.Theme;

/**
 * Created by Jenifly on 2017/7/1.
 */

public class ViewPagerTag extends View {

    private int unselectColor = Color.parseColor("#666666");
    private int count = 1;
    private int index = 1;
    private int _x = -1;
    private int _y = -1;
    private int radius = 8;
    private Paint paint;

    public ViewPagerTag(Context context) {
        super(context);
    }

    public ViewPagerTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        _y = getMeasuredHeight()/2;
        _x = getMeasuredWidth()/2;
    }

    public void setData(int count, int index){
        this.count = count;
        this.index = index;
        invalidate();
    }

    public void setData(int index){
        this.index = index;
        invalidate();
    }

    public void setRadius(int radius){
        this.radius = radius;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = _x - (count*3*radius)/2 + 2*radius;
        for (int i=0;i<count;i++){
            if(i!=index-1)
                paint.setColor(unselectColor);
            else
                paint.setColor( Cache.theme.getBGCOLOR_LIGHT());
            canvas.drawCircle(x + 3*i*radius,_y,radius,paint);
        }
    }
}
