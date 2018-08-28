public class SetShutdownTimes
{
	final static int COL = 80;													//number of coloumns in terminal emulator
	final static String DATABASE = "DO_NOT_MODIFY_database.txt";				//name of database file
	final static String DEFAULT_DATA = "00:0000:0000:0000:0000:0000:0000:00";	//default time string in file

	public static void main(String[] args)
	{
		//array of the current shutdown times
		String[] currTimes = new String[7];

		displayIntro();
		currTimes = displayShutTimes();

		//whether or not the user would like to alter the shutdown times
		boolean alter = Prin.yesOrNo("Would you like to edit these times?");

		//change the shutdown times if the user selected yes
		if(alter)
		{
			setShutTimes(currTimes);
			Prin.clearAll();
			displayShutTimes();
			Prin.tln("\n\nAll changes have been saved!");
		}
		else
			System.exit(0);
	}

	/**
	*Allows User to select shutdown times
	*@param the current shutdown times
	*/
	private static void setShutTimes(String[] currTimes)
	{
		//array of day names
		String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		//array of user times
		String[] times = new String[7];

		//holder for user selection (bool)
		boolean option = false;
		//temp holder for new time entered
		String newTime = "";
		//whether or not an enterd time is valid
		boolean valTime = true;

		for(int i = 0; i < 7; i++)
		{
			option = Prin.yesOrNo("Alter " + days[i] + "'s shutdown time?");

			if(option)
			{
				Prin.ln();

				Prin.t("Enter new time: ");
				newTime = Prin.getString();

				valTime = verifyTime(newTime);

				while(!valTime)
				{
					Prin.t("Time entered is invalid! Enter new time. e.g. 03:00: ");
					newTime = Prin.getString();

					valTime = verifyTime(newTime);
				}

				times[i] = newTime;
			}
			else
			{
					times[i] = currTimes[i];
			}
		}

		//write new times to file
		String tempTimes = "";
		for(int i = 0; i < 7; i++)
			tempTimes += times[i];
		FileCMD.writeFile(tempTimes, DATABASE);
	}

	private static boolean verifyTime(String entry)
	{
		boolean time = true;

		//make sure the length of time string is correct
		if(entry.length() != 5)
			time = false;
		//make sure there is a colon seperating minutes and hours
		else if(entry.charAt(2) != ':')
			time = false;
		//make sure there are actual numbers where there should be
		else
		{
			int[] positions = {0, 1, 3, 4};
			for(int i = 0; i < 4; i++)
			{
				if(!isDigit("" + entry.charAt( positions[i])))
				{
					Prin.tln("The substring: " + entry.charAt( positions[i]));
					time = false;
					break;
				}
			}
		}

		return time;
	}

	private static boolean isDigit(String input)
	{
		boolean digit = true;
		//default this to ten because a valid digit must be less--this will fail the test if Exception handling does not provide number
		int entry = 10;

		try{entry = Integer.parseInt(input);}
		catch(Exception e){digit = false;}

		if(entry < 0 || entry > 9)
			digit = false;

		return digit;
	}


	//----------------------------------------------------------------------------------------------------Display Methods--------------------------------------------------------------------------------------------------------------------

	/**
	*Displays title and description
	*/
	private static void displayIntro()
	{
		Prin.clearAll();
		Prin.title("Automatic Shutdown for Windows",  "*", COL);
		Prin.ln(2);

		Prin.tln("You will shortly be prompted to enter shutdown times for the various days of the\nweek.\n");
		Prin.tln("Keep in mind that you must use military time, e.g. 1:00 PM is written as 13:00.\n");
		Prin.tln("This program honors the shutdown time for whichever day of the week it was \nstarted on. Should you set the time as 13:00 on Monday, for example, and you \nstart your computer in the afternoon that day, this program will shut the \nsystem down on 1 AM Tuesday morning.");
		Prin.ln(2);
	}

	/**
	*Displays & Returns the current shutdown times
	*@return array of current shutdown times
	*/
	private static String[] displayShutTimes()
	{
		String[] currTimes = {DEFAULT_DATA.substring(0,5), DEFAULT_DATA.substring(5,10), DEFAULT_DATA.substring(10,15), DEFAULT_DATA.substring(15,20), DEFAULT_DATA.substring(20,25), DEFAULT_DATA.substring(25,30), DEFAULT_DATA.substring(30)};
		byte beg = 0;
		byte end = 5;
		boolean reset = false;
		Prin.title("Current Shutdown Times", " ", COL);
		Prin.ln(2);

		//get times from the database file
		String times = FileCMD.readFile(DATABASE);

		//put times into array
		try
		{
			for(int i = 0; i < 7; i++)
			{
				currTimes[i] = times.substring(beg, end);
				beg += 5;
				end += 5;
			}
		}
		//exception indicates that file is not formatted properly or missing
		catch(StringIndexOutOfBoundsException e)
		{
			//ask user to reset database if corrupted
			reset = Prin.yesOrNo("Databse file is corrupted or deleted. Would you like to reset it?");
			
			if(reset)
				FileCMD.writeFile(DEFAULT_DATA, DATABASE);
			else
				System.exit(0);
		}

		//print the actual times out
		Prin.tln("Sunday: \t" + currTimes[0] + "\nMonday: \t" + currTimes[1] + "\nTuesday: \t" + currTimes[2] + "\nWednesday: \t" + currTimes[3] + "\nThursday: \t" + currTimes[4] + "\nFriday: \t" + currTimes[5] + "\nSaturday: \t" + currTimes[6]);

		return currTimes;
	}
}