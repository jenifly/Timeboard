package com.jenifly.timeboard.view.timelyView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.jenifly.timeboard.utils.ScreenUtil;

import java.util.ArrayList;


/**
 * Created by Jenifly on 2017/7/2.
 */

public class DaysView extends LinearLayout {

    private int days,digits;
    private ArrayList<TimelyView> timelyViews = new ArrayList<>();
    private ArrayList<Integer> figures = new ArrayList<>();
    private LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);

    public DaysView(Context context) {
        super(context);
        init();
    }

    public DaysView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public DaysView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        this.setOrientation(HORIZONTAL);
        this.removeAllViews();
        int margin = ScreenUtil.dip2px(getContext(),2);
        lp.setMargins(margin,0,0,0);
        timelyViews.clear();
        if(digits == 0){
            TimelyView timelyView = new TimelyView(getContext());
            timelyView.setView(0, 9);
            this.addView(timelyView);
        }else {
            for (int i = 0; i < digits; i++) {
                TimelyView timelyView = new TimelyView(getContext());
                timelyView.setView(figures.get(i), 9);
                if (i != 0)
                    timelyView.setLayoutParams(lp);
                timelyViews.add(timelyView);
            }
            for (TimelyView t : timelyViews) {
                this.addView(t);
            }
        }
    }

    public void setDays(int days){
        this.days = days;
        refeshFigures();
        init();
    }

    public void setColor(int color){
        for(TimelyView t:timelyViews){
            t.setColor(color);
        }
    }

    private void refeshFigures(){
        this.figures.clear();
        String s = String.valueOf(days);
        for (int i= 1;i<=s.length();i++){
            this.figures.add(Integer.parseInt(String.valueOf(s.charAt(i-1))));
        }
        digits = figures.size();
    }

    public void addDay(int duration){
        int oldDays = days;
        ++days;
        if(String.valueOf(oldDays).length() != String.valueOf(days).length()){
            timelyViews.add(new TimelyView(getContext()));
            ++digits;
            timelyViews.get(digits-1).setLayoutParams(lp);
            addView(timelyViews.get(digits-1));
            int n = 0;
            for(TimelyView t:timelyViews){
                if(n==0) {
                    t.setView(0, 9);
                } else {
                    t.setView(9, 9);
                }
                t.Add(duration);
                n++;
            }
        }else {
            for(int i = 1;i<digits;i++) {
                int s = (int)Math.pow(10,i);
                if (oldDays / s != days / s)
                    timelyViews.get(digits - 1 - i).Add(duration);
            }
            timelyViews.get(digits-1).Add(duration);
        }
    }
}
