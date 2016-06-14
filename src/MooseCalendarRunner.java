import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.nio.file.StandardOpenOption.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.text.ParsePosition;


public class MooseCalendarRunner {

	// This is the only file we need. It holds the serialized objects.
	public static final Path insuranceEventPath = Paths.get("InsuranceEventList.ser");
	
	// This method loads the contents of the file into memory.
	public static ArrayList<InsuranceEvent> loadEvents ()
	{
		ArrayList<InsuranceEvent> insuranceEventList = new ArrayList<InsuranceEvent>();
		// Let's try not to open non-existing files.
		if (Files.exists(insuranceEventPath))
		{
			ObjectInputStream in = null;
			try
			{
				// Quite a bit of indirection ahead. First,
				// Files.readAllBytes takes the file and outputs a raw
				// byte array. That's just an array, so to actually call
				// input stream methods, it gets turned into a
				// ByteArayInputStream, and we use ObjectInputStream to
				// actually deserialize that data.
				byte[] allBytes = Files.readAllBytes(insuranceEventPath);
				ByteArrayInputStream byteis = new ByteArrayInputStream(allBytes);
				in = new ObjectInputStream(byteis);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}

			// Reading the objects from the byte array.
			// We will loop through until we get an EOF exception.
			boolean notEOF = true;
			while(notEOF)
			{
				try
				{
					insuranceEventList.add((InsuranceEvent) in.readObject());
				}
				catch(EOFException eofex)
				{
					notEOF = false;
				}
				catch (ClassNotFoundException | IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
		// Note that this can return an empty ArrayList.
		return insuranceEventList;
	}

	// This method serializes and saves the insuranceEvent array.
	// Note that this method completely overwrites the file.
	public static void saveEvents (ArrayList<InsuranceEvent> insuranceEventList)
	{
		ByteArrayOutputStream byteos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try
		{
			out = new ObjectOutputStream(byteos);
			for (InsuranceEvent ievent : insuranceEventList)
			{
				out.writeObject(ievent);
			}
			Files.write(insuranceEventPath, byteos.toByteArray());
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public static int taskChoice ()
	{
		Scanner s = new Scanner(System.in);
		int userChoice = 0;
		
		System.out.println("Type the number of the option you want and press " +
				"Enter.");
		System.out.println("1. Add reminders");
		System.out.println("2. Edit reminders");
		System.out.println("3. View Reminders");
		System.out.println("4. Delete Reminders");
		System.out.println("5. Make a payment");
		System.out.println("6. Exit");
		userChoice = s.nextInt();
		while(!(userChoice > 0 && userChoice < 7))
		{
			try {
				System.out.println("Please enter one of the choices above.");
				userChoice = s.nextInt();
			} catch (InputMismatchException ex)
			{
				s.nextLine();
				System.out.println("Please type only digits.");
			}
		}
		
		return userChoice;
	}
	
	public static void addInsuranceEvent ()
	{
		// First, ask the user for the insurance event information, store it in
		// memory, then make sure there are no duplicates in the file. If there
		// aren't any, then the event should be appended.
		InsuranceEvent temp = new InsuranceEvent();
		Scanner s = new Scanner(System.in);
		String dateString;
		String tempString;
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		System.out.println("Please enter the following:");
		System.out.println("Address of the insured property:");
		System.out.print("Number, street, & apt./suite/etc. (no commas): ");
		tempString = s.nextLine();
		System.out.print("City: ");
		tempString = tempString + " " + s.nextLine();
		System.out.print("State (two letters only): ");
		tempString = tempString + " " + s.nextLine().toUpperCase();
		System.out.print("Zip code: ");
		tempString = tempString + " " + s.nextLine();
		temp.setEventName(tempString);
		System.out.println("Name of the insurance carrier:");
		temp.setInsuranceCarrier(s.nextLine());
		// For the next two variables, we use nextLine so that we gobble up
		// the newline character. The resulting string then gets parsed and
		// the returned double gets set into temp.
		System.out.println("Premium:");
		temp.setPremium(Double.parseDouble(s.nextLine()));
		System.out.println("Level of coverage:");
		temp.setLevelOfCoverage(Double.parseDouble(s.nextLine())); 
		System.out.println("Date payment is due (MM/DD/YY format):");
		dateString = s.nextLine();
		temp.setEventDate(df.parse(dateString, new ParsePosition(0)));
		
		// Now that we have all the information recorded for the event, we
		// save it by loading the list from disk into memory, adding the
		// event, then writing the new array to disk.
		ArrayList<InsuranceEvent> insuranceEventList = loadEvents();
		insuranceEventList.add(temp);
		saveEvents(insuranceEventList);
	}
	
	public static void viewInsuranceEvents()
	{
		ArrayList<InsuranceEvent> insuranceEventList = loadEvents();
		InsuranceEvent currentEvent = null;
		
		for (int i = 0; i < insuranceEventList.size(); i++)
		{
			currentEvent = insuranceEventList.get(i);
			// Note that the bill number is one more than the index.
			System.out.println("Bill " + (i + 1) + " :");
			System.out.println(currentEvent.toString());
		}
		if (insuranceEventList.size() == 0)
		{
			System.out.println("No records to display.");
		}
	}
	
	public static void removeInsuranceEvent()
	{
		Scanner scanner = new Scanner(System.in);
		int index = -1;
		
		viewInsuranceEvents();
		
		System.out.println("Please enter the number of the record you would like to erase, or enter 0 to exit.");
		String line = scanner.nextLine();
		index = Integer.parseInt(line) - 1;
		try
		{
			ArrayList<InsuranceEvent> insuranceEventList = loadEvents();
			insuranceEventList.remove(index);
			saveEvents(insuranceEventList);
		}
		catch (IndexOutOfBoundsException ex)
		{
			System.out.println("No items removed.");
		}
	}

	public static void makePayment()
	{
		Scanner scanner = new Scanner(System.in);
		int index = -1;
		
		viewInsuranceEvents();
		
		System.out.println("Please enter the number of the bill that you paid, or enter 0 to exit.");
		String line = scanner.nextLine();
		index = Integer.parseInt(line) - 1;
		try
		{
			ArrayList<InsuranceEvent> insuranceEventList = loadEvents();
			insuranceEventList.get(index).pay();
			saveEvents(insuranceEventList);
		}
		catch (IndexOutOfBoundsException ex)
		{
			System.out.println("No bills marked as paid.");
		}
	}

	public static void main(String[] args)
			throws InterruptedException {
		
		int choice = 0;

		System.out.println("Moose Calendar by TwoGuysInAShed Productions");

		while (choice != 6)
		{
			choice = taskChoice();
			switch (choice)
			{
			case 1 :
				addInsuranceEvent();
				break;
			case 3 :
				viewInsuranceEvents();
				break;
			case 4 :
				removeInsuranceEvent();
			case 5 :
				makePayment();
			case 6 :
			default :
			}
		}

		// This calls the windows command interpreter (cmd.exe) with
		// flag /C and arguments start, java, and RemindersRunner. The
		// flag tells it to execute the following strings as a command
		// line command, viz. "start java RemindersRunner". Start is a
		// command that opens a new console window and it itself can
		// receive a command or program along with its arguments as an
		// argument, and in this case, it's java RemindersRunner.
		String[] command = { "cmd.exe", "/C", "start", "java", "RemindersRunner" };
		ProcessBuilder runSomeCode = new ProcessBuilder(command);
		
		runSomeCode.directory(new File("C:\\Users\\Glew\\workspace\\Moose-Project\\bin"));
		
		try
		{
			runSomeCode.start();
		}
		catch (IOException ex)
		{
			ex.printStackTrace(); 
		}
	}
}
