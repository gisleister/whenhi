package com.whenhi.hi.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.AboutActivity;
import com.whenhi.hi.activity.ChargeRecordActivity;
import com.whenhi.hi.activity.FavActivity;
import com.whenhi.hi.activity.IncomeRecordActivity;
import com.whenhi.hi.activity.LoginActivity;
import com.whenhi.hi.activity.OtherActivity;
import com.whenhi.hi.activity.ChargeActivity;
import com.whenhi.hi.activity.ProblemActivity;
import com.whenhi.hi.activity.RewardActivity;
import com.whenhi.hi.activity.SettingActivity;
import com.whenhi.hi.activity.ShareActivity;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.listener.LoginListener;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.UserModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.BindUtil;
import com.whenhi.hi.util.ClickUtil;
import com.whenhi.hi.util.DateUtil;

import org.w3c.dom.Text;

/**
 * Created by 王雷 on 2016/12/26.
 */

public class MoreNavFragment extends BaseFragment {
    private static final String TAG = MoreNavFragment.class.getSimpleName();
    private boolean isLogin = false;
    private int checkinState = 0;
    private String mobile = null;
    private Button checkIn;
    private View mView;
    private ImageView userAvatar;
    private TextView userNickname;
    private TextView incomeCount;
    private TextView favCount;

    public static Fragment newInstance() {
        MoreNavFragment fragment = new MoreNavFragment();
        return fragment;
    }

    public MoreNavFragment() {

        isLogin = App.isLogin();
        NoticeTransfer  noticeTransfer = new NoticeTransfer();
        noticeTransfer.setLoginListener(mLoginListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_nav, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;

        Context context = view.getContext();
        if (context instanceof AppCompatActivity){
            final AppCompatActivity activity = (AppCompatActivity)context;
            Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar).findViewById(R.id.toolbar);
            activity.setSupportActionBar(toolbar);
            android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
            }

        }


