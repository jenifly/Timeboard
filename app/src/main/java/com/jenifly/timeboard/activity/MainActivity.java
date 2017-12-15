package com.jenifly.timeboard.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import com.jenifly.timeboard.R;
import com.jenifly.timeboard.cache.Cache;
import com.jenifly.timeboard.constance.fragConst;
import com.jenifly.timeboard.data.DataHelper;
import com.jenifly.timeboard.event.baseEvent;
import com.jenifly.timeboard.event.delThisFrag;
import com.jenifly.timeboard.event.deleteFragEvent;
import com.jenifly.timeboard.event.fragEvent;
import com.jenifly.timeboard.event.gesturesEvent;
import com.jenifly.timeboard.event.refeshEvent;
import com.jenifly.timeboard.event.secondEvent;
import com.jenifly.timeboard.event.showDelImg;
import com.jenifly.timeboard.event.slideEvent;
import com.jenifly.timeboard.event.touchEvent;
import com.jenifly.timeboard.event.zoomEvent;
import com.jenifly.timeboard.adapter.fragAdapter;
import com.jenifly.timeboard.fragment.mainFrag;
import com.jenifly.timeboard.helper.ActivityHelper;
import com.jenifly.timeboard.info.BaseInfo;
import com.jenifly.timeboard.info.StmpInfo;
import com.jenifly.timeboard.listener.ZoomListenter;
import com.jenifly.timeboard.theme.Theme;
import com.jenifly.timeboard.utils.BlurBitmapUtils;
import com.jenifly.timeboard.listener.SimpleOnGestureListener;
import com.jenifly.timeboard.utils.OtherUtlis;
import com.jenifly.timeboard.utils.TimeStampUtils;
import com.jenifly.timeboard.utils.ViewSwitchUtils;
import com.jenifly.timeboard.view.popWindow.DateSelectPopWindow;
import com.jenifly.timeboard.view.ViewPagerTag;
import com.jenifly.timeboard.view.multichoicescirclebutton.MultiChoicesCircleButton;
import com.jenifly.timeboard.view.popWindow.TextModificationtPopWindow;
import com.jenifly.timeboard.view.verticalViewPager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.RelativeLayout.*;
import static com.jenifly.timeboard.cache.Cache.baseInfo;
import static com.jenifly.timeboard.cache.Cache.baseInfos;
import static com.jenifly.timeboard.cache.Cache.dataHelper;
import static com.jenifly.timeboard.cache.Cache.theme;

public class MainActivity extends FragmentActivity{

    @BindView(R.id.mainrootrl) RelativeLayout mainrootrl;
    @BindView(R.id.mainviewpage) verticalViewPager mViewPager;
    @BindView(R.id.viewPagerTag) ViewPagerTag viewPagerTag;
    @BindView(R.id.delpage) ImageView delpage;
    @BindView(R.id.addnewpage) ImageView addnewpage;
    @BindView(R.id.returnmain) ImageView returnmain;
    @BindView(R.id.mianpage) ImageView mianpage;
    @BindView(R.id.pagebarlt) LinearLayout pagebarlt;
    @BindView(R.id.llayoutviewpage) RelativeLayout llayoutviewpage;
    @BindView(R.id.mainbackgorund) ImageView mainbackgorund;
    @BindView(R.id.multiChoicesCircleButton) MultiChoicesCircleButton multiChoicesCircleButton;

