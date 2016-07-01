import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.io.File;


public class MooseCalendarRunner {

	// This path simply holds the name of the file that holds the
	// insurance event records. If we want to call it something else, we
	// just change it here.
	public static final Path ieFileRPath = Paths.get("ieRecords.ser");
	
	// This method returns the path to the only folder we use. A method as
	// opposed to a field is necessary due to the user's home directory
	// being variable.
	public static Path getAppDataFPath()
	{
		Path pathToHome = Paths.get(System.getProperty("user.home"));
		String pathString = "AppData\\Local\\TwoGuysInAShed\\MooseCalendar";
		Path fullAppDataPath = pathToHome.resolve(pathString);
		
		return fullAppDataPath;
	}
	
	// This method returns the actually useful path to the file. Under
	// normal circumstances, this should be the only method we actually use
	// for file operations.
	public static Path getIEFileFPath()
	{
		Path AppDataPath = getAppDataFPath();
		Path fullIEFilePath = AppDataPath.resolve(ieFileRPath);
		
		return fullIEFilePath;
	}
	
	// This returns the directory where our program is installed.
	public static Path getProgramFilesPath()
	{
		String pathString = System.getenv("%programfiles% (x86)");
		System.out.println(pathString);
		
		return Paths.get(pathString);
	}

	private static void setupAppData()
	{
		Path appDataFPath = getAppDataFPath();
		Path ieFileFPath = getIEFileFPath();

		// Let's check that our folders are what they should be.
		if (Files.notExists(appDataFPath))
		{
			try
			{
				Files.createDirectories(appDataFPath);
			}
			catch (IOException ioex)
			{
				ioex.printStackTrace();
			}
		}
		// And let's create our data file if it doesn't exist yet.
		if (Files.notExists(ieFileFPath))
		{
			try
			{
				Files.createFile(ieFileFPath);
			}
			catch (IOException ioex)
			{
				ioex.printStackTrace();
			}
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
	
	private static void viewBillsDueSoon(ListOnDisk<InsuranceEvent> ieList, Tui tui)
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
	
	public static void main(String[] args)
	{
		// In case the necessary files and folders don't exist.
		setupAppData();

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
		
		while (choice != 7)
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
				viewBillsDueSoon(ieList, tui);
				break;
			case 7 :
			default :
			}
		}

	}
}
