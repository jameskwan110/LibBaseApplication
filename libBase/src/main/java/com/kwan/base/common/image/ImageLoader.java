package com.kwan.base.common.image;

import android.widget.ImageView;

import java.io.File;

/**
 * Created by Mr.Kwan on 2017-7-29.
 */

public interface ImageLoader {

	void loadLocalResource(int resourceId, final ImageView imageView);

	void loadGif(Object url, final ImageView imageView,final int width);

	void loadUrl(String url, final ImageView imageView);

	void loadFile(String path, ImageView imageView);

	void loadFile(File file, ImageView imageView);

}
