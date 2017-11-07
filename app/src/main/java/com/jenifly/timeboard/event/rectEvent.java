package com.jenifly.timeboard.event;

/**
 * Created by Jenifly on 2017/7/9.
 */

public class rectEvent extends baseEvent{

    private int width,height,_X,_Y;

    public rectEvent(int width, int height, int _X, int _Y) {
        this.width = width;
        this.height = height;
        this._X = _X;
        this._Y = _Y;
    }
}
