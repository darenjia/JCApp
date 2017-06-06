package com.bokun.bkjcb.on_siteinspection.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DengShuai on 2017/6/6.
 */

public class EmptyActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        initAcitonBar();
        setListener();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initAcitonBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("使用说明");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        Utils.initSystemBar(this, toolbar);
      /*  ExpandableListView listView = (ExpandableListView) findViewById(R.id.instr_list);
        InstructionAdapter adapter = new InstructionAdapter(this);
        listView.setAdapter(adapter);*/
        Button button = (Button) findViewById(R.id.test_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory().toString(), "Bokun/picture");
                List<File> files = Arrays.asList(file.listFiles());
                String path = "test";
                for (int i = 0; i < files.size(); i++) {
                    LogUtil.logI(files.get(i).toString());
                }
            }
        });
    }

}
