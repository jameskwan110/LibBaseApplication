package com.kwan.base.transformer;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public class CubeTransformer implements PageTransformer {


    @Override
    public void transformPage(View view, float position) {
        if (position <= 0) {

            view.setPivotX(view.getMeasuredWidth());
            view.setPivotY(view.getMeasuredHeight() * 0.5f);


            view.setRotationY(90f * position);
        } else if (position <= 1) {

            view.setPivotX(0);
            view.setPivotY(view.getMeasuredHeight() * 0.5f);
            view.setRotationY(90f * position);
        }
    }
}
