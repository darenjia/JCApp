package com.bokun.bkjcb.on_siteinspection.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.util.List;

/**
 * Created by BKJCB on 2017/3/22.
 */

public class CheckItemView {

    private Context context;
    private List checkItems;
    private ViewHolder viewHolder;

    public CheckItemView(Context context, List checkItems) {
        this.context = context;
        this.checkItems = checkItems;
        LogUtil.logI("new CheckItemView");
    }

    public View getCheckItemView(int position) {
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
        }
        viewHolder.txt_content.setText("\t\t" + (position + 1) + "." + checkItems.get(position));
        return viewHolder.view;
    }

    public class ViewHolder {
        private TextView txt_content;
        private RadioGroup mRdaioGroup;
        private ImageView camera_view;
        private ImageView audio_view;
        private ImageView video_view;
        private Button btn_remark;
        private TextView comment_word;
        private LinearLayout commtent_pic;
        private LinearLayout commtent_video;
        private LinearLayout commtent_audio;
        private View view;

        public ViewHolder() {
            LogUtil.logI("new ViewHolder");
            initWidgets();
        }

        private void initWidgets() {
            view = LayoutInflater.from(context).inflate(R.layout.check_detial_view, null, false);
            txt_content = (TextView) findView(R.id.txt_content);
            camera_view = (ImageView) findView(R.id.check_content_btn_camera);
            comment_word = (TextView) findView(R.id.check_content_info);
            commtent_audio = (LinearLayout) findView(R.id.check_content_audio);
            commtent_pic = (LinearLayout) findView(R.id.check_content_pic);
            commtent_video = (LinearLayout) findView(R.id.check_content_video);
            video_view = (ImageView) findView(R.id.check_content_btn_video);
            audio_view = (ImageView) findView(R.id.check_content_btn_audio);
            btn_remark = (Button) findView(R.id.check_content_btn_remark);

        }

        private View findView(int id) {
            return view.findViewById(id);
        }


    }

}
