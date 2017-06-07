package com.whenhi.hi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.whenhi.hi.model.Comment;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.ClickUtil;

public class TextNewActivity extends BaseActivity{

    private static final String TAG =TextNewActivity.class.getSimpleName();

    private Feed mFeed;

    private Button commentBtn;
    private DialogPlus dialog;
    private EditText commentEditText;

    private int targetType = 1;
    private int targetId = 0;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
         //       WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_new);
        Intent intent = getIntent();
        mFeed = (Feed)intent.getSerializableExtra("Feed");


        TextView duanzi = (TextView) findViewById(R.id.duanzi_text);
        duanzi.setText(mFeed.getContent());

        ImageView close = (ImageView) findViewById(R.id.pic_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ImageView zan = (ImageView)findViewById(R.id.toolbar_love_image);
        ImageView fav = (ImageView)findViewById(R.id.toolbar_fav_image);
        ImageView share = (ImageView)findViewById(R.id.toolbar_share_image);
        showToolbarContent(zan,fav,share);

        ImageView comment = (ImageView)findViewById(R.id.comment_list_image);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getContext(), CommentActivity.class);
                intent.putExtra("Feed", mFeed);
                startActivity(intent);
            }
        });

        commentBtn = (Button) findViewById(R.id.comment_button_image);
        commentBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {


                commentInput();
            }
        });


    }



    private void commentInput(){
        Holder holder = new ViewHolder(R.layout.layout_dialog_comment);

        dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            View v = getCurrentFocus();
                            //imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
                        }

                    }
                })
                .setExpanded(false)//设置扩展模式可控制dialog的高度

                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.comment_send:
                                updateComment();
                                break;
                        }

                    }
                })
                .create();
        dialog.show();

        commentEditText = (EditText)findViewById(R.id.comment_edit_text);
        commentEditText.setFocusable(true);
        commentEditText.setFocusableInTouchMode(true);
        commentEditText.requestFocus();

        InputMethodManager imm = (InputMethodManager) commentEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

    }



    private void showToolbarContent(ImageView zan, ImageView fav, ImageView share){

        if(mFeed.getLikeState() == 1){
            zan.setImageResource(R.mipmap.xiangqing_icon_zan_click);
        }else{
            zan.setImageResource(R.mipmap.xiangqing_icon_zan);
        }

        if(mFeed.getFavoriteState() == 1){
            fav.setImageResource(R.mipmap.xiangqing_icon_shoucang_click);

        }else{
            fav.setImageResource(R.mipmap.xiangqing_icon_shoucang);
        }


        ClickUtil.toolbarClickDetail(fav,zan,share,getWindow().getDecorView(),mFeed);

    }



    private void updateComment(){
        boolean isLogin = App.isLogin();

        if(isLogin){
            content = commentEditText.getText().toString();

            if(targetType == 1){
                targetId = mFeed.getId();
            }

            if(content.equals("")){
                Toast.makeText(App.getContext(), "多少要写点东西哦", Toast.LENGTH_SHORT).show();
                return;
            }
            HttpAPI.addComment(mFeed.getId(),targetId,targetType,content,new HttpAPI.Callback<BaseModel>() {
                @Override
                public void onSuccess(BaseModel baseModel) {
                    if(baseModel.getState() == 0){
                        Toast.makeText(App.getContext(), "评论成功", Toast.LENGTH_SHORT).show();
                        Comment comment = new Comment();
                        comment.setUserId(Integer.parseInt(App.getUserId()));
                        comment.setUserLogo(App.getUserLogo());
                        comment.setUserName(App.getNickname());
                        comment.setContent(content);
                        comment.setFeedId(mFeed.getId());
                        NoticeTransfer.commentSuccess(comment);
                        commentEditText.setText("继续写点东西");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            View v = getCurrentFocus();
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                        dialog.dismiss();

                    }else{
                        Toast.makeText(App.getContext(), baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(App.getContext(), "服务器貌似出问题了", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Intent intent = new Intent(TextNewActivity.this, LoginActivity.class);
            startActivity(intent);

            overridePendingTransition(R.anim.activity_open,0);
        }
    }

    public void acceptCommentClick(Comment comment){
        targetType = 2;
        targetId = comment.getId();
        commentBtn.setText("@"+comment.getUserName());
    }

}




