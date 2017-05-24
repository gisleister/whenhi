package com.whenhi.hi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.whenhi.hi.model.Image;
import com.whenhi.hi.view.fragment.ImageFragment;

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
