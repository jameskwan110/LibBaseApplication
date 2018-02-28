package com.kwan.base.common.adatper;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Kwan on 2016-7-11.
 */
public class CommonViewPageAdapter<T extends View> extends PagerAdapter {

    private List<T> mViewList;
    private List<String> mTitleList;
    private DstroyItemListener dstroyItemListener;
    private ClickItemListener onClickItemListener;

    public CommonViewPageAdapter(List<T> mViewList, List<String> mTitleList) {
        this.mViewList = mViewList;
        this.mTitleList = mTitleList;
    }

    public CommonViewPageAdapter(List<T> mViewList) {

        this.mViewList = mViewList;
        mTitleList = new ArrayList<>();
        for (int i = 0; i < mViewList.size(); i++) {
            mTitleList.add(i + "");
        }

    }

    @Override
    public int getCount() {
        return mViewList.size();//页卡数
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;//官方推荐写法
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        if (!(mViewList.get(position) instanceof AdapterView)) {

            mViewList.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickItemListener != null)
                        onClickItemListener.onClickItem(container, position, view);
                }
            });
        }


        container.addView(mViewList.get(position));//添加页卡


        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));//删除页卡
        if (dstroyItemListener != null)
            dstroyItemListener.onDestroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);//页卡标题
    }

    public interface DstroyItemListener {
        void onDestroyItem(ViewGroup container, int position, Object object);

    }

    public void setDstroyItemListener(DstroyItemListener dstroyItemListener) {
        this.dstroyItemListener = dstroyItemListener;
    }

    public interface ClickItemListener {
        void onClickItem(ViewGroup container, int position, View view);

    }

    public void setOnClickItemListener(ClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }


}


