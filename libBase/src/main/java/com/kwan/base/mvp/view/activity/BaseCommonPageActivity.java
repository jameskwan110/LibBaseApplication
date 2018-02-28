package com.kwan.base.mvp.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.kwan.base.R;
import com.kwan.base.common.widget.indicator.CommonNavigator;
import com.kwan.base.common.widget.indicator.MagicIndicator;
import com.kwan.base.common.widget.indicator.ScaleTransitionPagerTitleView;
import com.kwan.base.common.widget.indicator.SimplePagerTitleView;
import com.kwan.base.common.widget.indicator.ViewPagerHelper;
import com.kwan.base.common.widget.indicator.abs.CommonNavigatorAdapter;
import com.kwan.base.common.widget.indicator.abs.IPagerIndicator;
import com.kwan.base.common.widget.indicator.abs.IPagerTitleView;

/**
 * ViewPageActivity
 * Created by Mr.Kwan on 2017-2-23.
 */

public abstract class BaseCommonPageActivity extends BaseCommonActivity  {

	protected MagicIndicator mTabLayout;
	protected ViewPager mViewPager;

	@Override
	protected int getMainViewId() {
		return R.layout.layout_base_page;
	}

	@Override
	protected void initViews() {
		super.initViews();

		mTabLayout = (MagicIndicator) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);


	}

	@Override
	protected void initData() {

	}

	@Override
	protected int getBottomViewId() {
		return 0;
	}

	@Override
	protected int getTopViewId() {
		return 0;
	}

	protected void setUpViewPage(final PagerAdapter adapter) {
		setUpViewPage(adapter, false, 12, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.txt_cobalt_blue));
	}

	protected void setUpViewPage(final PagerAdapter adapter, boolean isDivider, int selectTextColor, int unSelectTextColor) {
		setUpViewPage(adapter, isDivider, 12, selectTextColor, unSelectTextColor);
	}

	protected void setUpViewPage(final PagerAdapter adapter, int titleSize, int selectTextColor, int unSelectTextColor) {
		setUpViewPage(adapter, false, titleSize, selectTextColor, unSelectTextColor);
	}

	protected void setUpViewPage(final PagerAdapter adapter, boolean isDivider, int titleSize, int selectTextColor, int unSelectTextColor) {

		mViewPager.setAdapter(adapter);

		final CommonNavigator commonNavigator = new CommonNavigator(this);
		commonNavigator.setSkimOver(true);
		commonNavigator.setAdapter(new CommonNavigatorAdapter() {

			@Override
			public int getCount() {
				return adapter.getCount();
			}

			@Override
			public IPagerTitleView getTitleView(Context context, final int index) {

				SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
				simplePagerTitleView.setText(adapter.getPageTitle(index));
				simplePagerTitleView.setTextSize(18);
				//simplePagerTitleView.set
				simplePagerTitleView.setNormalColor(Color.parseColor("#939393"));
				simplePagerTitleView.setSelectedColor(Color.WHITE);
				simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mViewPager.setCurrentItem(index);
					}
				});
				return simplePagerTitleView;
			}

			@Override
			public IPagerIndicator getIndicator(Context context) {
				return null;
			}

		});

		mTabLayout.setNavigator(commonNavigator);
		ViewPagerHelper.bind(mTabLayout, mViewPager);
	}

}
