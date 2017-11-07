package com.jenifly.timeboard.event;

/**
 * Created by Jenifly on 2017/6/23.
 */
public class fbBTEvent extends baseEvent{

    private int index;

    public int getIndex() {
        return index;
    }

    public fbBTEvent(int index) {
        this.index = index;

    }
}
