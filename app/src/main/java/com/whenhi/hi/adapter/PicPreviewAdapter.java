package com.whenhi.hi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.IncomeIndexActivity;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.listener.GlideListener;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.Image;
import com.whenhi.hi.model.User;
import com.whenhi.hi.view.fragment.ImageFragment;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class PicPreviewAdapter extends FragmentStatePagerAdapter {
    private static final String IMAGE_URL = "image";

    List<Image> mDatas;

    public PicPreviewAdapter(FragmentManager fm, List<Image> data) {
        super(fm);
        mDatas = data;
    }

    @Override
    public Fragment getItem(int position) {
        Image image = mDatas.get(position);
        String url = image.getContent();
        Fragment fragment = ImageFragment.newInstance(url);
        return fragment;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
