package com.jenifly.timeboard.utils;

import android.app.Activity;
import android.view.View;

import com.jenifly.timeboard.info.BaseInfo;
import com.jenifly.timeboard.view.popWindow.InfoPopWindow;

import java.util.ArrayList;

import static com.jenifly.timeboard.cache.Cache.baseInfos;
import static com.jenifly.timeboard.cache.Cache.dataHelpertbId;

/**
 * Created by Jenifly on 2017/7/7.
 */

public class OtherUtlis {

    public static void ShowInfo(Activity activity, String info, View view){
        new InfoPopWindow.Builder(activity,info).build().show(view);
    }

    public static void refeshNewtbId(){
        for(BaseInfo baseInfo:baseInfos){
            if(baseInfo.getTBID() >= dataHelpertbId){
                dataHelpertbId = baseInfo.getTBID() + 1;
            }
        }
    }
}
