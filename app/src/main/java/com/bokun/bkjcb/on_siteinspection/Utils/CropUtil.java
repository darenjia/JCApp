package com.bokun.bkjcb.on_siteinspection.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.bokun.bkjcb.on_siteinspection.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by DengShuai on 2017/7/6.
 */

public class CropUtil {
    private static Context context = JCApplication.getContext();
    private static final int radio_dynamic = 2;
    private static final int radio_square = 1;
    private static final int radio_origin = 0;

    public static void pickFromGallery(Activity activity, int resultCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), resultCode);
    }

    public static void startCropActivity(@NonNull Uri uri, File file, Activity activity) {
        LogUtil.logI(file.getAbsolutePath());
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(file));
        uCrop = basisConfig(uCrop, 2, null, null);
        uCrop = advancedConfig(uCrop);
        uCrop.start(activity);
    }

    public static void startCropActivity(@NonNull Uri uri, File file, Context context, android.support.v4.app.Fragment fragment) {
        LogUtil.logI(file.getAbsolutePath());
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(file));
        uCrop = basisConfig(uCrop, 2, null, null);
        uCrop = advancedConfig(uCrop);
        uCrop.start(context, fragment);
    }

    /**
     * In most cases you need only to set crop aspect ration and max size for resulting image.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private static UCrop basisConfig(@NonNull UCrop uCrop, int id, String x, String y) {
        switch (id) {
            case radio_origin:
                uCrop = uCrop.useSourceImageAspectRatio();
                break;
            case radio_square:
                uCrop = uCrop.withAspectRatio(1, 1);
                break;
            case radio_dynamic:
                // do nothing
                break;
            default:
                try {
                    float ratioX = Float.valueOf(x);
                    float ratioY = Float.valueOf(y);
                    if (ratioX > 0 && ratioY > 0) {
                        uCrop = uCrop.withAspectRatio(ratioX, ratioY);
                    }
                } catch (NumberFormatException e) {
                    Log.i("TAG", String.format("Number please: %s", e.getMessage()));
                }
                break;
        }
        return uCrop;
    }

    /**
     * Sometimes you want to adjust more options, it's done via {@link com.yalantis.ucrop.UCrop.Options} class.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private static UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        options.setCompressionQuality(90);

        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(true);

        /*
        If you want to configure how gestures work for all UCropActivity tabs

        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        * */

        /*
        This sets max size for bitmap that will be decoded from source Uri.
        More size - more memory allocation, default implementation uses screen diagonal.

        options.setMaxBitmapSize(640);
        * */


       /*

        Tune everything (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧

        options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(666);
        options.setDimmedLayerColor(Color.CYAN);
        options.setCircleDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridStrokeWidth(20);
        options.setCropGridColor(Color.GREEN);
        options.setCropGridColumnCount(2);
        options.setCropGridRowCount(1);
        options.setToolbarCropDrawable(R.drawable.your_crop_icon);
        options.setToolbarCancelDrawable(R.drawable.your_cancel_icon);

        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.your_color_res));

        // Aspect ratio options
        options.setAspectRatioOptions(1,
            new AspectRatio("WOW", 1, 2),
            new AspectRatio("MUCH", 3, 4),
            new AspectRatio("RATIO", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
            new AspectRatio("SO", 16, 9),
            new AspectRatio("ASPECT", 1, 1));

       */
        options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        return uCrop.withOptions(options);
    }

    public static void handleCropResult(@NonNull Intent result, File file) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            saveCroppedImage(result, file);
        } else {
            Toast.makeText(context, "剪裁失败！", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(context, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "未知错误！", Toast.LENGTH_SHORT).show();
        }
    }

    private static void saveCroppedImage(Intent intent, File file) {
        Uri imageUri = intent.getData();
        if (imageUri != null && imageUri.getScheme().equals("file")) {
            try {
                copyFileToDownloads(imageUri, file);
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "未知错误！", Toast.LENGTH_SHORT).show();
        }

    }

    private static void copyFileToDownloads(Uri croppedFileUri, File file) throws Exception {

        FileInputStream inStream = new FileInputStream(new File(croppedFileUri.getPath()));
        FileOutputStream outStream = new FileOutputStream(file);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }
}
