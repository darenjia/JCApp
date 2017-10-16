package com.bokun.bkjcb.on_siteinspection.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.Adapter.InfoAdapter;
import com.bokun.bkjcb.on_siteinspection.Domain.TableData;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.PlanDao;
import com.bokun.bkjcb.on_siteinspection.SQLite.TableDataDao;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class InfoActivity extends BaseActivity {

    private ListView listView;
    private Toolbar toolbar;
    private String id;
    private ArrayList<InfoAdapter.TableContent> list;
    private InfoAdapter adapter;

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
        toolbar.setNavigationIcon(R.drawable.back);
        Utils.initSystemBar(this, toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void setListener() {

    }


    @Override
    protected void loadData() {
        id = getIntent().getStringExtra("ID");
        new LoadData().execute();
    }


    public static void ComeInfoActivity(Context context, int id) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("ID", String.valueOf(id));
        context.startActivity(intent);
    }

    private class LoadData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            TableDataDao dataDao = new TableDataDao(InfoActivity.this);
            TableData data = dataDao.query(id);
            PlanDao planDao = new PlanDao(InfoActivity.this);
            list = handleData(data);
            for (int i = 0; i < list.size(); i++) {
                InfoAdapter.TableContent content = list.get(i);
                content.keys = planDao.query(content.type);
            }
            dataDao.close();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (list.size() > 0) {
                adapter = new InfoAdapter(InfoActivity.this, list);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(InfoActivity.this, "该工程暂时无法获取详细信息", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<InfoAdapter.TableContent> handleData(TableData data) {
        ArrayList<InfoAdapter.TableContent> list = new ArrayList<>();
        if (data.getShengchan01().size() == 0) {
            return list;
        }
        TableData.ShengChan1 shengChan1 = data.getShengchan01().get(0);
        TableData.ShengChan2 shengChan2 = data.getShengchan02().get(0);
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan1.getScJbGcmc(), 1));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan1.getScJbGcdz(), 2));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan1.getScJbSzqx(), 3));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan1.getScJbJdxz(), 4));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScJszDw(), 5));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScJszSjzg(), 6));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScJszFddbr(), 7));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScJszLxr(), 8));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScJszLxdh(), 9));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_1, shengChan2.getScJszDwxz(), 10));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScSyzMc(), 11));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_2, String.valueOf(shengChan2.getScSyzCqz()), 12));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScSyzFddbr(), 13));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScSyzLxr(), 14));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScSyzLxdh(), 15));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_1, shengChan2.getScSyzLx(), 16));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScGlzMc(), 17));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScGlzSjzg(), 18));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScGlzFddbr(), 19));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScGlzLxr(), 20));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan2.getScGlzLxdh(), 21));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_1, shengChan2.getScGlzLx(), 22));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_16, shengChan1.getScXzJcnf(), 23));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_16, String.valueOf(shengChan1.getScXzJzmj()), 24));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan1.getScXzSjyt(), 25));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_16, String.valueOf(shengChan1.getScXzMfjzmj()), 26));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_16, String.valueOf(shengChan1.getScXzMzsd()), 27));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_16, String.valueOf(shengChan1.getScXzKbs()), 28));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_16, String.valueOf(shengChan1.getScXzDxcs()), 29));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_2, String.valueOf(shengChan1.getScXzJgys()), 30));
        ArrayList<TableData.JingGao> jingGaos = data.getJinggao();
        for (int i = 0; i < jingGaos.size(); i++) {
            TableData.JingGao jg = jingGaos.get(i);
            Logger.i(jg.getScGgLcjg());
            list.add(new InfoAdapter.TableContent(InfoAdapter.type_3, jg.getScGgLcjg(), (87 + i)));
        }
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_4, shengChan1.getScXzGcxs(), 32));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_5, shengChan1.getScXzJglb(), 33));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_6, shengChan1.getScXzGclb(), 34));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_7, String.valueOf(shengChan1.getScXzSsgc()), 35));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_8, String.valueOf(shengChan1.getScXzGcxz()), 36));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_9, String.valueOf(shengChan1.getScXzLtqtgc()), 37));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_13, String.valueOf(shengChan1.getScXzLtdxZb()), 39, shengChan1.getScXzLtdxDt()));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_10, shengChan1.getScXzDmjz(), 40, shengChan1.getScXzDmjzQt()));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_0, "", 41));
        ArrayList<TableData.ShiYongDW> dws = data.getScShiyongdw();
        for (int i = 0; i < dws.size(); i++) {
            TableData.ShiYongDW dw = dws.get(i);
            list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, dw.getSydwMc(), 42));
            list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, dw.getSydwFddbr(), 43));
            list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, dw.getSydwLxr(), 44));
            list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, dw.getSydwLxdh(), 45));
            list.add(new InfoAdapter.TableContent(InfoAdapter.type_1, dw.getSydwLx(), 46));
            list.add(new InfoAdapter.TableContent(InfoAdapter.type_11, dw.getSydwSyfs(), 47));
            list.add(new InfoAdapter.TableContent(InfoAdapter.type_12, String.valueOf(dw.getSydwGsdj()), 48));
            list.add(new InfoAdapter.TableContent(InfoAdapter.type_12, String.valueOf(dw.getSydwZaxk()), 49));
            list.add(new InfoAdapter.TableContent(InfoAdapter.type_12, String.valueOf(dw.getSydwWsxk()), 50));
            list.add(new InfoAdapter.TableContent(InfoAdapter.type_12, String.valueOf(dw.getSydwXfxk()), 51));
        }
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_0, "", 62));
        ArrayList<TableData.ShiYongYongTu> yongTus = data.getScShiyong();
        for (int i = 0; i < yongTus.size(); i++) {
            TableData.ShiYongYongTu yt = yongTus.get(i);
            list.add(new InfoAdapter.TableContent(InfoAdapter.type_14, yt.getSyScSsmc(), 63, String.valueOf(yt.getSyScJzmj()), yt.getSyScQtyt(), yt.getSyScCws()));
        }
        TableData.BiaogeInfo info = data.getBiaogeInfo().get(0);
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, shengChan1.getScJbBz(), 82));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, info.getScGgGdBgFzr(), 83));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, info.getScGgGdBgTbr(), 84));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, info.getScGgGdBgLxdh(), 85));
        list.add(new InfoAdapter.TableContent(InfoAdapter.type_15, info.getScGgGdBgBcrq(), 86));
        return list;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
