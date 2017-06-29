package com.whenhi.hi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.ClickUtil;

/**
 * Created by 王雷 on 2017/1/5.
 */

public class OtherActivity extends BaseActivity {

    public static final String TAG = OtherActivity.class.getSimpleName();

    private int type;
    private String mobile;
    private String score;
    private int smsType;
    private String titleText;
    private String tips;
    private String title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        Bundle bundle = this.getIntent().getExtras();
        type = bundle.getInt("type");
        smsType = bundle.getInt("smsType");
        mobile = bundle.getString("mobile");
        score = bundle.getString("score");
        titleText = bundle.getString("titleText");
        tips = bundle.getString("tips");
        title = bundle.getString("title");



        show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(R.anim.activity_close,0);
    }

    private void show(){
        if(type == 1){
            smsCode();
        }else if(type == 2){
            userCode();
        }else if(type == 3){
            caidan();
        }else if(type == 4){
            luckPan();
        }

    }


    private void luckPan(){
        Holder holder = new ViewHolder(R.layout.layout_dialog_caidan);

        DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        finish();
                    }
                })
                .setExpanded(false)//设置扩展模式可控制dialog的高度
                .setMargin(80, 0, 80, 0)
                .setPadding(0, 0, 0, 0)
                .setContentBackgroundResource(R.drawable.shape_caidan)

                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final DialogPlus dialog, View view) {
                        switch (view.getId()) {

                            case R.id.caidan_close:

                                dialog.dismiss();

                                break;
                        }

                    }
                })
                .create();
        dialog.show();
        TextView textCaidan = (TextView)holder.getInflatedView().findViewById(R.id.caidan_text);
        textCaidan.setText(tips);

        TextView textTitle = (TextView)holder.getInflatedView().findViewById(R.id.caidan_title);
        textTitle.setText(title);

    }


    private void caidan(){
        Holder holder = new ViewHolder(R.layout.layout_dialog_caidan);

        DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        finish();
                    }
                })
                .setExpanded(false)//设置扩展模式可控制dialog的高度
                .setMargin(80, 0, 80, 0)
                .setPadding(0, 0, 0, 0)
                .setContentBackgroundResource(R.drawable.shape_caidan)

                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final DialogPlus dialog, View view) {
                        switch (view.getId()) {

                            case R.id.caidan_close:
                                if(!App.isLogin()){
                                    ClickUtil.goToLogin(view.getContext());
                                }else{

                                }

                                dialog.dismiss();

                                break;
                        }

                    }
                })
                .create();
        dialog.show();
        TextView textCaidan = (TextView)holder.getInflatedView().findViewById(R.id.caidan_text);
        textCaidan.setText("+ "+score);

        TextView textTitle = (TextView)holder.getInflatedView().findViewById(R.id.caidan_title);
        textTitle.setText(titleText);

        if(!App.isLogin()){
            TextView close = (TextView)holder.getInflatedView().findViewById(R.id.caidan_close);
            close.setText("登录领取");
        }

    }


    /**
     * 短信验证
     */
    private void smsCode(){
        final Holder holder = new ViewHolder(R.layout.layout_dialog_sms);



        DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(Gravity.CENTER)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        finish();
                    }
                })
                .setExpanded(false)//设置扩展模式可控制dialog的高度
                .setMargin(80, 0, 80, 0)
                .setPadding(0, 0, 0, 0)
                .setContentBackgroundResource(R.drawable.shape_caidan)

                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final DialogPlus dialog, final View view) {
                        switch (view.getId()) {
                            case R.id.sms_close:
                                dialog.dismiss();
                                break;
                            case R.id.sms_request:
                                final EditText editTextMobile = (EditText) holder.getInflatedView().findViewById(R.id.sms_phone_text);
                                final TextView codeButton = (TextView)holder.getInflatedView().findViewById(R.id.sms_request);
                                mobile = editTextMobile.getText().toString();
                                if(!TextUtils.isEmpty(mobile)){
                                    codeButton.setText("正在发送");
                                    HttpAPI.sendCode(mobile,new HttpAPI.Callback<BaseModel>() {
                                        @Override
                                        public void onSuccess(BaseModel baseModel) {
                                            if(baseModel.getState() == 0){
                                                codeButton.setText("发送成功");

                                            }else{
                                                codeButton.setText("发送验证码");
                                                Toast.makeText(OtherActivity.this, baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            editTextMobile.setText("服务器出事了");
                                        }
                                    });
                                }else{
                                    Toast.makeText(OtherActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                                }

                                break;
                            case R.id.sms_ok:
                                final EditText editTextCode = (EditText) holder.getInflatedView().findViewById(R.id.sms_code_text);
                                String code = editTextCode.getText().toString();
                                if(!TextUtils.isEmpty(code) && !TextUtils.isEmpty(mobile)){
                                    HttpAPI.inviteSMSCode(code,mobile,new HttpAPI.Callback<BaseModel>() {
                                        @Override
                                        public void onSuccess(BaseModel baseModel) {
                                            if(baseModel.getState() == 0){
                                                NoticeTransfer.mobile(mobile);
                                                if(smsType == 1){//代表直接进入如提现界面
                                                    goToMoney(view);
                                                }else if(smsType == 2){//代表输入邀请码界面
                                                    smsType = 1;
                                                    userCode();

                                                }else if(smsType == 3){//代表进入我的提现列表界面
                                                    goToUserMoney(view);
                                                }

                                                dialog.dismiss();

                                            }else{
                                                Toast.makeText(OtherActivity.this, baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            editTextCode.setText("服务器出事了");
                                        }
                                    });
                                }else{
                                    Toast.makeText(OtherActivity.this, "手机号或者验证码不能为空", Toast.LENGTH_SHORT).show();
                                }


                                break;
                        }

                    }
                })
                .create();
        dialog.show();





        if(smsType == 2){
            TextView title = (TextView)holder.getInflatedView().findViewById(R.id.sms_title_text);
            title.setText("请先绑定手机号再输入邀请码");
        }

    }

    private void goToMoney(View view){

        Intent intent = new Intent(view.getContext(), MoneyActivity.class);

        view.getContext().startActivity(intent);

    }

    private void goToUserMoney(View view){

        Intent intent = new Intent(view.getContext(), MoneyRecordActivity.class);

        view.getContext().startActivity(intent);

    }

    private void userCode(){

        if(smsType == 2){
            smsCode();
        }else if(smsType == 1){
            final Holder holder = new ViewHolder(R.layout.layout_dialog_code);

            final DialogPlus dialog = DialogPlus.newDialog(this)
                    .setContentHolder(holder)
                    .setCancelable(false)
                    .setGravity(Gravity.CENTER)
                    .setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogPlus dialog) {
                            finish();
                        }
                    })
                    .setExpanded(false)//设置扩展模式可控制dialog的高度
                    .setMargin(80, 0, 80, 0)
                    .setPadding(0, 0, 0, 0)
                    .setContentBackgroundResource(R.drawable.shape_caidan)

                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(final DialogPlus dialog, View view) {
                            switch (view.getId()) {
                                case R.id.code_close:
                                    dialog.dismiss();
                                    break;
                                case R.id.code_ok:
                                    EditText codeEdit = (EditText) holder.getInflatedView().findViewById(R.id.code_text);
                                    String code = codeEdit.getText().toString();
                                    if(!TextUtils.isEmpty(code)){
                                        HttpAPI.inviteCode(code,new HttpAPI.Callback<BaseModel>() {
                                            @Override
                                            public void onSuccess(BaseModel baseModel) {
                                                if(baseModel.getState() == 0){
                                                    dialog.dismiss();

                                                }else{
                                                    Toast.makeText(OtherActivity.this, baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                                                }

                                            }

                                            @Override
                                            public void onFailure(Exception e) {
                                                Toast.makeText(OtherActivity.this, "邀请码验证系统处理点小问题", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(OtherActivity.this, "邀请码不能为空", Toast.LENGTH_SHORT).show();
                                    }

                                    break;
                            }

                        }
                    })
                    .create();
            dialog.show();
        }
    }



}
