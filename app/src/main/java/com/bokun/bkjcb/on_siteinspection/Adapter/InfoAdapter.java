package com.bokun.bkjcb.on_siteinspection.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.R;

import java.util.List;

/**
 * Created by DengShuai on 2017/10/11.
 */

public class InfoAdapter extends BaseAdapter {
    public static final int type_1 = 1;//机关、事业
    public static final int type_2 = 2;//是、否
    public static final int type_3 = 3;//2.2M
    public static final int type_4 = 4;//结建、单建
    public static final int type_5 = 5;//钢筋混凝土
    public static final int type_6 = 6;//地下室、半地下室、其它
    public static final int type_7 = 7;//市级、区级
    public static final int type_8 = 8;//普通地下室、民防工程
    public static final int type_9 = 9;//是、否、预留
    public static final int type_10 = 10;//住宅、医院、办公楼
    public static final int type_11 = 11;//自用、租用
    public static final int type_12 = 12;//有、无
    public static final int type_13 = 13;//周边地下室
    public static final int type_14 = 14;//建筑面积
    public static final int type_15 = 15;//
    public static final int type_16 = 16;//

    private Context context;
    private List<String> list;

    public InfoAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View view = null;

        view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.info_child_view, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        switch (getItemViewType(position)) {
            case type_1:
                break;
            case type_2:
                break;
            case type_3:
                break;
            case type_4:
                break;
            case type_5:
                break;
            case type_6:
                break;
            case type_7:
                break;
            case type_8:
                break;
            case type_9:
                break;
            case type_10:
                break;
            case type_11:
                break;
            case type_12:
                break;
            case type_13:
                break;
            case type_14:
                break;
            case type_15:
                break;
        }
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 14;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder {
        TextView tv;
        LinearLayout layout;

        public ViewHolder(View view) {
//            View view = View.inflate(context, layoutId,null);
            tv = (TextView) view.findViewById(R.id.table_title);
            layout = (LinearLayout) view.findViewById(R.id.empty_view);
        }

        public void setView(String title, View view) {
            tv.setText(title);
            layout.addView(view);
        }

    }
}
