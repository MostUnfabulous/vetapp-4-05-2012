package data;

/* Authors: Platinum Software
 * Version: 1.3 Alpha
 * 
 * Description: Controls the login passwords and usernames
 * 		all the passwords and usernames are case sensitive
 */
public class Auth 
{
		
	 
	static String user;
	static String passWrd;
	static String[] data = //Add Password to this array
		  {"001","002","003"}; 
	static String[] userNm = //Add usernames to this array
		  {"Test 001","Test 002","Test 003"}; 
		
		/**
		 *  Description: checks that the username and password 
		 *  		match up if not returns fail
		 * @param pass the password entered by user
		 * @param userName the username entered by user
		 * @return returns the user generated from username and password
		 */
		public static String passCheck(String pass, String userName)
		{
			for (int i = 0; i < data.length; i++)
			{
				if (pass.equals(data[i]) && userName.equals(userNm[i]))
				{
					user = userNm[i].toString();
					break;
				}
				else
				{
					user ="Fail";
				}
			}
			
			return user;
		}

		/**
		 * Description: get the user of the current session
		 * @return the user of the current session
		 */
		public static String getUser()
		{
			return user;
		}

}
