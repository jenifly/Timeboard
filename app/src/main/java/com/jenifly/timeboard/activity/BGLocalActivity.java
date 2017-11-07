package com.jenifly.timeboard.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jenifly.timeboard.R;
import com.jenifly.timeboard.cache.Cache;
import com.jenifly.timeboard.helper.ActivityHelper;
import com.jenifly.timeboard.theme.Theme;
import com.jenifly.timeboard.utils.BlurBitmapUtils;
import com.jenifly.timeboard.utils.OtherUtlis;
import com.jenifly.timeboard.view.instacropper.InstaCropperView;
import com.jenifly.timeboard.view.popWindow.InformationPopWindow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jenifly on 2017/3/11.
 */

public class BGLocalActivity extends AppCompatActivity {

    @BindView(R.id.bg_more_local_main) LinearLayout linearLayout;
    @BindView(R.id.instacropper) InstaCropperView instaCropperView;
    @BindView(R.id.bg_more_local_warning) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bg_more_local);
        ActivityHelper.initState(this);
        ButterKnife.bind(this);
        initview();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    instaCropperView.setImageUri(data.getData());
                }
                return;
        }
    }
    //0.5625
    private void initview(){
        initState();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
        linearLayout.setBackgroundColor(Cache.theme.getBGCOLOR_LIGHT());
        textView.setTextColor(Cache.theme.getBGCOLOR_DARK());
        instaCropperView.setRatios(0.5625F,0.5F,2F);
    }

    @OnClick(R.id.bg_more_local_back)void back(){
        finish();
    }

    @OnClick(R.id.bg_more_local_ok)void IOK(){
        new InformationPopWindow.Builder(BGLocalActivity.this).setOnOKListener(new InformationPopWindow.OnOKListener(){
            @Override
            public void MOK() {
                instaCropperView.crop(View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.AT_MOST),
                        View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.UNSPECIFIED), new InstaCropperView.BitmapCallback() {

                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
                        if (bitmap == null) {
                            OtherUtlis.ShowInfo(BGLocalActivity.this,"Returned bitmap is null!",textView);
                            return;
                        }

                        File file =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "instaCropper");

                        try {
                            FileOutputStream os = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                            os.flush();
                            os.close();
                        //    Cache.theme.RefeshColor(BGCOLOR_LIGHT, BGCOLOR_DARK, BGCOLOR_PRESS);
                         //   Cache.baseInfo.setBack_bg(Integer.parseInt(MainActivity.saveBitmap(BlurBitmapUtils.getBlurBitmap(getContext(),
                        //            BitmapFactory.decodeResource(getResources(), travel.getImage()), 12))));
                        //    Cache.dataHelper.updateBaseInfo(Cache.baseInfo);
                            OtherUtlis.ShowInfo(BGLocalActivity.this,"截图保存成功！",textView);
                        } catch (IOException e) {
                        }
                    }

                });
            }
        }).build().show(textView);
    }

    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
           // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
