package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Activity.InfoActivity;
import com.bokun.bkjcb.on_siteinspection.Adapter.StringAdapter;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.SQLite.SearchedWordDao;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DengShuai on 2017/5/27.
 */

public class SearchFragment extends MainFragment implements RequestListener {
    private View line_divider;
    private RelativeLayout view_search;
    private CardView card_search, result_view;
    private ImageView image_search_back, clearSearch;
    private EditText edit_text_search;
    private ListView listView, listContainer;//搜索结果列表
    private StringAdapter stringAdapter;//搜索历史适配器
    private Set<String> set;//判断有没有重复的搜索历史，重复的话不再保存
    private ArrayList<String> mItem;
    private ProgressBar marker_progress;
    private RelativeLayout error_tip;
    private ArrayList<CheckPlan> checkPlans;
    private LinearLayout linearLayout;
    private ResultAdapter resultAdapter;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.search_fragment_view, null);
        view_search = (RelativeLayout) view.findViewById(R.id.view_search);
        line_divider = view.findViewById(R.id.line_divider);
        edit_text_search = (EditText) view.findViewById(R.id.edit_search);
        card_search = (CardView) view.findViewById(R.id.card_search);
        result_view = (CardView) view.findViewById(R.id.result_view);
        image_search_back = (ImageView) view.findViewById(R.id.image_search_back);
        clearSearch = (ImageView) view.findViewById(R.id.clearSearch);
        listView = (ListView) view.findViewById(R.id.listView);
        listContainer = (ListView) view.findViewById(R.id.listContainer);
        error_tip = (RelativeLayout) view.findViewById(R.id.error_tip);
        marker_progress = (ProgressBar) view.findViewById(R.id.marker_progress);
        linearLayout = (LinearLayout) view.findViewById(R.id.nav_view);
        set = new HashSet<>();
        SetTypeFace();
        stringAdapter = new StringAdapter(context, SearchedWordDao.all(context, 1));
        mItem = new ArrayList<>();
        listView.setTextFilterEnabled(true);
        listView.setAdapter(stringAdapter);
        InitiateSearch();
        HandleSearch();
        IsAdapterEmpty();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        checkPlans = new ArrayList<>();
        resultAdapter = new ResultAdapter();
        listContainer.setAdapter(resultAdapter);
    }

    @Override
    protected void getDataFailed() {
        super.getDataFailed();
        view_search.setVisibility(View.GONE);
        error_tip.setVisibility(View.VISIBLE);
    }

    @Override
    protected void getDataSucceed(JsonResult object) {
        super.getDataSucceed(object);
    }

    private void InitiateSearch() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = stringAdapter.getItem(position);
                edit_text_search.setText(name);
                listView.setVisibility(View.GONE);
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_text_search.getWindowToken(), 0);
                search(name, 0);
            }
        });
        edit_text_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String txt = s.toString().trim();
                if (txt.length() > 0) {
                    clearSearch.setImageResource(R.mipmap.ic_close);
//                    listView.setFilterText(txt);
                    stringAdapter.getFilter().filter(txt);
                } else {
                    stringAdapter.initData();
                    clearSearch.setImageResource(R.mipmap.ic_arrow_back);
                    IsAdapterEmpty();
                }
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_text_search.getText().toString().length() != 0) {
                    edit_text_search.setText("");
                    listView.setVisibility(View.VISIBLE);
                    stringAdapter.initData();
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edit_text_search, InputMethodManager.SHOW_IMPLICIT);
                    IsAdapterEmpty();
                }
            }
        });
        listContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckPlan plan = checkPlans.get(position);
                InfoActivity.ComeInfoActivity(context, plan.getUrl());
            }
        });

    }

    private void UpdateQuickSearch(String item) {
        for (int i = 0; i < stringAdapter.getCount(); i++) {
            String name = stringAdapter.getItem(i);
            set.add(name);
        }
        if (set.add(item)) {
            SearchedWordDao.save(context, item, 1);
            stringAdapter.add(item);
            stringAdapter.notifyDataSetChanged();
        }
    }

    private void HandleSearch() {
        image_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_text_search.getText().toString().trim().length() > 0) {
                    clearItems();
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_text_search.getWindowToken(), 0);
                    UpdateQuickSearch(edit_text_search.getText().toString());
                    listView.setVisibility(View.GONE);
                    search(edit_text_search.getText().toString(), 0);
                }
            }
        });
        edit_text_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (edit_text_search.getText().toString().trim().length() > 0) {
                        clearItems();
                        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_text_search.getWindowToken(), 0);
                        UpdateQuickSearch(edit_text_search.getText().toString());
                        listView.setVisibility(View.GONE);
                        search(edit_text_search.getText().toString(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void IsAdapterEmpty() {
        if (stringAdapter.getCount() == 0) {
            line_divider.setVisibility(View.GONE);
        } else {
            line_divider.setVisibility(View.VISIBLE);
        }
    }

    private void SetTypeFace() {
        Typeface roboto_regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        edit_text_search.setTypeface(roboto_regular);
    }

    private void clearItems() {
        listContainer.setVisibility(View.GONE);
        mItem.clear();
    }

    public void cleanHistory() {
        stringAdapter = null;
        stringAdapter = new StringAdapter(context, SearchedWordDao.all(context, 1));
        listView.setAdapter(stringAdapter);
    }

    private void search(String item, int page_num) {
        view_search.setVisibility(View.VISIBLE);
        /*HttpRequestVo requestVo = new HttpRequestVo();
        requestVo.getRequestDataMap().put("", "");
        requestVo.setMethodName("GetXxclSc");
        HttpManager manager = new HttpManager(context, this, requestVo);
        //manager.postRequest();
        mHandler.sendEmptyMessageDelayed(9, 2000);*/
        checkPlans.clear();
        checkPlans.addAll(DataUtil.queryCheckPlan(item));
        view_search.setVisibility(View.GONE);
        if (checkPlans.size() == 0) {
            error_tip.setVisibility(View.VISIBLE);
        } else {
            error_tip.setVisibility(View.GONE);
            listContainer.setVisibility(View.VISIBLE);
            resultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void action(int i, Object object) {
        JsonResult result = JsonParser.parseSoap((SoapObject) object);
        Message msg = new Message();
        msg.what = i;
        msg.obj = result;
        mHandler.sendMessage(msg);
    }

    class ResultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return checkPlans.size();
        }

        @Override
        public Object getItem(int position) {
            return checkPlans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.checkitemlist, null);
            }
            TextView title = (TextView) convertView.findViewById(R.id.check_item_title);
            TextView state = (TextView) convertView.findViewById(R.id.check_item_state);
            CheckPlan plan = checkPlans.get(position);
            title.setText(plan.getName());
            state.setText(getState(plan.getState()));
            state.setTextColor(getColor(plan.getState()));
            return convertView;
        }
    }

    private int getColor(int state) {
        if (state == 1) {
            return getResources().getColor(R.color.holo_orange_light);
        } else if (state == 2) {
            return getResources().getColor(R.color.holo_blue_bright);
        } else if (state == 3) {
            return getResources().getColor(R.color.holo_green_light);
        }
        return getResources().getColor(R.color.text_color);
    }

    private String getState(int state) {
        if (state == 0) {
            return "（检查未开始）";
        } else if (state == 1) {
            return "（检查未完成）";
        } else if (state == 2) {
            return "（检查已完成）";
        } else {
            return "（数据已上传）";
        }
    }
}
