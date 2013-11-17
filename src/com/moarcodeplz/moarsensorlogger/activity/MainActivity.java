package com.moarcodeplz.moarsensorlogger.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.moarsensorlogger.R;
import com.moarcodeplz.moarsensorlogger.activity.fragment.GraphFragment;
import com.moarcodeplz.moarsensorlogger.activity.fragment.LockScreenFragment;
import com.moarcodeplz.moarsensorlogger.activity.fragment.MainFragment;
import com.moarcodeplz.moarsensorlogger.activity.fragment.SensorFragment;
import com.moarcodeplz.moarsensorlogger.activity.fragment.SettingFragment;
import com.moarcodeplz.moarsensorlogger.activity.fragment.TrainFragment;


public class MainActivity extends EntryActivity {
	public static final int MAIN =0;
	public static final int TRAINING =1;
	public static final int LOCKSCREEN =2;
	public static final int SENSOR = 3;
	public static final int SETTING=4;
	public static final int GRAPH=5;
	
	boolean firstAdded=false;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base);
        startFragmentTransaction(MAIN);
    }


    public void startFragmentTransaction(int id){
    	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    	Fragment fragment;
        switch (id){
            case MAIN:
            	if(!firstAdded){
                fragment = new MainFragment();
                transaction.add(R.id.act_base_content_frame, fragment);
                transaction.commit();
                firstAdded=true;
            	}
            	else{
            		fragment = new MainFragment();
                    transaction.replace(R.id.act_base_content_frame, fragment);
                    transaction.commit();	
            	}
                break;
            
            case TRAINING:
                fragment = new TrainFragment();
                transaction.addToBackStack(null);
                transaction.replace(R.id.act_base_content_frame, fragment);
                transaction.commit();
                break;
           
            case SENSOR:
            	fragment = new SensorFragment();
            	transaction.addToBackStack(null);
            	transaction.replace(R.id.act_base_content_frame, fragment);
                transaction.commit();
                break;
                
            case SETTING:
            	fragment = new SettingFragment();
            	transaction.addToBackStack(null);
            	transaction.replace(R.id.act_base_content_frame, fragment);
                transaction.commit();
                break;
            case LOCKSCREEN:
            	fragment = new LockScreenFragment();
            	transaction.addToBackStack(null);
            	transaction.replace(R.id.act_base_content_frame, fragment);
                transaction.commit();
                break;
            case GRAPH:
            	fragment = new GraphFragment();
            	transaction.addToBackStack(null);
            	transaction.replace(R.id.act_base_content_frame, fragment);
                transaction.commit();
                break;
        }
    }
}
