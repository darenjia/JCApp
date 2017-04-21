package com.bokun.bkjcb.on_siteinspection.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by BKJCB on 2017/3/24.
 */

public class AudioRecordDialog extends Dialog{
    public AudioRecordDialog(@NonNull Context context) {
        super(context);
    }

    protected AudioRecordDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setContentView(@NonNull View view) {

    }
}
