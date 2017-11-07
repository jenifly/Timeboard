package com.jenifly.timeboard.view.expandingPageView;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.jenifly.timeboard.adapter.ExpandingViewPagerAdapter;
import com.jenifly.timeboard.fragment.ExpandingFragment;


/**
 * Created by Qs on 16/6/20.
 */
public class ExpandingPagerFactory {


    public static ExpandingFragment getCurrentFragment(ViewPager viewPager){
        if (viewPager.getAdapter() instanceof ExpandingViewPagerAdapter) {
            ExpandingViewPagerAdapter adapter = (ExpandingViewPagerAdapter) viewPager.getAdapter();
            Fragment fragment = adapter.getCurrentFragment();
            if (fragment instanceof ExpandingFragment) {
                return (ExpandingFragment)fragment;
            }
        }
        return null;
    }

    public static void setupViewPager(final ViewPager viewPager,float index) {
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        layoutParams.width = (int)(((Activity) viewPager.getContext()).getWindowManager().getDefaultDisplay().getWidth() * index);
        layoutParams.height = (int)(((Activity) viewPager.getContext()).getWindowManager().getDefaultDisplay().getHeight() * index);;

        viewPager.setOffscreenPageLimit(2);

        if (viewPager.getParent() instanceof ViewGroup) {
            ViewGroup viewParent = ((ViewGroup) viewPager.getParent());
            viewParent.setClipChildren(false);
            viewPager.setClipChildren(false);
        }

        viewPager.setPageTransformer(true, new ExpandingViewPagerTransformer());

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                ExpandingFragment expandingFragment = getCurrentFragment(viewPager);
//                if(expandingFragment != null && expandingFragment.isOpenend()){
//                    expandingFragment.close();
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    public static boolean onBackPressed(ViewPager viewPager) {
        ExpandingFragment expandingFragment = getCurrentFragment(viewPager);
        if(expandingFragment != null && expandingFragment.isOpenend()){
            expandingFragment.close();
            return true;
        }
        return false;
    }
}
