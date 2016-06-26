import java.util.Scanner;
import java.util.InputMismatchException;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.ArrayList;

public class Tui {

	public void introduction()
	{
		System.out.println("Moose Calendar by TwoGuysInAShed Productions");
	}
	public int taskChoice()
	{
		Scanner s = new Scanner(System.in);
		int userChoice = 0;
		
		System.out.println("Type the number of the option you want and press " +
				"Enter.");
		System.out.println("1. Add bills");
		System.out.println("2. Edit bills");
		System.out.println("3. View bills");
		System.out.println("4. Delete bills");
		System.out.println("5. Check off bills as paid");
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
	
	public InsuranceEvent addInsuranceEventUI()
	{
		InsuranceEvent tempEvent = new InsuranceEvent();
		Scanner s = new Scanner(System.in);
		String dateString;
		String tempString;
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

		System.out.println("Please enter the following:");
		System.out.println("Company name or address of the insured property:");
		System.out.print("Company name/Number, street, & apt./suite/etc. (no commas): ");
		tempString = s.nextLine();

		System.out.print("City: ");
		tempString = tempString + " " + s.nextLine();

		System.out.print("State (two letters only): ");
		tempString = tempString + " " + s.nextLine().toUpperCase();

		System.out.print("Zip code: ");
		tempString = tempString + " " + s.nextLine();

		tempEvent.setEventName(tempString);

		System.out.println("Name of the insurance carrier:");
		tempEvent.setInsuranceCarrier(s.nextLine());

		// For the next two variables, we use nextLine so that we gobble up
		// the newline character. The resulting string then gets parsed and
		// the returned double gets set into tempEvent.
		System.out.println("Premium:");
		tempEvent.setPremium(Double.parseDouble(s.nextLine()));
		
		System.out.println("Level of coverage:");
		tempEvent.setLevelOfCoverage(Double.parseDouble(s.nextLine())); 

		System.out.println("Date payment is due (MM/DD/YY format):");
		dateString = s.nextLine();
		tempEvent.setEventDate(df.parse(dateString, new ParsePosition(0)));
		
		return tempEvent;
	}
	
	public void displayInsuranceEvents(ListOnDisk<InsuranceEvent> ieList)
	{
		InsuranceEvent currentEvent = null;
		
		for (int i = 0; i < ieList.size(); i++)
		{
			currentEvent = ieList.get(i);
			// Note that the bill number is one more than the index.
			System.out.println("Bill " + (i + 1) + " :");
			System.out.println(currentEvent.toString());
		}
		if (ieList.size() == 0)
			System.out.println("No records to display.");
	}
	
	public void remind(ListOnDisk<InsuranceEvent> ieList, ArrayList<Integer> indices)
	{
		InsuranceEvent currentEvent = null;

		System.out.println("These bills are due soon:");
		
		for (int i = 0; i < indices.size(); i++)
		{
			currentEvent = ieList.get(indices.get(i));
			System.out.println("Bill " + (i + 1) + " :");
			System.out.println(currentEvent.toString());
		}
	}
	
	public int getIndex(ListOnDisk<InsuranceEvent> ieList, String action)
	{
		Scanner scanner = new Scanner(System.in);
		int index = -1;

		displayInsuranceEvents(ieList);

		System.out.print("Please enter the number of the bill that you would like to ");
		System.out.print(action);
		System.out.println(", or enter 0 to exit.");
		String line = scanner.nextLine();
		index = Integer.parseInt(line) - 1;
		
		return index;
	}
	
	public void noActionTaken()
	{
		System.out.println("No action taken.");
	}
}
