package com.whenhi.hi.fragment;

import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.whenhi.hi.Constants;
import com.whenhi.hi.model.DiscoveryModel;

/**
 * Created by aspsine on 16/4/28.
 */
public class OtherFragmentAdapter implements FragmentNavigatorAdapter {

    private DiscoveryModel mDiscoveryModel;

    private FavListFragment mFavListFragment;
    private IncomeRecordListFragment mIncomeRecordListFragment;
    private CraftListFragment mCraftListFragment;
    private LoveIndexListFragment mLoveIndexListFragment;
    private ShareIndexListFragment mShareIndexListFragment;
    private IncomeIndexListFragment mIncomeIndexListFragment;

    public OtherFragmentAdapter(){

    }

    public void setDiscoveryModel(DiscoveryModel discoveryModel){
        mDiscoveryModel = discoveryModel;
    }

    @Override
    public Fragment onCreateFragment(int position) {
        switch (position) {
            case Constants.OTHER_FAV:
                mFavListFragment = FavListFragment.newInstance();
                return mFavListFragment;
            case Constants.OTHER_INCOME_RECORD:
                mIncomeRecordListFragment = IncomeRecordListFragment.newInstance();
                return mIncomeRecordListFragment;
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
                mIncomeIndexListFragment = IncomeIndexListFragment.newInstance();
                return mIncomeIndexListFragment;
            case Constants.OTHER_LOVE_INDEX_SUB:
                return LoveIndexFragment.newInstance(mDiscoveryModel.getData().getTopHotFeeds());
            case Constants.OTHER_SHARE_INDEX_SUB:
                return ShareIndexFragment.newInstance(mDiscoveryModel.getData().getTopShareFeeds());
            case Constants.OTHER_INCOME_INDEX_SUB:
                return IncomeIndexFragment.newInstance(mDiscoveryModel.getData().getTopUsers());
            case Constants.OTHER_MESSAGE:
                return MessageListFragment.newInstance();
            case Constants.OTHER_CHARGE_RECORD:
                return ChargeRecordListFragment.newInstance();
        }
        return null;
    }

    public void destroy(){

        if(mFavListFragment != null){
            mFavListFragment.destroy();
        }

        if(mIncomeRecordListFragment != null){
            mIncomeRecordListFragment.destroy();
        }

        if(mCraftListFragment != null){
            mCraftListFragment.destroy();
        }

        if(mLoveIndexListFragment != null){
            mLoveIndexListFragment.destroy();
        }

        if(mShareIndexListFragment != null){
            mShareIndexListFragment.destroy();
        }

        if(mIncomeIndexListFragment != null){
            mIncomeIndexListFragment.destroy();
        }
    }

    @Override
    public String getTag(int position) {
        return String.valueOf(position);
    }

    @Override
    public int getCount() {
        return 12;
    }
}