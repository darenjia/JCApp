package com.bokun.bkjcb.on_siteinspection.View;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LocalTools;

import java.util.List;

/**
 * Created by DengShuai on 2017/9/28.
 */

public class AlertGuidBuilder {

    private List<String> strings;
    private Context context;
    private AlertDialog dialog;
    private View view;
    private OnClickListener listener;
    private int current;

    public interface OnClickListener {
        void onClick(int position);
    }

    public AlertGuidBuilder(List<String> strings, Context context, OnClickListener listener, int current) {
        this.strings = strings;
        this.context = context;
        this.strings.add(0, "行业管理方面");
        this.strings.add(16, "民防设施方面");
        this.listener = listener;
        this.current = current;
    }

    public void builder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        view = View.inflate(context, R.layout.alertguide_view, null);
        ListView listView = (ListView) view.findViewById(R.id.guider_detail);
        TextView tv = (TextView) view.findViewById(R.id.guider_cancel);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int l = 0;
                if (position < 16 && position > 0) {
                    l = position - 1;
                } else if (position > 16) {
                    l = position - 2;
                }
                listener.onClick(l);
                dialog.dismiss();
            }
        });
        tv.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        listView.setAdapter(new AlertAdapter());
        listView.setSelection(current);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    class AlertAdapter extends BaseAdapter {

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
        public int getItemViewType(int position) {
            return strings.get(position).equals("行业管理方面") || strings.get(position).equals("民防设施方面") ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String data = strings.get(position);
            Holder holder = null;
            View view = null;

            view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.item_alertview, null);
                holder = creatHolder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            if (getItemViewType(position) == 0) {
                holder.UpdateTitle(data);
            } else {
                holder.UpdateUI(data, position);
            }
//            holder.UpdateUI(data, position);

            return view;
        }

        public Holder creatHolder(View view) {
            return new Holder(view);
        }
    }

    class Holder {
        private TextView tvAlert;

        public Holder(View view) {
            tvAlert = (TextView) view.findViewById(R.id.tvAlert);
        }

        public void UpdateUI(String data, int position) {
            String str;
            if (position < 16 && position > 0) {
                str = (position) + "." + data;
            } else if (position > 16 && position < strings.size() - 1) {
                str = (position - 1) + "." + data;
            } else {
                str = data;
            }
        /*    if (data.equals("行业管理方面") || data.equals("民防设施方面")) {
                UpdateTitle(data);
            } else {
                tvAlert.setText(str);
            }*/
            tvAlert.setText(str);
        }

        public void UpdateTitle(String data) {
            tvAlert.setText(data);
            tvAlert.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            tvAlert.setTextColor(context.getResources().getColor(R.color.white));
            tvAlert.setTextSize(15);
            tvAlert.setGravity(Gravity.CENTER);
            tvAlert.setHeight(LocalTools.dip2px(context, 36));
            tvAlert.setClickable(false);
        }
    }
}
