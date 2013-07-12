package com.myleakyconlondon.util;

import android.content.Context;
import android.view.View;
import android.widget.TabHost;

/**
 * User: Elizabeth
 * Date: 15/06/13
 * Time: 10:16
 */
public class TabFactory implements TabHost.TabContentFactory {

    private final Context mContext;

    public TabFactory(Context context) {
        mContext = context;
    }

    public View createTabContent(String tag) {
        View v = new View(mContext);
        v.setMinimumWidth(0);
        v.setMinimumHeight(0);
        return v;
    }
}