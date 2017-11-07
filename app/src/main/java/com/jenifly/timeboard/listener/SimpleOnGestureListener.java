package com.jenifly.timeboard.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.jenifly.timeboard.event.gesturesEvent;
import com.jenifly.timeboard.event.showDelImg;
import com.jenifly.timeboard.event.slideEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/8/23.
 */
public class SimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {

    private static final int FLING_MIN_DISTANCE = 20;
    private static final int FLING_MIN_VELOCITY = 60;
    private int mViewpageX,mViewpageY,mViewpageWidth,mViewpageHeight;

    public  void setViewPagePosition(int mViewpageWidth,int mViewpageHeight ){
        this.mViewpageWidth = mViewpageWidth*7/10;
        this.mViewpageHeight = mViewpageHeight*7/10;
        this.mViewpageX = (mViewpageWidth /2)-(mViewpageWidth*7/10/2 );
        this.mViewpageY = (mViewpageHeight /2)-(mViewpageHeight*7/10/2 );;

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if( e2.getX()-e1.getX() > FLING_MIN_DISTANCE  && Math.abs(velocityX) > FLING_MIN_VELOCITY)
            EventBus.getDefault().post(new slideEvent(MotionEvent.ACTION_MOVE, "right"));
        else if(e1.getX()-e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY)
            EventBus.getDefault().post(new slideEvent(MotionEvent.ACTION_MOVE,"left" ));
        else if(e1.getY()-e2.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY)
            EventBus.getDefault().post(new slideEvent(MotionEvent.ACTION_MOVE,"up" ));
        else if(e2.getY()-e1.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY)
            EventBus.getDefault().post(new slideEvent(MotionEvent.ACTION_MOVE,"down" ));

        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        JudgeSendEvent("onDown",e,null);
        return super.onDown(e);
    }

    //判断是否发出事件
    private void JudgeSendEvent(String action,MotionEvent e1, MotionEvent e2){
        switch (action ){
            case "onDown":
                if(e1.getX()<mViewpageX && e1.getY()>mViewpageY && e1.getY()<(mViewpageY+mViewpageHeight) )
                    EventBus.getDefault().post(new slideEvent(MotionEvent.ACTION_DOWN, "left"));
                if(e1.getX()>(mViewpageX+ mViewpageWidth)&& e1.getY()>mViewpageY && e1.getY()<(mViewpageY+mViewpageHeight) )
                    EventBus.getDefault().post(new slideEvent(MotionEvent.ACTION_DOWN, "right"));
                break;
        }

    }


}
