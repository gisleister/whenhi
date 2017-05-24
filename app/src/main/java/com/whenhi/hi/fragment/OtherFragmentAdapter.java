package com.whenhi.hi.fragment;

import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.whenhi.hi.Constants;
import com.whenhi.hi.model.DiscoveryModel;
import com.whenhi.hi.model.Image;

import java.util.List;

/**
 * Created by aspsine on 16/4/28.
 */
public class OtherFragmentAdapter implements FragmentNavigatorAdapter {

    private DiscoveryModel mDiscoveryModel;
    private List<Image> images;

    private FavListFragment mFavListFragment;
    private MyHaibiRecordListFragment mMyHaibiRecordListFragment;
    private CraftListFragment mCraftListFragment;
    private LoveIndexListFragment mLoveIndexListFragment;
    private ShareIndexListFragment mShareIndexListFragment;
    private HaibiIndexListFragment mHaibiIndexListFragment;

    public OtherFragmentAdapter(){

    }


    @Override
    public Fragment onCreateFragment(int position) {
        switch (position) {
            case Constants.OTHER_FAV:
                mFavListFragment = FavListFragment.newInstance();
                return mFavListFragment;
            case Constants.OTHER_INCOME_RECORD:
                mMyHaibiRecordListFragment = MyHaibiRecordListFragment.newInstance();
                return mMyHaibiRecordListFragment;
            case Constants.OTHER_CRAFT:
                mCraftListFragment = CraftListFragment.newInstance();
                return mCraftListFragment;
            case Constants.OTHER_PHONE:
                return ChargeFragment.newInstance();
            case Constants.OTHER_LOVE_INDEX:
                mLoveIndexListFragment = LoveIndexListFragment.newInstance();
                return mLoveIndexListFragment;
            case Constants.OTHER_SHARE_INDEX:
                mShareIndexListFragment = ShareIndexListFragment.newInstance();
                return mShareIndexListFragment;
            case Constants.OTHER_INCOME_INDEX:
                mHaibiIndexListFragment = HaibiIndexListFragment.newInstance();
                return mHaibiIndexListFragment;
            case Constants.OTHER_LOVE_INDEX_SUB:
                return LoveIndexFragment.newInstance(mDiscoveryModel.getData().getTopHotFeeds());
            case Constants.OTHER_SHARE_INDEX_SUB:
                return ShareIndexFragment.newInstance(mDiscoveryModel.getData().getTopShareFeeds());
            case Constants.OTHER_INCOME_INDEX_SUB:
                return HaibiIndexFragment.newInstance(mDiscoveryModel.getData().getTopUsers());
            case Constants.OTHER_MESSAGE:
                return MessageListFragment.newInstance();
            case Constants.OTHER_CHARGE_RECORD:
                return MoneyRecordListFragment.newInstance();
            case Constants.OTHER_INDEX:
                return IndexNavFragment.newInstance();
            case Constants.OTHER_MALE:
                return MaleListFragment.newInstance();
            case Constants.OTHER_EXPLORE:
                return ExploreListFragment.newInstance();
            case Constants.OTHER_LOOK:
                return LookListFragment.newInstance();
        }
        return null;
    }

    public void destroy(){


    }

    public void setImages(List<Image> list){
        images = list;
    }

    @Override
    public String getTag(int position) {
        return String.valueOf(position);
    }

    @Override
    public int getCount() {
        return 16;
    }
}