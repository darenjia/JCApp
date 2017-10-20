package com.bokun.bkjcb.on_siteinspection.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.github.chrisbanes.photoview.PhotoView;

public class InfoActivity extends BaseActivity {

    private PhotoView photoView;
    private Toolbar toolbar;
    private String id;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_info);
    }

    @Override
    protected void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        photoView = (PhotoView) findViewById(R.id.photoview);
        toolbar.setTitle("基本信息登记表");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        Utils.initSystemBar(this, toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //mmp
    }

    @Override
    protected void setListener() {

    }


    @Override
    protected void loadData() {
        id = getIntent().getStringExtra("ID");
//        new LoadData().execute();
        photoView.setImageResource(R.drawable.up);
//        photoView.setImageBitmap();
    }


    public static void ComeInfoActivity(Context context, int id) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("ID", String.valueOf(id));
        context.startActivity(intent);
    }

    private class LoadData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
