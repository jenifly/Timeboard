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

public class InformationPopWindow {

    @BindView(R.id.title) TextView title;
    @BindView(R.id.information) TextView information;
    @BindView(R.id.mok)
    JButton mbtn1;
    @BindView(R.id.cancel)
    JButton mbtn2;
    @BindView(R.id.view1) View view1;
    @BindView(R.id.view2) View view2;
    @BindView(R.id.view3) View view3;

    private PopupWindow mPopupWindow;
    private Builder mBuilder;

    static public class Builder{
        private Activity mActivity;
        private boolean isOutsideTouchable;
        private OnOKListener onOKListener;

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

        public Builder setOnOKListener(OnOKListener onOKListener){
            this.onOKListener = onOKListener;
            return this;
        }

        public InformationPopWindow build(){
            return new InformationPopWindow(this);
        }


    }
    private InformationPopWindow(final Builder builder){
        mBuilder = builder;
        //init PopWindow
        View popview = View.inflate(builder.mActivity, R.layout.information_popwindow, null);
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
        ButterKnife.bind(this, root);
        title.setTextColor(Cache.theme.getBGCOLOR_DARK());
        mbtn1.setTextColor(Cache.theme.getBGCOLOR_DARK());
        mbtn2.setTextColor(Cache.theme.getBGCOLOR_DARK());
        information.setTextColor(Cache.theme.getBGCOLOR_DARK());
        mbtn1.setBackColor(0, Cache.theme.getBGCOLOR_LIGHT());
        mbtn2.setBackColor(0, Cache.theme.getBGCOLOR_LIGHT());
        mbtn1.setCornerRadii(new float[]{0,0,0,0,24,24,0,0});
        mbtn2.setCornerRadii(new float[]{0,0,0,0,0,0,24,24});
        view1.setBackgroundColor(Cache.theme.getBGCOLOR_LIGHT());
        view2.setBackgroundColor(Cache.theme.getBGCOLOR_LIGHT());
        view3.setBackgroundColor(Cache.theme.getBGCOLOR_LIGHT());
        mbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBuilder.onOKListener != null)
                    mBuilder.onOKListener.MOK();
                dismiss();
            }
        });
        mbtn2.setOnClickListener(new View.OnClickListener() {
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

    public interface OnOKListener {
        void MOK();
    }
}
