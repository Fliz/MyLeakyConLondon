package com.myleakyconlondon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * User: Elizabeth Hamlet
 */
public class LeakyConLondonScheduleActivity extends FragmentActivity {

    @Override
        public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void detailAdd(View view) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        startActivity(intent);
    }
}
