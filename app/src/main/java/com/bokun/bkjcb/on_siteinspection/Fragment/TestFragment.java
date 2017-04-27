package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.eugene.fithealth.LogQuickSearchData.LogQuickSearch;
import com.eugene.fithealth.LogQuickSearchData.LogQuickSearchAdapter;
import com.eugene.fithealth.Utilities.InitiateSearch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by DengShuai on 2017/4/7.
 */

public class TestFragment extends MainFragment implements PoiSearch.OnPoiSearchListener {
    private View line_divider, toolbar_shadow;
    private RelativeLayout view_search;
    private CardView card_search;
    private ImageView image_search_back, clearSearch;
    private AutoCompleteTextView edit_text_search;
    private ListView listView, listContainer;//搜索结果列表
    private LogQuickSearchAdapter logQuickSearchAdapter;//搜索历史适配器
    private Set<String> set;//判断有没有重复的搜索历史，重复的话不再保存
    private ArrayList<String> mItem;
    private ProgressBar marker_progress;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private StringAdapter resultAdapter;
    private Button location;
    private ListPopupWindow listPopupWindow;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.test_view, null);
        view_search = (RelativeLayout) view.findViewById(R.id.view_search);
        line_divider = view.findViewById(R.id.line_divider);
        toolbar_shadow = view.findViewById(R.id.toolbar_shadow);
        edit_text_search = (AutoCompleteTextView) view.findViewById(R.id.edit_text_search);
        card_search = (CardView) view.findViewById(R.id.card_search);
        image_search_back = (ImageView) view.findViewById(R.id.image_search_back);
        clearSearch = (ImageView) view.findViewById(R.id.clearSearch);
        listView = (ListView) view.findViewById(R.id.listView);
        listContainer = (ListView) view.findViewById(R.id.listContainer);
        marker_progress = (ProgressBar) view.findViewById(R.id.marker_progress);
        location = (Button) view.findViewById(R.id.location);
        marker_progress.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FFFFFF"),//Pink color
                android.graphics.PorterDuff.Mode.MULTIPLY);
        set = new HashSet<>();
        SetTypeFace();
        logQuickSearchAdapter = new LogQuickSearchAdapter(context, 0, LogQuickSearch.all());
        mItem = new ArrayList<>();
        listView.setAdapter(logQuickSearchAdapter);
        resultAdapter = new StringAdapter(mItem);
        listContainer.setAdapter(resultAdapter);
        listPopupWindow = new ListPopupWindow(getContext());
        listPopupWindow.setAdapter(resultAdapter);
        listPopupWindow.setAnchorView(location);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setModal(true);
        InitiateSearch();
        HandleSearch();
        IsAdapterEmpty();
        return view;
    }

    private void InitiateSearch() {
        /*imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.setVisibility(View.GONE);
                IsAdapterEmpty();
                InitiateSearch.handleToolBar(context, card_search, view_search, listView, edit_text_search, line_divider);
            }
        });*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogQuickSearch logQuickSearch = logQuickSearchAdapter.getItem(position);
                edit_text_search.setText(logQuickSearch.getName());
                listView.setVisibility(View.GONE);
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_text_search.getWindowToken(), 0);
                toolbar_shadow.setVisibility(View.GONE);
                search(logQuickSearch.getName(), 0);
            }
        });
        edit_text_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = edit_text_search.getText().toString();
                if (newText.length() == 0) {
                    logQuickSearchAdapter = new LogQuickSearchAdapter(context, 0, LogQuickSearch.all());
                    listView.setAdapter(logQuickSearchAdapter);
                    clearSearch.setImageResource(com.eugene.fithealth.R.mipmap.ic_keyboard_voice);
                    IsAdapterEmpty();
                } else {
                    /*logQuickSearchAdapter = new LogQuickSearchAdapter(context, 0, LogQuickSearch.FilterByName(edit_text_search.getText().toString()));
                    listView.setAdapter(logQuickSearchAdapter);*/
                    clearSearch.setImageResource(com.eugene.fithealth.R.mipmap.ic_close);
                    IsAdapterEmpty();
                    InputtipsQuery inputquery = new InputtipsQuery(newText, "上海");
                    Inputtips inputTips = new Inputtips(getContext(), inputquery);
                    inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
                        @Override
                        public void onGetInputtips(List<Tip> list, int rCode) {
                            if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
                                List<String> listString = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    listString.add(list.get(i).getName());
                                }
//                                StringAdapter aAdapter = new StringAdapter(listString);
                                ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                                        getContext(),
                                        R.layout.expandable_child_item_view, listString);
