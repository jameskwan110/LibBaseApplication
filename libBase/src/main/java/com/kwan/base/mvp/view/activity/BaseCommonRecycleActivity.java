package com.kwan.base.mvp.view.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kwan.base.R;
import com.kwan.base.common.widget.indicator.SpacesItemDecoration;
import com.kwan.base.common.widget.observablescrollview.ObservableRecyclerView;
import com.zhy.autolayout.AutoLinearLayout;

import swipetoloadlayout.OnLoadMoreListener;
import swipetoloadlayout.OnRefreshListener;
import swipetoloadlayout.SwipeToLoadLayout;


/**
 * 带RecycleView的Activity
 * Created by Mr.Kwan on 2017-2-24.
 */

public abstract class BaseCommonRecycleActivity extends BaseCommonActivity implements OnRefreshListener, OnLoadMoreListener {

	protected ObservableRecyclerView mRecyclerView;
	protected SwipeToLoadLayout swipeRefreshLayout;
	protected BaseQuickAdapter mAdapter;
	protected int pageIndex = 0;
	protected int pageSize = 10;
	protected AutoLinearLayout ll_no_item;
	protected boolean isLoadMore;

	@Override
	protected int getMainViewId() {
		return R.layout.layout_base_recycle;
	}

	@Override
	protected void initViews() {
		super.initViews();

		ll_no_item = (AutoLinearLayout) findViewById(R.id.ll_no_item);

		mRecyclerView = (ObservableRecyclerView) findViewById(R.id.swipe_target);
		mRecyclerView.setLayoutManager(getLayoutManager());
		mRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mRecyclerView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		mRecyclerView.setHasFixedSize(true);
		if (getItemDecoration() != null)
			mRecyclerView.addItemDecoration(getItemDecoration());
		//mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		//解决更新 闪烁
		mRecyclerView.setItemAnimator(null);
		mRecyclerView.setNestedScrollingEnabled(false);

		mAdapter = getAdapter();
		mRecyclerView.setAdapter(mAdapter);

		swipeRefreshLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setOnLoadMoreListener(this);

	}

	protected abstract RecyclerView.ItemDecoration getItemDecoration();

	protected void setRecyclerViewPadding(int padding, boolean isLeftRight) {
		mRecyclerView.addItemDecoration(new SpacesItemDecoration(padding, isLeftRight));
	}

	@Override
	protected void initData() {
		swipeRefreshLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);
			}
		}, 1);
	}

	protected abstract BaseQuickAdapter getAdapter();

	protected abstract RecyclerView.LayoutManager getLayoutManager();

	@Override
	protected int getTopViewId() {
		return 0;
	}

	@Override
	protected int getBottomViewId() {
		return 0;
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

	public void setNoItem(boolean isEmpty) {

		if (isEmpty) {
			ll_no_item.setVisibility(View.VISIBLE);
			swipeRefreshLayout.setVisibility(View.GONE);
		} else {
			ll_no_item.setVisibility(View.GONE);
			swipeRefreshLayout.setVisibility(View.VISIBLE);
		}

	}

	protected void closeSwipeRefresh(){
		swipeRefreshLayout.setRefreshing(false);
		swipeRefreshLayout.setLoadingMore(false);
	}
}
