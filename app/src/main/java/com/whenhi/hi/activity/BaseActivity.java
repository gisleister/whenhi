package com.whenhi.hi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;


/**
 * Created by 王雷 on 2016/12/29.
 */

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainActicity");
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainActicity");
        MobclickAgent.onPause(this);
    }


}
