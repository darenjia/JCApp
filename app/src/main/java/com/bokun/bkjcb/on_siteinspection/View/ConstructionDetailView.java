package com.bokun.bkjcb.on_siteinspection.View;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.R;


/**
 * Created by BKJCB on 2017/3/21.
 */

public class ConstructionDetailView {

    private Context context;
    private CheckPlan checkPlan;

    public ConstructionDetailView(Context context, CheckPlan checkPlan) {
        this.context = context;
        this.checkPlan = checkPlan;
    }

    public View getConstructionDetailView(View.OnClickListener listener) {
        View view = View.inflate(context, R.layout.constn_detail_view, null);
        TextView mViewName = (TextView) view.findViewById(R.id.construction_name);
        TextView mViewId = (TextView) view.findViewById(R.id.construction_id);
        TextView mViewAddress = (TextView) view.findViewById(R.id.construction_address);
        TextView mViewArea = (TextView) view.findViewById(R.id.construction_area);
        TextView mViewType = (TextView) view.findViewById(R.id.construction_type);
//        TextView mViewTel = (TextView) view.findViewById(R.id.construction_tel);
        TextView mViewManager = (TextView) view.findViewById(R.id.construction_manager);
        TextView mViewUser = (TextView) view.findViewById(R.id.construction_user);
        Button mButtonCheck = (Button) view.findViewById(R.id.btn_check);

        mViewName.setText(checkPlan.getName());
        mViewId.setText(checkPlan.getIdentifier() + "");
        mViewAddress.setText("");
//        mViewTel.setText("");
        mViewArea.setText("");
        mViewType.setText("");
        mViewManager.setText("");
        mViewUser.setText("");
        mButtonCheck.setOnClickListener(listener);
        return view;
    }

}
