package com.jenifly.timeboard.event;

/**
 * Created by Jenifly on 2017/6/23.
 */
//显示删除按钮
public class showDelImg extends baseEvent {


    private int  isShow;

    public showDelImg(int isShow) {
        this.isShow = isShow;
    }

    public int isShow() {
        return isShow;
    }

}
