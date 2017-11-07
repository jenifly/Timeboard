package com.jenifly.timeboard.view.timelyView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.jenifly.timeboard.utils.ScreenUtil;

/**
 * Created by Jenifly on 2017/7/5.
 */

public class HMSView extends LinearLayout{

    private int value, type;
    private TimelyView timelyView0;
    private TimelyView timelyView1;
    private LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);

    public HMSView(Context context) {
        super(context);
        init();
    }

    public HMSView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public HMSView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setColor(int color){
        timelyView0.setColor(color);
        timelyView1.setColor(color);
    }

    private void init() {
        int margin = ScreenUtil.dip2px(getContext(), 3);
        lp.setMargins(margin, 0, 0, 0);
        this.removeAllViews();
        this.setOrientation(HORIZONTAL);
        timelyView0 = new TimelyView(getContext());
        timelyView1 = new TimelyView(getContext());
        timelyView1.setLayoutParams(lp);
        this.addView(timelyView0);
        this.addView(timelyView1);
    }

    public void setValue(int value,int type){
        this.value = value;
        init();
        timelyView0.setView(getInt(0), type==0?2:5);
        timelyView1.setView(getInt(1), 9);
        invalidate();
    }

    private int getInt(int i){
        return Integer.parseInt(String.valueOf(String.format("%02d",value).charAt(i)));
    }

    public void addOne(int duration) {
        ++value;
        if (value%10 == 0) {
            timelyView0.Add(duration);
            if(type!=0 && value == 60)
                value = 0;
        }
        if(getInt(0) == 2 && timelyView1.getLimit() != 3)
            timelyView1.setLimit(3);
        else if(timelyView1.getLimit()!=9)
            timelyView1.setLimit(9);
        if(type == 0 && value == 24){
            timelyView0.Add(duration);
            value = 0;
        }
        timelyView1.Add(duration);
    }
}
