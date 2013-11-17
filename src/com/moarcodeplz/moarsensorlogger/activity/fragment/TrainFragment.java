package com.moarcodeplz.moarsensorlogger.activity.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moarsensorlogger.R;
import com.moarcodeplz.moarsensorlogger.activity.MainActivity;

public class TrainFragment extends Fragment {
	Button startBtn;
	TextView text;
	TextView traintext;
	int trainTxtCounter=0;
	boolean success=false;
	//train stage. 0= first time start, 1= first time finish
	// 2= second time (confirm) start, 3= confirm finish
	int stage=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_train, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startBtn = (Button) view.findViewById(R.id.start_stop_btn);
        text = (TextView) view.findViewById(R.id.traintext);
        traintext = (TextView) view.findViewById(R.id.intrain);

        startBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch(stage){
				case 0:
					startBtn.setText("Finish");
					startBtn.setBackgroundResource(R.drawable.btn_green);
					text.setText("Press Finish when done.");
					stage++;
					traintext.setVisibility(View.VISIBLE);
					break;
				case 1:
					startBtn.setText("Start");
					startBtn.setBackgroundResource(R.drawable.btn_yellow);
					stage++;
					text.setText("Press Start to confirm the pattern training.");
					traintext.setVisibility(View.GONE);
					break;
				case 2:
					startBtn.setText("Finish");
					startBtn.setBackgroundResource(R.drawable.btn_red);
					text.setText("Press Finish to save the motion.");
					stage++;
					traintext.setVisibility(View.VISIBLE);
					break;
				case 3:
					checkValidity();
					break;
				}
				
			}
		});
        
        final Handler handler = new Handler();

		Runnable runnable = new Runnable() {
		    @Override
		    public void run() {
		    	 if(trainTxtCounter==0){
		    		  traintext.setText("Training .");
		    		  trainTxtCounter++;
		    	  }
		    	  else if(trainTxtCounter==1){
		    		  traintext.setText("Training ..");
		    		  trainTxtCounter++;
		    	  }
		    	  else if(trainTxtCounter==2){
		    		  traintext.setText("Training ...");
		    		  trainTxtCounter++;
		    	  }
		    	  else if(trainTxtCounter==3){
		    		  traintext.setText("Training ....");
		    		  trainTxtCounter=0;
		    	  }
		    	if (true) {
		            handler.postDelayed(this, 500);
		        }
		    }
		};
		
		handler.post(runnable);
	    
       
    }
    
    private void checkValidity(){
    	if(!success){
	    	new AlertDialog.Builder(getActivity())
	        .setTitle("Validity")
	        .setMessage("Patterns do not Match. Try Again?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	            	((MainActivity)getActivity()).startFragmentTransaction(MainActivity.TRAINING);	            }
	         })
	        .setNegativeButton("No", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	            	((MainActivity)getActivity()).startFragmentTransaction(MainActivity.MAIN);
	            	Toast.makeText(getActivity(), "Motion Pattern Not Saved", Toast.LENGTH_SHORT).show();
	            }
	         })
	         .show();
    	}
    	else{
    		new AlertDialog.Builder(getActivity())
	        .setTitle("Validity")
	        .setMessage("Patterns match!")
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	            	((MainActivity)getActivity()).startFragmentTransaction(MainActivity.MAIN);
	            	Toast.makeText(getActivity(), "Motion Pattern Saved", Toast.LENGTH_SHORT).show();

	            }
	         })
	        
	         .show();
    	}
    }
}
