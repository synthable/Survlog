package com.grasscove.survlog.ui;

import android.content.Context;
import android.content.res.Configuration;

public final class UIUtils {

	public static boolean isTablet;

	public static final boolean isTablet(Context c) {
		return (
			((c.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) ||
			((c.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)
		);
		//return c.getResources().getConfiguration().SCREENLAYOUT_SIZE_MASK <= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static final boolean isXlarge(Context c) {
		return (c.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE;
		//return c.getResources().getConfiguration().SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	public static final boolean isHcOrLater() {
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
	}
}