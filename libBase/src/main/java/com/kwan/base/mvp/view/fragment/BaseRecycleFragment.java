package com.kwan.base.mvp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kwan.base.R;
import com.kwan.base.common.widget.observablescrollview.ObservableRecyclerView;
import com.kwan.base.common.widget.observablescrollview.ObservableScrollViewCallbacks;

import swipetoloadlayout.OnLoadMoreListener;
import swipetoloadlayout.OnRefreshListener;
import swipetoloadlayout.SwipeToLoadLayout;


/**
 *
 * Created by Mr.Kwan on 2017-2-23.
 */

public abstract class BaseRecycleFragment extends BasePageItemFragment implements OnRefreshListener, OnLoadMoreListener {

	protected ObservableRecyclerView mRecyclerView;
	protected ObservableScrollViewCallbacks observableScrollViewCallbacks;
	protected BaseQuickAdapter mBaseAdapter;
	protected SwipeToLoadLayout swipeRefreshLayout;
	protected int pageIndex = 0;
	protected int pageSize = 20;
	protected boolean isLoadMore = false;

	@Override
	public void fetchData() {
		Log.d("BaseRecycleFragment", "fetchData::begin autoRefresh");
		swipeRefreshLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);
			}
		}, 1);
	}

	@Override
	protected int getContentViewId() {
		return R.layout.layout_base_recycle;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRecyclerView = mContentView.findViewById(R.id.swipe_target);
		swipeRefreshLayout = mContentView.findViewById(R.id.swipeToLoadLayout);

		mRecyclerView.setLayoutManager(getLayoutManager());
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setScrollViewCallbacks(observableScrollViewCallbacks);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.addItemDecoration(getItemDecoration());

		//int spanCount = 2;
		//int spacing = 20;
		//mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));

		//麻痹的 关闭硬件加速 否则 cardView 报错

		mRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mRecyclerView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		mBaseAdapter = getAdapter();
		mRecyclerView.setAdapter(mBaseAdapter);

		swipeRefreshLayout.setRefreshEnabled(true);
		swipeRefreshLayout.setLoadMoreEnabled(true);

		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setOnLoadMoreListener(this);

	}

	@Override
	public void onRefresh() {
		isLoadMore = false;
		pageIndex = 0;
	}

	@Override
	public void onLoadMore() {
		isLoadMore = true;
		pageIndex++;
	}

	protected void closeSwipeRefresh(){
		swipeRefreshLayout.setRefreshing(false);
		swipeRefreshLayout.setLoadingMore(false);
	}

	protected abstract RecyclerView.ItemDecoration getItemDecoration();

	protected abstract BaseQuickAdapter getAdapter();

	protected abstract RecyclerView.LayoutManager getLayoutManager();


}
