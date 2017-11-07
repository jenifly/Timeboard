package com.jenifly.timeboard.view.popWindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jenifly.timeboard.R;
import com.jenifly.timeboard.cache.Cache;
import com.jenifly.timeboard.info.BaseInfo;
import com.jenifly.timeboard.info.StmpInfo;
import com.jenifly.timeboard.theme.Theme;
import com.jenifly.timeboard.view.JEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jenifly.timeboard.cache.Cache.baseInfo;


/**
 * Created by Jenifly on 2017/3/11.
 */

public class TextModificationtPopWindow {

    @BindView(R.id.cancelBtn) TextView cancelBtn;
    @BindView(R.id.confirmBtn) TextView confirmBtn;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.text1) TextView text1;
    @BindView(R.id.text2) TextView text2;
    @BindView(R.id.text3) TextView text3;
    @BindView(R.id.view1) View view1;
    @BindView(R.id.text_modification1)
    JEditText text_modification1;
    @BindView(R.id.text_modification2)
    JEditText text_modification2;
    @BindView(R.id.text_modification3)
    JEditText text_modification3;

    private PopupWindow mPopupWindow;
    private Builder mBuilder;


    static public class Builder{
        private Activity mActivity;
        private boolean isOutsideTouchable = false;
        private OnConfirmListener onConfirmListener;

        public Builder(Activity mActivity){
            this.mActivity = mActivity;
        }

        public Builder setOnConfirmListener(OnConfirmListener onConfirmListener){
            this.onConfirmListener = onConfirmListener;
            return this;
        }

        public TextModificationtPopWindow build(){
            return new TextModificationtPopWindow(this);
        }

    }

    private TextModificationtPopWindow(final Builder builder){
        mBuilder = builder;
        //init PopWindow
        View popview = View.inflate(builder.mActivity, R.layout.text_modification_popwindow, null);
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
                ArrayList<StmpInfo> stmpInfos = new ArrayList<>();
                if(text_modification1.getText()+""=="")
                    baseInfo.setText1("null");
                else
                    stmpInfos.add(new StmpInfo(BaseInfo.TEXT1,text_modification1.getText()+""));
                if(text_modification2.getText()+""=="")
                    baseInfo.setText2("null");
                else
                    stmpInfos.add(new StmpInfo(BaseInfo.TEXT2,text_modification2.getText()+""));
                if(text_modification3.getText()+""=="")
                    baseInfo.setText3("null");
                else
                    stmpInfos.add(new StmpInfo(BaseInfo.TEXT3,text_modification3.getText()+""));
                if (mBuilder.onConfirmListener !=null)
                    mBuilder.onConfirmListener.Confirm(stmpInfos);
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void initViews(final View root) {
        ButterKnife.bind(this,root);
        setTextColor(cancelBtn, Cache.theme.getBGCOLOR());
        setTextColor(confirmBtn,Cache.theme.getBGCOLOR());
        setTextColor(title,Cache.theme.getBGCOLOR_DARK());
        setTextColor(text1,Cache.theme.getBGCOLOR());
        setTextColor(text2,Cache.theme.getBGCOLOR());
        setTextColor(text3,Cache.theme.getBGCOLOR());
        setText(text_modification1,baseInfo.getText1());
        setText(text_modification2,baseInfo.getText2());
        setText(text_modification3,baseInfo.getText3());
        view1.setBackgroundColor(Cache.theme.getBGCOLOR_LIGHT());
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
        void Confirm(ArrayList<StmpInfo> stmpInfos);
    }
}
