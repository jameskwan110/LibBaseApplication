package com.kwan.base.common.widget.indicator;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kwan.base.util.DpOrSp2PxUtil;
import com.kwan.base.util.ScreenUtil;

/**
 * Created by Mr.Kwan on 2017-6-19.
 */

public class ScrollViewFilterHelp  {

	LinearLayout mScrollContet;
	String[] mFileterNames;
	TextView currentTextView, lastTextView;

	public ScrollViewFilterHelp(Context context, LinearLayout mScrollContet, String[] mFileterNames) {
		this.mFileterNames = mFileterNames;
		this.mScrollContet = mScrollContet;

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ScreenUtil.getScreenHeight(context)/7);
		lp.leftMargin = DpOrSp2PxUtil.dp2pxConvertInt(context, 10);
		lp.rightMargin = DpOrSp2PxUtil.dp2pxConvertInt(context, 10);
		lp.topMargin = DpOrSp2PxUtil.dp2pxConvertInt(context, 15);
		lp.bottomMargin = DpOrSp2PxUtil.dp2pxConvertInt(context, 15);

		for (int i = 0; i < mFileterNames.length; i++) {



			String filter = mFileterNames[i];
			TextView textView = new TextView(context);
			textView.setText(filter);
			textView.setTextColor(Color.parseColor("#939393"));
			textView.setTextSize(14);
			textView.setLayoutParams(lp);
			mScrollContet.addView(textView);

			final int position = i;

			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//onClick( v, position);
					lastTextView = currentTextView;
					currentTextView = (TextView) v;
					currentTextView.setTextColor(Color.WHITE);
					lastTextView.setTextColor(Color.parseColor("#939393"));
					lastTextView.setTextSize(14);
					currentTextView.setTextSize(18);
					if (itemSelect != null)
						itemSelect.onItemClick(v, position);
				}

			});

		}
		currentTextView = (TextView) mScrollContet.getChildAt(0);
		currentTextView.setTextColor(Color.WHITE);

	}

	public interface OnItemSelect {
		void onItemClick(View v, int position);
	}

	OnItemSelect itemSelect;

	public OnItemSelect getItemSelect() {
		return itemSelect;
	}

	public void setItemSelect(OnItemSelect itemSelect) {
		this.itemSelect = itemSelect;
	}
}
