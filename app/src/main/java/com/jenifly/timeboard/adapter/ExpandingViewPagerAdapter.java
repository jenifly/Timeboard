package com.jenifly.timeboard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.jenifly.timeboard.fragment.ExpandingFragment;
import com.jenifly.timeboard.info.BGInfo;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 21/06/2016.
 */
public class ExpandingViewPagerAdapter extends FragmentStatePagerAdapter {

    WeakReference<Fragment> currentFragmentReference;
    List<BGInfo> travels;

    public ExpandingViewPagerAdapter(FragmentManager fm) {
        super(fm);
        travels = new ArrayList<>();

    }

    public void addAll(List<BGInfo> travels){
        this.travels.addAll(travels);
        notifyDataSetChanged();
    }

    public Fragment getCurrentFragment() {
        if(currentFragmentReference != null){
            return currentFragmentReference.get();
        }
        return null;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object && object instanceof Fragment) {
            currentFragmentReference = new WeakReference<>((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        BGInfo travel = travels.get(position);
        return ExpandingFragment.newInstance(travel);
    }

    @Override
    public int getCount() {
        return travels.size();
    }
}
