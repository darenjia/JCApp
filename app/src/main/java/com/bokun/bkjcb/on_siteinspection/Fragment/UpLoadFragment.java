package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.bokun.bkjcb.on_siteinspection.Activity.MainActivity;
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

    public interface OnDataChangeListener {
        void onDateChange();
    }

    private OnDataChangeListener listener;

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
        unFinished.setListenter(listener);
        fragments.add(unFinished);
        UpLoadChirldFragment finished = new UpLoadChirldFragment();
        finished.setFinished(true);
        fragments.add(finished);
        final PagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    ((UpLoadChirldFragment) fragments.get(position)).refresh("上传完成");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getContext()).showMenu(false);
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

    public void setListener(OnDataChangeListener listener) {
        this.listener = listener;
    }

    public void refresh() {
        ((UpLoadChirldFragment) fragments.get(0)).refresh("等待上传");
    }
}
