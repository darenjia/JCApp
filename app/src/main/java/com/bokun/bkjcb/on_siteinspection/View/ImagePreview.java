package com.bokun.bkjcb.on_siteinspection.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LocalTools;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BKJCB on 2017/3/30.
 */

public class ImagePreview implements View.OnClickListener {
    private Context context;
    private List<Bitmap> list;
    private ViewPager viewPager;
    private ButtonClickListener listener;
    private PagerAdapter adapter;
    private EditText fileName;
    private List<String> files;
    private TextView textView;
    private TextView pathName;
    private Button btnDelete;
    private Button btnCancel;
    private String type;
    private LinearLayout.LayoutParams params;

    public interface ButtonClickListener {
        void onDelete();

        void onCancel();
    }

    public ImagePreview(Context context,String type) {
        this.context = context;
        this.type =type;
    }

    public View getImagePreviewView(List<String> imagePath) {
        this.list = new ArrayList<>();
        this.files = imagePath;
        View view = initView();
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,20);
        LoadImageTask task = new LoadImageTask();
        task.execute(imagePath);
//        initViewDate();
        initListener();
        return view;
    }

    private void initViewDate() {
        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                ImageView imageView ;
                if (type.equals("image")) {
                    imageView = LocalTools.setImageView(context,params,null,list.get(position));
                } else if (type.equals("video")) {
                    imageView = LocalTools.setImageView(context,params,new BitmapDrawable(list.get(position)),list.get(position));
                } else {
                    imageView = LocalTools.setImageView(context,params,null,null);
                }
                container.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        String path = files.get(position);
                        File file = new File(path);
                        LogUtil.logI("图片路径" + path);
                        Uri uri;
                        String type;
                        if (Build.VERSION.SDK_INT >= 24) {
                            uri = FileProvider.getUriForFile(context, "com.bokun.bkjcb.on_siteinspection.fileProvider", file);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.parse("file://" + path);
                        }
                        if (path.endsWith(".jpg")) {
                            type = "image/*";
                        } else if (path.endsWith(".mp4")) {
                            type = "video/*";
                        } else {
                            type = "audio/*";
                        }
                        intent.setDataAndType(uri, type);
                        context.startActivity(intent);
                    }
                });
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };
        viewPager.setAdapter(adapter);
        textView.setText("1/" + list.size());
        pathName.setText(getFileName(0));
    }

    @NonNull
    private View initView() {
        View view = View.inflate(context, R.layout.image_pre_view, null);
        textView = (TextView) view.findViewById(R.id.preview_title);
        fileName = (EditText) view.findViewById(R.id.preview_name);
        btnDelete = (Button) view.findViewById(R.id.preview_delete);
        btnCancel = (Button) view.findViewById(R.id.preview_cancel);
        viewPager = (ViewPager) view.findViewById(R.id.preview_viewpager);
        pathName = (TextView) view.findViewById(R.id.preview_file_name);
        return view;
    }

    private void initListener() {
        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        pathName.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText((position + 1) + "/" + list.size());
                pathName.setText(getFileName(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOnClickListener(this);
        fileName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == fileName && !hasFocus) {
                    LogUtil.logI("filename lose focus");
                    fileName.setVisibility(View.GONE);
                    pathName.setVisibility(View.INVISIBLE);
                    pathName.setText(fileName.getText());
                }
            }
        });
        fileName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.change_file_name || id == EditorInfo.IME_NULL) {
                    changeFileName();
                    return true;
                }
                return false;
            }
        });
    }

    private void changeFileName() {
        Toast.makeText(context, "file name change!", Toast.LENGTH_SHORT).show();
        pathName.requestFocus();
    }

    private void getBitmap(List<String> imagePath) {
        Bitmap bitmap;
        String path;
        for (int i = 0; i < imagePath.size(); i++) {
            path = imagePath.get(i);
            if (path.endsWith(".jpg")) {
                bitmap = Utils.compressBitmap(imagePath.get(i));
            } else if (path.endsWith(".mp4")) {
                bitmap = ThumbnailUtils.createVideoThumbnail(imagePath.get(i), MediaStore.Images.Thumbnails.MINI_KIND);
            } else {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.vector_drawable_music);
            }
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_error);
            }
            list.add(bitmap);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.preview_delete) {
            list.remove(viewPager.getCurrentItem());
            adapter.notifyDataSetChanged();
            LogUtil.logI("size" + list.size());
            listener.onDelete();
            if (list.size() == 0) {
                listener.onCancel();
            }
            textView.setText(String.format("%d/%d", viewPager.getCurrentItem() + 1, list.size()));
        } else if (v.getId() == R.id.preview_cancel) {
            listener.onCancel();
        } else if (v.getId() == R.id.preview_file_name) {
            fileName.setText(pathName.getText());
            fileName.setVisibility(View.VISIBLE);
            pathName.setVisibility(View.GONE);
            fileName.requestFocus();
        } else {
            Toast.makeText(context, "click!", Toast.LENGTH_SHORT).show();
        }
    }

    public int getPosition() {
        return viewPager.getCurrentItem();
    }

    public void setListener(ButtonClickListener listener) {
        this.listener = listener;
    }

    public String getFileName(int position) {
        String path = files.get(position);
        return path.substring(path.lastIndexOf("/") + 1);
    }

    class LoadImageTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            getBitmap((List<String>) objects[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            initViewDate();
        }
    }
}
