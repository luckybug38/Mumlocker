package com.moarcodeplz.moarsensorlogger.preference;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class BigEditTextPreference extends EditTextPreference {

	public BigEditTextPreference(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);
	}
	
	public BigEditTextPreference(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
	}
	
	public BigEditTextPreference(Context ctx) {
		super(ctx);
	}
	
	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		TextView summary = (TextView)view.findViewById(android.R.id.summary);
		summary.setMaxLines(20);
	}
	
}
