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
import com.jenifly.timeboard.theme.Theme;
import com.jenifly.timeboard.view.JButton;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Jenifly on 2017/3/11.
 */

public class BgMorePopWindow {

    @BindView(R.id.title) TextView title;
    @BindView(R.id.bg_intnet)
    JButton mbtn1;
    @BindView(R.id.bg_local)
    JButton mbtn2;
    @BindView(R.id.bg_tackphoto)
    JButton mbtn3;
    @BindView(R.id.view1) View view1;

    private PopupWindow mPopupWindow;
    private Builder mBuilder;

    static public class Builder{
        private Activity mActivity;
        private boolean isOutsideTouchable;
        private OnBGMIListener onBGMIListener;
        private OnBGMLListener onBGMLListener;

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

        public Builder setOnBGMIListener(OnBGMIListener onBGMIListener){
            this.onBGMIListener = onBGMIListener;
            return this;
        }

        public Builder setOnBGMLListener(OnBGMLListener onBGMLListener){
            this.onBGMLListener = onBGMLListener;
            return this;
        }

        public BgMorePopWindow build(){
            return new BgMorePopWindow(this);
        }


    }
    private BgMorePopWindow(final Builder builder){
        mBuilder = builder;
        //init PopWindow
        View popview = View.inflate(builder.mActivity, R.layout.bg_more_popwindow, null);
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

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

    }

    private void initViews(final View root) {
        ButterKnife.bind(this,root);
        title.setTextColor( Cache.theme.getBGCOLOR_DARK());
        mbtn1.setTextColor( Cache.theme.getBGCOLOR_DARK());
        mbtn2.setTextColor( Cache.theme.getBGCOLOR_DARK());
        mbtn3.setTextColor( Cache.theme.getBGCOLOR_DARK());
        mbtn1.setBackColor(0,  Cache.theme.getBGCOLOR_LIGHT());
        mbtn2.setBackColor(0,  Cache.theme.getBGCOLOR_LIGHT());
        mbtn3.setBackColor(0,  Cache.theme.getBGCOLOR_LIGHT());
        mbtn1.setFillet(true, 24);
        mbtn2.setFillet(true, 24);
        mbtn3.setFillet(true, 24);
        mbtn1.setBorder(true, 2 , Cache.theme.getBGCOLOR_LIGHT(), 0);
        mbtn2.setBorder(true, 2 , Cache.theme.getBGCOLOR_LIGHT(), 0);
        mbtn3.setBorder(true, 2 , Cache.theme.getBGCOLOR_LIGHT(), 0);
        view1.setBackgroundColor( Cache.theme.getBGCOLOR_LIGHT());
        mbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBuilder.onBGMIListener !=null)
                    mBuilder.onBGMIListener.BGMI();
                dismiss();
            }
        });
        mbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBuilder.onBGMLListener !=null)
                    mBuilder.onBGMLListener.BGML();
                dismiss();
            }
        });
        mbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });
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

    public interface OnBGMIListener {
        void BGMI();
    }

    public interface OnBGMLListener {
        void BGML();
    }
}
