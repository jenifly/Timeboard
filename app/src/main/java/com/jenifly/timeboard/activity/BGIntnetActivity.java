package com.jenifly.timeboard.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jenifly.timeboard.R;
import com.jenifly.timeboard.adapter.BGMIRAdapter;
import com.jenifly.timeboard.cache.Cache;
import com.jenifly.timeboard.helper.ActivityHelper;
import com.jenifly.timeboard.theme.Theme;
import com.jenifly.timeboard.view.mRefreshView.PullToRefreshView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jenifly on 2017/3/11.
 */

public class BGIntnetActivity extends AppCompatActivity {

    @BindView(R.id.bg_more_intent_main) LinearLayout linearLayout;
    @BindView(R.id.bg_more_intnet_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_to_refresh) PullToRefreshView mPullToRefreshView;

    public static final int REFRESH_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bg_more_intnet);
        ActivityHelper.initState(this);
        ButterKnife.bind(this);
        initview();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        BGMIRAdapter bgmirAdapter = new BGMIRAdapter();
        recyclerView.setAdapter(bgmirAdapter);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });
        bgmirAdapter.setOnSelectListener(new BGMIRAdapter.onSelectListener() {
            @Override
            public void onSelect(int position) {
                Toast.makeText(getBaseContext(),position + "-",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initview(){
        initState();
        linearLayout.setBackgroundColor(Cache.theme.getBGCOLOR_LIGHT());
     //   linearLayout.setBackgroundColor(Color.parseColor("#dB2f60"));
    }

    @OnClick(R.id.bg_more_intent_back)void back(){
        finish();
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
