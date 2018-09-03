import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
*This is the portion of the Program that executes upon startup of the machine
*@author Dan Martineau
*/

public class Shutdown
{
	private static final String REF_FILE = "DO_NOT_MODIFY_database.txt";		//Name of the database file where the shutdown times are stored

	public static void main(String[] args)
	{
		//find the current day of the week
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK); 
		
		//read the shutdown times from the database
		String[] times = readTimes();
		//get today's shutdown time
		String todayTime = times[day - 1];
		
		while(true)
		{
			//shutdown computer if time is reached
			compareTime(todayTime);
			
			//re-read the shutdown times from the database
			times = readTimes();
			//get today's shutdown time
			todayTime = times[day - 1];
		}
	}

	/**
	*Method reads the times from the database file and puts them into an array
	*@return the array of times
	*/
	private static String[] readTimes()
	{
		String rawTimes = FileCMD.readFile(REF_FILE);
		String[] times = new String[7];
		byte beg = 0;
		byte end = 5;

		for(int i = 0; i < 7; i++)
		{
			times[i] = rawTimes.substring(beg, end);
			beg += 5;
			end += 5;
		}

		return times;
	}
	
	/**
	*Method compares the shutdown time with actual time and
	*shuts computer down if the two match.
	*Method will detect if the time in the database is changed
	*/
	private static void compareTime(String time)
	{
		//get current time
		Calendar cal = Calendar.getInstance();
		//format time in 00:00 format (military time)
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		//shut the computer down if the times match
		if(time.equals(sdf.format(cal.getTime())))
		{
			try{Runtime.getRuntime().exec("shutdown -s -f");}
			//this is only for testing as console will not normally be running
			catch(Exception e){Prin.tln("Failed to shut down...");}
		}
		
		//pause every two seconds to avoid generating excessive overhead
		Prin.pause(2000);
	}
}