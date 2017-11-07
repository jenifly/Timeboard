package com.jenifly.timeboard.view.popWindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jenifly.timeboard.R;
import com.jenifly.timeboard.cache.Cache;
import com.jenifly.timeboard.theme.Theme;
import com.jenifly.timeboard.utils.AnimationUtil;
import com.jenifly.timeboard.view.PickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Jenifly on 2017/3/11.
 */

public class DateSelectPopWindow implements PickerView.onSelectListener {

    @BindView(R.id.cancelBtn) TextView cancelBtn;
    @BindView(R.id.confirmBtn) TextView confirmBtn;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.view1) View view1;
    @BindView(R.id.view2) View view2;
    @BindView(R.id.view3) View view3;
    @BindView(R.id.view4) View view4;
    @BindView(R.id.view5) View view5;
    @BindView(R.id.view6) View view6;
    @BindView(R.id.show_select) TextView showselect;
    @BindView(R.id.year_pv) PickerView year_pv;
    @BindView(R.id.month_pv) PickerView month_pv;
    @BindView(R.id.day_pv) PickerView day_pv;
    @BindView(R.id.hour_pv) PickerView hour_pv;
    @BindView(R.id.minute_pv) PickerView minute_pv;
    @BindView(R.id.second_pv) PickerView second_pv;
    @BindView(R.id.secect_date) LinearLayout secect_date;
    @BindView(R.id.secect_time) LinearLayout secect_time;

    private PopupWindow mPopupWindow;
    private Builder mBuilder;
    private String[] datetime ={"2017","01","01","01","01","01"};
    private Calendar c;
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;
    private int mCurrentHour;
    private int mCurrentMinute;
    private int mCurrentSecond;

    static public class Builder{
        private Activity mActivity;
        private boolean isOutsideTouchable = false;
        private OnConfirmListener onConfirmListener;

        public Builder(Activity mActivity){
            this.mActivity = mActivity;
        }
        
        /**
         * set if can touchable if your finger touch outside
         * @param isOutsideTouchable
         * @return
         */
        public Builder setOutsideTouchable(boolean isOutsideTouchable){
            this.isOutsideTouchable = isOutsideTouchable;
            return this;
        }

        public Builder setOnConfirmListener(OnConfirmListener onConfirmListener){
            this.onConfirmListener = onConfirmListener;
            return this;
        }

        public DateSelectPopWindow build(){
            return new DateSelectPopWindow(this);
        }


    }

    private DateSelectPopWindow(final Builder builder){
        mBuilder = builder;

        //init PopWindow
        View popview = View.inflate(builder.mActivity, R.layout.date_select_popwindow, null);
        mPopupWindow = new PopupWindow(popview, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(builder.isOutsideTouchable);
        initViews(mPopupWindow.getContentView());

        initListener();

    }

    /**
     * init listener
     */
    private void initListener() {
        // change the background's color
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   if(confirmBtn.getText().equals("完成")){
                       if(mBuilder.onConfirmListener != null)
                           mBuilder.onConfirmListener.Confirm(datetime[0]+"-"+datetime[1]+"-"+datetime[2]+"-"+datetime[3]+"-"+datetime[4]+"-"+datetime[5]);
                       dismiss();
                   }else {
                       confirmBtn.setText("完成");
                       cancelBtn.setText("上一步");
                       secect_date.setVisibility(View.GONE);
                       secect_time.setVisibility(View.VISIBLE);
                       secect_date.setAnimation(AnimationUtil.HideViewLeft());
                       secect_time.setAnimation(AnimationUtil.ShowViewRight());
                   }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cancelBtn.getText().equals("取消"))
                    dismiss();
                else {
                    confirmBtn.setText("下一步");
                    cancelBtn.setText("取消");
                    secect_date.setVisibility(View.VISIBLE);
                    secect_time.setVisibility(View.GONE);
                    secect_date.setAnimation(AnimationUtil.ShowViewLeft());
                    secect_time.setAnimation(AnimationUtil.HideViewRight());
                }
            }
        });

    }

    private void initViews(final View root) {
        ButterKnife.bind(this,root);
        setTextColor(cancelBtn,  Cache.theme.getBGCOLOR_LIGHT());
        setTextColor(confirmBtn, Cache.theme.getBGCOLOR_LIGHT());
        setTextColor(title,  Cache.theme.getBGCOLOR_DARK());
        setTextColor(showselect,  Cache.theme.getBGCOLOR_LIGHT());
        view1.setBackgroundColor( Cache.theme.getBGCOLOR_LIGHT());
        view2.setBackgroundColor( Cache.theme.getBGCOLOR_LIGHT());
        view3.setBackgroundColor( Cache.theme.getBGCOLOR_LIGHT());
        view4.setBackgroundColor( Cache.theme.getBGCOLOR_LIGHT());
        view5.setBackgroundColor( Cache.theme.getBGCOLOR_LIGHT());
        view6.setBackgroundColor( Cache.theme.getBGCOLOR_LIGHT());
        LoadData();
        ShowSelect();
        year_pv.setOnSelectListener(this);
        month_pv.setOnSelectListener(this);
        day_pv.setOnSelectListener(this);
        hour_pv.setOnSelectListener(this);
        minute_pv.setOnSelectListener(this);
        second_pv.setOnSelectListener(this);
    }

    @Override
    public void onSelect(View view, String text) {
        switch (view.getId()){
            case R.id.year_pv:
                datetime[0] = text;
                setDate(month_pv,mCurrentYear,mCurrentMonth,datetime[0],datetime[1],12);
                setDateDay();
                setDate(hour_pv,mCurrentDay,mCurrentHour,datetime[2],datetime[3],24);
                setDate(minute_pv,mCurrentHour,mCurrentMinute,datetime[3],datetime[4],60);
                setDate(second_pv,mCurrentMinute,mCurrentSecond,datetime[4],datetime[5],60);
                break;
            case R.id.month_pv:
                datetime[1] = text;
                setDateDay();
                setDate(hour_pv,mCurrentDay,mCurrentHour,datetime[2],datetime[3],24);
                setDate(minute_pv,mCurrentHour,mCurrentMinute,datetime[3],datetime[4],60);
                setDate(second_pv,mCurrentMinute,mCurrentSecond,datetime[4],datetime[5],60);
                break;
            case R.id.day_pv:
                datetime[2] = text;
                setDate(hour_pv,mCurrentDay,mCurrentHour,datetime[2],datetime[3],24);
                setDate(minute_pv,mCurrentHour,mCurrentMinute,datetime[3],datetime[4],60);
                setDate(second_pv,mCurrentMinute,mCurrentSecond,datetime[4],datetime[5],60);
                break;
            case R.id.hour_pv:
                datetime[3] = text;
                setDate(minute_pv,mCurrentHour,mCurrentMinute,datetime[3],datetime[4],60);
                setDate(second_pv,mCurrentMinute,mCurrentSecond,datetime[4],datetime[5],60);
                break;
            case R.id.minute_pv:
                datetime[4] = text;
                setDate(second_pv,mCurrentMinute,mCurrentSecond,datetime[4],datetime[5],60);
                break;
            case R.id.second_pv:
                datetime[5] = text;
                break;
        }
        ShowSelect();
    }

    private void setDateDay(){
        List<String> days = new ArrayList();
        if(mCurrentMonth != Integer.valueOf(datetime[1]) || mCurrentYear != Integer.valueOf(datetime[0])){
            c.set(Calendar.YEAR, Integer.valueOf(datetime[0]));
            c.set(Calendar.MONTH, Integer.valueOf(datetime[1]) - 1);
            int daysCountOfMonth = c.getActualMaximum(Calendar.DATE);
            for (int i = 1; i <= daysCountOfMonth; i++)
            {
                days.add(String.format("%02d", i));
            }
        }else {
            for (int i = 1; i <= mCurrentDay; i++)
            {
                days.add(String.format("%02d", i));
            }
        }
        day_pv.setData(days);
        if(Integer.valueOf(datetime[2]) > mCurrentDay)
            day_pv.setSelected(0);
        else
            day_pv.setSelected(datetime[2]);
    }

    private void ShowSelect(){
        showselect.setText(datetime[0]+"年"+datetime[1]+"月" +datetime[2]+"日  "
                +datetime[3]+"时"+datetime[4]+"分"+datetime[5]+"秒");
    }

    private void setDate(PickerView pv, int mCurrent1, int mCurrent2, String select1, String select2, int lenght){
        List<String> list = new ArrayList();
        int index;
        if(pv.getId() == R.id.month_pv){
            index = 1;
            lenght++;
        }
        else
            index = 0;
        if(mCurrent1 == Integer.valueOf(select1)){
            for (int i = index; i < mCurrent2 + 1; i++)
            {
                list.add(String.format("%02d", i));
            }
            pv.setData(list);
        }else {
            if(pv.getPvSize() != lenght) {
                for (int i = index; i < lenght; i++) {
                    list.add(String.format("%02d", i));
                }
                pv.setData(list);
            }
        }

        if(Integer.valueOf(select2) > mCurrent2)
            pv.setSelected(0);
        else
            pv.setSelected(select2);
    }

    private void LoadData(){
        List<String> years = new ArrayList();
        c = Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("HH-mm-ss");
        String[] time = sdf.format(new Date()).split("-");
        mCurrentYear = c.get(Calendar.YEAR);
        mCurrentMonth = c.get(Calendar.MONTH) + 1;
        mCurrentDay = c.get(Calendar.DAY_OF_MONTH);
        mCurrentHour = Integer.valueOf(time[0]);
        mCurrentMinute = Integer.valueOf(time[1]);
        mCurrentSecond = Integer.valueOf(time[2]);
        datetime[0] = mCurrentYear + "";
        datetime[1] = String.format("%02d", mCurrentMonth);
        datetime[2] = String.format("%02d", mCurrentDay);
        datetime[3] = time[0];
        datetime[4] = time[1];
        datetime[5] = time[2];
        for (int i = 1920; i <= mCurrentYear; i++)
        {
            years.add("" + i);
        }
        setDate(month_pv,mCurrentYear,mCurrentMonth,datetime[0],datetime[1],12);
        setDateDay();
        setDate(hour_pv,mCurrentDay,mCurrentHour,datetime[2],datetime[3],24);
        setDate(minute_pv,mCurrentHour,mCurrentMinute,datetime[3],datetime[4],60);
        setDate(second_pv,mCurrentMinute,mCurrentSecond,datetime[4],datetime[5],60);
        year_pv.setData(years);
        year_pv.setPvName("年");
        month_pv.setPvName("月");
        day_pv.setPvName("日");
        hour_pv.setPvName("时");
        minute_pv.setPvName("分");
        second_pv.setPvName("秒");
        year_pv.setSelected(datetime[0]);
        month_pv.setSelected(datetime[1]);
        day_pv.setSelected(datetime[2]);
        hour_pv.setSelected(datetime[3]);
        minute_pv.setSelected(datetime[4]);
        second_pv.setSelected(datetime[5]);
    }

    private void setText(TextView tv, String str){
        if (tv != null && str != null){
            tv.setText(str);
        }
    }

    private void setTextColor(TextView tv, int color){
        if (tv != null && color != 0){
            tv.setTextColor(color);
        }
    }

    public void dismiss() {
        if (mPopupWindow != null){
            mPopupWindow.dismiss();
        }
    }

    /**
     * parent is the popwindow show location
     * @param parent
     */
    public void show(View parent){
        if (mPopupWindow != null){
            backgroundAlpha(0.8f);
            mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        }
    }
    /**
     * set background alpha
     * @param alpha
     */
    public void backgroundAlpha(float alpha) {
        try {
            WindowManager.LayoutParams lp = mBuilder.mActivity.getWindow().getAttributes();
            lp.alpha = alpha; //0.0-1.0
            mBuilder.mActivity.getWindow().setAttributes(lp);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface OnConfirmListener {
        void Confirm(String datetime);
    }
}
