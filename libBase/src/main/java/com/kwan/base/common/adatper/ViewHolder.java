package com.kwan.base.common.adatper;

import android.view.View;

/**
 * Created by Tommy on 11/24/15.
 */
public class ViewHolder {
    public static <T extends View> T get(View view, int rid){
        Object child = view.getTag(rid);
        if (child==null) {
            child = view.findViewById(rid);
            view.setTag(rid,child);
        }
        return (T)child;
    }
}
