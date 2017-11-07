package com.jenifly.timeboard.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jenifly on 2017/7/7.
 */

public class TimeStampUtils {

    public static int[] getTimeStamp(long timestamp){
        long starttime = timestamp;
        long nowtime = new Date().getTime()/1000;
        long lovetime = nowtime - starttime;
        long days = lovetime / (60 * 60 * 24);
        long hours = (lovetime - days * 60 * 60 * 24)/3600;
        long minutes = (lovetime - days * (60 * 60 * 24) - hours * (60 * 60))/60;
        long secend = (lovetime-days*(60 * 60 * 24)-hours*(60 * 60))- minutes*60;
        return new int[]{(int)days, (int)hours, (int)minutes, (int)secend};
    }

    public static long getTimeStamp(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        try {
            Date date = sdr.parse(time);
            return date.getTime()/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
