package com.bokun.bkjcb.on_siteinspection.Activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;

import com.bokun.bkjcb.on_siteinspection.Adapter.InstructionAdapter;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

/**
 * Created by DengShuai on 2017/6/6.
 */

public class EmptyActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.instr_list);
        InstructionAdapter adapter = new InstructionAdapter(this);
        listView.setAdapter(adapter);
    }
}
