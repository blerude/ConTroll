package com.example.controll;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button smsManagerBtn;
	private EditText timePeriod;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		smsManagerBtn.setOnClickListener(new OnClickListener() {
			 public void onClick(View view) {
				 timerTruth = true;
				 timePeriod = (EditText) findViewById(R.id.timer);
				 int time = Integer.parseInt(timePeriod.getText().toString());
				 timer.schedule(timerT, time);
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

	}
	
	//set debug
	boolean debug = true;
	
	//make random object
	Random randomGen = new Random();
	
	//make array of embarrassing text messages
	String[] messages = {
		"I hope you know that I’ve always loved you.",
		"When are you gonna stop by and see my stuffed bird collection?",
		"Your skin looks smooth.",
		"You free to go get waxed later?",
		"What’s crackin’ home slice?",
		"Do you know where I could get ten pounds of bok choy without anyone asking questions?",
		"I have to ask you a serious question about wombats.",
		"Just bought a one-way ticket to Bangkok, you in?",
		"My goldfish is drowning, please advise",
		"SOS – we’re out of toilet paper",
		"It's been too long...wanna get lunch sometime this week?",
		"Happy birthday!!!",
		"Wow I just saw your 8 missed calls from the last half hour, what's so urgent?",
		"Just got your voicemail from last night oh my god so funny!",
		"I had a great time last night, thanks for dinner! <3",
		"Haha I was just Facebook stalking you and saw your new haircut. You look great! I'd love to take you to dinner this weekend, let me know what you think :)",
		"Your new profile picture is the cutest thing I've ever seen ;) hmuuuuuuu",
		"Hey I saw your post about your car...I'm really interested in a new Prius. How much were you thinking of selling it for?",
		"I’m starting a Stop Old People movement, you in?",
		" Rumor has it you’re into me",
		"Thinking about getting a belly ring, whatcha think?",
		"Did you know leprechauns AREN’T real?!",
		"Text me back for a truth is, I’m kinda desperate",
		"You’re the light to my delight, am I yours?",
		"Hey can we meet up and you teach me how to dougie?"
	};
	
	public void sendSmsByManager() {
		
		try {
			// Get the default instance of the SmsManager
			SmsManager smsManager = SmsManager.getDefault();
			
			//get random phone number
			String[] phoneNumbers = makeNumbersArray();
			
			String randNumber = null;
			while (randNumber == null) {
				randNumber = phoneNumbers[randomGen.nextInt(phoneNumbers.length)];
			}
			//get random message
			String randMessage = messages[randomGen.nextInt(messages.length)];
			
			if (!debug) {
				smsManager.sendTextMessage(randNumber, 
 					null,  
					randMessage, 
					null, 
					null);
 				Toast.makeText(getApplicationContext(), "Your sms has successfully sent!",
					Toast.LENGTH_LONG).show();
			} else {
				smsManager.sendTextMessage("2012130686", 
	 					null,  
						randMessage + randNumber, 
						null, 
						null);
	 				Toast.makeText(getApplicationContext(), "Your sms has successfully sent!",
						Toast.LENGTH_LONG).show();
			}
			
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(),"Your sms has failed...",
					Toast.LENGTH_LONG).show();
			ex.printStackTrace();
		}
	}
	
    //set timer = false
    boolean timerTruth = false;
    
    //timer stuff
    //make new timer and timerTask
    Timer timer;
    TimerTask timerT = new TimerTask() 
    {
         @Override
         public void run() 
         {
               timerTruth = false;
         }
    };
    
    @Override
    public void onPause()
    {
       if (timerTruth) {
    	   sendSmsByManager();
       }
    }
    
    //make method that collects phone numbers in array
    public String[] makeNumbersArray() { 
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
    	
    	return phoneNumbers;
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
