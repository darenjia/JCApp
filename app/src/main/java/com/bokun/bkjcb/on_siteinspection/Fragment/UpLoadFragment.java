package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.bokun.bkjcb.on_siteinspection.R;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/4/17.
 */

public class UpLoadFragment extends MainFragment {
    String[] tags = {"上传任务", "已完成"};
    TabLayout mTabLayout;
    ViewPager mViewPager;
    ArrayList<Fragment> fragments;

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.upload_view, null);
        mTabLayout = (TabLayout) view.findViewById(R.id.upload_tablayout);
        mViewPager = (ViewPager) view.findViewById(R.id.upload_viewpager);
        return view;
    }

    @Override
    public void initData() {
        fragments = new ArrayList<>();
        UpLoadChirldFragment unFinished = new UpLoadChirldFragment();
        unFinished.setFinished(false);
        fragments.add(unFinished);
        UpLoadChirldFragment finished = new UpLoadChirldFragment();
        finished.setFinished(true);
        fragments.add(finished);
        PagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tags[position];
        }
    }
}
