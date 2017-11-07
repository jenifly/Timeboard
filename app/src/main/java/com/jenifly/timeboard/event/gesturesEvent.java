package com.jenifly.timeboard.event;

/**
 * Created by Jenifly on 2017/6/23.
 */
public class gesturesEvent extends baseEvent{

//用于传递 fragment的event
    int event;

    public gesturesEvent(int event) {
        this.event = event;
    }

    public int getgesturesTag() {
        return event;
    }
}
