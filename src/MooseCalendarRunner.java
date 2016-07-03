import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.attribute.FileTime;


public class MooseCalendarRunner {

	// This path simply holds the name of the file that holds the
	// insurance event records. If we want to call it something else, we
	// just change it here.
	public static final String userDirString;
	public static final String appDataDirString;
	public static final Path ieFileRelPath = Paths.get("ieRecords.ser");
	public static final Path ieFileAbsPath;
	private static final Path appDataDirAbsPath;
	// private static final Path programFilesDirAbsPath;
	private static final Path backupDirAbsPath;
	private static final Path[] ieBackupAbsPaths;
	static
	{
		userDirString = System.getProperty("user.home");
		String appDataPortion = "AppData\\Local\\";
		String ourProgramPortion = "TwoGuysInAShed\\MooseCalendar";
		appDataDirString = appDataPortion + ourProgramPortion;

		appDataDirAbsPath = Paths.get(userDirString).resolve(appDataDirString);
		backupDirAbsPath = appDataDirAbsPath.resolve("Backups");
		ieFileAbsPath = appDataDirAbsPath.resolve(ieFileRelPath);
		ieBackupAbsPaths = new Path[4];
		ieBackupAbsPaths[0] = backupDirAbsPath.resolve("Backup1.ser");
		ieBackupAbsPaths[1] = backupDirAbsPath.resolve("Backup2.ser");
		ieBackupAbsPaths[2] = backupDirAbsPath.resolve("Backup3.ser");
		ieBackupAbsPaths[3] = backupDirAbsPath.resolve("Backup4.ser");
	}
	
	// This method returns the actually useful path to the file. Under
	// normal circumstances, this should be the only method we actually use
	// for file operations.
	public static Path getIEFileFPath()
	{
		Path userDirPath = Paths.get(userDirString); 

		return userDirPath.resolve(appDataDirString).resolve(ieFileRelPath);
	}
	
	public static Path getAppDataDirAbsPath ()
	{
		return Paths.get(userDirString).resolve(appDataDirString);
	}
	
	private static void setup()
	{
		// Creating the backup directory creates all the directories above
		// it, so we can just do this in one go.
		if (Files.notExists(backupDirAbsPath))
			try
			{
				Files.createDirectories(backupDirAbsPath);
			}
			catch (IOException ioex)
			{
				ioex.printStackTrace();
			}

		// Let's create our data file if it doesn't exist yet.
		if (Files.notExists(ieFileAbsPath))
			try
			{
				Files.createFile(ieFileAbsPath);
			}
			catch (IOException ioex)
			{
				ioex.printStackTrace();
			}
		
		// And let's create the backup files.
		for (Path currentBackupFile : ieBackupAbsPaths)
			if (Files.notExists(currentBackupFile))
				try
				{
					Files.createFile(currentBackupFile);
				}
				catch (IOException ioex)
				{
					ioex.printStackTrace();
				}
	}

	private static void addInsuranceEvent (ListOnDisk<InsuranceEvent> ieList, Tui tui)
	{
		ieList.add(tui.addInsuranceEventUI());
	}
	
	private static void viewInsuranceEvents(ListOnDisk<InsuranceEvent> ieList, Tui tui)
	{
		tui.displayInsuranceEvents(ieList);
	}
	
	private static void removeInsuranceEvent(ListOnDisk<InsuranceEvent> ieList, Tui tui)
	{
		int index = -1;
		
		index = tui.getIndex(ieList, "remove");
		try
		{
			ieList.remove(index);
		}
		catch (IndexOutOfBoundsException ex)
		{
			tui.noActionTaken();
		}
	}

	private static void recordPayment(ListOnDisk<InsuranceEvent> ieList, Tui tui)
	{
		int index = -1;
		
		index = tui.getIndex(ieList, "record as paid");
		try
		{
			InsuranceEvent temp = ieList.get(index);
			temp.pay();
			ieList.set(index, temp);
		}
		catch (IndexOutOfBoundsException ex)
		{
			tui.noActionTaken();
		}
	}
	
