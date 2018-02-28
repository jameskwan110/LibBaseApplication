package com.kwan.base.common.widget.indicator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/7/25.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * int spanCount = 3; // 3 columns
     * int spacing = 50; // 50px
     * boolean includeEdge = false;
     * mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
     */

    private int spanCount;
    private int spacing;
    private boolean isHead;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge ,boolean isHead) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.isHead = isHead;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        if (includeEdge) {

            if(isHead){
                outRect.left = (column + 1) * spacing / spanCount;
                outRect.right = spacing - column * spacing / spanCount;
                if (position < spanCount) {
                    if (position == 1) {
                        outRect.top = 0;
                    } else {
                        outRect.top = spacing;
                    }
                }
            }else{
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;
                if (position < spanCount) {
                    outRect.top = spacing;
                }
            }
            outRect.bottom = spacing;

        } else {
            outRect.left = column * spacing / spanCount;
            outRect.right = spacing - (column + 1) * spacing / spanCount;
            if (position >= spanCount) {
                outRect.top = spacing;
            }
        }
    }

}
