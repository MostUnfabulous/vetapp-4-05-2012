package activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import vetapp.main.R;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import data.Auth;

/* Authors: Platinum Software
 * Version: 1.3 Alpha
 * 
 * Description: Connects to the phones calendar and displays all events for that day on screen,
 *   also allows the event to be clicked and takes to a google navigation with that location.
 */

public class CalendarActivity extends ListActivity 
{

    private static final String DEBUG_TAG = "CalendarActivity";
    ArrayList<String> eventTime = new ArrayList<String>();
    ArrayList<String> eventDate = new ArrayList<String>();
    ArrayList<String> eventTitle = new ArrayList<String>();
    ArrayList<String> eventDesc = new ArrayList<String>();
    ArrayList<String> eventLoc = new ArrayList<String>();
    ArrayList<String> TimDesc = new ArrayList<String>();
    ArrayList<String> eventUid = new ArrayList<String>();
    

    /*
     * @see android.app.Activity#onCreate(android.os.Bundle)
     * Description: Runs upon the intent opening, runs helper methods to get calendar and event data,
     * 		creates a viewlist to display events and allow clicking.
     */
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        try {
            //Log.i(DEBUG_TAG, "Starting Calendar Test");

           int iTestCalendarID = ListSelectedCalendars(Auth.getUser());
        
           if (iTestCalendarID != 0) 
           {

               	
            	CalendarArray(iTestCalendarID);
            
            	String []strArray = new String[TimDesc.size()];
            	TimDesc.toArray(strArray);            	
            	
            	setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,strArray ));  // creates and displays the list view
        		ListView list = getListView();
        		
