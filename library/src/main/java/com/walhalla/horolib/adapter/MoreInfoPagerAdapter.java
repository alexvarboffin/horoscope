package com.walhalla.horolib.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.walhalla.horolib.ui.fragment.tab.TabChildFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by combo on 26.09.2017.
 */

/**
 * Dont use FragmentPagerAdapter
 * Only FragmentStatePagerAdapter
 */
public class MoreInfoPagerAdapter extends FragmentStatePagerAdapter {

    public final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public MoreInfoPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        //...
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(TabChildFragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


}