        checkIn = (Button)view.findViewById(R.id.user_checkin);
        userAvatar= (ImageView) view.findViewById(R.id.user_avater);
        userNickname= (TextView) view.findViewById(R.id.user_nickname);
        incomeCount = (TextView)view.findViewById(R.id.user_income_count);
        favCount = (TextView)view.findViewById(R.id.user_fav_count);


        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkinState == 0){
                    if(App.isLogin()){
                        checkin();
                    }else{
                        ClickUtil.goToLogin(mView);
                    }
                }else{
                    Toast.makeText(mView.getContext(), "您已经签到过了哦", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if(isLogin){
            getUserInfo(view);
        }

        RelativeLayout userInfo = (RelativeLayout)view.findViewById(R.id.user_info_layout);
        userInfo.setOnClickListener(new RelativeLayout.OnClickListener(){//创建监听
            public void onClick(View v) {

                if(!isLogin){
                    ClickUtil.goToLogin(view);
                }

            }

        });

        TextView income = (TextView)view.findViewById(R.id.user_income_text);
        income.setOnClickListener(new RelativeLayout.OnClickListener(){//创建监听
            public void onClick(View v) {

                if(isLogin){
                    Intent intent = new Intent(view.getContext(), IncomeRecordActivity.class);
                    view.getContext().startActivity(intent);
                }else{
                    ClickUtil.goToLogin(view);
                }

            }

        });


        TextView fav = (TextView)view.findViewById(R.id.user_fav_text);
        fav.setOnClickListener(new RelativeLayout.OnClickListener(){//创建监听
            public void onClick(View v) {

                if(isLogin){
                    Intent intent = new Intent(view.getContext(), FavActivity.class);
                    view.getContext().startActivity(intent);
                }else{
                    ClickUtil.goToLogin(view);
                }


            }

        });

        RelativeLayout recharge = (RelativeLayout)view.findViewById(R.id.user_recharge_layout);
        recharge.setOnClickListener(new RelativeLayout.OnClickListener(){//创建监听
            public void onClick(View v) {

                if(isLogin){
                    if(TextUtils.isEmpty(mobile)){//当用户已经登录但是没有绑定手机号时执行
                        smsCode(view,1);
                    }else{
                        goToPhone(view,mobile);
                    }

                }else{
                    ClickUtil.goToLogin(view);
                }


            }

        });


        RelativeLayout userCode = (RelativeLayout)view.findViewById(R.id.user_code_layout);
        userCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogin){
                    if(TextUtils.isEmpty(mobile)){
                        smsCode(view,2);//用户已经登录但是没有绑定手机号需要绑定手机号先
                    }else{
                        userCode(view);//用户已登录状态下邀请码验证
                    }

                }else {
                    ClickUtil.goToLogin(view);
                }
            }
        });


        RelativeLayout about = (RelativeLayout)view.findViewById(R.id.user_about_layout);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AboutActivity.class);

                view.getContext().startActivity(intent);
            }
        });

        RelativeLayout reward = (RelativeLayout)view.findViewById(R.id.user_reward_layout);
        reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), RewardActivity.class);

                view.getContext().startActivity(intent);
            }
        });


        RelativeLayout setting = (RelativeLayout)view.findViewById(R.id.user_setup_layout);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), SettingActivity.class);

                view.getContext().startActivity(intent);
            }
        });

        RelativeLayout invite = (RelativeLayout)view.findViewById(R.id.user_invite_layout);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogin){
                    goToShare(view);
                }else{
                    ClickUtil.goToLogin(view);
                }

            }
        });

        RelativeLayout problem = (RelativeLayout)view.findViewById(R.id.user_problem_layout);
        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(view.getContext(), ProblemActivity.class);

                view.getContext().startActivity(intent);
            }
        });

        RelativeLayout chargeRecord = (RelativeLayout)view.findViewById(R.id.user_recharge_record_layout);
        chargeRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isLogin){
                    Intent intent = new Intent(view.getContext(), ChargeRecordActivity.class);

                    view.getContext().startActivity(intent);
                }else{
                    ClickUtil.goToLogin(view);
                }

            }
        });





    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void goToShare(View view){//我的里面邀请好友 type=1
        Feed feed = new Feed();
        feed.setType(1);
        feed.setTitle("每月最多领取150元话费");
        feed.setContent("很嗨-汇聚全世界最优质最搞笑的内容");
        String invitePageUrl = App.getInvitePageUrl();
        String userLogo = App.getUserLogo();

        feed.setShareUrl(invitePageUrl);
        feed.setImageUrl(userLogo);
        ClickUtil.goToShare(view, feed);
    }


    private void getUserInfo(final View view){
        HttpAPI.getUserDetail(new HttpAPI.Callback<UserModel>() {
            @Override
            public void onSuccess(UserModel userModel) {
                if(userModel.getState() == 0){

                    incomeCount.setText(""+userModel.getData().getScore());
                    favCount.setText(""+userModel.getData().getFavoriteCount());



                    userNickname.setText(userModel.getData().getName());
                    checkinState = userModel.getData().getCheckinState();
                    mobile = userModel.getData().getMobile();
                    isLogin = App.isLogin();
                    App.userInit(userModel.getData());
                    NoticeTransfer.refreshMeesage();
                    if(checkinState == 1){
                        checkIn.setText("已签到");
                        checkIn.setBackgroundResource(R.drawable.shape_button_no);
                    }


                    Glide.with(App.getContext())
                            .load(userModel.getData().getLogo())
                            .transform(new CircleTransform(view.getContext()))
                            .error(R.mipmap.user_default)
                            .into(userAvatar);

                    BindUtil.bindJPush();//执行绑定操作为push做准备
                }else{

                }

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(mView.getContext(), "用户系统有点小问题", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 邀请码验证
     * @param view
     */
    private void userCode(View view){
        Intent intent = new Intent(view.getContext(), OtherActivity.class);

        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("mobile", mobile);
        bundle.putInt("type",2);
        bundle.putInt("smsType",1);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
        Context context = view.getContext();
        if (context instanceof Activity){
            Activity activity = (Activity)context;
            activity.overridePendingTransition(R.anim.activity_open,0);
        }
    }

    /**
     * 短信验证
     * @param view
     */
    private void smsCode(View view,int smsType){//smsType 1代表验证完成后直接进入充值页面 2代表进入输入邀请码界面
        Intent intent = new Intent(view.getContext(), OtherActivity.class);

        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("mobile", mobile);
        bundle.putInt("type",1);
        bundle.putInt("smsType",smsType);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
        Context context = view.getContext();
        if (context instanceof Activity){
            Activity activity = (Activity)context;
            activity.overridePendingTransition(R.anim.activity_open,0);
        }
    }


    private void checkin(){
        HttpAPI.userCheckini(new HttpAPI.Callback<BaseModel>() {
            //            @Override
            public void onSuccess(BaseModel baseModel) {
                if(baseModel.getState() == 0){
                    getUserInfo(mView);
                    checkinState = 1;
                    checkIn.setBackgroundResource(R.drawable.shape_button_no);
                    checkIn.setText("已签到");
                    String data = baseModel.getData();
                    if(!TextUtils.isEmpty(data)){
                        NoticeTransfer.refresh();
                        Toast.makeText(App.getContext(), data, Toast.LENGTH_SHORT).show();
                    }
                }else{

                }

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(mView.getContext(),"签到系统有点小问题", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToPhone(View view, String mobile){

        Intent intent = new Intent(view.getContext(), ChargeActivity.class);

        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("mobile", mobile);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);

    }



    private LoginListener mLoginListener = new LoginListener() {
        @Override
        public void login(boolean is) {
            if(is){
                getUserInfo(mView);
                isLogin = App.isLogin();
            }
        }

        @Override
        public void logout(boolean is) {
            isLogin = App.isLogin();

            incomeCount.setText("0");
            favCount.setText("0");



            userNickname.setText("点击登录兑换话费");
            checkinState = 0;
            mobile = null;
            checkIn.setBackgroundResource(R.drawable.shape_button);
            checkIn.setText("签到");
            NoticeTransfer.refreshMeesage();

            Glide.with(App.getContext())
                    .load(R.mipmap.user_default)
                    .transform(new CircleTransform(mView.getContext()))
                    .error(R.mipmap.user_default)
                    .into(userAvatar);
        }

        @Override
        public void refresh() {
            getUserInfo(mView);
            isLogin = App.isLogin();
        }

        @Override
        public void mobile(String m) {
            mobile = m;
        }
    };




}
