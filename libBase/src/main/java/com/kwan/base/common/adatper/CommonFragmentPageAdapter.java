package com.kwan.base.common.adatper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Kwan on 2016-5-4.
 */
public class CommonFragmentPageAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments = new ArrayList<>();                         //fragment列表
	private List<String> mTitles = new ArrayList<>();                              //tab名的列表

	public CommonFragmentPageAdapter(FragmentManager fm) {
		super(fm);
	}

	public CommonFragmentPageAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
		super(fm);
		mFragments = fragments;
		mTitles = titles;
	}


	public void  addFragment(Fragment fragment, String title) {
		mFragments.add(fragment);
		mTitles.add(title);
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	//此方法用来显示tab上的名字
	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles.get(position % mTitles.size());
	}

	//这里重写 防止销毁fragment 造成切换卡顿
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// super.destroyItem(container, position, object);
	}

	public List<String> getTitles() {
		return mTitles;
	}

}
