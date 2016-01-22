package com.example.espdronesremote;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;


import com.example.espdronesremote.JoystickView.OnJoystickMoveListener;

import com.example.espdronesremote.JoystickView;

import com.example.espdronesremote.StartPointSeekBar;

import android.R.*;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toast;


public class MainActivity extends Activity {
	String value;
	byte   packetDataControl[] = {0x7f,126,126,0,126,0};//startBit,ch1,ch2,ch3,ch4,sum(ch1...4)
	byte packetDataSetUp[] = {0x7f,0x7f,0x01,0,0,0,0};//startBit,startBit,Yaw= 0x01 Pitch = 0x02,Roll = 0x03,kp,ki,kd,sum(kp ki kd)
	long count = 0;
	String serverIp;
	InetAddress serverAddr;
	 /*private TextView angleTextView;
	    private TextView powerTextView;
	    private TextView directionTextView;
	    private TextView joy2,dataOut,sendOk;
	    private Button kpUp ,kiUp ,kdUp,kpDown,kiDown,kdDown;
	    private TextView kpValue,kiValue,kdValue;
	    */
	RadioButton sendData,stopData;
	boolean sendDatas = false;
	boolean sendDataFished = true;
	
	private Button setUp;
	private TextView sendOk;
	StartPointSeekBar trimYaw,trimRoll,trimPitch;
	 private TextView trimValues;
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
	    private int REFRESH_TIME = 20;
	    
	    static int kpSend;//trimYawValue,trimRollValue,trimPitchValue;
	    static int kiSend;
	    static int kdSend;
	    double trimYawValue,trimRollValue,trimPitchValue;
	    int yv,pv,rv;
	    //float kpShow,kiShow,kdShow;// = kpSend/100
	    int kiRoll = 1,kiPitch = 1;
	    private final static String STORETEXT="storetext.txt";

	    private EditText txtEditor;
	    private RadioGroup radioGroup;

	    /** Called when the activity is first created. */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//Alway on
		sendOk = (TextView) findViewById(R.id.dataSend);
		setUp = (Button)findViewById(R.id.toSetPID);//click to page setup PID
		radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		
		trimValues = (TextView) findViewById(R.id.trimValue);//for show trim data each CH
		//sendData = (RadioButton)findViewById(R.id.sendData);
		//stopData = (RadioButton)findViewById(R.id.stopData);
		//setup RadioButton
		
