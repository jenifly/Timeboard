package com.jenifly.timeboard.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.jenifly.timeboard.constance.fragConst;


/**
 * Created by Administrator on 2016/8/20.
 */
public class fragAdapter extends FragmentStatePagerAdapter  {

    private Context context;
    private FragmentManager fm;
    public fragAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context = context;
        this.fm=fm;
    }

    @Override
    public Fragment getItem(int position) {
        return fragConst.fraglist.get(position);
    }

    @Override
    public int getCount() {
        return fragConst.fraglist.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }


    @Override
    public int getItemPosition(Object object) {
        //需要实现一种机制避免更新所有的fragment
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

    }
}
