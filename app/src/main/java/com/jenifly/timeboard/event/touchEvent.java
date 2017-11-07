package com.jenifly.timeboard.event;

/**
 * Created by Jenifly on 2017/10/31.
 */

public class touchEvent extends baseEvent{
    private String direction;//点击位置 或者 滑动方向

    public touchEvent(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
}
