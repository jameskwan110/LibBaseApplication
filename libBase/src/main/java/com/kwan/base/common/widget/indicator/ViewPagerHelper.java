package com.kwan.base.common.widget.indicator;

import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * 简化和ViewPager绑定
 * Created by hackware on 2016/8/17.
 */

public class ViewPagerHelper {
    public static void bind(final MagicIndicator magicIndicator, ViewPager viewPager) {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

				Log.d("ViewPagerHelper","onPageScrolled — position::"+position+"::positionOffset::"+positionOffset);
				magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
				Log.d("ViewPagerHelper","onPageSelected");
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
				Log.d("ViewPagerHelper","onPageScrollStateChanged");
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
    }
}
