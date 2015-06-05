package activities;

/* Authors: Platinum Software
 * Version: 1.3 Alpha
 * 
 * Description: displays a menu screen and creates 
 * 		event listeners for all the buttons.
 */

import data.Auth;
import vetapp.main.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends Activity 
{

	
    /**
     * Description: Upon creation sets the layout 
     * 		to menu and sets the welcome
     */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		SetWelcome();
	}

    /**
     * Description: Generates a message at the top of the screen
     * 		using the Username to personalize it
     */
    public void SetWelcome()
    {
    	TextView welc =  (TextView) findViewById(R.id.textView1);
    	welc.setText("Welcome "+ Auth.getUser());
    }
    
    /**
     * Description: Starts the calendar activity
     */
    public void CalenderClickHandler(View view) 
    {
    	Intent intent = new Intent(this, CalendarActivity.class);
    	startActivity(intent);
    }

    /**
     * Description: Opens google maps and displays on the screen
     */
    public void DirectionClickHandler(View view) 
	{
    	Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
		Uri.parse("geo:0,0?q="));
		startActivity(intent);
	}

    /**
     * Description: Starts the logout activity and closes the app
     */
    public void LogoutClickHandler(View view) 
    {
    	Intent intent = new Intent(this, LoginActivity.class);
    	startActivity(intent);
    	finish();
    }
    
    /**
     * Description: creates and displays an alert showing
     * 		the app details
     */
    public void AboutClickHandler(View view)
    {
    	new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.app_name)
    			.setMessage("Vet App\n\nVersion: 1.3 Alpha\n\nPlatinum Software").create().show();	
    }
}