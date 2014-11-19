package com.example.controll;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText phoneNumber;
	private EditText smsBody;
	private Button smsManagerBtn;
	private Button smsSendToBtn;
	private Button smsViewBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		smsBody = (EditText) findViewById(R.id.smsBody);
		smsManagerBtn = (Button) findViewById(R.id.smsManager);
		smsSendToBtn = (Button) findViewById(R.id.smsSIntent);
		smsViewBtn = (Button) findViewById(R.id.smsVIntent);

		smsManagerBtn.setOnClickListener(new OnClickListener() {
			 public void onClick(View view) {
				 sendSmsByManager();
			 }
		});
		
		smsSendToBtn.setOnClickListener(new OnClickListener() {
			 public void onClick(View view) {
				 sendSmsBySIntent();
			 }
		});
		
		smsViewBtn.setOnClickListener(new OnClickListener() {
			 public void onClick(View view) {
				 sendSmsByVIntent();
			 }
		});
		
        //make button
        Button action = (Button) findViewById(R.id.button1);
        action.setOnClickListener(
        		new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						//send SMS
					}
        		}
        );
        
        //make phone number array
    	makeNumbersArray(); 

	}
	
	public void sendSmsByManager() {
		try {
			// Get the default instance of the SmsManager
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNumber.getText().toString(), 
					null,  
					smsBody.getText().toString(), 
					null, 
					null);
			Toast.makeText(getApplicationContext(), "Your sms has successfully sent!",
					Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(),"Your sms has failed...",
					Toast.LENGTH_LONG).show();
			ex.printStackTrace();
		}
	}
	
	public void sendSmsBySIntent() {
		// add the phone number in the data
		Uri uri = Uri.parse("smsto:" + phoneNumber.getText().toString());
		
		Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);
		// add the message at the sms_body extra field
		smsSIntent.putExtra("sms_body", smsBody.getText().toString());
		try{
			startActivity(smsSIntent);
		} catch (Exception ex) {
			Toast.makeText(MainActivity.this, "Your sms has failed...",
					Toast.LENGTH_LONG).show();
			ex.printStackTrace();
		}
	}
	
	public void sendSmsByVIntent() {
		
		Intent smsVIntent = new Intent(Intent.ACTION_VIEW);
		// prompts only sms-mms clients
		smsVIntent.setType("vnd.android-dir/mms-sms");
		
		// extra fields for number and message respectively
		smsVIntent.putExtra("address", phoneNumber.getText().toString());
		smsVIntent.putExtra("sms_body", smsBody.getText().toString());
		try{
			startActivity(smsVIntent);
		} catch (Exception ex) {
			Toast.makeText(MainActivity.this, "Your sms has failed...",
					Toast.LENGTH_LONG).show();
			ex.printStackTrace();
		}

	}
    //set timer = false
    boolean timerTruth = false;
    
    //timer stuff
    //make new timer and timerTask
    Timer timer;
    TimerTask timert = new TimerTask() 
    {
         @Override
         public void run() 
         {
               timerTruth = false;
         }
    };
    
    //make sendText method
    public static void sendText() {
    	
    };
    
    @Override
    public void onPause()
    {
       //send SMS
    }
    
    //make method that collects phone numbers in array
    public void makeNumbersArray() { 
    	//make cursor that navigates contact data
    	ContentResolver cr = getContentResolver(); 
    	Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null); 	
    	
    	//initialize phone
    	String phone = null; 

    	//initialize array, initialize index
    	String[] phoneNumbers = new String[cur.getCount()];
    	int index = 0;

    	//traverse contact data	
    	if (cur.getCount() > 0) { 
    		//check if more contacts are left
    		while(cur.moveToNext()) { 
    			//assign phone number to variable phone
    			String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
    			if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) { 
    				Cursor pCur = cr.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null); 
    				while(pCur.moveToNext()) { 
    					phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
    				} 
    				pCur.close();
    			} 

    			//put phone number in array
    			phoneNumbers[index] = phone;
    			index++;
    		}
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
    
}
}