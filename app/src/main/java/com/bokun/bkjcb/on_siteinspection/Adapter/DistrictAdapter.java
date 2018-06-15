package com.bokun.bkjcb.on_siteinspection.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Domain.District;
import com.bokun.bkjcb.on_siteinspection.R;

import java.util.List;

/**
 * Created by DengShuai on 2017/5/27.
 */

public class DistrictAdapter extends BaseAdapter {

    List<District> districts;
    Context context;

    public DistrictAdapter(Context context, List<District> list) {
        this.districts = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return districts.size();
    }

    @Override
    public District getItem(int position) {
        return districts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        District district = districts.get(position);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.district_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.word.setText(district.getName());
        return convertView;
    }

    static class ViewHolder {
        TextView word;

        ViewHolder(View view) {
            word = (TextView) view.findViewById(R.id.child_title);
        }
    }

}
