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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Adapter.StringAdapter;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.LogQuickSearch;
import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.ToastUtil;

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
    private StringAdapter logQuickSearchAdapter;//搜索历史适配器
    private Set<String> set;//判断有没有重复的搜索历史，重复的话不再保存
    private ArrayList<String> mItem;
    private ProgressBar marker_progress;
    private RelativeLayout error_tip;

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
        set = new HashSet<>();
        SetTypeFace();
        logQuickSearchAdapter = new StringAdapter(context, LogQuickSearch.all(context, 1));
        mItem = new ArrayList<>();
        listView.setTextFilterEnabled(false);
        listView.setAdapter(logQuickSearchAdapter);
        InitiateSearch();
        HandleSearch();
        IsAdapterEmpty();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void getDataFailed() {
        super.getDataFailed();
    }

    @Override
    protected void getDataSucceed(JsonResult object) {
        super.getDataSucceed(object);
    }

    private void InitiateSearch() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = logQuickSearchAdapter.getItem(position);
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
                    listView.setFilterText(txt);
                } else {
                    logQuickSearchAdapter.initData();
                    clearSearch.setImageResource(R.mipmap.ic_keyboard_voice);
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
                    logQuickSearchAdapter.initData();
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    IsAdapterEmpty();
                }
            }
        });

    }

    private void UpdateQuickSearch(String item) {
        for (int i = 0; i < logQuickSearchAdapter.getCount(); i++) {
            String name = logQuickSearchAdapter.getItem(i);
            set.add(name);
        }
        if (set.add(item)) {
            LogQuickSearch.save(context, item, 1);
            logQuickSearchAdapter.add(item);
            logQuickSearchAdapter.notifyDataSetChanged();
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
        if (logQuickSearchAdapter.getCount() == 0) {
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


    private void search(String item, int page_num) {
        view_search.setVisibility(View.VISIBLE);
        HttpRequestVo requestVo = new HttpRequestVo();
        requestVo.getRequestDataMap().put("", "");
        requestVo.setMethodName("GetXxclSc");
        HttpManager manager = new HttpManager(context, this, requestVo);
        //manager.postRequest();
        ToastUtil.show(context, "开始搜索");
        marker_progress.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                view_search.setVisibility(View.GONE);
                error_tip.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void action(int i, Object object) {
        JsonResult result = JsonParser.parseSoap((SoapObject) object);
        Message msg = new Message();
        msg.what = i;
        msg.obj = result;
        mHandler.sendMessage(msg);
    }
}
