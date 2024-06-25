package com.kakusummer.sample.dialog;

import androidx.appcompat.app.AppCompatActivity;

public interface IDialog {
    /**
     * 是否展示
     *
     * @return
     */
    boolean isShowing();

    void show(AppCompatActivity mActivity, String tag);

    void dismiss();

    IDialogDismissListener getOnDismissListener();

    void setOnDismissListener(IDialogDismissListener listener);
}
