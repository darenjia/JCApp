package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.bokun.bkjcb.on_siteinspection.R;

/**
 * Created by DengShuai on 2017/4/7.
 */

public class TestFragment extends MainFragment {

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.expandable_group_item_view, null);
        return view;
    }
}
