package com.bokun.bkjcb.on_siteinspection.View;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Activity.InfoActivity;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.R;


/**
 * Created by BKJCB on 2017/3/21.
 */

public class ConstructionDetailView {

    private Context context;
    private CheckPlan checkPlan;
    private static ConstructionDetailView view;
    private TextView mViewName;
    private TextView mViewId;
    private TextView mViewAddress;
    private TextView mViewArea;
    private TextView mViewType;
    private TextView mViewManager;
    private TextView mViewUser;
    private TextView mTitle;
    private Button mButtonCheck;
    private Button mButtonLook;
    private View resView;
    private String title;

    private ConstructionDetailView(Context context) {
        this.context = context;
    }

    public static ConstructionDetailView getConstructionView(Context context) {
        if (view == null) {
            view = new ConstructionDetailView(context);
        }
        return view;
    }

    public View setData(CheckPlan checkPlan, boolean flag, View.OnClickListener listener, String title) {
        this.checkPlan = checkPlan;
        this.title = title;
        return getConstructionDetailView(flag, listener);
    }

    private View getConstructionDetailView(boolean flag, View.OnClickListener listener) {
        initViews();
        mViewName.setText(checkPlan.getName());
        mViewId.setText(checkPlan.getIdentifier() + "");
        mViewAddress.setText(checkPlan.getAddress());
//        mViewTel.setText("");
        mViewArea.setText(checkPlan.getArea() + "m²");
        mViewType.setText(checkPlan.getType());
        mViewManager.setText("");
        mViewUser.setText("");
        mTitle.setText(title);
        if (flag) {
            mButtonCheck.setText("当前计划暂不可检查");
            mButtonCheck.setClickable(false);
            mButtonCheck.setBackgroundColor(context.getResources().getColor(R.color.text_color));
        } else {
            mButtonCheck.setOnClickListener(listener);
        }
        mButtonLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoActivity.ComeInfoActivity(context,checkPlan.getSysId());
            }
        });
        return resView;
    }

    private void initViews() {
        resView = View.inflate(context, R.layout.constn_detail_view, null);
        mViewName = (TextView) resView.findViewById(R.id.construction_name);
        mViewId = (TextView) resView.findViewById(R.id.construction_id);
        mTitle = (TextView) resView.findViewById(R.id.construction_title);
        mViewAddress = (TextView) resView.findViewById(R.id.construction_address);
        mViewArea = (TextView) resView.findViewById(R.id.construction_area);
        mViewType = (TextView) resView.findViewById(R.id.construction_type);
//        TextView mViewTel = (TextView) view.findViewById(R.id.construction_tel);
        mViewManager = (TextView) resView.findViewById(R.id.construction_manager);
        mViewUser = (TextView) resView.findViewById(R.id.construction_user);
        mButtonCheck = (Button) resView.findViewById(R.id.btn_check);
        mButtonLook = (Button) resView.findViewById(R.id.btn_detail);
    }

}
