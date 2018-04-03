package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Activity.InfoActivity;
import com.bokun.bkjcb.on_siteinspection.Activity.MainActivity;
import com.bokun.bkjcb.on_siteinspection.Adapter.SeachResultAdapter;
import com.bokun.bkjcb.on_siteinspection.Adapter.StringAdapter;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.SQLite.SearchedWordDao;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.NetworkUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.ToastUtil;
import com.bokun.bkjcb.on_siteinspection.View.ConstructionDetailView;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

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
    private ListView listView;//搜索结果列表
    private PullLoadMoreRecyclerView listContainer;
    private StringAdapter stringAdapter;//搜索历史适配器
    private Set<String> set;//判断有没有重复的搜索历史，重复的话不再保存
    private ArrayList<String> mItem;
    private ProgressBar marker_progress;
    private RelativeLayout error_tip;
    private ArrayList<CheckPlan> checkPlans;
    private ArrayList<CheckPlan> queryPlans;
    private LinearLayout linearLayout;
    private SeachResultAdapter resultAdapter;
    private TextView result_title;
    private boolean isFromNet = false;
    private Button local_search;
    private int page = 1;
    private String oldKey;
    private boolean isClear;
    private AlertDialog dialog;
    private TextView errorText;

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
        listContainer = (PullLoadMoreRecyclerView) view.findViewById(R.id.listContainer);
        error_tip = (RelativeLayout) view.findViewById(R.id.error_tip);
        local_search = (Button) view.findViewById(R.id.btn_sea_local);
        marker_progress = (ProgressBar) view.findViewById(R.id.marker_progress);
        linearLayout = (LinearLayout) view.findViewById(R.id.nav_view);
        result_title = (TextView) view.findViewById(R.id.result_title);
        errorText = (TextView)view.findViewById(R.id.error_tv);
        set = new HashSet<>();
