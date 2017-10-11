package com.bokun.bkjcb.on_siteinspection.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.bokun.bkjcb.on_siteinspection.R;

public class InfoActivity extends BaseActivity {

    private ListView listView;
    private Toolbar toolbar;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_info);
    }

    @Override
    protected void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.info_list);
        toolbar.setTitle("基本信息登记表");
        setSupportActionBar(toolbar);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {

    }
    public static void ComeInfoActivity(Context context,int id){
        Intent intent = new Intent(context,InfoActivity.class);
        intent.putExtra("ID",id);
        context.startActivity(intent);
    }
}
