package com.bokun.bkjcb.on_siteinspection.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengShuai on 2017/5/27.
 */

public class StringAdapter extends BaseAdapter implements Filterable {

    List<String> strings;
    List<String> oldStrings;
    Context context;
    MyFilter mFilter;

    public StringAdapter(Context context, List<String> list) {
        this.strings = list;
        this.context = context;
        this.oldStrings = list;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public String getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = (TextView) View.inflate(context, R.layout.search_tips_view, null);
        } else {
            textView = (TextView) convertView;
        }
        textView.setText(strings.get(position));
        return textView;
    }

    public void add(String string) {
        oldStrings.add(string);
    }

    //当ListView调用setTextFilter()方法的时候，便会调用该方法
    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    //我们需要定义一个过滤器的类来定义过滤规则
    class MyFilter extends Filter {
        //我们在performFiltering(CharSequence charSequence)这个方法中定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<String> list;
            if (TextUtils.isEmpty(charSequence)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                list = oldStrings;
            } else {//否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();
                for (String str : oldStrings) {
                    if (str.contains(charSequence)) {
                        list.add(str);
                    }
                }
            }
            result.values = list; //将得到的集合保存到FilterResults的value变量中
            result.count = list.size();//将集合的大小保存到FilterResults的count变量中

            return result;
        }

        //在publishResults方法中告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            strings = (List<String>) filterResults.values;
            if (filterResults.count > 0) {
                notifyDataSetChanged();//通知数据发生了改变
            } else {
                notifyDataSetInvalidated();//通知数据失效
            }
        }
    }

    public void initData() {
        strings = oldStrings;
        notifyDataSetChanged();
    }
}
