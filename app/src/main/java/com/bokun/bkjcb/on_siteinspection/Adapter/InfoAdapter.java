package com.bokun.bkjcb.on_siteinspection.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Domain.TableKeys;
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
    public static final int type_14 = 14;//用途
    public static final int type_15 = 15;//空
    public static final int type_16 = 16;//带单位
    public static final int type_0 = 0;//居中标题
//    public static final int type_18 = 18;//车位数

    private Context context;
    private List<TableContent> list;

    public InfoAdapter(Context context, List<TableContent> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View view = null;
        TableContent content = list.get(position);

        view = convertView;
        if (view == null) {
            view = inflateView(R.layout.info_child_view);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        switch (getItemViewType(position)) {
            case type_0:
                TextView child4 = (TextView) inflateView(R.layout.info_type_17);
                child4.setText(content.keys.getTitle());
                holder.setView("", child4);
                holder.tv.setVisibility(View.GONE);
                break;
            case type_1:
                RadioGroup group1 = (RadioGroup) inflateView(R.layout.info_type_1);
                if (content.value != null) {
                    if (content.value.equals("机关")) {
                        group1.check(R.id.type_1_jg);
                    } else if (content.value.equals("事业")) {
                        group1.check(R.id.type_1_sy);
                    } else if (content.value.equals("企业")) {
                        group1.check(R.id.type_1_qy);
                    } else if (content.value.equals("社会团体")) {
                        group1.check(R.id.type_1_st);
                    } else if (content.value.equals("个人")) {
                        group1.check(R.id.type_1_gr);
                    } else {
                        group1.check(R.id.type_1_qt);
                    }
                }
                holder.setView(content.keys.getTitle(), group1);
                break;
            case type_2:
                RadioGroup group2 = (RadioGroup) inflateView(R.layout.info_type_2);
                if (content.value != null) {
                    if (content.value.equals("0")) {
                        group2.check(R.id.type_2_no);
                    } else if (content.value.equals("1")) {
                        group2.check(R.id.type_2_yes);
                    }
                }
                holder.setView(content.keys.getTitle(), group2);
                break;
            case type_3:
                RadioGroup group3 = (RadioGroup) inflateView(R.layout.info_type_3);
                if (content.value != null) {
                    if (content.value.equals("2.2M以下")) {
                        group3.check(R.id.type_3_s);
                    } else if (content.value.equals("2.2～2.8M")) {
                        group3.check(R.id.type_3_m);
                    } else if (content.value.equals("2.8M以上")) {
                        group3.check(R.id.type_3_l);
                    }
                }
                holder.setView(content.keys.getTitle(), group3);
                break;
            case type_4:
                RadioGroup group4 = (RadioGroup) inflateView(R.layout.info_type_4);
                if (content.value != null) {
                    if (content.value.equals("结建")) {
                        group4.check(R.id.type_4_jj);
                    } else if (content.value.equals("单建")) {
                        group4.check(R.id.type_4_dj);
                    }
                }
                holder.setView(content.keys.getTitle(), group4);
                break;
            case type_5:
                RadioGroup group5 = (RadioGroup) inflateView(R.layout.info_type_5);
                if (content.value != null) {
                    if (content.value.equals("钢筋混凝土")) {
                        group5.check(R.id.type_5_hlt);
                    } else if (content.value.equals("砖混")) {
                        group5.check(R.id.type_5_zh);
                    } else {
                        group5.check(R.id.type_5_qt);
                    }
                }
                holder.setView(content.keys.getTitle(), group5);
                break;
            case type_6:
                RadioGroup group6 = (RadioGroup) inflateView(R.layout.info_type_6);
                if (content.value != null) {
                    if (content.value.equals("地下室")) {
                        group6.check(R.id.type_6_dxs);
                    } else if (content.value.equals("半地下室")) {
                        group6.check(R.id.type_6_bdxs);
                    } else {
                        group6.check(R.id.type_6_qt);
                    }
                }
                holder.setView(content.keys.getTitle(), group6);
                break;
            case type_7:
                RadioGroup group7 = (RadioGroup) inflateView(R.layout.info_type_7);
                if (content.value != null) {
                    if (content.value.equals("1")) {
                        group7.check(R.id.type_7_sj);
                    } else if (content.value.equals("0")) {
                        group7.check(R.id.type_7_qj);
                    }
                }
                holder.setView(content.keys.getTitle(), group7);
                break;
            case type_8:
                RadioGroup group8 = (RadioGroup) inflateView(R.layout.info_type_8);
                if (content.value != null) {
                    if (content.value.equals("1")) {
                        group8.check(R.id.type_8_mf);
                    } else if (content.value.equals("0")) {
                        group8.check(R.id.type_8_dxs);
                    }
                }
                holder.setView(content.keys.getTitle(), group8);
                break;
            case type_9:
                RadioGroup group9 = (RadioGroup) inflateView(R.layout.info_type_9);
                if (content.value != null) {
                    if (content.value.equals("1")) {
                        group9.check(R.id.type_9_yes);
                    } else if (content.value.equals("0")) {
                        group9.check(R.id.type_9_no);
                    } else if (content.value.equals("2")) {
                        group9.check(R.id.type_9_yl);
                    }
                }
                holder.setView(content.keys.getTitle(), group9);
                break;
            case type_10:
                RadioGroup group10 = (RadioGroup) inflateView(R.layout.info_type_10);
                if (content.value != null) {
                    if (content.value.equals("住宅")) {
                        group10.check(R.id.type_10_zz);
                    } else if (content.value.equals("学校")) {
                        group10.check(R.id.type_10_xx);
                    } else if (content.value.equals("医院")) {
                        group10.check(R.id.type_10_yy);
                    } else if (content.value.equals("商业、办公楼")) {
                        group10.check(R.id.type_10_sy);
                    } else if (content.value.equals("绿地、公园")) {
                        group10.check(R.id.type_10_ld);
                    } else if (content.value.equals("其他")) {
                        group10.check(R.id.type_10_qt);
                        RadioButton radioButton = (RadioButton) group10.findViewById(R.id.type_10_qt);
                        radioButton.setText("其他  " + content.other);
                    }
                }
                holder.setView(content.keys.getTitle(), group10);
                break;
            case type_11:
                RadioGroup group11 = (RadioGroup) inflateView(R.layout.info_type_11);
                if (content.value != null) {
                    if (content.value.equals("自用")) {
                        group11.check(R.id.type_11_ziy);
                    } else if (content.value.equals("出租")) {
                        group11.check(R.id.type_11_zuy);
                    }
                }
                holder.setView(content.keys.getTitle(), group11);
                break;
            case type_12:
                RadioGroup group12 = (RadioGroup) inflateView(R.layout.info_type_12);
                if (content.value != null) {
                    if (content.value.equals("1")) {
                        group12.check(R.id.type_12_is);
                    } else if (content.value.equals("0")) {
                        group12.check(R.id.type_12_none);
                    }
                }
                holder.setView(content.keys.getTitle(), group12);
                break;
            case type_13:
                View child1 = inflateView(R.layout.info_type_13);
                CheckBox zb = (CheckBox) child1.findViewById(R.id.type_13_zb);
                CheckBox dt = (CheckBox) child1.findViewById(R.id.type_13_dt);
                TextView dtName = (TextView) child1.findViewById(R.id.tv_13_dt);
                if (content.value != null) {
                    if (content.value.equals("1")) {
                        zb.setChecked(true);
                    }
                }
                if (content.other!=null) {
                    dt.setChecked(true);
                    dtName.setText("（站名:" + content.other + "）");
                }
                holder.setView(content.keys.getTitle(), child1);
                break;
            case type_14:
                View child2 = inflateView(R.layout.info_type_14);
                CheckBox useType = (CheckBox) child2.findViewById(R.id.type_14_title);
                TextView area = (TextView) child2.findViewById(R.id.type_14_area);
                LinearLayout cw = (LinearLayout) child2.findViewById(R.id.type_14_cw);
                TextView num = (TextView) child2.findViewById(R.id.type_14_cws);
                useType.setChecked(true);
                if (content.cws != 0) {
                    cw.setVisibility(View.VISIBLE);
                    num.setText(content.cws + "");
                }
                if (content.oother != null) {
                    useType.setText(content.value + "(" + content.oother + ")");
                } else {
                    useType.setText(content.value);
                }
                area.setText(content.other);
                holder.setView("", child2);
                holder.tv.setVisibility(View.GONE);
                break;
            case type_15:
                TextView tv = (TextView) inflateView(R.layout.info_type_15);
                tv.setText(content.value);
                holder.setView(content.keys.getTitle(), tv);
                break;
            case type_16:
                View child3 = inflateView(R.layout.info_type_16);
                TextView contentValue = (TextView) child3.findViewById(R.id.type_16_content);
                TextView contentUnit = (TextView) child3.findViewById(R.id.type_16_unit);
                contentValue.setText(content.value);
                contentUnit.setText(content.keys.getUnit());
                holder.setView(content.keys.getTitle(), child3);
                break;
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 17;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).key;
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
            layout.removeAllViews();
            layout.addView(view);
        }

    }

    private View inflateView(int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(layoutId, null);
    }

    public static class TableContent {
        int key;
        String value;
        public int type;
        String other;
        String oother;
        int cws;//车位数专用
        public TableKeys keys;

        public TableContent(int key, String value, int type) {
            this.key = key;
            this.value = value;
            this.type = type;
        }

        public TableContent(int key, String value, int type, String other) {
            this.key = key;
            this.value = value;
            this.type = type;
            this.other = other;
        }

        public TableContent(int key, String value, int type, String other, String oother, int cw) {
            this.key = key;
            this.value = value;
            this.type = type;
            this.other = other;
            this.oother = oother;
            this.cws = cw;
        }
    }
}
