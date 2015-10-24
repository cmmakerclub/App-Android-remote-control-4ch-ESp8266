package com.example.espdronesremote;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;


import com.example.espdronesremote.JoystickView.OnJoystickMoveListener;

import com.example.espdronesremote.JoystickView;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener{
	String value;
	long count = 0;
	 private TextView angleTextView;
	    private TextView powerTextView;
	    private TextView directionTextView;
	    private TextView joy2,dataOut,sendOk;
	    private Button kpUp ,kiUp ,kdUp,kpDown,kiDown,kdDown;
	    private TextView kpValue,kiValue,kdValue;
	    //TimerTask mTimer;
	    //private SeekBar seekBar;
	    // Importing also other views
	    private JoystickView joystick,joystick2;
	    MyClientTask myClientTask;
	    
	    int x = 0,ch2_roll = 126;
	    int y = 0,ch1_ele = 126;
	    int x2 = 0,ch4_yaw = 126;
	    int y2 = 0,ch3_power = 0;
	    String xxx = "";
	    int t = 0;
	    long timemer;
	    boolean startJoy2 = false;
	    int sendCount = 0;
	    private int REFRESH_TIME = 60;
	    
	    int kpSend,kiSend,kdSend;
	    float kpShow,kiShow,kdShow;// = kpSend/100
	    /** Called when the activity is first created. */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//Alway on
		
		//set up to connect UI
		angleTextView = (TextView) findViewById(R.id.angleTextView);
        powerTextView = (TextView) findViewById(R.id.powerTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);
        joy2 = (TextView) findViewById(R.id.joy2);
        dataOut = (TextView)findViewById(R.id.dataOuT);
        sendOk = (TextView) findViewById(R.id.check);
        
        kpUp= (Button) findViewById(R.id.kpUp);
        kiUp = (Button) findViewById(R.id.kiUp);
        kdUp = (Button) findViewById(R.id.kdUp);
        
        kpDown= (Button) findViewById(R.id.kpDown);
        kiDown = (Button) findViewById(R.id.kiDown);
        kdDown = (Button) findViewById(R.id.kdDown);
        
        kpValue = (TextView) findViewById(R.id.kpValue);
        kiValue = (TextView) findViewById(R.id.kiValue);
        kdValue = (TextView) findViewById(R.id.kdValue);
        
        kpUp.setOnClickListener(this);
        kiUp.setOnClickListener(this);
        kdUp.setOnClickListener(this);
        
        kpDown.setOnClickListener(this);
        kiDown.setOnClickListener(this);
        kdDown.setOnClickListener(this);
        
        
        kpValue.setText(""+kpShow);
        kiValue.setText(""+kiShow);
        kdValue.setText(""+kdShow);
        // Add Button event
       /* kpUp.setOnClickListener(new View.OnClickListener() {
          @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
        	  kpSend++;
        	  kpShow = (float)kpSend/100;
        	  kpValue.setText(""+kpShow);
			}
        });
        
        kiUp.setOnClickListener(new View.OnClickListener() {
            @Override
  			public void onClick(View v) {
  				// TODO Auto-generated method stub
          	  kiSend++;
          	  kiShow = kiSend/100;
          	  kiValue.setText(""+kiShow);
  			}
          });
        
        kdUp.setOnClickListener(new View.OnClickListener() {
            @Override
  			public void onClick(View v) {
  				// TODO Auto-generated method stub
          	  kdSend++;
          	  kdShow = kdSend/100;
          	  kdValue.setText(""+kdShow);
  			}
          });
        //Add Button kp ki kd Down value
        
        kpDown.setOnClickListener(new View.OnClickListener() {
            @Override
  			public void onClick(View v) {
  				// TODO Auto-generated method stub
          	  kpSend--;
          	  if(kpSend < 0){
          		kpSend = 0;
          	  }
          	  kpShow = kpSend/100;
          	  kpValue.setText(""+kpShow);
  			}
          });
        
        kiDown.setOnClickListener(new View.OnClickListener() {
            @Override
  			public void onClick(View v) {
  				// TODO Auto-generated method stub
          	  kiSend--;
          	  if(kiSend < 0){
          		kiSend = 0;
          	  }
          	  kiShow = kiSend/100;
          	  kiValue.setText(""+kiShow);
  			}
          });
        
        kdDown.setOnClickListener(new View.OnClickListener() {
            @Override
  			public void onClick(View v) {
  				// TODO Auto-generated method stub
          	  kdSend--;
          	  if(kdSend < 0){
          		kdSend = 0;
          	  }
          	  kdShow = kdSend/100;
          	  kdValue.setText(""+kdShow);
  			}
          });*/
        
        //set oncilck
      
           
       
        
        //Referencing also other views
        joystick = (JoystickView) findViewById(R.id.joystickView);
        joystick2 = (JoystickView) findViewById(R.id.joystickView2);
        joystick2.tLMode = true;
        joystick2.setyPositionY((int)y2);
        
        joy2.setText("y2 = "+String.valueOf(y2)+"   x2 = "+String.valueOf(x2));
        directionTextView.setText("y = "+String.valueOf(y)+"   x = "+String.valueOf(x));
        dataOut.setText("Ele = "+String.valueOf(ch1_ele)+"  Roll = "+String.valueOf(ch2_roll)+"  Power = "+String.valueOf(ch3_power)+"  Yaw = "+String.valueOf(ch4_yaw));
        sendOk.setText("No send");
        //
        //new Timer().scheduleAtFixedRate(new TimerTask()  {
         //   @Override
        //    public void run() {
        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick.setOnJoystickMoveListener(new OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
            	
            	
                angleTextView.setText(" " + String.valueOf(angle) + "'");
                powerTextView.setText(" " + String.valueOf(power) + "%");
                
                y = (int) ((Math.cos(Math.toRadians(angle)))*power);
            	x = (int) ((Math.sin(Math.toRadians(angle)))*power);
            	
            	//ch1_ele	 = 
            	sendUDPdata();
            	//sendFinish = false;
    		     //end send data
            	
                directionTextView.setText("y = "+String.valueOf(y)+"   x = "+String.valueOf(x));
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
        ///
        ///
        joystick2.setOnJoystickMoveListener(new OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
            	startJoy2 = true;
            	joystick2.tMode = true;
            	joystick2.tLMode = false;
                angleTextView.setText(" " + String.valueOf(angle) + "'");
                powerTextView.setText(" " + String.valueOf(power) + "%");
                
                y2 = (int) ((Math.cos(Math.toRadians(angle)))*power);
            	x2 = (int) ((Math.sin(Math.toRadians(angle)))*power);
            	
            	
            	 joy2.setText("y2 = "+String.valueOf(y2)+"   x2 = "+String.valueOf(x2));
            	 sendUDPdata();
            	 //sendFinish = false;
     		     //end send data
                 
            }
            
            
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
        
       // joystick2.setyPositionY(y2);
        
        
        
        if(joystick2.joyCenter == true  &&  joystick.joyCenter == true){
        	
	        new Timer().scheduleAtFixedRate(new TimerTask()  {
	           @Override
	           public void run() {
	                   // What you want to do goes here
	        	   
	        	  
	            	sendUDPdata();
	        	   
	                 }
	            }, 0, REFRESH_TIME);
        }//end if
        
	}//end onCreate
	
	 @Override
     public void onClick(View v) {
         switch(v.getId()){
             case R.id.kpUp:
                  //DO something
            	 kpSend++;
           	  kpShow = (float)(kpSend)/10;
           	  kpValue.setText(""+kpShow);
             break;
             case R.id.kiUp:
                  //DO something
            	 kiSend++;
             	  kiShow = (float)(kiSend)/10;
             	  kiValue.setText(""+kiShow);
             break;
             case R.id.kdUp:
                  //DO something
            	 kdSend++;
             	  kdShow = (float)(kdSend)/10;
             	  kdValue.setText(""+kdShow);
             break;
             
             case R.id.kpDown:
                 //DO something
            	 kpSend--;
             	  if(kpSend < 0){
             		kpSend = 0;
             	  }
             	  kpShow = (float)(kpSend)/10;
             	  kpValue.setText(""+kpShow);
            break;
            case R.id.kiDown:
                 //DO something
            	 kiSend--;
             	  if(kiSend < 0){
             		kiSend = 0;
             	  }
             	  kiShow = (float)(kiSend)/10;
             	  kiValue.setText(""+kiShow);
            break;
            case R.id.kdDown:
                 //DO something
            	 kdSend--;
             	  if(kdSend < 0){
             		kdSend = 0;
             	  }
             	  kdShow = (float)(kdSend)/10;
             	  kdValue.setText(""+kdShow);
            break;
         }

   }



	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void sendUDPdata() {
		//sendCount++;
		if(sendCount == 0){
		
//timemer = System.currentTimeMillis();
        
        //if( (timemer % 5)  < 2){// send data to ESP here
        	//get x y to ech ch
        	ch1_ele =  (int) ((y+99)*1.28);
        	ch2_roll =  (int) ((x+99)*1.28);
        	if(startJoy2 == true){
        		ch3_power =  (int) ((y2+99)*1.28);
        	}else{
        		ch3_power = 0;	
        	}
        	ch4_yaw =  (int) ((x2+99)*1.28);
        	//dataOut.setText("Ele = "+String.valueOf(ch1_ele)+"  Roll = "+String.valueOf(ch2_roll)+"  Power = "+String.valueOf(ch3_power)+"  Yaw = "+String.valueOf(ch4_yaw));
        	//
        	
        	
        	
        	String dataSends =  "a"+Integer.toString(ch1_ele)+"b"+Integer.toString(ch2_roll)+"c"+Integer.toString(ch3_power)+"d"+Integer.toString(ch4_yaw)+"p"+Integer.toString(kpSend)+"i"+Integer.toString(kiSend)+"k"+Integer.toString(kdSend)+"!";
			myClientTask = new MyClientTask(dataSends);//send data to UDP
		       
			//sendCount++;
			//if(sendCount == 0){
			     myClientTask.execute();//work UDP
			     sendCount++;
			     count++;
			     if(count > 10000){
			    	 count = 0;
			     }
			    
			}
		    
       // }
	}
	
	
	
	
	
	public class MyClientTask extends AsyncTask<Void, Void, Void> {
		
		String num;
		  //String response = "";
		  
		  MyClientTask(String value){
		  // dstAddress = addr;
		   //dstPort = port;
			  num = value;
		  }
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			 String udpMsg = num;
			  DatagramSocket ds = null;
			//long tick = System.currentTimeMillis();
			
				
			
						   try {
							   ds = new DatagramSocket();
						        InetAddress serverAddr = InetAddress.getByName("192.168.4.1");
						        //DatagramSocket ds = new DatagramSocket();
						       // DatagramPacket dp;
						        DatagramPacket dp = new DatagramPacket(udpMsg.getBytes(), udpMsg.length(), serverAddr,80);
						        ds.send(dp);
						        
						        
						   }catch (UnknownHostException e) {
							    // TODO Auto-generated catch block
							    e.printStackTrace();
							    //response = "UnknownHostException: " + e.toString();
							   } catch (IOException e) {
							    // TODO Auto-generated catch block
							    e.printStackTrace();
							   //response = "IOException: " + e.toString();
							   }finally{
								   ds.close();
							   }
						   
			   return null;
		}
		
		  @Override
		  protected void onPostExecute(Void result) {
		  // textResponse.setText(response);
			  //Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",Toast.LENGTH_SHORT).show();
			  sendOk.setText("Send OK"+count+" "+num);
			  if(ch3_power > 100){
				 // dataOut.setText("Ele = "+String.valueOf(ch1_ele)+"  Roll = "+String.valueOf(ch2_roll)+"  Power = "+String.valueOf(ch3_power)+"  Yaw = "+String.valueOf(ch4_yaw)+"!!!"); 
				 xxx = "!!!" ;
			  }
			  dataOut.setText("Ele = "+String.valueOf(ch1_ele)+"  Roll = "+String.valueOf(ch2_roll)+"  Power = "+String.valueOf(ch3_power)+"  Yaw = "+String.valueOf(ch4_yaw)+" "+xxx);
			  
			  sendCount--;
		   super.onPostExecute(result);
		//super.isCancelled();
		  }
	}





	



	
	
}
