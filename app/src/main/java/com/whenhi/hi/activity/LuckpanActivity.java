package com.whenhi.hi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.BaseFeedModel;
import com.whenhi.hi.model.LuckModel;
import com.whenhi.hi.model.Rule;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.util.Util;
import com.whenhi.hi.view.luckpan.EnvironmentLayout;
import com.whenhi.hi.view.luckpan.LuckPanLayout;
import com.whenhi.hi.view.luckpan.RotatePan;

import java.util.List;


public class LuckpanActivity extends BaseActivity implements RotatePan.AnimationEndListener{

    private EnvironmentLayout layout;
    private RotatePan rotatePan;
    private LuckPanLayout luckPanLayout;
    private ImageView goBtn;
    private ImageView yunIv;
    private TextView hitUser;
    private TextView gameRule;

    private Feed mFeed;

    private String tips;

    private String[] prizes = {"一 等 奖","二 等 奖","三 等 奖","四 等 奖","五 等 奖","谢 谢 参 与"};;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luckpan);

        Intent intent = getIntent();
        mFeed = (Feed)intent.getSerializableExtra("Feed");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar).findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar).findViewById(R.id.toolbar_title);
        textView.setText("幸运大抽奖");
        setSupportActionBar(toolbar);

        initData();

    }


    private void initData(){
        HttpAPI.lotteryDetail(mFeed.getId(),mFeed.getFeedCategory(), new HttpAPI.Callback<BaseFeedModel>() {
            @Override
            public void onSuccess(BaseFeedModel baseFeedModel) {


                if(baseFeedModel.getState() == 0){
                    List<Rule> rules = baseFeedModel.getData().getRules();
                    initView(rules,baseFeedModel.getData());
                }else{
                    Toast.makeText(App.getContext(), baseFeedModel.getMsgText(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    private void initView(List<Rule> rules, Feed feed){

        for(int i = 0; i < rules.size(); i++){
            Rule rule = rules.get(i);
            prizes[i] = rule.getName();
        }

        hitUser = (TextView) findViewById(R.id.hit_user_text);
        gameRule = (TextView) findViewById(R.id.game_rule_text);

        hitUser.setText(feed.getLotteryCountDesc());
        gameRule.setText(feed.getContent());

        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        luckPanLayout.startLuckLight();
        rotatePan = (RotatePan) findViewById(R.id.rotatePan);
        rotatePan.setPrizes(prizes);
        rotatePan.setAnimationEndListener(this);
        goBtn = (ImageView)findViewById(R.id.go);
        yunIv = (ImageView)findViewById(R.id.yun);

        luckPanLayout.post(new Runnable() {
            @Override
            public void run() {
                int height =  getWindow().getDecorView().getHeight();
                int width = getWindow().getDecorView().getWidth();

                int backHeight = 0;

                int MinValue = Math.min(width,height);
                MinValue -= Util.dip2px(LuckpanActivity.this,10)*2;
                backHeight = MinValue/2;

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) luckPanLayout.getLayoutParams();
                lp.width = MinValue;
                lp.height = MinValue;

                luckPanLayout.setLayoutParams(lp);

                MinValue -= Util.dip2px(LuckpanActivity.this,28)*2;
                lp = (RelativeLayout.LayoutParams) rotatePan.getLayoutParams();
                lp.height = MinValue;
                lp.width = MinValue;

                rotatePan.setLayoutParams(lp);


                lp = (RelativeLayout.LayoutParams) goBtn.getLayoutParams();
                lp.topMargin += backHeight;
                lp.topMargin -= (goBtn.getHeight()/2);
                goBtn.setLayoutParams(lp);

                getWindow().getDecorView().requestLayout();


            }
        });
    }




    public void rotation(View view){
        initGo();

    }

    private void initGo(){
        HttpAPI.lotteryGo(mFeed.getId(),new HttpAPI.Callback<LuckModel>() {
            @Override
            public void onSuccess(LuckModel luckModel) {




                if(luckModel.getState() == 0){

                    //Toast.makeText(App.getContext(), ""+luckModel.getData().getResultId(), Toast.LENGTH_SHORT).show();
                    int luckId = luckModel.getData().getResultId();
                    if(luckId == -1){
                        showTips(LuckpanActivity.this,luckModel.getData().getTips(),"休息下吧");
                    }else{
                        hitUser.setText(luckModel.getData().getLotteryCountDesc());
                        if(luckId > 0){
                            tips = luckModel.getData().getTips();
                            rotatePan.startRotate(luckId - 1);
                            luckPanLayout.setDelayTime(100);
                            goBtn.setEnabled(false);
                        }

                    }

                }else{
                    Toast.makeText(App.getContext(), luckModel.getMsgText(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


    public static void showTips(Context context, String tips, String title){
        Intent intent = new Intent(context, OtherActivity.class);

        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("tips", tips);
        bundle.putString("title", title);
        bundle.putInt("type",4);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (context instanceof Activity){
            Activity activity = (Activity)context;
            activity.overridePendingTransition(R.anim.activity_open,0);
        }
    }

    @Override
    public void endAnimation(int position) {
        goBtn.setEnabled(true);
        luckPanLayout.setDelayTime(500);
        showTips(LuckpanActivity.this,tips,"恭喜您了");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
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


}
