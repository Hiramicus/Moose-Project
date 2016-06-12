import java.io.IOException;
import java.io.InputStream;
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


public class MooseCalendarRunner {

	public static final Path insuranceEventPath = Paths.get("InsuranceEvents.ser");
	
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
		userChoice = s.nextInt();
		while(!(userChoice > 0 && userChoice < 5))
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
		InsuranceEvent temp = new InsuranceEvent(new Date(), "temp", "temp", 0.0, 0.0);
		Scanner s = new Scanner(System.in);
		String dateString;
		SimpleDateFormat df = new SimpleDateFormat();

		System.out.println("Please enter the following:");
		System.out.println("Address of the insured property:");
		temp.setEventName(s.nextLine());
		System.out.println("Name of the insurance carrier:");
		temp.setInsuranceCarrier(s.nextLine());
		System.out.println("Premium:");
		temp.setPremium(s.nextDouble());
		System.out.println("Level of coverage:");
		temp.setLevelOfCoverage(s.nextDouble());
		System.out.println("Date payment is due (YYYY-MM-DD format):");
		dateString = s.nextLine();
		try 
		{
			temp.setEventDate(df.parse(dateString));
		}
		catch (ParseException ex)
		{
			
		}
	}
	public static void main(String[] args)
			throws InterruptedException {
		
		Path file = Paths.get("FirstInsurance.ser");
		int choice = 0;
		InsuranceEvent testInsurance1 
		= new InsuranceEvent(new Date(), "Property1", "ABC", 10000.0, 100.0);
		InsuranceEvent testInsurance2 
		= new InsuranceEvent(new Date(), "Property2", "DEF", 10000.0, 100.0);
		InsuranceEvent testInsurance3 
		= new InsuranceEvent(new Date(), "Property3", "GHI", 10000.0, 100.0);
		
		
		try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
			out.writeObject(testInsurance1);
			out.writeObject(testInsurance2);
			out.writeObject(testInsurance3);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		InsuranceEvent outInsurance = null;
		try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
			for(int i = 0; i < 3; i++) {
				try {
					outInsurance = (InsuranceEvent) in.readObject();
					System.out.println(outInsurance.toString());
				}
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		System.out.println("Moose Calendar by TwoGuysInAShed Productions");

		while (choice != -1)
		{
			choice = taskChoice();
			switch (choice)
			{
			case 1 :
				addInsuranceEvent();
				break;
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
		ProcessBuilder runSomeCode = new ProcessBuilder("cmd.exe", "/C", "start", "java", "RemindersRunner" );
		
		runSomeCode.directory(new File("C:\\Users\\Hiram\\workspace\\Moose-Project\\bin"));
		
		try { runSomeCode.start(); } catch (IOException ex) { ex.printStackTrace(); System.out.print("here");}
	}
}