//                                listView.setAdapter(aAdapter);
                                edit_text_search.setAdapter(aAdapter);
                            } else {
                            }
                        }
                    });
                    inputTips.requestInputtipsAsyn();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_text_search.getText().toString().length() == 0) {

                } else {
                    edit_text_search.setText("");
                    listView.setVisibility(View.VISIBLE);
                    clearItems();
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    IsAdapterEmpty();
                }
            }
        });
        listContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (location.getVisibility() != View.VISIBLE) {
                    location.setVisibility(View.VISIBLE);
                }
                location.setText(mItem.get(position));
                InitiateSearch.handleToolBar(context, card_search, view_search, listView, edit_text_search, line_divider);
                listContainer.setVisibility(View.GONE);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow.show();
            }
        });
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                location.setText(mItem.get(position));
                listPopupWindow.dismiss();
            }
        });
    }


    private void UpdateQuickSearch(String item) {
        for (int i = 0; i < logQuickSearchAdapter.getCount(); i++) {
            LogQuickSearch ls = logQuickSearchAdapter.getItem(i);
            String name = ls.getName();
            set.add(name.toUpperCase());
        }
        if (set.add(item.toUpperCase())) {
            LogQuickSearch recentLog = new LogQuickSearch();
            recentLog.setName(item);
            recentLog.setDate(new Date());
            recentLog.save();
            logQuickSearchAdapter.addLog(recentLog);
            logQuickSearchAdapter.notifyDataSetChanged();
        }
    }

    private void HandleSearch() {
        image_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitiateSearch.handleToolBar(context, card_search, view_search, listView, edit_text_search, line_divider);
                listContainer.setVisibility(View.GONE);
                toolbar_shadow.setVisibility(View.VISIBLE);
                clearItems();
            }
        });
        edit_text_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                LogUtil.logI("action:" + actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (edit_text_search.getText().toString().trim().length() > 0) {
                        clearItems();
                        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_text_search.getWindowToken(), 0);
                        UpdateQuickSearch(edit_text_search.getText().toString());
                        listView.setVisibility(View.GONE);
                        search(edit_text_search.getText().toString(), 0);
                        toolbar_shadow.setVisibility(View.GONE);
                    }
                    return true;
                } else if (actionId == EditorInfo.IME_ACTION_NONE) {

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

    /**
     * Handle FatSecret Search
     */

    private void clearItems() {
        listContainer.setVisibility(View.GONE);
        mItem.clear();
        resultAdapter.notifyDataSetChanged();
    }


    private void search(String item, int page_num) {
        marker_progress.setVisibility(View.VISIBLE);
        currentPage = 0;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(item, "", "上海");
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setCityLimit(true);

        poiSearch = new PoiSearch(getContext(), query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        LogUtil.logI("mItem" + mItem);
                        for (int i = 0; i < poiItems.size(); i++) {
                            PoiItem poiItem = poiItems.get(i);
                            mItem.add(poiItem.getTitle());
                        }
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
//                        showSuggestCity(suggestionCities);
                    } else {

                    }
                }
            } else {
            }
            marker_progress.setVisibility(View.GONE);
            resultAdapter.notifyDataSetChanged();
            if (mItem.size() > 0) {
                toolbar_shadow.setVisibility(View.GONE);
                listContainer.setVisibility(View.VISIBLE);
                listContainer.setVerticalScrollbarPosition(0);
                listContainer.setSelection(0);
            } else {
                toolbar_shadow.setVisibility(View.VISIBLE);
                listContainer.setVisibility(View.GONE);
            }
        } else {
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    class StringAdapter extends BaseAdapter {
        List<String> list;

        public StringAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) View.inflate(getContext(), R.layout.expandable_child_item_view, null);
            textView.setText(list.get(position));
            return textView;
        }
    }
}
