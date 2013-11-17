package com.moarcodeplz.moarsensorlogger.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.moarsensorlogger.R;
import com.moarcodeplz.moarsensorlogger.activity.MainActivity;

public class MainFragment extends Fragment {
	
	Button trainBtn;
	Button sensorBtn;
	Button settingBtn;
	Button munlockerBtn;
	Button graphBtn;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trainBtn = (Button)view.findViewById(R.id.trainingBtn);
        trainBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).startFragmentTransaction(MainActivity.TRAINING);
			}
		});
        sensorBtn = (Button) view.findViewById(R.id.sensorBtn);
        sensorBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).startFragmentTransaction(MainActivity.SENSOR);				
			}
		});
        settingBtn = (Button) view.findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).startFragmentTransaction(MainActivity.SETTING);				
			}
		});
        
        munlockerBtn = (Button) view.findViewById(R.id.munlockerBtn);
        munlockerBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).startFragmentTransaction(MainActivity.LOCKSCREEN);				
			}
		});
        
        graphBtn = (Button) view.findViewById(R.id.graphBtn);
        graphBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).startFragmentTransaction(MainActivity.GRAPH);				
			}
		});
    }
    
}