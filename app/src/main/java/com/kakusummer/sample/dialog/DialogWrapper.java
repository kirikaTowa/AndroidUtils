package com.kakusummer.sample.dialog;


public class DialogWrapper {
    /**
     * 窗口管理
     */
    private IDialog mDialog;

    private int mPriority;

    private boolean isCanShow;

    private String tag;

    private DialogWrapper(Builder builder) {
        mDialog = builder.mDialog;
        mPriority = builder.mPriority;
        isCanShow = builder.isCanShow;
        tag = builder.tag;
    }

    public IDialog getDialog() {
        return mDialog;
    }

    public void setDialog(IDialog dialog) {
        mDialog = dialog;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public boolean isCanShow() {
        return isCanShow;
    }

    public void setCanShow(boolean canShow) {
        isCanShow = canShow;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public static class Builder {
        private IDialog mDialog;

        private int mPriority;

        private boolean isCanShow;

        private String tag;

        public Builder dialog(IDialog dialog) {
            this.mDialog = dialog;
            return this;
        }

        public Builder priority(int mPriority) {
            this.mPriority = mPriority;
            return this;
        }

        public Builder isCanShow(boolean flag) {
            this.isCanShow = flag;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public DialogWrapper build() {
            return new DialogWrapper(this);
        }
    }
}
