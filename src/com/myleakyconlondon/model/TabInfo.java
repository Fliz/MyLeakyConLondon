package com.myleakyconlondon.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * User: Elizabeth
 * Date: 15/06/13
 * Time: 10:13
 */
public class TabInfo {

    public String tag;
    public Class<?> tabClass;
    public Bundle args;
    public Fragment fragment;

    public TabInfo(String tag, Class<?> tabClass, Bundle args) {
        this.tag = tag;
        this.tabClass = tabClass;
        this.args = args;
    }
}
