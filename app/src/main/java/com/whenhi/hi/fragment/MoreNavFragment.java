package com.whenhi.hi.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.MoneyRecordActivity;
import com.whenhi.hi.activity.FavActivity;
import com.whenhi.hi.activity.MyHaibiRecordActivity;
import com.whenhi.hi.activity.LookActivity;
import com.whenhi.hi.activity.MoneyActivity;
import com.whenhi.hi.activity.OtherActivity;
import com.whenhi.hi.activity.ProblemActivity;
import com.whenhi.hi.activity.SettingActivity;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.listener.LoginListener;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.UserModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.BindUtil;
import com.whenhi.hi.util.ClickUtil;

/**
 * Created by 王雷 on 2016/12/26.
 */

public class MoreNavFragment extends BaseFragment {
    private static final String TAG = MoreNavFragment.class.getSimpleName();


    private int checkinState = 0;
    private String mobile = null;
    private Button checkIn;
    private ImageView userAvatar;
    private TextView userNickname;
    private TextView haibiCount;
    private TextView favCount;
    private TextView userContent;
    private TextView userMoney;


    public static Fragment newInstance() {
        MoreNavFragment fragment = new MoreNavFragment();
        return fragment;
    }

    public MoreNavFragment() {
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

        initView(view);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkinState == 0){
                    if(App.isLogin()){
                        checkin(v);
                    }else{
                        ClickUtil.goToLogin(view.getContext());
                    }
                }else{
                    Toast.makeText(view.getContext(), "您已经签到过了哦", Toast.LENGTH_SHORT).show();
                }

            }
        });

        LinearLayout user = (LinearLayout)view.findViewById(R.id.user_layout);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.isLogin()) {
                    ClickUtil.goToLogin(view.getContext());
                }
            }
        });

        LinearLayout haibi = (LinearLayout)view.findViewById(R.id.user_haibi_layout);
        haibi.setOnClickListener(new RelativeLayout.OnClickListener(){//创建监听
            public void onClick(View v) {

                if(App.isLogin()){
                    Intent intent = new Intent(view.getContext(), MyHaibiRecordActivity.class);
                    view.getContext().startActivity(intent);
                }else{
                    ClickUtil.goToLogin(view.getContext());
                }

            }

        });

        LinearLayout fav = (LinearLayout)view.findViewById(R.id.user_fav_layout);
        fav.setOnClickListener(new RelativeLayout.OnClickListener(){//创建监听
            public void onClick(View v) {

                if(App.isLogin()){
                    Intent intent = new Intent(view.getContext(), FavActivity.class);
                    view.getContext().startActivity(intent);
                }else{
                    ClickUtil.goToLogin(view.getContext());
                }


            }

        });

        LinearLayout money = (LinearLayout)view.findViewById(R.id.how_money);
        money.setOnClickListener(new RelativeLayout.OnClickListener(){//创建监听
            public void onClick(View v) {

                if(App.isLogin()){
                    if(TextUtils.isEmpty(mobile)){//当用户已经登录但是没有绑定手机号时执行
                        smsCode(view,1);
                    }else{
                        goToMoney(view);
                    }

                }else{
                    ClickUtil.goToLogin(view.getContext());
                }


            }

        });

        LinearLayout gomoney = (LinearLayout)view.findViewById(R.id.user_money_layout);
        gomoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(App.isLogin()){
                    if(TextUtils.isEmpty(mobile)){//当用户已经登录但是没有绑定手机号时执行
                        smsCode(view,3);
                    }else{
                        Intent intent = new Intent(view.getContext(), MoneyRecordActivity.class);

                        view.getContext().startActivity(intent);
                    }
                }else{
                    ClickUtil.goToLogin(view.getContext());
                }

            }
        });

        LinearLayout problem = (LinearLayout)view.findViewById(R.id.user_problem_layout);
        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(view.getContext(), ProblemActivity.class);

                view.getContext().startActivity(intent);
            }
        });

        LinearLayout friends = (LinearLayout)view.findViewById(R.id.user_friends_layout);
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(App.isLogin()){
                    goToShare(view);
                }else{
                    ClickUtil.goToLogin(view.getContext());
                }

            }
        });

        LinearLayout setting = (LinearLayout)view.findViewById(R.id.user_setting_layout);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), SettingActivity.class);

                view.getContext().startActivity(intent);
            }
        });

        LinearLayout look = (LinearLayout)view.findViewById(R.id.user_history_layout);
        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(App.isLogin()){
                    Intent intent = new Intent(view.getContext(), LookActivity.class);

                    view.getContext().startActivity(intent);
                }else{
                    ClickUtil.goToLogin(view.getContext());
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


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            NoticeTransfer.refresh();
        }

    }

    private void initView(View view){
        checkIn = (Button)view.findViewById(R.id.user_checkin);
        userAvatar= (ImageView) view.findViewById(R.id.user_avater);
        userNickname= (TextView) view.findViewById(R.id.user_nickname);
        haibiCount = (TextView)view.findViewById(R.id.user_haibi_count);
        favCount = (TextView)view.findViewById(R.id.user_fav_count);
        userContent = (TextView)view.findViewById(R.id.user_content);
        userMoney = (TextView) view.findViewById(R.id.user_money);


        if(App.isLogin()){
            getUserInfo();
        }
    }

    public void checkin(final View view){
        HttpAPI.userCheckini(new HttpAPI.Callback<BaseModel>() {
            //            @Override
            public void onSuccess(BaseModel baseModel) {
                if(baseModel.getState() == 0){
                    getUserInfo();
                    checkinState = 1;
                    checkIn.setBackgroundResource(R.drawable.shape_button_no);
                    checkIn.setText("已签到");
                    String data = baseModel.getData();
                    if(!TextUtils.isEmpty(data)){
                        getUserInfo();
                        Toast.makeText(App.getContext(), data, Toast.LENGTH_SHORT).show();
                    }
                }else{

                }

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(view.getContext(),"签到系统有点小问题", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void getUserInfo(){
        HttpAPI.getUserDetail(new HttpAPI.Callback<UserModel>() {
            @Override
            public void onSuccess(UserModel userModel) {
                if(userModel.getState() == 0){

                    if(userModel.getData() == null)
                        return;
                    haibiCount.setText(""+userModel.getData().getScore());
                    favCount.setText(""+userModel.getData().getFavoriteCount());
                    if(userModel.getData().getGender().equals("M")){
                        userContent.setText("性别:男");
                    }else if(userModel.getData().getGender().equals("F")){
                        userContent.setText("性别:女");
                    }else{
                        userContent.setText("性别:不男不女");
                    }

                    userMoney.setText(userModel.getData().getMoney());


                    App.setHaibiNum(userModel.getData().getScore());

                    userNickname.setText(userModel.getData().getName());
                    checkinState = userModel.getData().getCheckinState();
                    mobile = userModel.getData().getMobile();
                    App.userInit(userModel.getData());
                    if(checkinState == 1){
                        checkIn.setText("已签到");
                        checkIn.setBackgroundResource(R.drawable.shape_button_no);
                    }

                    Glide.with(App.getContext())
                            .load(userModel.getData().getLogo())
                            .transform(new CircleTransform(App.getContext()))
                            .error(R.mipmap.user_default)
                            .into(userAvatar);
                }else{

                }

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(App.getContext(), "用户系统有点小问题", Toast.LENGTH_SHORT).show();
            }
        });

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

    private void goToMoney(View view){

        Intent intent = new Intent(view.getContext(), MoneyActivity.class);

        view.getContext().startActivity(intent);

    }

    private void goToShare(View view){//我的里面邀请好友 type=1
        Feed feed = new Feed();
        feed.setType(1);
        feed.setTitle("很嗨-没事偷着乐");
        feed.setContent("每天给自己一个开心的理由");
        String invitePageUrl = App.getInvitePageUrl();
        String userLogo = App.getUserLogo();

        feed.setShareUrl(invitePageUrl);
        feed.setImageUrl(userLogo);
        ClickUtil.goToShare(view.getContext(), feed);
    }


    private LoginListener mLoginListener = new LoginListener() {
        @Override
        public void login(boolean is) {
            if(is){
                getUserInfo();
                BindUtil.bindJPush();
            }
        }

        @Override
        public void logout(boolean is) {

            haibiCount.setText("0");
            favCount.setText("0");



            userNickname.setText("点击登录");
            checkinState = 0;
            mobile = null;
            checkIn.setBackgroundResource(R.drawable.shape_button);
            checkIn.setText("签到");
            userMoney.setText("0元");
            userContent.setText("个人详细资料");
            NoticeTransfer.refreshMeesage();

            Glide.with(App.getContext())
                    .load(R.mipmap.user_default)
                    .transform(new CircleTransform(App.getContext()))
                    .error(R.mipmap.user_default)
                    .into(userAvatar);
        }

        @Override
        public void refresh() {
            if(App.isLogin()){
                getUserInfo();
            }
        }

        @Override
        public void mobile(String m) {
            mobile = m;
        }
    };





}
