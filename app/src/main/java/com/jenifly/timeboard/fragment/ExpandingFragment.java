package com.jenifly.timeboard.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jenifly.timeboard.R;
import com.jenifly.timeboard.activity.MainActivity;
import com.jenifly.timeboard.cache.Cache;
import com.jenifly.timeboard.event.baseEvent;
import com.jenifly.timeboard.event.previewEvent;
import com.jenifly.timeboard.event.touchEvent;
import com.jenifly.timeboard.info.BGInfo;
import com.jenifly.timeboard.info.BaseInfo;
import com.jenifly.timeboard.info.StmpInfo;
import com.jenifly.timeboard.theme.Theme;
import com.jenifly.timeboard.utils.BlurBitmapUtils;
import com.jenifly.timeboard.utils.ColorUtils;
import com.jenifly.timeboard.view.JButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandingFragment extends Fragment{

    @BindView(R.id.back)CardView back;
    @BindView(R.id.front)CardView front;
    @BindView(R.id.bottomLayout)LinearLayout bottomLayout;
    @BindView(R.id.image)ImageView image;
    @BindView(R.id.title)TextView title;
    @BindView(R.id.fb_preview) JButton fb_preview;
    @BindView(R.id.fb_use) JButton fb_use;

    private static final float SCALE_OPENED = 1.2f;
    private static final int SCALE_CLOSED = 1;
    private static final int ELEVATION_OPENED = 40;
    private static final String ARG_TRAVEL = "ARG_TRAVEL";
    private BGInfo travel;
    private int BGCOLOR_LIGHT;
    private int BGCOLOR_PRESS;
    private int BGCOLOR_DARK;
    private float startY;

    float defaultCardElevation;
    private OnExpandingClickListener mListener;
    private ObjectAnimator frontAnimator;
    private ObjectAnimator backAnimator;

    public static ExpandingFragment newInstance(BGInfo travel){
        ExpandingFragment fragment = new ExpandingFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TRAVEL, travel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            travel = args.getParcelable(ARG_TRAVEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expanding_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        defaultCardElevation = front.getCardElevation();
        init();
    }

    private void init(){
        if (travel != null) {
            image.setImageResource(travel.getImage());
            title.setText(travel.getName());
        }
        setupDownGesture(image);
        Palette.Builder bd = new Palette.Builder(((BitmapDrawable)getResources().getDrawable(travel.getImage())).getBitmap());
        Palette palette = bd.generate();
        BGCOLOR_LIGHT = palette.getLightMutedColor(Color.parseColor("#5CADAD"));
        BGCOLOR_PRESS = ColorUtils.getColorWithAlpha(150,BGCOLOR_LIGHT);
        BGCOLOR_DARK = palette.getDarkMutedColor(Color.parseColor("#222222"));
        fb_preview.setBackColor( BGCOLOR_LIGHT, BGCOLOR_PRESS);
        fb_use.setBackColor( BGCOLOR_LIGHT, BGCOLOR_PRESS);
    }

    @butterknife.OnClick({R.id.image,R.id.fb_preview,R.id.fb_use})void click(View v){
        switch (v.getId()){
            case R.id.image:
                if (!isOpenend())
                    open();
                break;
            case R.id.fb_preview:
                if (mListener != null)
                    mListener.onExpandingClick(image);
                break;
            case R.id.fb_use:
                Cache.baseInfo.setTbgb(travel.getImage()+"");
                Cache.baseInfo.setBgcolor_dark(BGCOLOR_DARK);
                Cache.baseInfo.setBgcolor_light(BGCOLOR_LIGHT);
                Cache.baseInfo.setBgcolor_press(BGCOLOR_PRESS);
                Cache.theme.RefeshColor(BGCOLOR_LIGHT, BGCOLOR_DARK, BGCOLOR_PRESS);
                Cache.baseInfo.setBack_bg(MainActivity.saveBitmap(BlurBitmapUtils.getBlurBitmap(getContext(),
                        BitmapFactory.decodeResource(getResources(), travel.getImage()), 12)));
                Cache.dataHelper.updateBaseInfo(Cache.baseInfo);
                EventBus.getDefault().post(new touchEvent("changeBG"));
                break;
        }
    }

    private void setupDownGesture(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float my = 0;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        my = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isOpenend() && event.getY() - startY > 0) {
                            close();
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExpandingClickListener) {
            mListener = (OnExpandingClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "ExpandingFragment must implement OnExpandingClickListener");
        }
    }

    public boolean isClosed() {
        return ViewCompat.getScaleX(back) == SCALE_CLOSED;
    }

    public boolean isOpenend() {
        return ViewCompat.getScaleX(back) == SCALE_OPENED;
    }

    public void toggle() {
        if (isClosed()) {
            open();
        } else {
            close();
        }
    }

    public void open() {
        ViewGroup.LayoutParams layoutParams = bottomLayout.getLayoutParams();
        layoutParams.height = front.getHeight() / 8 ;
        bottomLayout.setLayoutParams(layoutParams);
        ViewCompat.setPivotY(back, 0);

        PropertyValuesHolder front1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, -front.getHeight() / 8);
        PropertyValuesHolder front2 = PropertyValuesHolder.ofFloat(View.SCALE_X, 1, 1);
        frontAnimator = ObjectAnimator.ofPropertyValuesHolder(front, front1, front2);
        PropertyValuesHolder backX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f);
        PropertyValuesHolder backY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.06f);
        backAnimator = ObjectAnimator.ofPropertyValuesHolder(back, backX, backY);
        back.setPivotY(0);
        frontAnimator.start();
        backAnimator.start();

        front.setCardElevation(ELEVATION_OPENED);
    }

    public void close() {
        if (frontAnimator != null) {
            frontAnimator.reverse();
            backAnimator.reverse();
            backAnimator = null;
            frontAnimator = null;
        }
        front.setCardElevation(defaultCardElevation);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public interface OnExpandingClickListener {
        void onExpandingClick(View v);
    }

    @Subscribe
    public void onEventMainThread(baseEvent event) {
        if (event instanceof previewEvent) {
            if (mListener != null) {
                mListener.onExpandingClick(getView());
            }
        }
    }

    /**
     * Temporarily not used
     */
    interface Child {
        void onAttachedToExpanding(ExpandingFragment expandingFragment);

        void onDetachedToExpanding();
    }

    public interface ChildTop extends ExpandingFragment.Child {
    }

    public interface ChildBottom extends ExpandingFragment.Child {
    }
}
