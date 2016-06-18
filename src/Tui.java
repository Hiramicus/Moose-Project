import java.util.Scanner;
import java.util.InputMismatchException;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.ArrayList;

public class Tui {

	public int taskChoice ()
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
		System.out.println("6. View reminders");
		System.out.println("7. Exit");
		userChoice = s.nextInt();
		while(!(userChoice > 0 && userChoice < 8))
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
	
	public InsuranceEvent addInsuranceEventUI ()
	{
		InsuranceEvent tempEvent = new InsuranceEvent();
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
	
	public void displayInsuranceEvents (ArrayList<InsuranceEvent> insuranceEventList)
	{
		InsuranceEvent currentEvent = null;
		
		for (int i = 0; i < insuranceEventList.size(); i++)
		{
			currentEvent = insuranceEventList.get(i);
			// Note that the bill number is one more than the index.
			System.out.println("Bill " + (i + 1) + " :");
			System.out.println(currentEvent.toString());
		}
		if (insuranceEventList.size() == 0)
			System.out.println("No records to display.");
	}
}
