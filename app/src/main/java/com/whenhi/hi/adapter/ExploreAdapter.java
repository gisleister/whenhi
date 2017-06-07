package com.whenhi.hi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.HaibiIndexActivity;
import com.whenhi.hi.activity.MaleActivity;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.image.RoundTransform;
import com.whenhi.hi.listener.GlideListener;
import com.whenhi.hi.listener.OnChildItemClickListener;
import com.whenhi.hi.listener.OnChildItemLongClickListener;
import com.whenhi.hi.listener.OnGroupItemClickListener;
import com.whenhi.hi.listener.OnGroupItemLongClickListener;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.DiscoveryModel;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.Image;
import com.whenhi.hi.model.User;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.util.ClickUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王雷 on 2017/2/21.
 */

public class ExploreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private static final int TYPE_MALE = 0;
    private static final int TYPE_HAIBI_INDEX = 1;
    private static final int TYPE_GROUP = 2;
    private static final int TYPE_CHILD = 3;


    private final List<Feed> mFeeds;
    private final List<User> mUsers;
    private GlideListener mGlideListener;



    protected OnItemClickListener mOnItemClickListener;

    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }


    public ExploreAdapter() {
        mFeeds = new ArrayList<>();
        mUsers = new ArrayList<>();
        mGlideListener = new GlideListener();


    }

    public void setList(List<Feed> feeds) {
        mFeeds.clear();

        append(feeds);
    }

    public void append(List<Feed> feeds) {
        mFeeds.addAll(feeds);
        if(feeds.size() > 0){
            notifyDataSetChanged();
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_MALE;
        } else if (position == 1) {
            return TYPE_HAIBI_INDEX;
        } else if (position == 2) {
            return TYPE_GROUP;
        } else {
            return TYPE_CHILD;
        }
    }

    @Override
    public int getItemCount() {
        return mFeeds.size() + 3;//因为多了一个header,内容和详情 所以ITEM的 的游标 +1 +1 +1
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int type = getItemViewType(i);
        View itemView = null;
        switch (type) {
            case TYPE_MALE:
                itemView = inflate(viewGroup, R.layout.item_explore_male);
                return new MaleHolder(itemView);
            case TYPE_HAIBI_INDEX:
                itemView = inflate(viewGroup, R.layout.item_viewpager_recycler);
                return new HaibiIndexHolder(itemView);
            case TYPE_GROUP:
                itemView = inflate(viewGroup, R.layout.item_explore_group);
                return new GroupHolder(itemView);
            case TYPE_CHILD:
                itemView = inflate(viewGroup, R.layout.item_feed_all);
                final ChildHolder holder = new ChildHolder(itemView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        int childPosition = getChildPosition(position);
                        Feed feed = mFeeds.get(childPosition);
                        mOnItemClickListener.onItemClick(childPosition, feed, view);
                    }
                });
                return holder;
        }
        throw new IllegalArgumentException("Wrong type!");
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int type = getItemViewType(i);
        switch (type) {
            case TYPE_MALE:
                onBindHeaderHolder((MaleHolder) viewHolder);
                break;
            case TYPE_HAIBI_INDEX:
                onBindHabiIndexHolder((HaibiIndexHolder) viewHolder);
                break;
            case TYPE_GROUP:
                onBindGroupHolder((GroupHolder) viewHolder);
                break;
            case TYPE_CHILD:
                onBindChildHolder((ChildHolder) viewHolder, getChildPosition(i));
                break;
        }
    }

    private void onBindHeaderHolder(MaleHolder holder) {

        holder.maleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gender = App.getGender();
                if(!TextUtils.isEmpty(gender)){

                    if(gender.equals("F")){
                        Toast.makeText(App.getContext(), "只有男性才能进入哦!", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(v.getContext(), MaleActivity.class);
                        v.getContext().startActivity(intent);
                    }
                }else{
                    ClickUtil.goToLogin(v.getContext());
                }


            }
        });

        holder.haibiIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HaibiIndexActivity.class);
                v.getContext().startActivity(intent);

            }
        });
    }

    private void onBindHabiIndexHolder(HaibiIndexHolder holder) {

        final HaibiIndexAdapter adapter = new HaibiIndexAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.recyclerView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setAdapter(adapter);
        HttpAPI.discoveryHome(new HttpAPI.Callback<DiscoveryModel>() {
            @Override
            public void onSuccess(DiscoveryModel discoveryModel) {
                if(discoveryModel.getState() == 0){;
                    adapter.setList(discoveryModel.getData().getTopUsers());
                }else{

                }


            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });




    }

    private void onBindGroupHolder(final GroupHolder holder) {
        //holder.headerText.setText("热门评论");

        final Context context = holder.itemView.getContext();

    }

    private void onBindChildHolder(ChildHolder holder, int childPosition) {
        //Character character = mSections.get(parentPosition).getCharacters().get(childPosition);
        Feed feed = mFeeds.get(childPosition);
        if(feed == null)
            return;
        Context context = App.getContext();

        mGlideListener.setErroReport(true);
        mGlideListener.setFeedCategory(feed.getFeedCategory());
        mGlideListener.setFeedId(feed.getId());


        holder.nicknameUser.setText(feed.getUserName());
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.avatarUser);
        ImageView target = imageViewWeakReference.get();

        Glide.with(context)
                .load(feed.getUserLogo())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .transform(new CircleTransform(context))
                .error(R.mipmap.logo)
                .into(target);

        showContent(holder,feed,context);//显示内容
        showToolbarContent(holder,feed);//显示工具栏
        other(holder,feed.getFeedCategory());//工具栏是否隐藏

    }



    int getChildPosition(int position) {

        int childPositionInGroup = position - 1 - 1 -1;//评论的位置在header详情和和group的下面所以要 -1 -1 -1
        return childPositionInGroup;
    }



    static class MaleHolder extends RecyclerView.ViewHolder {
        ImageView maleImage;
        LinearLayout haibiIndex;
        public MaleHolder(View itemView) {
            super(itemView);
            maleImage = (ImageView) itemView.findViewById(R.id.item_male_image);
            haibiIndex = (LinearLayout) itemView.findViewById(R.id.explore_haibi_index_layout);

        }
    }

    static class HaibiIndexHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public HaibiIndexHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler);
        }
    }

    static class GroupHolder extends RecyclerView.ViewHolder {


        public GroupHolder(View itemView) {
            super(itemView);



        }
    }

    static class ChildHolder extends RecyclerView.ViewHolder {
        ImageView avatarUser;
        TextView nicknameUser;
        ImageView imageContent;
        TextView textContent;
        ImageView imagePlay;






        LinearLayout userLayout;
        LinearLayout toolbarLayout;
        RelativeLayout contentImageLayout;
        LinearLayout contentTextLayout;

        ImageView loveImage;
        ImageView favImage;
        ImageView shareImage;
        ImageView commentImage;


        TextView loveText;
        TextView favText;
        TextView shareText;
        TextView commentText;

        Button imageNum;


        public ChildHolder(View itemView) {
            super(itemView);
            userLayout = (LinearLayout) itemView.findViewById(R.id.item_user_layout);
            contentImageLayout = (RelativeLayout) itemView.findViewById(R.id.item_content_image_layout);
            contentTextLayout = (LinearLayout) itemView.findViewById(R.id.item_content_text_layout);




            toolbarLayout = (LinearLayout) itemView.findViewById(R.id.item_toolbar_layout);

            avatarUser = (ImageView) itemView.findViewById(R.id.user_avatar);
            nicknameUser = (TextView) itemView.findViewById(R.id.user_nickname);

            imageContent = (ImageView) itemView.findViewById(R.id.item_content_image);
            textContent = (TextView) itemView.findViewById(R.id.item_content_text);

            imagePlay = (ImageView) itemView.findViewById(R.id.item_content_play);


            loveImage = (ImageView) itemView.findViewById(R.id.toolbar_love_image);
            shareImage = (ImageView) itemView.findViewById(R.id.toolbar_share_image);
            favImage = (ImageView) itemView.findViewById(R.id.toolbar_fav_image);
            commentImage = (ImageView) itemView.findViewById(R.id.toolbar_comment_image);


            loveText = (TextView) itemView.findViewById(R.id.toolbar_love_text);
            shareText = (TextView) itemView.findViewById(R.id.toolbar_share_text);
            favText = (TextView) itemView.findViewById(R.id.toolbar_fav_text);
            commentText = (TextView) itemView.findViewById(R.id.toolbar_comment_text);
            imageNum = (Button) itemView.findViewById(R.id.item_image_num);

        }
    }

    private void other(ChildHolder holder, int type){
        if( type == 8 || type == 10){//彩蛋和抽奖 没有头像
            holder.userLayout.setVisibility(View.GONE);
            holder.toolbarLayout.setVisibility(View.GONE);

        }else{
            holder.userLayout.setVisibility(View.VISIBLE);
            holder.toolbarLayout.setVisibility(View.VISIBLE);
        }



    }



    private void showContent(ChildHolder holder, final Feed feed, Context context){

        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.imageContent);
        ImageView target = imageViewWeakReference.get();

        int width = App.deviceWidth();
        int height = 202;
        //String para = "?imageMogr2/gravity/SouthWest/crop/" + width + "x" + height;
        String para = "";

        if(feed.getFeedCategory() == 1){//视频
            holder.textContent.setText(feed.getContent());
            holder.imageNum.setVisibility(View.GONE);
            holder.contentImageLayout.setVisibility(View.VISIBLE);
            holder.imagePlay.setImageResource(R.mipmap.shouye_shipin_bofang);
            if(TextUtils.isEmpty(feed.getContent())){
                holder.contentTextLayout.setVisibility(View.GONE);
            }else{
                holder.contentTextLayout.setVisibility(View.VISIBLE);
            }

            if (target != null) {
                Glide.with(context)
                        .load(feed.getImageUrl()+para)
                        .placeholder(R.mipmap.bg_image)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(mGlideListener.getListener())
                        .transform(new RoundTransform(context,10))
                        .error(R.mipmap.bg_image)
                        //.override(width, height)
                        .into(target);
            }

            holder.contentImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickUtil.goToVideoNew(v.getContext(),feed);
                    ClickUtil.reportClickInfo(feed);
                }
            });

        }else if(feed.getFeedCategory() == 2){//动图

        }else if(feed.getFeedCategory() == 3){//漫画
            holder.textContent.setText(feed.getContent());
            holder.imagePlay.setImageResource(R.mipmap.shouye_tupian_kan);
            if(TextUtils.isEmpty(feed.getContent())){
                holder.contentTextLayout.setVisibility(View.GONE);
            }else{
                holder.contentTextLayout.setVisibility(View.VISIBLE);
            }
            List<Image> images = feed.getResList();
            if(images != null){
                if(images.size() > 0){
                    Image image = images.get(0);
                    if (target != null) {
                        Glide.with(context)
                                .load(image.getContent()+para)
                                .placeholder(R.mipmap.bg_image)
                                .centerCrop()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .listener(mGlideListener.getListener())
                                .transform(new RoundTransform(context,10))
                                .error(R.mipmap.bg_image)
                                //.override(600, 200)
                                //.fitCenter()
                                .into(target);
                    }

                }
            }


        }else if(feed.getFeedCategory() == 4){//段子
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 15, 0, 0);
            holder.textContent.setLayoutParams(lp);
            holder.textContent.setText(feed.getContent());
            holder.imageNum.setVisibility(View.GONE);
            holder.contentImageLayout.setVisibility(View.GONE);
            feed.setImageUrl(App.getUserLogo());//给分享时候图片赋值为用户头像
        }else if(feed.getFeedCategory() == 5){//图片
            holder.textContent.setText(feed.getContent());
            holder.contentImageLayout.setVisibility(View.VISIBLE);
            holder.imagePlay.setImageResource(R.mipmap.shouye_tupian_kan);
            if(TextUtils.isEmpty(feed.getContent())){
                holder.contentTextLayout.setVisibility(View.GONE);
            }else{
                holder.contentTextLayout.setVisibility(View.VISIBLE);
            }

            List<Image> images = feed.getResList();
            if(images != null){
                if(images.size() > 0){
                    holder.imageNum.setVisibility(View.VISIBLE);
                    holder.imageNum.setText(""+images.size());
                    Image image = images.get(0);
                    if (target != null) {
                        Glide.with(context)
                                .load(image.getContent()+para)
                                .placeholder(R.mipmap.bg_image)
                                .centerCrop()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .listener(mGlideListener.getListener())
                                .transform(new RoundTransform(context,10))
                                .error(R.mipmap.bg_image)
                                //.override(width, height)
                                .into(new GlideDrawableImageViewTarget(target, 0));
                    }

                }
            }

            holder.contentImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickUtil.goToPicNew(v.getContext(),feed);
                    ClickUtil.reportClickInfo(feed);
                }
            });

        }else if(feed.getFeedCategory() == 6){//广告

        }else if(feed.getFeedCategory() == 7){//分割线

        }else if(feed.getFeedCategory() == 8){//彩蛋
            holder.imageNum.setVisibility(View.GONE);
            holder.contentImageLayout.setVisibility(View.VISIBLE);
            holder.imagePlay.setImageResource(R.mipmap.shouye_huodong_kai);
            holder.contentTextLayout.setVisibility(View.GONE);
            if (target != null) {
                Glide.with(context)
                        .load(feed.getMaskUrl()+para)
                        .placeholder(R.mipmap.bg_image)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(mGlideListener.getListener())
                        .transform(new RoundTransform(context,10))
                        .error(R.mipmap.bg_image)
                        //.override(width, height)
                        .into(target);
            }
            holder.contentImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickUtil.click(feed,v.getContext());
                }
            });
        }else if(feed.getFeedCategory() == 9){//webview
            holder.imageNum.setVisibility(View.GONE);
            holder.textContent.setText(feed.getSummary());
            holder.contentImageLayout.setVisibility(View.VISIBLE);
            holder.imagePlay.setImageResource(R.mipmap.shouye_tupian_kan);
            if(TextUtils.isEmpty(feed.getSummary())){
                holder.contentTextLayout.setVisibility(View.GONE);
            }else{
                holder.contentTextLayout.setVisibility(View.VISIBLE);
            }
            if (target != null) {
                Glide.with(context)
                        .load(feed.getImageUrl()+para)
                        .placeholder(R.mipmap.bg_image)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(mGlideListener.getListener())
                        .transform(new RoundTransform(context,10))
                        .error(R.mipmap.bg_image)
                        //.override(width, height)
                        .into(target);
            }

            feed.setContent(feed.getSummary());
            holder.contentImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickUtil.click(feed,v.getContext());
                }
            });

        }else if(feed.getFeedCategory() == 10){//抽奖
            //holder.textContent.setText(feed.getMaskContent());
            holder.imageNum.setVisibility(View.GONE);
            holder.contentImageLayout.setVisibility(View.VISIBLE);
            holder.imagePlay.setImageResource(R.mipmap.shouye_huodong_kai);
            holder.contentTextLayout.setVisibility(View.GONE);
            if (target != null) {
                Glide.with(context)
                        .load(feed.getPicUrl()+para)
                        .placeholder(R.mipmap.bg_image)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(mGlideListener.getListener())
                        .transform(new RoundTransform(context,10))
                        .error(R.mipmap.bg_image)
                        //.override(width, height)
                        .into(target);
            }

            holder.contentImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickUtil.click(feed,v.getContext());
                }
            });
        }


    }

    private void showToolbarContent(ChildHolder holder, Feed feed){

        if(feed.getLikeCount() == 0){
            holder.loveText.setText("");
        }else{
            holder.loveText.setText(""+feed.getLikeCount());
        }

        if(feed.getFavoriteCount() == 0){
            holder.favText.setText("");
        }else{
            holder.favText.setText(""+feed.getFavoriteCount() + "");
        }

        if(feed.getShareCount() == 0){
            holder.shareText.setText("");
        }else{
            holder.shareText.setText(""+feed.getShareCount() + "");
        }

        if(feed.getCommentCount() == 0){
            holder.commentText.setText("");
        }else{
            holder.commentText.setText(""+feed.getCommentCount() + "");
        }





        if(feed.getLikeState() == 1){
            holder.loveImage.setImageResource(R.mipmap.shouye_icon_zan_click);
        }else{
            holder.loveImage.setImageResource(R.mipmap.shouye_icon_zan);
        }

        if(feed.getFavoriteState() == 1){
            holder.favImage.setImageResource(R.mipmap.shouye_icon_shoucang_click);

        }else{
            holder.favImage.setImageResource(R.mipmap.shouye_icon_shoucang);
        }
        ClickUtil.toolbarClick(holder.loveImage, holder.favImage,holder.loveText,holder.favText,holder.favImage,holder.loveImage,holder.shareImage,holder.commentImage,holder.itemView,feed);
    }






}
