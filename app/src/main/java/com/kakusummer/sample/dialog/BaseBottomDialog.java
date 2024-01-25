package com.kakusummer.sample.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kakusummer.androidutils.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


import org.jetbrains.annotations.NotNull;


public abstract class BaseBottomDialog<T extends ViewDataBinding> extends BottomSheetDialogFragment implements IDialog {
    protected Activity mActivity;
    protected T mBind;

    private IDialogDismissListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogStyle);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(mActivity,getStyle());
    }

    protected abstract int getLayoutID();


    protected  int getStyle(){
        return R.style.BottomSheetStyle;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, getLayoutID(), container, false);
        mBind.setLifecycleOwner(this);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return mBind.getRoot();
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        processLogic();

    }

    protected void processLogic() {

    }



    protected void init() {
    }


    @Override
    public void show(@NotNull FragmentManager manager, String tag) {
        if (this.isAdded()) {
            return;
        }
        if (manager.findFragmentByTag(tag) != null) {
            return;
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }


    @Override
    public boolean isShowing() {
        if (getDialog() == null) {
            return false;
        }
        return getDialog().isShowing();
    }



    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener!=null) {
            listener.onDismiss();
        }
    }

    @Override
    public void show(AppCompatActivity mActivity, String tag) {
        show(mActivity.getSupportFragmentManager(), tag);
    }

    @Override
    public IDialogDismissListener getOnDismissListener() {
        return this.listener;
    }

    @Override
    public void setOnDismissListener(IDialogDismissListener listener) {
        this.listener = listener;
    }




}