		  /* Attach CheckedChangeListener to radio group */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if(null!=rb && checkedId > -1){
                    //Toast.makeText(MainActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                	if(rb.getText().equals("Stop!")){
                		sendDatas = false;
        				sendOk.setText("Stop!!! ");
                	}
                	if(rb.getText().equals("Send!")){
                		sendDatas = true;
                		sendOk.setText("Send!");
        				
                	}
                }

            }
        });
		
		/*sendData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				sendDatas = true;
				
			}
		});
		
		stopData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				sendDatas = false;
				sendOk.setText("Stop!!! ");
				//sendData.clear
			}
		});
		
		
		
		//set button setUp
		setUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), set_pid.class);
				startActivity(i);
			}
		});
		*/
		
		//
		
		
		//set up to connect UI
		//angleTextView = (TextView) findViewById(R.id.angleTextView);
       // powerTextView = (TextView) findViewById(R.id.powerTextView);
       // directionTextView = (TextView) findViewById(R.id.directionTextView);
        //joy2 = (TextView) findViewById(R.id.joy2);
       /* dataOut = (TextView)findViewById(R.id.dataOuT);
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
        */
		/*trimYValues = (TextView) findViewById(R.id.trimYValue);
		trimRValues = (TextView) findViewById(R.id.trimRValue);
		trimPValues = (TextView) findViewById(R.id.trimPValue);
		
		
		 kpUp= (Button) findViewById(R.id.KpUp);
	        kiUp = (Button) findViewById(R.id.KiUp);
	        kdUp = (Button) findViewById(R.id.KdUp);
	        
	        kpDown= (Button) findViewById(R.id.KpDown);
	        kiDown = (Button) findViewById(R.id.KiDown);
	        kdDown = (Button) findViewById(R.id.KdDown);*/
	      
	        trimYaw = (StartPointSeekBar) findViewById(R.id.trimYaws);
	        trimRoll = (StartPointSeekBar) findViewById(R.id.trimRolls);
	        trimPitch = (StartPointSeekBar) findViewById(R.id.trimPitchs);
	        
	        trimYaw.setProgress(0);
	        trimRoll.setProgress(0);
	        trimPitch.setProgress(0);
	        
	      
	        trimYaw.setOnSeekBarChangeListener(new StartPointSeekBar.OnSeekBarChangeListener() {
	            @Override
	            public void onOnSeekBarValueChange(StartPointSeekBar bar, double value) {
	                //Log.d(LOGTAG, "seekbar value:" + value);
	            	trimYawValue = value;
	            	yv = (int) trimYawValue/4;
	            	trimValues.setText("Yaw:"+Integer.toString(yv)+" Pitch:"+Integer.toString(pv)+" Roll:"+Integer.toString(rv));
	            	
	            	
	            	
	            }
	        });
	        
	        trimRoll.setOnSeekBarChangeListener(new StartPointSeekBar.OnSeekBarChangeListener() {
	            @Override
	            public void onOnSeekBarValueChange(StartPointSeekBar bar, double value) {
	                //Log.d(LOGTAG, "seekbar value:" + value);
	            	trimRollValue = value;
	            	rv = (int) trimRollValue/4;
	            	trimValues.setText("Yaw:"+Integer.toString(yv)+" Pitch:"+Integer.toString(pv)+" Roll:"+Integer.toString(rv));
	            }
	        });
	        
	        trimPitch.setOnSeekBarChangeListener(new StartPointSeekBar.OnSeekBarChangeListener() {
	            @Override
	            public void onOnSeekBarValueChange(StartPointSeekBar bar, double value) {
	                //Log.d(LOGTAG, "seekbar value:" + value);
	            	trimPitchValue = value;
	            	pv = (int) trimPitchValue/4;
	            	trimValues.setText("Yaw:"+Integer.toString(yv)+" Pitch:"+Integer.toString(pv)+" Roll:"+Integer.toString(rv));
	            }
	        });
	        
	        
	       // StartPointSeekBar seekBarX = (StartPointSeekBar) findViewById(R.id.seek_bar);
	        /*seekBarX.setProgress(0);
	        seekBarX.setOnSeekBarChangeListener(new StartPointSeekBar.OnSeekBarChangeListener() {
	            @Override
	            public void onOnSeekBarValueChange(StartPointSeekBar bar, double value) {
	                //Log.d(LOGTAG, "seekbar value:" + value);
	            }
	        });
	        */
	        /*kpUp.setOnClickListener(this);
	        kiUp.setOnClickListener(this);
	        kdUp.setOnClickListener(this);
	        
	        kpDown.setOnClickListener(this);
	        kiDown.setOnClickListener(this);
	        kdDown.setOnClickListener(this);
	        
	        pidValue = (TextView) findViewById(R.id.pidValue);
	        pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kpShow);
	        */
	       
	        
	        
		
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
        /*
        joy2.setText("y2 = "+String.valueOf(y2)+"   x2 = "+String.valueOf(x2));
        directionTextView.setText("y = "+String.valueOf(y)+"   x = "+String.valueOf(x));
        dataOut.setText("Ele = "+String.valueOf(ch1_ele)+"  Roll = "+String.valueOf(ch2_roll)+"  Power = "+String.valueOf(ch3_power)+"  Yaw = "+String.valueOf(ch4_yaw));
        sendOk.setText("No send");
        */
        //
        //new Timer().scheduleAtFixedRate(new TimerTask()  {
         //   @Override
        //    public void run() {
        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick.setOnJoystickMoveListener(new OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
            	
            	
                //angleTextView.setText(" " + String.valueOf(angle) + "'");
                //powerTextView.setText(" " + String.valueOf(power) + "%");
                
                y = (int) ((Math.cos(Math.toRadians(angle)))*power);
            	x = (int) ((Math.sin(Math.toRadians(angle)))*power);
            	
            	//ch1_ele	 = 
            	sendUDPdata();
            	//sendFinish = false;
    		     //end send data
            	
                //directionTextView.setText("y = "+String.valueOf(y)+"   x = "+String.valueOf(x));
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
                //angleTextView.setText(" " + String.valueOf(angle) + "'");
                //powerTextView.setText(" " + String.valueOf(power) + "%");
                
                y2 = (int) ((Math.cos(Math.toRadians(angle)))*power);
            	x2 = (int) ((Math.sin(Math.toRadians(angle)))*power);
            	
            	
            	 //joy2.setText("y2 = "+String.valueOf(y2)+"   x2 = "+String.valueOf(x2));
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
	
	
	/*
	
	 @Override
     public void onClick(View v) {
         switch(v.getId()){
         
             case R.id.KpUp:
                  //DO something
            	 //trimYaw++;
            	 kpSend++;
            	 
           	  kpShow = (float)(kpSend)/10;
           	  //kpValue.setText(""+kpShow);
           	pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kdShow);
             break;
             case R.id.KiUp:
                  //DO something
            	 //trimYaw--;
            	 kiSend++;
             	 kiShow = (float)(kiSend)/10;
             	 // kiValue.setText(""+kiShow);
             	pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kdShow);
             break;
             case R.id.KdUp:
                 //DO something
           	 //trimYaw--;
           	 kdSend++;
            	  kdShow = (float)(kdSend)/10;
            	 // kiValue.setText(""+kiShow);
            	  pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kdShow);
            break;
             case R.id.KpDown:
                 //DO something
           	 //trimYaw--;
           	 kpSend--;
           	if(kpSend < 0){
        		kpSend = 0;
        	}
            	  kpShow = (float)(kpSend)/10;
            	 // kiValue.setText(""+kiShow);
            	  pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kdShow);
            break;
             case R.id.KiDown:
                 //DO something
           	 //trimYaw--;
           	 kiSend--;
           	if(kiSend < 0){
        		kiSend = 0;
        	}
            	  kiShow = (float)(kiSend)/10;
            	 // kiValue.setText(""+kiShow);
            	  pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kdShow);
            break;
             case R.id.KdDown:
                 //DO something
           	 //trimYaw--;
           	 kdSend--;
           	if(kdSend < 0){
        		kdSend = 0;
        	}
            	  kdShow = (float)(kdSend)/10;
            	 // kiValue.setText(""+kiShow);
            	  pidValue.setText("Kp = "+kpShow+"  Ki = "+kiShow+"   Kd =  "+kdShow);
            break;
             
            
         }

   }
*/


	
	/* (non-Javadoc)
		 * @see android.app.Activity#onPause()
		 */
		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			//myClientTask.cancel(true);
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
		//if(sendCount == 0){
		
//timemer = System.currentTimeMillis();
        
        //if( (timemer % 5)  < 2){// send data to ESP here
        	//get x y to ech ch
        	ch1_ele =  (int) ((y+99)*1.28);
        	ch2_roll =  (int) ((x+99)*1.28);
        	if(startJoy2 == true){
        		ch3_power =  (int) ((y2+99)*1.8)-55;
        		if(ch3_power<0){
        			ch3_power = 0;
        		}
        	}else{
        		ch3_power = 0;	
        	}
        	ch4_yaw =  (int) ((x2+99)*1.28);
        	//offset
        	//
        	//.... CH1 ....
        	
        	if(ch1_ele > 126){//offset up elev
        		if(ch1_ele < 136){
        			ch1_ele = 126;
        		}else{
        			ch1_ele -= 9;
        		}
        		
        	}
        	if(ch1_ele < 126){//offset down elev
        		if(ch1_ele > 116){
        			ch1_ele = 126;
        		}else{
        			ch1_ele += 9;
        		}
        	}
        	if(ch1_ele == 126){
        		kiPitch = 1;
        	}else{
        		kiPitch = 0;
        	}
        	
        	ch1_ele += pv;
        	//
        	//------- Ch2 ------

        	if(ch2_roll > 126){//offset right roll
        		if(ch2_roll < 136){
        			ch2_roll = 126;
        		}else{
        			ch2_roll -= 9;
        		}
        		
        	}
        	if(ch2_roll < 126){//offset left roll 
        		if(ch2_roll > 116){
        			ch2_roll = 126;
        		}
        		else{
        			ch2_roll += 9;
        		}
        	}
        	if(ch2_roll == 126){
        		kiRoll = 1;
        	}else{
        		kiRoll = 0;
        	}
        	ch2_roll += rv;
        	
        	//
        	//++++++++  CH4  ++++++++
        	if(ch4_yaw > 126){//offset right yaw
        		if(ch4_yaw < 136){
        			ch4_yaw = 126;
        		}else{
        			ch4_yaw -= 10;
        		}
        		
        	}
        	if(ch4_yaw < 126){//offset left yaw 
        		if(ch4_yaw > 116){
        			ch4_yaw = 126;
        		}else{
        			ch4_yaw += 10;
        		}
        	}
        	ch4_yaw += yv;
        	//
        	//end off set
        	if(ch1_ele < 0){
        		ch1_ele = 0;
        	}
        	if(ch2_roll < 0){
        		ch2_roll = 0;
        	}
        	
        	if(ch4_yaw < 0){
        		ch4_yaw = 0;
        	}
        	//KI! off when control roll pitch
        	
        	//dataOut.setText("Ele = "+String.valueOf(ch1_ele)+"  Roll = "+String.valueOf(ch2_roll)+"  Power = "+String.valueOf(ch3_power)+"  Yaw = "+String.valueOf(ch4_yaw));
        	//
        	if(sendDatas){
        	
        	//String dataSends =  "a"+Integer.toString(ch1_ele)+"b"+Integer.toString(ch2_roll)+"c"+Integer.toString(ch3_power)+"d"+Integer.toString(ch4_yaw)+"p"+Integer.toString(kpSend)+"i"+Integer.toString(kiSend)+"k"+Integer.toString(kdSend)+"z"+Integer.toString(kiRoll)+"x"+Integer.toString(kiPitch)+"!";
			
		    packetDataControl[1] = (byte) ((byte) ch1_ele - 126);   
		    packetDataControl[2] = (byte) ((byte) ch2_roll -126); 
		    packetDataControl[3] = (byte) ((byte) ch3_power - 126); 
		    packetDataControl[4] =  (byte) ((byte) ch4_yaw - 126) ; 
		    packetDataControl[5] =  (byte) ((byte) (ch1_ele + ch2_roll + ch3_power + ch4_yaw) -126 -122); //sum
			//sendCount++;
			if(sendDataFished){
				myClientTask = new MyClientTask("");//send data to UDP
			     myClientTask.execute();//work UDP
			     //sendCount++;
			     sendDataFished = false;
			     count++;
			     if(count > 10000){
			    	 count = 0;
			     }
			  }
			    
			}else{
				
				//
			}
		//}
		    
       // }
	}//end sendUDPdata()
	
	public void readFileInEditor()

	{

	try {

	InputStream in = openFileInput(STORETEXT);

	if (in != null) {

	InputStreamReader tmp=new InputStreamReader(in);

	BufferedReader reader=new BufferedReader(tmp);

	String str;

	StringBuilder buf=new StringBuilder();

	while ((str = reader.readLine()) != null) {
		yv = Integer.parseInt(str);
	buf.append(str+"n");

	}

	in.close();

	txtEditor.setText(buf.toString());

	}

	}

	catch (java.io.FileNotFoundException e) {

	// that's OK, we probably haven't created it yet

	}

	catch (Throwable t) {

	

	}

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
						         serverAddr = InetAddress.getByName("192.168.5.1");
						        
						        //DatagramSocket ds = new DatagramSocket();
						       // DatagramPacket dp;
						        DatagramPacket dp = new DatagramPacket(packetDataControl, packetDataControl.length, serverAddr,12345);
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
			  //Toast.makeText(getApplicationContext(), "Send OK"+count+" "+num,Toast.LENGTH_SHORT).show();
			  sendOk.setText("Send OK"+count+" "+packetDataControl[2]+"server ip"+serverAddr);
			  if(!sendDatas){
				  sendOk.setText("Stop!!");
			  }
			  if(ch3_power > 100){
				 // dataOut.setText("Ele = "+String.valueOf(ch1_ele)+"  Roll = "+String.valueOf(ch2_roll)+"  Power = "+String.valueOf(ch3_power)+"  Yaw = "+String.valueOf(ch4_yaw)+"!!!"); 
				 xxx = "!!!" ;
			  }
			  //dataOut.setText("Ele = "+String.valueOf(ch1_ele)+"  Roll = "+String.valueOf(ch2_roll)+"  Power = "+String.valueOf(ch3_power)+"  Yaw = "+String.valueOf(ch4_yaw)+" "+xxx);
			  sendDataFished = true;
		   super.onPostExecute(result);
		//super.isCancelled();
		  }
	}





	



	
	
}
