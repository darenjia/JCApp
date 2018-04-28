package com.bokun.bkjcb.on_siteinspection.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.R;

import java.io.IOException;

/**
 * Created by DengShuai on 2017/6/9.
 */

public class GuidViewBuilder {
    private Context context;
    private String[][] strings = {{"1.选择需检查的工程，进入工程检查界面",
            "2.进入检查界面，进行计划检查",
            "3.检查完成，点击提交，会保存记录",
            "4.进入上传界面点击上传，上传完成"},
            {"1.获取权限是为了软件的正常使用，不会泄露用户隐私，请勿拒绝",
                    "2.如果因权限拒绝导致软件功能无法正常使用，请在设置中重新授予",
                    " 设置—应用管理—地下室检查—权限",
                    " 将拒绝的权限重新打开"},
            {"如果遇到获取数据失败，请下拉刷新一下", "请检查手机网络是否通畅"},
            {"如遇到程序异常闪退或长时间无响应，请在设置中找到**地下室检查**清空应用缓存或重装该应用"},
            {"如果安装新版本软件后运行闪退，请先卸载老版本，重新安装新版本"},
            {"1.不要主动删除CheckApp文件夹下的文件，以防上传文件失败"}};

    public GuidViewBuilder(Context context) {
        this.context = context;
    }

    public View build(int flag) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < strings[flag].length; i++) {
            linearLayout.addView(getChildView(strings[flag][i], ""));
        }
        return linearLayout;
    }

    private View getChildView(String text, String imgName) {
        View view = View.inflate(context, R.layout.instruction_view, null);
        TextView textView = (TextView) view.findViewById(R.id.guide_txtsm);
        ImageView imageView = (ImageView) view.findViewById(R.id.guide_imgsm);
        textView.setText(text);
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(imgName));
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
//            e.printStackTrace();
            imageView.setVisibility(View.GONE);
        }
        return view;
    }
}