//        SetTypeFace();
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
        listContainer.setLinearLayout();
        resultAdapter = new SeachResultAdapter(checkPlans, context, new SeachResultAdapter.onItemClickListener() {
            @Override
            public void onItemClick(CheckPlan checkPlan) {
                createDailog(checkPlan);
            }
        });
        listContainer.setPullRefreshEnable(false);
        listContainer.setFooterViewBackgroundColor(R.color.blue_low);
        listContainer.setFooterViewTextColor(R.color.white);
        listContainer.setAdapter(resultAdapter);
        listContainer.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void getDataFailed() {
        super.getDataFailed();
        LogUtil.logI("查询获取数据失败");
        if (checkPlans.size() > 0) {
            if (listContainer.isLoadMore()) {
                listContainer.setPullLoadMoreCompleted();
                page--;
            }
        } else {
            view_search.setVisibility(View.GONE);
            result_view.setVisibility(View.GONE);
            errorText.setText("查询失败，请稍后重试！");
            error_tip.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void getDataSucceed(JsonResult object) {
        view_search.setVisibility(View.GONE);
        LogUtil.logI("获取数据成功");
        if (listContainer.isLoadMore()) {
            listContainer.setPullLoadMoreCompleted();
        }
        if (queryPlans.size() < 20) {
            listContainer.setPushRefreshEnable(false);
        } else {
            listContainer.setPushRefreshEnable(true);
        }
        setResultList(queryPlans);
        result_title.setText(R.string.search_result_internet);

       /* ArrayList<String> paths = new ArrayList<>();
        for (int i = 0; i < checkPlans.size(); i++) {
            paths.add(checkPlans.get(i).getUrl());
        } if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            File file = new File(Environment.getExternalStorageDirectory() + Constants.FILE_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
//            new Thread(new DownloadManager(paths)).start();
        } else {
            Snackbar.make(getView(), R.string.mis_error_no_permission_sdcard, Snackbar.LENGTH_LONG).setAction("设置", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    getActivity().startActivity(intent);
                }
            }).show();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getContext()).showMenu(false);
    }

    private void InitiateSearch() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = stringAdapter.getItem(position);
                edit_text_search.setText(name);
                listView.setVisibility(View.GONE);
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_text_search.getWindowToken(), 0);
                search(name);
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
                listView.setVisibility(View.VISIBLE);
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
                    result_view.setVisibility(View.GONE);
                    clearSearch.setImageResource(R.drawable.up_jt);
                    stringAdapter.initData();
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edit_text_search, InputMethodManager.SHOW_IMPLICIT);
                    IsAdapterEmpty();
                } else {
                    listView.setVisibility(listView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    clearSearch.setImageResource(listView.getVisibility() == View.VISIBLE ? R.drawable.up_jt : R.mipmap.ic_arrow_back);
                }
            }
        });
        listContainer.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                sendRequest(edit_text_search.getText().toString().trim());
            }
        });
        local_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromNet = false;
//                isFromNet = true;
                setResultList(DataUtil.queryCheckPlan(edit_text_search.getText().toString().trim()));
                result_title.setText(R.string.search_result_local);
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
                    search(edit_text_search.getText().toString().trim());
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
                        search(edit_text_search.getText().toString().trim());
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
        result_view.setVisibility(View.GONE);
        mItem.clear();
    }

    public void cleanHistory() {
        stringAdapter = null;
        stringAdapter = new StringAdapter(context, SearchedWordDao.all(context, 1));
        listView.setAdapter(stringAdapter);
    }

    private void search(String item) {
        view_search.setVisibility(View.VISIBLE);
        result_view.setVisibility(View.GONE);
        error_tip.setVisibility(View.GONE);
        if (NetworkUtils.isEnable(context)) {
            isFromNet = true;
            sendRequest(item);
        } else {
            ToastUtil.showShortToast(context, "当前为无网络状态，查询方式为本地查询！");
            isFromNet = false;
            setResultList(DataUtil.queryCheckPlan(item));
        }
    }

    private void sendRequest(String item) {
        if (oldKey == null || !oldKey.equals(item)) {
            page = 1;
            isClear = true;
            oldKey = item;
        } else {
            page++;
            isClear = false;
        }
        HttpRequestVo requestVo = new HttpRequestVo();
        requestVo.getRequestDataMap().put("quxian", JCApplication.user.getQuxian());
        requestVo.getRequestDataMap().put("nameordz", item);
        requestVo.getRequestDataMap().put("page", String.valueOf(page));
        requestVo.setMethodName("GetXxclSc_cx");
        HttpManager manager = new HttpManager(context, this, requestVo);
        manager.postRequest();
    }

    private void setResultList(ArrayList<CheckPlan> list) {
        if (isClear) {
            checkPlans.clear();
        }
        checkPlans.addAll(list);
        view_search.setVisibility(View.GONE);
        if (checkPlans.size() == 0) {
            error_tip.setVisibility(View.VISIBLE);
            result_view.setVisibility(View.GONE);
            errorText.setText("未查询到相关数据！");
        } else {
            error_tip.setVisibility(View.GONE);
            result_view.setVisibility(View.VISIBLE);
            resultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void action(int i, Object object) {
        JsonResult result = null;
        if (RequestListener.EVENT_GET_DATA_SUCCESS == i) {
            result = JsonParser.parseSoap((SoapObject) object);
            queryPlans = JsonParser.getJSONData(result.resData);
        }
        Message msg = new Message();
        msg.what = i;
        msg.obj = result;
        mHandler.sendMessage(msg);
    }

    private void createDailog(final CheckPlan checkPlan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ConstructionDetailView constructionDetailView = ConstructionDetailView.getConstructionView(context);
        View view = constructionDetailView.setData(checkPlan, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_detail) {
                    dialog.dismiss();
                    InfoActivity.ComeInfoActivity(context, checkPlan, isFromNet);
                }
            }
        }, 1);
        builder.setView(view);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

}
