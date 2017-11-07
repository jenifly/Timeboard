package com.jenifly.timeboard.listener;

import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.jenifly.timeboard.constance.fragConst;
import com.jenifly.timeboard.event.deleteFragEvent;
import com.jenifly.timeboard.event.fragEvent;
import com.jenifly.timeboard.event.gesturesEvent;
import com.jenifly.timeboard.event.showDelImg;
import com.jenifly.timeboard.event.touchEvent;
import com.jenifly.timeboard.fragment.mainFrag;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jenifly on 2017/10/31.
 */

public class ZoomListenter implements View.OnTouchListener {

    private int mode = 0;
    private float oldDist;
    private int touchType;
    private CardView cardView;
    private mainFrag mainFrag;
    private List<int[]> positionlist = new ArrayList<>();
    private float point_x, point_y;
    private float mov_y;
    private int left, right, top, bottom;
    private boolean isFull,isOk;

    public ZoomListenter(mainFrag mainFrag,CardView cardView){
        this.cardView = cardView;
        this.mainFrag = mainFrag;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = 1;
                touchType = 0;
                point_y = event.getY();
                point_x = event.getX();
                positionlist.clear();
                isOk = true;
                isFull = mainFrag.getResources().getDisplayMetrics().widthPixels - cardView.getWidth() < 10;
                break;
            case MotionEvent.ACTION_UP:
                cardView.requestDisallowInterceptTouchEvent(false);
                mode = 0;
                switch (touchType){
                    case 0:
                        EventBus.getDefault().post(new touchEvent("click"));
                        break;
                    case 1:
                        if(Math.abs(event.getX()-point_x)<20 && Math.abs(event.getY()-point_y)<20)
                            EventBus.getDefault().post(new gesturesEvent(0));
                        if (positionlist.size() >= 2) {
                            if (fragConst.fraglist.size() > 1 && 0-positionlist.get(positionlist.size() - 1)[1] > cardView.getWidth() / 2) {
                                mainFrag.delAnime();
                                EventBus.getDefault().post(new showDelImg(3));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        EventBus.getDefault().post(new deleteFragEvent(mainFrag.getFragTag()));
                                    }
                                }, 300);
                                return true;
                            }
                        } else {
                            EventBus.getDefault().post(new fragEvent(mainFrag.getFragTag()));
                        }
                        for (int i = positionlist.size() - 1; i >= 0; i--) {
                            cardView.layout(positionlist.get(i)[0], positionlist.get(i)[1], positionlist.get(i)[2], positionlist.get(i)[3]);
                        }
                        cardView.layout(0, 0, cardView.getWidth(), cardView.getHeight());
                        mainFrag.setAlpha(255);
                        EventBus.getDefault().post(new showDelImg(3));
                        break;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode -= 1;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                cardView.requestDisallowInterceptTouchEvent(true);
                mode += 1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isOk && mode >= 2) {
                    float newDist = spacing(event);
                    if (newDist > oldDist + 1) {
                        //  newDist / oldDist; 倍数
                        oldDist = newDist;
                        isOk = false;
                        EventBus.getDefault().post(new touchEvent("magnify"));
                    }
                    if (newDist < oldDist - 1) {
                        oldDist = newDist;
                        isOk = false;
                        EventBus.getDefault().post(new touchEvent("shrink"));
                    }
                }
                if( mode < 2) {
                    mov_y = event.getY() - point_y;
                    if (!isFull && Math.abs(mov_y) > 10 && Math.abs(mov_y) - Math.abs(event.getX() - point_x) > 0) {
                        touchType = 1;
                        left = cardView.getLeft();
                        right = cardView.getRight();
                        top = cardView.getTop();
                        bottom = cardView.getBottom();
                        if (Math.abs(mainFrag.getResources().getDisplayMetrics().widthPixels - cardView.getWidth()) > 5) {
                            cardView.layout(left, top + (int) mov_y, right, bottom + (int) mov_y);
                            EventBus.getDefault().post(new showDelImg(0));
                            int[] position = {left, top + (int) mov_y, right, bottom + (int) mov_y};
                            positionlist.add(position);
                            if (position[1] < 0) {
                                int alpha = (int) ((1 - (-(float) position[1]) / (cardView.getWidth() / 2)) * 255);
                                mainFrag.setAlpha(alpha > 40 && alpha <= 255 ? alpha : 40);
                            }
                            if (0 - position[1] > cardView.getWidth() / 2) {
                                EventBus.getDefault().post(new showDelImg(1));
                            } else {
                                EventBus.getDefault().post(new showDelImg(2));
                            }
                        }
                    }
                }
                break;
        }
        return true;
    }


    private int spacing(MotionEvent event) {
        int x = (int)(event.getX(0) - event.getX(1));
        int y = (int)(event.getY(0) - event.getY(1));
        return (int)Math.sqrt(x * x + y * y);
    }
}
