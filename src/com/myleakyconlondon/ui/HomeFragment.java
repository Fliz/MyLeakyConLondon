package com.myleakyconlondon.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * User: Elizabeth Hamlet
 * Date: 28/01/13
 * Time: 21:43
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       return inflater.inflate(R.layout.menu, container, false);
    }
}
