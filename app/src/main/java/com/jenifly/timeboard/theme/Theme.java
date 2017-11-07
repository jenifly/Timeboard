package com.jenifly.timeboard.theme;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.jenifly.timeboard.cache.Cache;
import com.jenifly.timeboard.info.BaseInfo;
import com.jenifly.timeboard.utils.ColorUtils;

/**
 * Created by Jenifly on 2017/7/1.
 */

public class Theme {
    public Theme(Context context) {
        RefeshColor(context);
    }

    public Theme(Drawable drawable) {
        RefeshColor(drawable);
    }

    public Theme(int BGCOLOR, int BGCOLOR_LIGHT, int BGCOLOR_DARK, int BGCOLOR_PRESS) {
        this.BGCOLOR = BGCOLOR;
        this.BGCOLOR_LIGHT = BGCOLOR_LIGHT;
        this.BGCOLOR_DARK = BGCOLOR_DARK;
        this.BGCOLOR_PRESS = BGCOLOR_PRESS;
    }

    public int getBGCOLOR() {
        return BGCOLOR;
    }

    public int getBGCOLOR_LIGHT() {
        return BGCOLOR_LIGHT;
    }

    public int getBGCOLOR_DARK() {
        return BGCOLOR_DARK;
    }

    public int getBGCOLOR_PRESS() {
        return BGCOLOR_PRESS;
    }

    private int BGCOLOR = Color.parseColor("#666666");
    private int BGCOLOR_LIGHT;
    private int BGCOLOR_DARK;
    private int BGCOLOR_PRESS;

    public void RefeshColor(int BGCOLOR_LIGHT, int BGCOLOR_DARK, int BGCOLOR_PRESS){
        this.BGCOLOR_LIGHT = BGCOLOR_LIGHT;
        this.BGCOLOR_DARK = BGCOLOR_DARK;
        this.BGCOLOR_PRESS = BGCOLOR_PRESS;
    }

    public void RefeshColor(Context context){
        RefeshColor(context.getResources().getDrawable(Integer.parseInt(Cache.baseInfo.getbBg())));
    }

    public void RefeshColor(){
        BGCOLOR_LIGHT = Cache.baseInfo.getBGCOLOR_LIGHT();
        BGCOLOR =  Cache.baseInfo.getBGCOLOR();
        BGCOLOR_DARK =  Cache.baseInfo.getBGCOLOR_DARK();
        BGCOLOR_PRESS =  Cache.baseInfo.getBGCOLOR_PRESS();
    }

    public void RefeshColor(Drawable drawable){
        Palette.Builder bd = new Palette.Builder(((BitmapDrawable)drawable.mutate()).getBitmap());
        Palette palette = bd.generate();
        BGCOLOR_LIGHT = palette.getLightMutedColor(Color.RED);
        BGCOLOR = palette.getMutedColor(Color.RED);
        BGCOLOR_DARK = palette.getDarkMutedColor(Color.parseColor("#222222"));
        BGCOLOR_PRESS = ColorUtils.getColorWithAlpha(150,BGCOLOR_LIGHT);
    }
}
