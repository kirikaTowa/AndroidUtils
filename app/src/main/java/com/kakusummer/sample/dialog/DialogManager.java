package com.kakusummer.sample.dialog;

import android.app.Activity;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class DialogManager {
    private boolean isBlockTask;// 是否阻塞所有弹窗显示
    private DialogCompleteCallback mCallback;
    private List<DialogWrapper> mDialogs;

    public static DialogManager getInstance() {
        DialogManager manager = Holder.INSTANCE;
        return manager;
    }

    public boolean isBlockTask() {
        return isBlockTask;
    }

    public void setBlockTask(boolean blockTask) {
        isBlockTask = blockTask;
    }

    public synchronized void addDialog(AppCompatActivity mActivity, DialogWrapper dialogWrapper) {
        if (dialogWrapper != null) {
            if (mDialogs == null) {
                mDialogs = new ArrayList<>();
            }

            if (hasAddWindow(dialogWrapper)) {
                return;
            }

            if (dialogWrapper.getDialog() != null) {
                dialogWrapper.getDialog().setOnDismissListener(() -> {
                    mDialogs.remove(dialogWrapper);
                    Log.d("yeTest", "addDialog setOnDismissListener showNextDialog: ");
                    showNextDialog(mActivity);
                });
            }
            mDialogs.add(dialogWrapper);
        }
    }

    public synchronized void showDialog(AppCompatActivity activity, int priority, IDialog dialog) {
        DialogWrapper wrapper = getTargetDialog(priority);
        if (wrapper != null) {
            if (wrapper.getDialog() == null) {
                IDialogDismissListener oldListener = dialog.getOnDismissListener();
                if (!isBlockTask) {
                    showTargetDialog(activity, wrapper);
                }
            }
        } else {
            DialogWrapper newWrapper = new DialogWrapper.Builder()
                    .priority(priority)
                    .isCanShow(true)
                    .dialog(dialog)
                    .build();
            dialog.setOnDismissListener(() -> {
                mDialogs.remove(newWrapper);
                Log.d("yeTest", "showDialog setOnDismissListener showNextDialog: ");
                showNextDialog(activity);
            });
            addDialog(activity, newWrapper);
            if (!isBlockTask) {
                showTargetDialog(activity, newWrapper);
            }
        }
    }

    private void checkCapacity(DialogWrapper dialogWrapper) {
        if (mDialogs != null) {

            int minPriority = 0;
            int position = 0;
            for (int i = 0; i < mDialogs.size(); i++) {
                DialogWrapper wrapper = mDialogs.get(i);
                if (i == 0) {
                    minPriority = wrapper.getPriority();
                }
                if (wrapper.getPriority() < minPriority) {
                    position = i;
                }
            }
            mDialogs.remove(position);

            mDialogs.add(dialogWrapper);
        }
    }

    /**
     * 展示弹窗
     * 从优先级最高的Window开始显示
     */
    public synchronized void show(AppCompatActivity activity) {
        DialogWrapper wrapper = getMaxPriorityDialog();
        DialogWrapper topShowWindow = getShowingWindow();
        if (wrapper != null && wrapper.isCanShow() && topShowWindow == null) {
            IDialog dialog = wrapper.getDialog();
            Log.d("yeTest", "show: "+dialog);
            String tag = wrapper.getTag();
            if (activity.getSupportFragmentManager().findFragmentByTag(tag) == null) {
                if (dialog != null && isActivityAlive(activity)) {
                    dialog.show(activity, wrapper.getTag());
                }
            }
        }
    }

    /**
     * 展示目标弹窗
     */
    public void showTargetDialog(AppCompatActivity activity, DialogWrapper wrapper) {
        if (wrapper != null && wrapper.getDialog() != null) {
            DialogWrapper topShowWindow = getShowingWindow();
            if (topShowWindow == null) {
                if (wrapper.getDialog() != null && wrapper.isCanShow()) {
                    wrapper.getDialog().show(activity, wrapper.getTag());
                }
            }
//            if (topShowWindow == null) {
//                int priority = wrapper.getPriority();
//                DialogWrapper maxPriorityWindow = getMaxPriorityDialog();
//                if (maxPriorityWindow != null && wrapper.isCanShow() && priority >= maxPriorityWindow.getPriority()) {
//                    if (wrapper.getDialog() != null && isActivityAlive(activity)) {
//                        wrapper.getDialog().show(activity, wrapper.getTag());
//                    }
//                }
//            }
        }
    }

    private synchronized DialogWrapper getTargetDialog(int priority) {
        if (mDialogs != null) {
            for (int i = 0, size = mDialogs.size(); i < size; i++) {
                DialogWrapper wrapper = mDialogs.get(i);
                if (wrapper != null && wrapper.getPriority() == priority) {
                    return wrapper;
                }
            }
        }
        return null;
    }

    /**
     * 获取当前处于show状态的弹窗
     */
    public synchronized DialogWrapper getShowingWindow() {
        if (mDialogs != null) {
            for (int i = 0, size = mDialogs.size(); i < size; i++) {
                DialogWrapper wrapper = mDialogs.get(i);
                if (wrapper != null && wrapper.getDialog() != null && wrapper.getDialog().isShowing()) {
                    return wrapper;
                }
            }
        }
        return null;
    }

    /**
     * 隐藏指定dialog
     *
     * @param priority
     */
    public synchronized void dismissTargetDialog(int priority) {
        if (mDialogs == null) {
            return;
        }
        for (DialogWrapper dialog : mDialogs) {
            if (dialog.getPriority() == priority) {
                if (dialog.getDialog() != null && dialog.getDialog().isShowing()) {
                    dialog.getDialog().dismiss();
                }
            }
        }
    }

    /**
     * 展示下一个优先级最大的Window
     */
    public synchronized void showNextDialog(AppCompatActivity mActivity) {
        if (isBlockTask || mCallback == null) {
            return;
        }
        DialogWrapper dialogWrapper = getMaxPriorityDialog();
        if (dialogWrapper != null && dialogWrapper.isCanShow()) {
            if (dialogWrapper.getDialog() != null && isActivityAlive(mActivity)) {
                dialogWrapper.getDialog().show(mActivity, dialogWrapper.getTag());
                return;
            }
        }
        if (dialogWrapper != null && dialogWrapper.getDialog() == null) {
            mCallback.complete();
            return;
        }
        if (dialogWrapper == null) {
            mCallback.complete();
        }
    }

    private boolean isActivityAlive(Activity activity) {
        return activity != null
                && !activity.isDestroyed()
                && !activity.isFinishing();
    }

    private DialogWrapper getMaxPriorityDialog() {
        Log.d("yeTest", "当前Dialog列表: "+mDialogs.size());
        if (mDialogs != null) {
            int maxPriority = -1;
            int position = -1;
            for (int i = 0; i < mDialogs.size(); i++) {
                DialogWrapper wrapper = mDialogs.get(i);
                if (wrapper.getDialog() == null) {
                    continue;
                }
                if (wrapper.getPriority() > maxPriority) {
                    position = i;
                    maxPriority = wrapper.getPriority();
                }
            }
            if (position != -1) {
                return mDialogs.get(position);
            }
        }
        return null;
    }

    private boolean hasAddWindow(DialogWrapper dialogWrapper) {
        if (mDialogs != null) {
            for (int i = 0; i < mDialogs.size(); i++) {
                DialogWrapper wrapper = mDialogs.get(i);
                if (wrapper != null) {
                    return wrapper.getPriority() == dialogWrapper.getPriority();
                }
            }
        }
        return false;
    }

    /**
     * 清除弹窗管理者
     *
     * @param dismiss 是否同时dismiss掉弹窗管理者维护的弹窗
     */
    public synchronized void clear(boolean dismiss) {
        try {
            if (mDialogs != null) {
                if (dismiss) {
                    for (int i = 0, size = mDialogs.size(); i < size; i++) {
                        if (mDialogs.get(i) != null) {
                            IDialog window = mDialogs.get(i).getDialog();
                            if (window != null && window.isShowing()) {
                                window.dismiss();
                            }
                        }
                    }
                }
                mDialogs.clear();
            }
        } catch (Exception e) {
        }
    }

    public void setDialogCompleteCallback(DialogCompleteCallback callback) {
        mCallback = callback;
    }

    private static class Holder {
        private static final DialogManager INSTANCE = new DialogManager();
    }
}
