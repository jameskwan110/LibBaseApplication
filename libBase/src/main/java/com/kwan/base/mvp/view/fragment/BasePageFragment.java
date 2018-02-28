package com.kwan.base.mvp.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.kwan.base.R;
import com.kwan.base.common.widget.indicator.CommonNavigator;
import com.kwan.base.common.widget.indicator.MagicIndicator;
import com.kwan.base.common.widget.indicator.SimplePagerTitleView;
import com.kwan.base.common.widget.indicator.ViewPagerHelper;
import com.kwan.base.common.widget.indicator.WrapPagerIndicator;
import com.kwan.base.common.widget.indicator.abs.CommonNavigatorAdapter;
import com.kwan.base.common.widget.indicator.abs.IPagerIndicator;
import com.kwan.base.common.widget.indicator.abs.IPagerTitleView;

/**
 * 包含一个ViewPage的fragment
 */
public abstract class BasePageFragment extends BasePageItemFragment implements View.OnClickListener {

	protected MagicIndicator mTabLayout;
	protected ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTabLayout = mBaseContentView.findViewById(R.id.tabs);
		mViewPager = mBaseContentView.findViewById(R.id.viewPager);
	}

	@Override
	protected int getContentViewId() {
		return R.layout.layout_base_page;
	}

	protected String getTitle() {
		return "title";
	}

	protected void setUpViewPage(final PagerAdapter adapter) {
		setUpViewPage(adapter, false, 16);
	}

	protected void setUpViewPage(final PagerAdapter adapter, boolean isDivider) {
		setUpViewPage(adapter, isDivider, 16);
	}

	protected void setUpViewPage(final PagerAdapter adapter, int titleSize) {
		setUpViewPage(adapter, false, titleSize);
	}

	//Todo 提供设置字体大小,颜色,样式方法
	protected void setUpViewPage(final PagerAdapter adapter, boolean isDivider, int titleSize) {

		mViewPager.setAdapter(adapter);

		final CommonNavigator commonNavigator = new CommonNavigator(mBaseActivity);
		commonNavigator.setScrollPivotX(0.35f);
		commonNavigator.setAdapter(new CommonNavigatorAdapter() {

			@Override
			public int getCount() {
				return adapter.getCount();
			}

			@Override
			public IPagerTitleView getTitleView(Context context, final int index) {

				SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
				simplePagerTitleView.setText(adapter.getPageTitle(index));
				simplePagerTitleView.setTextSize(15);
				//simplePagerTitleView.set
				simplePagerTitleView.setNormalColor(Color.parseColor("#282828"));
				simplePagerTitleView.setSelectedColor(Color.parseColor("#FFFFFF"));
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
				WrapPagerIndicator indicator = new WrapPagerIndicator(context);
				indicator.setFillColor(Color.parseColor("#f23030"));
				return indicator;
			}

		});

		mTabLayout.setNavigator(commonNavigator);
		ViewPagerHelper.bind(mTabLayout, mViewPager);
	}

	@Override
	public void onClick(View v) {

	}

}
