package com.example.espdronesremote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class set_pid extends Activity{
	private Button backToMain,submit;
	SeekBar kp,ki,kd;
	EditText hostIp,port;
	float kpValueS,kiValueS,kdValueS;
	TextView kpShowValueS,kiShowValueS,kdShowValueS;
	String hostIpToSend = "192.168.5.1",portOfHost = "12345";
	
	int selectYPR_value = 1;
	byte packetDataSetUp[] = {(byte)(0xf0),(byte)(0xf0),0x01,0,0,0,0};//startBit,startBit,Yaw= 0x01 Pitch = 0x02,Roll = 0x03,kp,ki,kd,sum(kp ki kd)
	
	private RadioGroup selectYPR;
	
	MyClientTask2 taskSendPacketSetUp = new MyClientTask2();
	InetAddress serverAddr;
	
	boolean useSetUp = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//Alway on
		setContentView(R.layout.page_set_pid);
		//taskSendPacketSetUp = new MyClientTask();
		
	
		backToMain = (Button)findViewById(R.id.backToMain);//click for back to Mian
		submit = (Button)findViewById(R.id.buttonSubmit);
		
		kpShowValueS = (TextView)findViewById(R.id.textViewKp);
		kiShowValueS = (TextView)findViewById(R.id.textViewKi);
		kdShowValueS = (TextView)findViewById(R.id.textViewKd);
		
		kp = (SeekBar)findViewById(R.id.seekBarKp);
		ki = (SeekBar)findViewById(R.id.seekBarKi);
		kd = (SeekBar)findViewById(R.id.seekBarKd);
		//set seekbar work
		kp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener () {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				kpValueS = progress;
				kpShowValueS.setText("Kp "+Float.toString(kpValueS/10));
				
			}
		});
		
		
		
ki.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener () {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				kiValueS = progress;
				kiShowValueS.setText("Ki "+Float.toString(kiValueS/10));
				
			}
		});


kd.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener () {
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		kdValueS = progress;
		kdShowValueS.setText("Kd "+Float.toString(kdValueS/10));
		
	}
});

		//end seekbar work
		
		hostIp = (EditText)findViewById(R.id.editTextHostIp);
		hostIp.setText(hostIpToSend);
		port = (EditText)findViewById(R.id.editTextPort);
		port.setText(portOfHost);
		
		// set EditText work
		//!work in submit
		//end EditText work
		
		
		
		  
	        
	        
	        
		//set button back work
		backToMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				/*
				 * Destroy set_pid and open Main
				 */
				if(!taskSendPacketSetUp.isCancelled()){
					taskSendPacketSetUp.cancel(true);//stop task
				}
				finish();//end setup PID. Back to Main
				
				startActivity(i);
			}
		});
		
		//set button submit work
				submit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//get value IP,Port
						setHostIpToSend(hostIp.getText().toString());
						setPortOfHost(port.getText().toString());
						//setUseSetUp(true);
						MainActivity.hostip = getHostIpToSend();
						MainActivity.portS = getPortOfHost();
						
						//caculet value
						packetDataSetUp[2] = (byte) selectYPR_value;
						
						packetDataSetUp[3] = (byte) ((byte) (kpValueS - 128));
						packetDataSetUp[4] = (byte) ((byte) (kiValueS- 128));
						packetDataSetUp[5] = (byte) ((byte) (kdValueS- 128));
						
						packetDataSetUp[6] = (byte)(((byte) (kpValueS - 128)) + ((byte) (kiValueS - 128)) + ((byte) (kdValueS- 128)));
						//
						//Toast.makeText(getApplicationContext(), "host:"+getHostIpToSend()+" port:"+getPortOfHost()+" kp:"+packetDataSetUp[3]+" ki:"+packetDataSetUp[4]+" kd:"+packetDataSetUp[5]+" YPR:"+packetDataSetUp[2]+" sum"+packetDataSetUp[6],Toast.LENGTH_SHORT).show();
						//send UDP!
						taskSendPacketSetUp = new MyClientTask2();
						taskSendPacketSetUp.execute();
					}
				});
				
				
				//
				selectYPR = (RadioGroup)findViewById(R.id.radioGroupYPR);
				//set seletc Yaw Pitch Roll work
				
				selectYPR.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						 RadioButton rb = (RadioButton) group.findViewById(checkedId);
			                if(null!=rb && checkedId > -1){
			                    //Toast.makeText(MainActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
			                	if(rb.getText().equals("Yaw")){
			                		selectYPR_value = 0x01;
			                	}
			                	if(rb.getText().equals("Pitch")){
			                		selectYPR_value = 0x02;
			        				
			                	}
			                	if(rb.getText().equals("Roll")){
			                		selectYPR_value = 0x03;
			        				
			                	}
			                }
					}
				});
				
				//end seletc Yaw Pitch Roll work
		
		
	}//end OnCreate


	

	/**
	 * @return the hostIpToSend
	 */
	public String getHostIpToSend() {
		return hostIpToSend;
	}


	/**
	 * @param hostIpToSend the hostIpToSend to set
	 */
	public void setHostIpToSend(String hostIpToSend) {
		this.hostIpToSend = hostIpToSend;
	}


	/**
	 * @return the portOfHost
	 */
	public String getPortOfHost() {
		return portOfHost;
	}


	/**
	 * @param portOfHost the portOfHost to set
	 */
	public void setPortOfHost(String portOfHost) {
		this.portOfHost = portOfHost;
	}
	
	
	
	
	
	// send UDP
	public class MyClientTask2 extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			if(!taskSendPacketSetUp.isCancelled()){
				 //String udpMsg = num;
				  DatagramSocket ds = null;
				//long tick = System.currentTimeMillis();
				
					
				
							   try {
								   ds = new DatagramSocket();
							         serverAddr = InetAddress.getByName(getHostIpToSend());
							        
							        //DatagramSocket ds = new DatagramSocket();
							       // DatagramPacket dp;
							        DatagramPacket dp = new DatagramPacket(packetDataSetUp, packetDataSetUp.length, serverAddr,Integer.parseInt(getPortOfHost()));
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
				}//end ceck task cancel		   
				  
				
			
			
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//Toast.makeText(getApplicationContext(), "host:"+getHostIpToSend()+" port:"+getPortOfHost()+" kp:"+packetDataSetUp[3]+" ki:"+packetDataSetUp[4]+" kd:"+packetDataSetUp[5]+" YPR:"+packetDataSetUp[2]+" sum"+packetDataSetUp[6],Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
		
		
	}





	/**
	 * @return the useSetUp
	 */
	public boolean isUseSetUp() {
		return useSetUp;
	}




	/**
	 * @param useSetUp the useSetUp to set
	 */
	public void setUseSetUp(boolean useSetUp) {
		this.useSetUp = useSetUp;
	}
	
		
	
	
	
	

	
	
	
	 



	

}//end class set_pid
