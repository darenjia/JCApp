package com.bokun.bkjcb.on_siteinspection.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bokun.bkjcb.on_siteinspection.View.CheckItemView;

import java.util.List;

/**
 * Created by BKJCB on 2017/3/20.
 */

public class FragmentAdapter extends PagerAdapter {


    private List<String> checkItems;
    private Context context;
    private CheckItemView checkItemView;

    public FragmentAdapter(Context context, List checkItems) {
        this.checkItems = checkItems;
        this.context = context;
    }

    @Override
    public int getCount() {
//        LogUtil.logI("count"+checkItems.size());
        return checkItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        View view = View.inflate(context, R.layout.check_detial_view, container);
        checkItemView = new CheckItemView(context, checkItems);
        View view = checkItemView.getCheckItemView(position);
        container.addView(view);
        return view;
    }


}
