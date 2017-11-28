package com.bokun.bkjcb.on_siteinspection.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.R;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/11/27.
 * Description :
 */

public class SeachResultAdapter extends RecyclerView.Adapter<SeachResultAdapter.ViewHolder> {
    public interface onItemClickListener {
        void onItemClick(CheckPlan checkPlan);
    }

    private ArrayList<CheckPlan> checkPlans;
    private Context context;
    private onItemClickListener listenr;

    public SeachResultAdapter(ArrayList<CheckPlan> checkPlans, Context context, onItemClickListener listenr) {
        this.checkPlans = checkPlans;
        this.context = context;
        this.listenr = listenr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkitemlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CheckPlan plan = checkPlans.get(position);
        holder.title.setText(plan.getName());
        holder.title.setTextColor(context.getResources().getColor(R.color.text_color));
      /*  holder.state.setText(getState(plan.getState()));
        holder.state.setTextColor(getColor(plan.getState()));*/
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenr.onItemClick(plan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return checkPlans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView state;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.title = (TextView) itemView.findViewById(R.id.check_item_title);
//            this.state = (TextView) itemView.findViewById(R.id.check_item_state);
        }
    }

    private int getColor(int state) {
        if (state == 1) {
            return context.getResources().getColor(R.color.holo_orange_light);
        } else if (state == 2) {
            return context.getResources().getColor(R.color.holo_blue_bright);
        } else if (state == 3) {
            return context.getResources().getColor(R.color.holo_green_light);
        }
        return context.getResources().getColor(R.color.text_color);
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
