package com.jenifly.timeboard.info;

/**
 * Created by Jenifly on 2017/7/2.
 */

public class BaseInfo {

    public static final String ID = "_id";
    public static final String ISMAIN = "_ismain";
    public static final String TBID = "_tbid";
    public static final String TYPR = "_type";
    public static final String TIMESTAMP = "_timestamp";
    public static final String TEXT1 = "_text1";
    public static final String TEXT2 = "_text2";
    public static final String TEXT3 = "_text3";
    public static final String TBGB = "_tbgb";
    public static final String BACK_BG = "_back_bg";
    public static final String BGCOLOR = "_bgcolor";
    public static final String BGCOLOR_LIGHT = "_bgcolor_light";
    public static final String BGCOLOR_DARK = "_bgcolor_dark";
    public static final String BGCOLOR_PRESS = "_bgcolor_press";

    public static int TIMING = 0; //顺时
    public static int COUNTDOWN = 1; //倒计时

    private int id;
    private int tbId;
    private int isMain;
    private int type;
    private long timestamp = 0;
    private String text1, text2, text3;
    private String tbgb;
    private int bgcolor;
    private int bgcolor_light;
    private int bgcolor_dark;
    private int bgcolor_press;
    private long back_bg;

    public BaseInfo(int id, int tbId, int isMain, int type, long timestamp, String text1, String text2, String text3, String tbgb, long back_bg, int BGCOLOR, int BGCOLOR_LIGHT, int BGCOLOR_DARK, int BGCOLOR_PRESS) {
        this.id = id;
        this.tbId = tbId;
        this.isMain = isMain;
        this.type = type;
        this.timestamp = timestamp;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.tbgb = tbgb;
        this.back_bg = back_bg;
        this.bgcolor = BGCOLOR;
        this.bgcolor_light = BGCOLOR_LIGHT;
        this.bgcolor_dark = BGCOLOR_DARK;
        this.bgcolor_press = BGCOLOR_PRESS;
    }

    public long getBack_bg() {
        return back_bg;
    }

    public void setBack_bg(long back_bg) {
        this.back_bg = back_bg;
    }

    public BaseInfo(int tbId, int isMain, int type, long timestamp, String text1, String text2, String text3, String tbgb, long back_bg, int BGCOLOR, int BGCOLOR_LIGHT, int BGCOLOR_DARK, int BGCOLOR_PRESS) {
        this.id = id;
        this.tbId = tbId;
        this.isMain = isMain;
        this.type = type;
        this.timestamp = timestamp;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.tbgb = tbgb;
        this.back_bg = back_bg;
        this.bgcolor = BGCOLOR;
        this.bgcolor_light = BGCOLOR_LIGHT;
        this.bgcolor_dark = BGCOLOR_DARK;
        this.bgcolor_press = BGCOLOR_PRESS;
    }

    public static String getID() {
        return ID;
    }

    public int getBGCOLOR() {
        return bgcolor;
    }

    public int getBGCOLOR_LIGHT() {
        return bgcolor_light;
    }

    public int getBGCOLOR_DARK() {
        return bgcolor_dark;
    }

    public int getBGCOLOR_PRESS() {
        return bgcolor_press;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }

    public int getIsMain() {
        return this.isMain;
    }

    public int getTBID(){
        return this.tbId;
    }

    public int getType(){
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public void setTbgb(String tbgb) {
        this.tbgb = tbgb;
    }

    public void setBgcolor_light(int bgcolor_light) {
        this.bgcolor_light = bgcolor_light;
    }

    public static void setTIMING(int TIMING) {
        BaseInfo.TIMING = TIMING;
    }

    public void setBgcolor_dark(int bgcolor_dark) {
        this.bgcolor_dark = bgcolor_dark;
    }

    public void setBgcolor_press(int bgcolor_press) {
        this.bgcolor_press = bgcolor_press;
    }

    public long getTimestamp(){
        return this.timestamp;
    }

    public String getText1(){
        return this.text1;
    }

    public String getText2(){
        return this.text2;
    }

    public String getText3(){
        return this.text3;
    }

    public String getbBg(){
        return this.tbgb;
    }
}