    private final static int RESUTECODE = 0;
    private fragAdapter fragPagerAdapter;
    private DisplayMetrics dm2;
    private boolean isRunTimer = false;
    private boolean currentIsFull = true;
    private int mainpageindex;
    private int mLastPos = -1;
    private Runnable mBlurRunnable;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            EventBus.getDefault().post(new secondEvent());
            handler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityHelper.initState(this);
        ButterKnife.bind(this);
        requestPermission();
    }

    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESUTECODE);
        }else {
            viewInit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RESUTECODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewInit();
                }
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "您已拒绝为本工具赋予存储限权，位保证本工具正常运行，请为添加限权！", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void Datainit(){
        File folder = new File(Environment.getExternalStorageDirectory(),"Timeboard");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File folder1 = new File(Environment.getExternalStorageDirectory(),"Timeboard/cache");
        if (!folder1.exists()) {
            folder1.mkdir();
        }
        if(dataHelper == null)
            dataHelper = new DataHelper(getBaseContext());
        baseInfos = dataHelper.getBaseInfoList();
        int size = baseInfos.size();
        if(size>0) {
            for (int i = 0; i < size; i++) {
                BaseInfo mbaseInfo = baseInfos.get(i);
                if(mbaseInfo.getIsMain() == 1) {
                    mainpageindex = i;
                    baseInfo = mbaseInfo;
                    theme = new Theme(mbaseInfo.getBGCOLOR(), mbaseInfo.getBGCOLOR_LIGHT(), mbaseInfo.getBGCOLOR_DARK(), mbaseInfo.getBGCOLOR_PRESS());
                    mianpage.setColorFilter(theme.getBGCOLOR_LIGHT());
                }
                else
                    mianpage.clearColorFilter();
                mainFrag tmp = new mainFrag(mbaseInfo);
                fragConst.fraglist.add(tmp);
            }
        }else {
            long backgb = saveBitmap(BlurBitmapUtils.getBlurBitmap(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.bg), 12));
            theme = new Theme(getResources().getDrawable(R.drawable.bg));
            dataHelper.addBaseInfo(new BaseInfo(0,1,0,0,"null","null","null",R.drawable.bg+"",
                   backgb, theme.getBGCOLOR(), theme.getBGCOLOR_LIGHT(), theme.getBGCOLOR_DARK(), theme.getBGCOLOR_PRESS()));
            Datainit();
        }
    }

    public static long saveBitmap(Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            long fileName = System.currentTimeMillis()/100;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            File outputFile = new File(Cache.CachePath, fileName + "");
            fos = new FileOutputStream(outputFile);
            fos.write(byteArray);
            fos.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void viewInit() {
        Datainit();
        fragPagerAdapter = new fragAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(fragPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(mainpageindex);
        EventBus.getDefault().register(this);
        dm2 = getResources().getDisplayMetrics();
        startTimer();
        mainbackgorund.setImageURI(Uri.fromFile(new File(Cache.CachePath + baseInfo.getBack_bg())));
        viewPagerTag.setData(fragConst.fraglist.size(),mViewPager.getCurrentItem()+1);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPagerTag.setData(position+1);
                baseInfo = ((mainFrag)fragPagerAdapter.getItem(position)).getmBaseInfo();
                theme.RefeshColor();
                //   mainbackgorund.setImageURI(Uri.fromFile(new File(Cache.CachePath + baseInfo.getBack_bg())));
                if (baseInfo.getIsMain() == 1)
                    mianpage.setColorFilter(theme.getBGCOLOR_LIGHT());
                else
                    mianpage.clearColorFilter();
                multiChoicesCircleButton.setVisibility(INVISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(delpage.getVisibility() == VISIBLE)
                    delpage.setVisibility(INVISIBLE);
                if(state == mViewPager.SCROLL_STATE_IDLE)
                    notifyBackgroundChange(mViewPager.getCurrentItem());
            }
        });
        List<MultiChoicesCircleButton.Item> buttonItems = new ArrayList<>();
        MultiChoicesCircleButton.Item item1 = new MultiChoicesCircleButton.Item("背景选择", getResources().getDrawable(R.mipmap.ic_pic), 30);
        MultiChoicesCircleButton.Item item2 = new MultiChoicesCircleButton.Item("关于", getResources().getDrawable(R.mipmap.ic_smilingface), 90);
        MultiChoicesCircleButton.Item item3 = new MultiChoicesCircleButton.Item("文字设置", getResources().getDrawable(R.mipmap.ic_text), 150);
        MultiChoicesCircleButton.Item item4 = new MultiChoicesCircleButton.Item("相恋时间",getResources().getDrawable(R.mipmap.ic_time), 30);
        MultiChoicesCircleButton.Item item5 = new MultiChoicesCircleButton.Item("其他设置", getResources().getDrawable(R.mipmap.ic_setting), 150);
        buttonItems.add(item1);
        buttonItems.add(item2);
        buttonItems.add(item3);
        buttonItems.add(item4);
        buttonItems.add(item5);
        multiChoicesCircleButton.setButtonItems(buttonItems);
        multiChoicesCircleButton.setOnSelectedItemListener(new MultiChoicesCircleButton.OnSelectedItemListener() {
            @Override
            public void onSelected(MultiChoicesCircleButton.Item item, int index) {
                switch (index) {
                    case 0:
                        startActivity(new Intent(MainActivity.this,PicsActivity.class));
                        break;
                    case 1:
                      //    new AboutPopWindow.Builder(MainActivity.this).build().show(multiChoicesCircleButton);
                        break;
                    case 2:
                        new TextModificationtPopWindow.Builder(MainActivity.this)
                                .setOnConfirmListener(new TextModificationtPopWindow.OnConfirmListener() {
                                    @Override
                                    public void Confirm(ArrayList<StmpInfo> stmpIfos) {
                                        OtherUtlis.ShowInfo(MainActivity.this,"设置成功！",multiChoicesCircleButton);
                                        Cache.dataHelper.updateBaseInfo(mViewPager.getCurrentItem(),stmpIfos);
                                        baseInfo.setText1(stmpIfos.get(0).value);
                                        baseInfo.setText2(stmpIfos.get(1).value);
                                        baseInfo.setText3(stmpIfos.get(2).value);
                                        baseInfos = dataHelper.getBaseInfoList();
                                        EventBus.getDefault().post(new refeshEvent(BaseInfo.TEXT1));
                                    }
                                }).build().show(multiChoicesCircleButton);
                        break;
                    case 3:
                        new DateSelectPopWindow.Builder(MainActivity.this)
                                .setOnConfirmListener(new DateSelectPopWindow.OnConfirmListener() {
                                    @Override
                                    public void Confirm(String datetime) {
                                        Long timestamp = TimeStampUtils.getTimeStamp(datetime);
                                        Cache.dataHelper.updateBaseInfo(mViewPager.getCurrentItem(),
                                                new StmpInfo(BaseInfo.TIMESTAMP,String.valueOf(timestamp)));
                                        baseInfos = dataHelper.getBaseInfoList();
                                        baseInfo.setTimestamp(timestamp);
                                        OtherUtlis.ShowInfo(MainActivity.this,"设置成功！",multiChoicesCircleButton);
                                        EventBus.getDefault().post(new refeshEvent(BaseInfo.TIMESTAMP));
                                    }
                                }).build().show(multiChoicesCircleButton);
                        break;
                    case 4:
                    //     startActivity(new Intent(MainActivity.this,SettingActivity.class));
                        break;
                }
                multiChoicesCircleButton.setVisibility(View.GONE);
            }
        });
    }

    private void notifyBackgroundChange(final int position) {
        if(mLastPos == position) return;
        mainbackgorund.removeCallbacks(mBlurRunnable);
        mBlurRunnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(Cache.CachePath + baseInfo.getBack_bg()));
                    Log.e("-----", "run: "+baseInfo.getBack_bg() );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ViewSwitchUtils.startSwitchBackgroundAnim(mainbackgorund, BlurBitmapUtils.getBlurBitmap(mainbackgorund.getContext(), bitmap,12));
            }
        };
        mainbackgorund.post(mBlurRunnable);
        mLastPos = mViewPager.getCurrentItem();
    }

    @OnClick({R.id.addnewpage,R.id.mianpage, R.id.delpage,R.id.returnmain}) void bthander(View  view) {
        switch (view.getId()) {
            case R.id.addnewpage:
                addNewPage();
                break;
            case R.id.mianpage:
                if(dataHelper.updateMainKey(baseInfo.getTBID())){
                    Toast.makeText(this, "设置成功成功！", Toast.LENGTH_SHORT).show();
                    mianpage.setColorFilter(theme.getBGCOLOR_LIGHT());
                    baseInfos = dataHelper.getBaseInfoList();
                }
                else
                    Toast.makeText(this, "已经是主页了！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.returnmain:
                zoomchange(true);
                break;
        }
    }

    private void addNewPage() {
        theme.RefeshColor(getResources().getDrawable(R.drawable.bg));
        long backgb = saveBitmap(BlurBitmapUtils.getBlurBitmap(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.bg), 12));
        dataHelper.addBaseInfo(new BaseInfo(mViewPager.getCurrentItem() + 1,0,0,0,"null",
                "null","null",R.drawable.bg+"", backgb, theme.getBGCOLOR(), theme.getBGCOLOR_LIGHT(), theme.getBGCOLOR_DARK(), theme.getBGCOLOR_PRESS()));
        baseInfos = dataHelper.getBaseInfoList();
        mainFrag tmp = new mainFrag(new BaseInfo(mViewPager.getCurrentItem() + 1,0,0,0,"null",
                "null","null",R.drawable.bg+"", backgb, theme.getBGCOLOR(), theme.getBGCOLOR_LIGHT(), theme.getBGCOLOR_DARK(), theme.getBGCOLOR_PRESS()));
        fragConst.fraglist.add(mViewPager.getCurrentItem() + 1, tmp);
        fragPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                zoomchange(true);
            }
        }, 400);
        viewPagerTag.setData(fragConst.fraglist.size(),mViewPager.getCurrentItem() + 1);
    }

    private void removePage(int position) {
        if (position >= 0 && position < fragConst.fraglist.size()) {
            if (fragConst.fraglist.size() <= 1) {
                return;
            }
            dataHelper.delBaseInfo(fragConst.fraglist.get(position).getFragId());
            baseInfos = dataHelper.getBaseInfoList();
            fragConst.fraglist.remove(position);
            viewPagerTag.setData(fragConst.fraglist.size(),mViewPager.getCurrentItem() + 1);
            fragPagerAdapter.notifyDataSetChanged();
        }
    }

    private void zoomchange(boolean isMagnify) {
        multiChoicesCircleButton.setVisibility(INVISIBLE);
        if(!isMagnify && currentIsFull){
            currentIsFull = false;
            mViewPager.setPageMargin(fragConst.page_interval);
            llayoutviewpage.setGravity(CENTER_IN_PARENT);
            fragPagerAdapter.notifyDataSetChanged();
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f);
            ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(mViewPager, pvhX, pvhY);
            scale.setDuration(80);
            scale.start();
            RelativeLayout.LayoutParams Rlparam = new RelativeLayout.LayoutParams(dm2.widthPixels * 8 / 10, dm2.heightPixels * 8 / 10);
            Rlparam.addRule(CENTER_IN_PARENT);
            llayoutviewpage.setLayoutParams(Rlparam);
            if (baseInfo.getIsMain() == 1)
                mianpage.setColorFilter( theme.getBGCOLOR_LIGHT());
            else
                mianpage.clearColorFilter();
            pagebarlt.setVisibility(VISIBLE);
            EventBus.getDefault().post(new zoomEvent(false));
        }
        if(isMagnify && !currentIsFull) {
            currentIsFull = true;
            mViewPager.setPageMargin(0);
            fragPagerAdapter.notifyDataSetChanged();
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1f);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1f);
            ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(mViewPager, pvhX, pvhY);
            scale.setDuration(80);
            scale.start();
            RelativeLayout.LayoutParams Rlparam2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            Rlparam2.addRule(CENTER_IN_PARENT);
            llayoutviewpage.setLayoutParams(Rlparam2);
            pagebarlt.setVisibility(INVISIBLE);
            EventBus.getDefault().post(new zoomEvent(true));
        }
    }


    @Subscribe
    public void onEventMainThread(baseEvent event) {
        if (event instanceof fragEvent) {
            zoomchange(true);
        }
        if (event instanceof slideEvent) {
            switch (((slideEvent) event).getType()) {
                case MotionEvent.ACTION_DOWN:
                    switch (((slideEvent) event).getDirection()) {
                        case "left":
                            int cItem = mViewPager.getCurrentItem();
                            mViewPager.setCurrentItem(cItem > 0 ? cItem - 1 : cItem);
                            break;
                        case "right":
                            int rItem = mViewPager.getCurrentItem();
                            mViewPager.setCurrentItem(rItem < fragConst.fraglist.size() - 1 ? rItem + 1 : rItem);
                            break;
                    }
                    break;
            }
        }
        if (event instanceof deleteFragEvent) {
            int i = 0;
            String Tag = ((deleteFragEvent) event).getFragTag();
            for (mainFrag temp : fragConst.fraglist) {
                if (temp.getFragTag().equals(Tag)) {
                    Handler dohandler = new Handler();
                    final int page = i;
                    dohandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            removePage(page);
                        }
                    }, 80);
                }
                i++;
            }
        }
        if (event instanceof showDelImg) {
            if(fragConst.fraglist.size() > 1)
                switch (((showDelImg) event).isShow()){
                    case 0:
                        delpage.setVisibility(VISIBLE);
                        break;
                    case 1:
                        delpage.setColorFilter(Color.RED);
                        break;
                    case 2:
                        delpage.clearColorFilter();
                        break;
                    case 3:
                        delpage.clearColorFilter();
                        delpage.setVisibility(INVISIBLE);
                        break;
                }
        }
        if (event instanceof delThisFrag) {
            removePage(mViewPager.getCurrentItem());
        }
        if(event instanceof touchEvent){
            switch (((touchEvent) event).getDirection()){
                case "click":
                    if(currentIsFull) {
                        if (multiChoicesCircleButton.getVisibility() == VISIBLE)
                            multiChoicesCircleButton.setVisibility(INVISIBLE);
                        else
                            multiChoicesCircleButton.setVisibility(VISIBLE);
                    }  else
                        zoomchange(true);
                    break;
                case "magnify":
                    zoomchange(true);
                    break;
                case "shrink":
                    zoomchange(false);
                    break;
                case "changeBG":
                    OtherUtlis.ShowInfo(MainActivity.this,"设置成功！",multiChoicesCircleButton);
                    fragConst.fraglist.get(mViewPager.getCurrentItem()).ReBackground();
                    mainbackgorund.setImageURI(Uri.fromFile(new File(Cache.CachePath + baseInfo.getBack_bg())));
                    break;
            }
        }
    }

    private void startTimer(){
        if(!isRunTimer) {
            handler.postDelayed(runnable, 1000);
            isRunTimer = true;
        }
    }

    private void stopTimer(){
        handler.removeCallbacks(runnable);
        isRunTimer = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        fragConst.fraglist.clear();
        fragConst.new_mainfrag_count = 0; //调用次数清0
        stopTimer();
    }

    @Override
    public void onBackPressed() {
        if (!currentIsFull) {
            zoomchange(true);
        }else{
            finish();
        }
    }
}
