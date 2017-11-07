package com.jenifly.timeboard.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jenifly.timeboard.R;

import com.jenifly.timeboard.adapter.ExpandingViewPagerAdapter;
import com.jenifly.timeboard.cache.Cache;
import com.jenifly.timeboard.event.baseEvent;
import com.jenifly.timeboard.event.fbBTEvent;
import com.jenifly.timeboard.event.touchEvent;
import com.jenifly.timeboard.fragment.ExpandingFragment;
import com.jenifly.timeboard.helper.ActivityHelper;
import com.jenifly.timeboard.helper.EasyTransition;
import com.jenifly.timeboard.helper.EasyTransitionOptions;
import com.jenifly.timeboard.utils.BlurBitmapUtils;
import com.jenifly.timeboard.info.BGInfo;
import com.jenifly.timeboard.utils.ViewSwitchUtils;
import com.jenifly.timeboard.view.expandingPageView.ExpandingPagerFactory;
import com.jenifly.timeboard.view.popWindow.BgMorePopWindow;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jenifly on 2017/3/11.
 */

public class PicsActivity extends AppCompatActivity implements ExpandingFragment.OnExpandingClickListener{

    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.blurView) ImageView mBlurView;
    @BindView(R.id.pics_index) TextView pics_index;

    private Runnable mBlurRunnable;
    private  List<BGInfo> travelUtilses;
    private int mLastPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pics_main);
        ButterKnife.bind(this);
        initview();
    }

    private void initview(){
        ActivityHelper.initState(this);
        EventBus.getDefault().register(this);
        travelUtilses = generateTravelList();
        setupWindowAnimations();
        ExpandingViewPagerAdapter adapter = new ExpandingViewPagerAdapter(getSupportFragmentManager());
        adapter.addAll(travelUtilses);
        viewPager.setAdapter(adapter);
        pics_index.setText("1/" + travelUtilses.size());
        notifyBackgroundChange(0);
        ExpandingPagerFactory.setupViewPager(viewPager, 0.7f);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ExpandingFragment expandingFragment = ExpandingPagerFactory.getCurrentFragment(viewPager);
                if(expandingFragment != null && !expandingFragment.isClosed()){
                    expandingFragment.close();
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == viewPager.SCROLL_STATE_IDLE)
                    notifyBackgroundChange(viewPager.getCurrentItem());
            }
        });
    }

    @Override
    public void onExpandingClick(View v) {
        Cache.picCurrent = generateTravelList().get(viewPager.getCurrentItem()).getImage();
        Intent intent = new Intent(PicsActivity.this, PicPreviewActivity.class);
        EasyTransition.startActivity(intent, EasyTransitionOptions.makeTransitionOptions(PicsActivity.this, v));
    }

    @OnClick({R.id.pics_back,R.id.bg_more})void click(View view){
        switch (view.getId()){
            case R.id.pics_back:
                finish();
                break;
            case R.id.bg_more:
                new BgMorePopWindow.Builder(PicsActivity.this).setOnBGMIListener(new BgMorePopWindow.OnBGMIListener() {
                    @Override
                    public void BGMI() {
                        startActivity(new Intent(PicsActivity.this,BGIntnetActivity.class));
                    }
                }).setOnBGMLListener(new BgMorePopWindow.OnBGMLListener() {
                    @Override
                    public void BGML() {
                        startActivity(new Intent(PicsActivity.this,BGLocalActivity.class));
                    }
                }).build().show(pics_index);
                break;
        }
    }

    private List<BGInfo> generateTravelList(){
        List<BGInfo> travels = new ArrayList<>();
        travels.add(new BGInfo("", R.drawable.bg));
        travels.add(new BGInfo("", R.drawable.seychelles));
        travels.add(new BGInfo("", R.drawable.shh));
        travels.add(new BGInfo("", R.drawable.newyork));
        travels.add(new BGInfo("", R.drawable.p1));
        return travels;
    }

    private void notifyBackgroundChange(final int position) {
        if(mLastPos == position) return;
        final int resId = travelUtilses.get(position).getImage();
        mBlurView.removeCallbacks(mBlurRunnable);
        mBlurRunnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), bitmap,12));
                pics_index.setText((position+1)+"/" + travelUtilses.size());
            }
        };
        mBlurView.post(mBlurRunnable);
        mLastPos = viewPager.getCurrentItem();
    }

    @Override
    public void onBackPressed() {
        if(!ExpandingPagerFactory.onBackPressed(viewPager)){
            super.onBackPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Explode slideTransition = new Explode();
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    @Subscribe
    public void onEventMainThread(baseEvent event) {
        if (event instanceof fbBTEvent) {
            switch (((fbBTEvent) event).getIndex()) {
                case 1:

                    break;
                case 2:

                    break;
            }
        }
        switch (((touchEvent)event).getDirection()){
            case "changeBG":
                this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