	private static void resortList(ListOnDisk<InsuranceEvent> ieList, Tui tui)
	{

	}
	
	private static ArrayList<Integer> eventsDueSoon (ListOnDisk<InsuranceEvent> ieList)
	{
		ArrayList<Integer> indices = new ArrayList<Integer>();

		for (int i = 0; i < ieList.size(); i++)
			if (ieList.get(i).getUrgency().compareTo(InsuranceEvent.Urgency.NOTDUESOON) > 0)
				indices.add(i);

		return indices;
	}

	private static ArrayList<Integer> eventsShouldRemind (ListOnDisk<InsuranceEvent> ieList)
	{
		ArrayList<Integer> indices = new ArrayList<Integer>();

		for (int i = 0; i < ieList.size(); i++)
			if (ieList.get(i).shouldRemind())
				indices.add(i);

		return indices;
	}
	
	// This returns the events that the user was reminded of. This data is
	// otherwise lost after running this method.
	private static ArrayList<Integer> remind(ListOnDisk<InsuranceEvent> ieList, Tui tui)
	{
		ArrayList<Integer> indices = eventsShouldRemind(ieList);
		InsuranceEvent currentEvent;

		tui.remind(ieList, indices);
		
		for (int i = 0; i < indices.size(); i++)
		{
			currentEvent = ieList.get(indices.get(i));
			currentEvent.reminderDisplayed();
			ieList.set(indices.get(i), currentEvent);
		}
		
		return indices;
	}
	
	public static void backup(ListOnDisk<InsuranceEvent> ieList)
	{
		// This will point to the oldest backup file.
		int indexToOldest = 0;

		// This is an array of the last modification times for the backups.
		FileTime[] modTimes = new FileTime[4];

		// This keeps track of the oldest time yet found as the array is
		// traversed.
		FileTime oldestTime;
		
		// Let's populate the modification times array first. If we find an
		// exception, we get out without modifying anything.
		try
		{
			for (int i = 0; i < 4; i++)
				modTimes[i] = Files.getLastModifiedTime(ieBackupAbsPaths[i]);
		}
		catch (IOException ioex)
		{
			ioex.printStackTrace();
			return;
		}

		// We put in a default in case everything comes out equal.
		oldestTime  = modTimes[0];
		indexToOldest = 0; 
		
		// This uses knock-out elimination to find the oldest time.
		for (int i = 1; i < 4; i++)
		{
			if (oldestTime.compareTo(modTimes[i]) > 0)
			{
				oldestTime = modTimes[i];
				indexToOldest = i;
			}
		}
		Path oldestFile = ieBackupAbsPaths[indexToOldest];
		
		// Here we use the handy toNewFile method to do all the copying
		// for us.
		ieList.toNewFile(oldestFile);
	}
	
	public static void main(String[] args)
	{
		// In case the necessary files and folders don't exist.
		setup();

		Path ieFileFPath = getIEFileFPath();
		ListOnDisk<InsuranceEvent> ieList;
		ieList = new ListOnDisk<InsuranceEvent>(ieFileFPath);
		Tui tui = new Tui();
		boolean remindExecuted = false;
		int choice = 0;
		ArrayList<Integer> eventsRemindedOf;
		
		if (args.length > 0)
		{
			if (args[0].equals("r") || args[0].equals("R"))
			{
				eventsRemindedOf = remind(ieList, tui);
				remindExecuted = true;
			}
		}
		
		while (choice != 6)
		{
			choice = tui.taskChoice();
			switch (choice)
			{
			case 1 :
				addInsuranceEvent(ieList, tui);
				break;
			case 3 :
				viewInsuranceEvents(ieList, tui);
				break;
			case 4 :
				removeInsuranceEvent(ieList, tui);
				break;
			case 5 :
				recordPayment(ieList, tui);
				break;
			case 6 :
				resortList(ieList, tui);
				break;
			case 7 :
			default :
			}
		}

	}
}
