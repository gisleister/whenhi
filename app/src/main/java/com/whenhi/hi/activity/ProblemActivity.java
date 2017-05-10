package com.whenhi.hi.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.util.ClickUtil;


public class ProblemActivity extends BaseActivity {

    private String mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar).findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar).findViewById(R.id.toolbar_title);
        textView.setText("问题反馈");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.fanhui);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        TextView problemButton = (TextView)findViewById(R.id.user_problem_button);
        final EditText problemText = (EditText)findViewById(R.id.user_problem_text);


        problemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContent = problemText.getText().toString();
                if(App.isLogin()){
                    if(TextUtils.isEmpty(mContent)){
                        Toast.makeText(ProblemActivity.this, "反馈内容不能为空哦", Toast.LENGTH_SHORT).show();
                    }else{
                        problem();
                    }
                }else{
                    Toast.makeText(ProblemActivity.this, "您还没有登录哦，抓紧一键登录吧", Toast.LENGTH_SHORT).show();
                    ClickUtil.goToLogin(v);
                }



            }
        });

    }


    private void problem(){
        HttpAPI.reportProblem(mContent,new HttpAPI.Callback<BaseModel>() {

            @Override
            public void onSuccess(BaseModel baseModel) {
                if(baseModel.getState() == 0){
                    Toast.makeText(ProblemActivity.this, "您的问题已经反馈成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(ProblemActivity.this, baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ProblemActivity.this, "您的问题反馈失败", Toast.LENGTH_SHORT).show();
            }
        });
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
