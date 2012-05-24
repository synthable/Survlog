package com.grasscove.survlog;

import com.grasscove.survlog.ui.UIUtils;

import android.app.Application;

public class SurvlogApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		UIUtils.isTablet = UIUtils.isTablet(this);
	}
}