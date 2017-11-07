package com.jenifly.timeboard.cache;

import android.os.Environment;

import com.jenifly.timeboard.data.DataHelper;
import com.jenifly.timeboard.info.BaseInfo;
import com.jenifly.timeboard.theme.Theme;

import java.util.ArrayList;

/**
 * Created by Jenifly on 2017/7/7.
 */

public class Cache {
    public static DataHelper dataHelper;
    public static int dataHelpertbId = 0;
    public static ArrayList<BaseInfo> baseInfos;
    public static BaseInfo baseInfo;
    public static int picCurrent;
    public static Theme theme;
    public static String CachePath = Environment.getExternalStorageDirectory() + "/Timeboard/cache/";
}
