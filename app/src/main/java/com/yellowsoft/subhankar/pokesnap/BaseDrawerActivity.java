package com.yellowsoft.subhankar.pokesnap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by subhankar on 8/1/2016.
 */
public class BaseDrawerActivity extends Activity {
    @Override
    public void setContentView(int layoutResID) {
        setContentView(R.layout.activity_drawer);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
    }
}
