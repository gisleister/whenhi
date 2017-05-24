package com.whenhi.hi.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.model.UpdateData;
import com.whenhi.hi.model.UpdateModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.CacheUtil;


public class SettingActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar).findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar).findViewById(R.id.toolbar_title);
        textView.setText("设置");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.fanhui);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        long cacheSize = 0;
        final TextView cacheText = (TextView)findViewById(R.id.setting_cache_text);
        try {
            cacheSize = CacheUtil.getInstance().getFolderSize(App.getContext().getCacheDir());
            if (cacheSize > 0){
                String cacheStr = CacheUtil.getInstance().getFormatSize(cacheSize);
                cacheText.setText(cacheStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        LinearLayout cache = (LinearLayout) findViewById(R.id.setting_cache_layout);
        cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DataCleanManager.cleanInternalCache(App.getContext());
                CacheUtil.getInstance().clearImageDiskCache();
                cacheText.setText("0M");
                Toast.makeText(SettingActivity.this, "您的缓存已经全部清空", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout version = (LinearLayout) findViewById(R.id.setting_version_layout);
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateApp();
            }
        });
        TextView versionText = (TextView)findViewById(R.id.setting_version_text);
        versionText.setText(App.getAppVersionName());


        LinearLayout about = (LinearLayout) findViewById(R.id.setting_about_layout);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });


        TextView logout = (TextView) findViewById(R.id.setting_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.loginout();
                NoticeTransfer.logout(true);
                Toast.makeText(SettingActivity.this, "您已经退出登录", Toast.LENGTH_SHORT).show();
                finish();
            }
        });




    }


    private void updateApp(){
        HttpAPI.updateApp(new HttpAPI.Callback<UpdateModel>() {
            @Override
            public void onSuccess(UpdateModel updateModel) {
                if(updateModel.getState() == 0){
                    UpdateData updateData = updateModel.getData();
                    boolean isForce = false;
                    String updateText = updateData.getVersionDesc();
                    String url = updateData.getDownloadUrl();
                    int type = updateData.getUpgradeType();
                    if (type == 2){
                        isForce = true;
                        updateDialog(isForce,updateText,url);
                    }else if(type == 1){
                        updateDialog(isForce,updateText,url);
                    }else{
                        Toast.makeText(SettingActivity.this, "您已经是最新版本了", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SettingActivity.this, "系统貌似出问题了", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(SettingActivity.this, "系统貌似出问题了", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateDialog(final boolean isForce, String updateText, final String url){
        Holder holder = new ViewHolder(R.layout.layout_dialog_update);

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(Gravity.CENTER)
                .setMargin(80, 0, 80, 0)
                .setPadding(0, 0, 0, 0)
                .setExpanded(false)//设置扩展模式可控制dialog的高度
                .setContentBackgroundResource(R.drawable.shape_caidan)

                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.update_ok:
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(url);
                                intent.setData(content_url);
                                startActivity(intent);
                                if(!isForce){
                                    dialog.dismiss();
                                }

                                break;
                            case R.id.update_close:
                                dialog.dismiss();
                                break;
                        }

                    }
                })
                .create();
        dialog.show();

        //内容的更新要在dialog显示之后进行
        TextView updateContent =  (TextView) holder.getInflatedView().findViewById(R.id.update_text);
        updateContent.setText(updateText);

        Button button = (Button)holder.getInflatedView().findViewById(R.id.update_close);
        if(isForce){
            button.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
