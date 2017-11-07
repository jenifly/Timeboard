package com.jenifly.timeboard.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jenifly.timeboard.R;
import com.jenifly.timeboard.cache.Cache;
import com.jenifly.timeboard.helper.ActivityHelper;
import com.jenifly.timeboard.helper.EasyTransition;
import com.jenifly.timeboard.view.timelyView.TimerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jenifly on 2017/7/2.
 */

public class PicPreviewActivity extends AppCompatActivity{

    @BindView(R.id.image) ImageView image;
    @BindView(R.id.preview_back) ImageView preview_back;
    @BindView(R.id.preview_main) RelativeLayout preview_main;
    @BindView(R.id.preview_timerview) TimerView preview_timerview;


    private boolean finishEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ActivityHelper.initState(this);
        ButterKnife.bind(this);
        image.setImageDrawable(getResources().getDrawable( Cache.picCurrent));
        long transitionDuration = 600;
        if (null != savedInstanceState)
            transitionDuration = 0;
        finishEnter = false;
        EasyTransition.enter(
                this,
                transitionDuration,
                new DecelerateInterpolator(),
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finishEnter = true;
                        preview_main.setVisibility(View.VISIBLE);
                        preview_back.setVisibility(View.VISIBLE);
                    }
                });
    }

    @OnClick(R.id.preview_back)void click(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (finishEnter) {
            finishEnter = false;
            startBackAnim();
        }
    }

    private void startBackAnim() {
        preview_back.animate()
                .setDuration(200)
                .scaleX(0)
                .scaleY(0);
        preview_main.animate()
                .setDuration(300)
                .alpha(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        EasyTransition.exit(PicPreviewActivity.this, 400, new DecelerateInterpolator());
                    }
                });
    }
}
