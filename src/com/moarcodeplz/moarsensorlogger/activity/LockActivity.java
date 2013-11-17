package com.moarcodeplz.moarsensorlogger.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.moarsensorlogger.R;

public class LockActivity extends EntryActivity {
	RelativeLayout ll;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.lock_layout);
	    final Handler handler2 = new Handler();
	    ll= (RelativeLayout)findViewById(R.id.lockmainlayout);
	    handler2.postDelayed(new Runnable() {
	      @Override
	      public void run() {
	    	  
	  	    ll.setBackgroundResource(R.drawable.unlocked);
	  	  final Handler handler = new Handler();
  	    handler.postDelayed(new Runnable() {
  	      @Override
  	      public void run() {
  	    	  finish();
  	      }
  	    }, 1000);
	      }
	    }, 5000);
	    
	    ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	final Handler handler = new Handler();
        	    handler.postDelayed(new Runnable() {
        	      @Override
        	      public void run() {
        	    	  finish();
        	      }
        	    }, 1000);
            }
        });
	    
	    
	   	}
}