        		list.setTextFilterEnabled(true);
        		  list.setOnItemClickListener(new OnItemClickListener() 
        		  {

					public void onItemClick(AdapterView<?> parent, View view,
                          int position, long id) {

                        Log.i(DEBUG_TAG,((TextView) view).getText().toString());
                        
                        char[] fullChar = ((TextView) view).getText().toString().toCharArray();
                        char[] uidChar = new char[3];
                        uidChar[0] = fullChar[0];
                        uidChar[1] = fullChar[1];
                        uidChar[2] = fullChar[2];
                        
                        String uid = new String (uidChar);
                        String [] eventUidarray = new String[eventUid.size()];
                        
                        eventUid.toArray(eventUidarray);
                        
                        //Log.i(DEBUG_TAG, uid);
                        int index = 0;
                        
                        for (int no = 0; no < eventUid.size(); no++) //finds index of the selected event
                        {
                        	if(eventUidarray[no].equals(uid))
                        	{
                        		index = no;
                        		//Log.i(DEBUG_TAG, "" +  index);
                        	}
                        }
                        
                        String loc = eventLoc.get(index); //Sets the location of the event
                        
                        if ( loc == "") 
                        {
                            Toast.makeText(getApplicationContext(), "No address found for this appointment",
                                    Toast.LENGTH_SHORT).show(); 
                        }
                        else // opens navigation with the location searched
                        {
                        	Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("google.navigation:q= " + loc));
                        	startActivity(intent);
                        }
                        
                      }
                    });
        		 
           	} else
           	{
                //Log.i(DEBUG_TAG, "No 'Test' calendar found.");
                Toast.makeText(getApplicationContext(), "No Calendar found",
                        Toast.LENGTH_SHORT).show(); 
            }

            //Log.i(DEBUG_TAG, "Ending Calendar Test");
          
        } catch(Exception e) 
        {
           // Log.e(DEBUG_TAG, "General failure", e);
        	Toast.makeText(getApplicationContext(), "General Faliure while loading.",
                    Toast.LENGTH_SHORT).show();
        }
        
  
    }


    /*
     * Description: Finds the associated calendar for that Username and gets its id
     * 		and returns it to the on create.
     */
    private int ListSelectedCalendars(String calNameFind) 
    {
        int result = 0;
        String[] projection = new String[] { "_id", "name" };
        String selection = "selected=1";
        String path = "calendars";

        Cursor managedCursor = getCalendarManagedCursor(projection, selection,path);

        if (managedCursor != null && managedCursor.moveToFirst()) 
        {

            //Log.i(DEBUG_TAG, "Listing Selected Calendars Only");

            int nameColumn = managedCursor.getColumnIndex("name");
            int idColumn = managedCursor.getColumnIndex("_id");

            do 
            {
                String calName = managedCursor.getString(nameColumn);
                String calId = managedCursor.getString(idColumn);
               // Log.i(DEBUG_TAG, "Found Calendar '" + calName + "' (ID=" 
               //  	+ calId + ")");
                if (calName != null && calName.contains(calNameFind)) 
                {
                    result = Integer.parseInt(calId);
                }
            } while (managedCursor.moveToNext());
        } else {
            //Log.i(DEBUG_TAG, "No Calendars");
        	Toast.makeText(getApplicationContext(), "No Calendars found",
                    Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    /**
     * Description: uses the generated managed cursor to get
     * 		all event details 
     * @param calID the id of the found calendar
     */
    public void CalendarArray(int calID) {
    	
    	eventTime.clear();
       	eventDate.clear();
       	eventTitle.clear();
       	eventDesc.clear();
       	eventLoc.clear();
       	TimDesc.clear();

       	int titleIndex = 0;
       	int timeIndex = 0;
       	int descIndex = 0;
       	int locationIndex = 0;
       	int latIndex = 0;
       	int longIndex = 0;
       	
        Cursor managedCursor = getCalendarManagedCursor(null, "calendar_id="
                + calID, "events");
        
        if (managedCursor != null && managedCursor.moveToFirst()) {
        	
           // Log.i(DEBUG_TAG, "Listing Calendar Event Details");
            int uid = 1;

            do {
            	String Suid = String.format("%03d", uid);
                //Log.i(DEBUG_TAG, "**Getting Calendar Event Details**");
                
              for (int i = 0; i < managedCursor.getColumnCount(); i++) 
            	
                {
                  if   (managedCursor.getColumnName(i).equals("title"))
                  {
                	  titleIndex = i;
                  }
                  
                  if   (managedCursor.getColumnName(i).equals("dtstart"))
                  {
                	  timeIndex = i;
                  }
                  
                  if   (managedCursor.getColumnName(i).equals("description"))
                  {
                	  descIndex = i;
                  }
                  
                  if   (managedCursor.getColumnName(i).equals("location"))
                  {
                	  locationIndex = i;
                  }
                  
                  if   (managedCursor.getColumnName(i).equals("latitude"))
                  {
                	  latIndex = i;
                  }
                  
                  if   (managedCursor.getColumnName(i).equals("long"))
                  {
                	  longIndex = i;
                  }
                  
                  //Log.i(DEBUG_TAG,
                  //  		managedCursor.getColumnName(i) + "="
                  //         + managedCursor.getString(i) + " " + i);
                } 
              
              String EDate = (milliToDate(managedCursor.getLong(timeIndex)));
              String ADate = (milliToDate(System.currentTimeMillis()));
              
                if (EDate.equals(ADate)) //Checks all events are on the current day and adds their details to the ArrayLists
                {
                	eventTime.add(milliToTime(managedCursor.getLong(timeIndex)));
                	eventDate.add(milliToDate(managedCursor.getLong(timeIndex)));
                	eventTitle.add(managedCursor.getString(titleIndex));
                 	eventDesc.add(managedCursor.getString(descIndex));
                 	eventUid.add(Suid);
                 	
                 	if (locationIndex == 0)
                 	{
                 		eventLoc.add(managedCursor.getString(latIndex) + ", " + managedCursor.getString(longIndex));
                 	}
                 	else if (longIndex == 0 && latIndex == 0)
                 	{
                 		eventLoc.add(managedCursor.getString(locationIndex));
                 	}
                    
                	TimDesc.add(Suid + " " + (milliToDate(managedCursor.getLong(timeIndex))) + "  " +milliToTime(managedCursor.getLong(timeIndex)) +  "\n" + managedCursor.getString(titleIndex));
                	uid ++;
                }	
                   	
               // Log.i(DEBUG_TAG, "//////title index =  " + titleIndex);
               // Log.i(DEBUG_TAG, "**Stopped getting Calendar Event Details**");
                
            } while (managedCursor.moveToNext());
            
            
            
        } else 
        {
           // Log.i(DEBUG_TAG, "No Calendars");
        	Toast.makeText(getApplicationContext(), "No Calendars found at given id",
                    Toast.LENGTH_SHORT).show();
        }
        
    }


    /**
     * Description: gets the managed cursor for the calendars
     * 		also their id and name
     * @param projection an array containing the calendar id and name
     * @param selection initiates the WHERE clause in the SQL statement
     * @param path part of the Uri path needed to get the calendar
     * @return returns the cursor
     */
    private Cursor getCalendarManagedCursor(String[] projection,
            String selection, String path) {
        Uri calendars = Uri.parse("content://calendar/" + path);

        Cursor managedCursor = null;
        try {
            managedCursor = managedQuery(calendars, projection, selection,
                    null, null);
        } catch (IllegalArgumentException e) {
            //Log.w(DEBUG_TAG, "Failed to get provider at ["
             //       + calendars.toString() + "]");
        }

        if (managedCursor == null) {
            // try again
            calendars = Uri.parse("content://com.android.calendar/" + path);
            try {
                managedCursor = managedQuery(calendars, projection, selection,
                        null, null);
            } catch (IllegalArgumentException e) {
              //  Log.w(DEBUG_TAG, "Failed to get provider at ["
               //         + calendars.toString() + "]");
            }
        }
        return managedCursor;
    }

   
    /**
     * Description: takes the epoch time and converts it to a
     *  	date format
     * @param milli the epoch time to be converted
     * @return returns the formatted time
     */
    private String milliToDate(long milli)
    {
    	
    	SimpleDateFormat timeformatter = new SimpleDateFormat("dd/MM/yyyy");
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(milli);
    	
    	return timeformatter.format(calendar.getTime()).toString();
    		
    }
    
    /**
     * Description: takes the epoch time and converts it to a
     *  	time format
     * @param milli the epoch time to be converted
     * @return returns the formatted time
     */
    private String milliToTime(long milli)
    {
    	
    	SimpleDateFormat timeformatter = new SimpleDateFormat("hh:mm");
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(milli);
    	
    	return timeformatter.format(calendar.getTime()).toString();
    	
    }
}

