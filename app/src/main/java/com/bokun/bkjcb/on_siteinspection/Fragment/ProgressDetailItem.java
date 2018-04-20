package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Domain.ProgressDetail;
import com.bokun.bkjcb.on_siteinspection.R;
import com.elvishew.xlog.XLog;

/**
 * Created by DengShuai on 2018/4/9.
 * Description :
 */

public class ProgressDetailItem extends Fragment {

    private TextView djh;
    private TextView name;
    private TextView address;
    private TextView fj;
    private TextView yh;
    private ProgressDetail detail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pro_detail_item, null);
        djh = (TextView) view.findViewById(R.id.pro_djh);
        name = (TextView) view.findViewById(R.id.pro_gcm);
        address = (TextView) view.findViewById(R.id.pro_dz);
        fj = (TextView) view.findViewById(R.id.pro_fj);
        yh = (TextView) view.findViewById(R.id.pro_yh);
        return view;
    }

    public void setData(ProgressDetail data) {
        this.detail = data;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        djh.setText(detail.getAq_gcxxdjh());
        name.setText(detail.getAq_gcmc());
        address.setText(detail.getAq_gcdz());
        fj.setText(detail.getFkfj());
        yh.setText(handleWord(detail.getGzyh()));
    }

    private String handleWord(String word) {
        word = word.replace("\n", "\n\t\t\t");
        word="\t\t\t"+word;
        XLog.i(word);
        return word;
    }
}
