package com.bokun.bkjcb.on_siteinspection.Adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.R;

/**
 * Created by BKJCB on 2017/3/20.
 */

public class InstructionAdapter extends BaseExpandableListAdapter {

    private Context context;
    private String[] titles;
    private String[][] contents;

    public InstructionAdapter(Context contextst) {
        this.context = context;
        initData();
    }

    private void initData() {
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return titles.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return contents[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return titles[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return contents[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(context, R.layout.expandable_group_item_view, null);
        } else {
            view = convertView;
        }
        TextView title = (TextView) view.findViewById(R.id.group_title);
        TextView quxian = (TextView) view.findViewById(R.id.group_quxian);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.checkitemlist, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.check_item_title);
        TextView state = (TextView) convertView.findViewById(R.id.check_item_state);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return childId;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return groupId;
    }

    private String getState(int state) {
        if (state == 0) {
            return "（检查未开始）";
        } else if (state == 1) {
            return "（检查未完成）";
        } else if (state == 2) {
            return "（检查已完成）";
        } else {
            return "（数据已上传）";
        }
    }
}
