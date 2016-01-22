package com.example.espdronesremote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class set_pid extends Activity implements OnClickListener{
	private Button backToMain;
	private Button kpUp ,kiUp ,kdUp,kpDown,kiDown,kdDown;
	float kpShow,kiShow,kdShow;// = kpSend/100
	private TextView pidValue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//Alway on
		setContentView(R.layout.page_set_pid);
		
		pidValue = (TextView)findViewById(R.id.pidValue);
		backToMain = (Button)findViewById(R.id.backToMain);//click for back to Mian
		
		
		  kpUp= (Button) findViewById(R.id.kpUp);
	        kiUp = (Button) findViewById(R.id.kiUp);
	        kdUp = (Button) findViewById(R.id.kdUp);
	        
	        kpDown= (Button) findViewById(R.id.kpDown);
	        kiDown = (Button) findViewById(R.id.kiDown);
	        kdDown = (Button) findViewById(R.id.kdDown);
	        
	        kpUp.setOnClickListener(this);
	        kiUp.setOnClickListener(this);
	        kdUp.setOnClickListener(this);
	        
	        kpDown.setOnClickListener(this);
	        kiDown.setOnClickListener(this);
	        kdDown.setOnClickListener(this);
	        
	        
	        
	        
		//set button work
		backToMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();//end setup PID. Back to Main
			}
		});
	}//end OnCreate
	
	
	

	
	
	
	 public void onClick(View v) {
        switch(v.getId()){
        
            case R.id.kpUp:
                 //DO something
           	 //trimYaw++;
           	 MainActivity.kpSend++;
           	 
          	  kpShow = (float)(MainActivity.kpSend)/10;
          	  //kpValue.setText(""+kpShow);
          	pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kdShow);
            break;
            case R.id.kiUp:
                 //DO something
           	 //trimYaw--;
            	 MainActivity.kiSend++;
            	 kiShow = (float)( MainActivity.kiSend)/10;
            	 // kiValue.setText(""+kiShow);
            	pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kdShow);
            break;
            case R.id.kdUp:
                //DO something
          	 //trimYaw--;
            	 MainActivity.kdSend++;
           	  kdShow = (float)( MainActivity.kdSend)/10;
           	 // kiValue.setText(""+kiShow);
           	  pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kdShow);
           break;
            case R.id.kpDown:
                //DO something
          	 //trimYaw--;
            	 MainActivity.kpSend--;
          	if( MainActivity.kpSend < 0){
          		 MainActivity.kpSend = 0;
       	}
           	  kpShow = (float)( MainActivity.kpSend)/10;
           	 // kiValue.setText(""+kiShow);
           	  pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kdShow);
           break;
            case R.id.kiDown:
                //DO something
          	 //trimYaw--;
            	 MainActivity.kiSend--;
          	if( MainActivity.kiSend < 0){
          		 MainActivity.kiSend = 0;
       	}
           	  kiShow = (float)( MainActivity.kiSend)/10;
           	 // kiValue.setText(""+kiShow);
           	  pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kdShow);
           break;
            case R.id.kdDown:
                //DO something
          	 //trimYaw--;
            	 MainActivity.kdSend--;
          	if( MainActivity.kdSend < 0){
          		 MainActivity.kdSend = 0;
       	}
           	  kdShow = (float)( MainActivity.kdSend)/10;
           	 // kiValue.setText(""+kiShow);
           	  pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kdShow);
           break;
            
           
        }

  }



	

}//end class set_pid
