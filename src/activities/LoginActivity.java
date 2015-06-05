package activities;

/* Authors: Platinum Software
 * Version: 1.3 Alpha
 * 
 * Description: Allows users to login and access their 
 * 		own area of the application
 */

import data.Auth;
import vetapp.main.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity 
{
	private EditText text;
	private EditText text1;
	
	
	
	
    /**
     * Description: When the application is started show the login screen 
     * 		and assigns the edit text boxes to variables
     */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		text = (EditText) findViewById(R.id.editText1);
		text1 = (EditText) findViewById(R.id.editText2);
       
    }
    
    /**
     * Description: When the login button is pressed it checks the password
     * 		and user name and logs the user into their appropriate area.
     * 		also checks for failed attempts and tells user if this is the case.	
     */
    public void LoginClickHandler(View view) 
    {
    	
    	if (text.getText().length() == 0) 
    	{
			Toast.makeText(this, "Please Enter Passcode",
				Toast.LENGTH_SHORT).show();
			return;
    	}
    	
    	String user= Auth.passCheck(text.getText().toString(), text1.getText().toString());
    	
    	if  (user.equals("Fail"))
    	{		
    		Toast.makeText(this, R.string.Incopass,
    			Toast.LENGTH_LONG).show();
    	}
    	
    	
    	
    	else
        	
    	{		
    		Toast.makeText(this,"Welcome " + user,
				Toast.LENGTH_LONG).show();
    		
    		Intent intent = new Intent(this, MenuActivity.class);
    		startActivity(intent);
    		finish();
    	
    	}
    }
}