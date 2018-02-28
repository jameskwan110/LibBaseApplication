package com.kwan.base.common.widget.indicator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Mr.Kwan on 2016-7-20.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * mRecyclerView.addItemDecoration(new SpacesItemDecoration(5));
     * */

    private int space;
    private boolean isLeftRight;

    public SpacesItemDecoration(int space,boolean isLeftRight) {
        this.space = space;
        this.isLeftRight = isLeftRight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(isLeftRight) {
            outRect.left = space;
            outRect.right = space;
        }
        outRect.bottom = space;

        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}
