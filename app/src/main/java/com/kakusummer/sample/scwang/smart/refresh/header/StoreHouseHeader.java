package com.kakusummer.sample.scwang.smart.refresh.header;

import android.content.Context;
import android.util.AttributeSet;

import com.kakusummer.sample.scwang.smart.refresh.header.StoreHouseHeaderSource;
import com.scwang.smart.refresh.layout.api.RefreshHeader;

/**
 * StoreHouseHeader
 * Created by scwang on 2017/5/31.
 * from https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh
 */
public class StoreHouseHeader extends StoreHouseHeaderSource implements RefreshHeader {

    //<editor-fold desc="View">
    public StoreHouseHeader(Context context) {
        this(context, null);
    }

    public StoreHouseHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //</editor-fold>
}