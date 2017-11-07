package com.jenifly.timeboard.event;

/**
 * Created by Jenifly on 2017/6/23.
 */
public class refeshEvent extends baseEvent{

    private String type;

    public String getType() {
        return type;
    }

    public refeshEvent(String type) {
        this.type = type;
    }
}
