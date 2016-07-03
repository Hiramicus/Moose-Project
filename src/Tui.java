import java.util.Scanner;
import java.util.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.text.ParseException;

public class Tui {
	
	public void introduction()
	{
		System.out.println("Moose Calendar by TwoGuysInAShed Productions");
	}
	
	public Date forceDateInput()
	{
		Scanner scanner = new Scanner(System.in);
		String line;
		boolean goodInput = false;
		Date result = new Date();
		String message = "Please enter a date in the correct format.";
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		
		while (!goodInput)
		{
			try
			{
				line = scanner.nextLine();
				result = df.parse(line);
				goodInput = true;
			}
			catch (ParseException pex)
			{
				System.out.println(message);
			}
		}
		
		return result;
	}

	public int forceIntRangeInput(int a, int b)
	{
		Scanner scanner = new Scanner(System.in);
		String line;
		boolean goodInput = false;
		int result = 0;
		String please = "Please enter a number between ";
		String message = please + a + " and " + b + ".";
		
		while (!goodInput)
		{
			try
			{
				line = scanner.nextLine();
				result = Integer.parseInt(line);
				if (result >= a && result <= b)
					goodInput = true;
				else
					System.out.println(message);
			}
			catch (NumberFormatException nfex)
			{
				System.out.println(message);
			}
		}
		
		return result;
	}

	public double forceDoubleInput()
	{
		Scanner scanner = new Scanner(System.in);
		String line;
		boolean goodInput = false;
		double result = 0;
		String message = "Please enter a number (no commas).";
		
		while (!goodInput)
		{
			try
			{
				line = scanner.nextLine();
				result = Double.parseDouble(line);
				goodInput = true;
			}
			catch (NumberFormatException nfex)
			{
				System.out.println(message);
			}
		}
		
		return result;
	}

	public int taskChoice()
	{
		Scanner scanner = new Scanner(System.in);
		int userChoice = 0;
		
		System.out.println("Type the number of the option you want and press " +
				"Enter.");
		System.out.println("1. Add bills");
		System.out.println("2. Edit bills");
		System.out.println("3. View bills");
		System.out.println("4. Delete bills");
		System.out.println("5. Check off bills as paid");
		System.out.println("6. Resort list");
		System.out.println("7. Exit");
		
		userChoice = forceIntRangeInput(1, 7);
		
		return userChoice;
	}
	
	public InsuranceEvent addInsuranceEventUI()
	{
		InsuranceEvent tempEvent = new InsuranceEvent();
		Scanner s = new Scanner(System.in);
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
		double tempPremium = forceDoubleInput();
		tempEvent.setPremium(tempPremium);
		
		System.out.println("Level of coverage:");
		double tempLevelOfCoverage = forceDoubleInput();
		tempEvent.setLevelOfCoverage(tempLevelOfCoverage);

		boolean goodFormat = true;
		System.out.println("Date payment is due (MM/DD/YY format):");
		Date tempDate;
		tempDate = forceDateInput();
		tempEvent.setEventDate(tempDate);		

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
		int index = -1;

		displayInsuranceEvents(ieList);

		System.out.print("Please enter the number of the bill that you would like to ");
		System.out.print(action);
		System.out.println(", or enter 0 to exit.");
		
		index = forceIntRangeInput(0, ieList.size());
		
		return index - 1;
	}
	
	public int getSortChoice()
	{
		Scanner scanner = new Scanner(System.in);
		int choice = 0;

		System.out.print("Enter the number corresponding to the sort ");
		System.out.println("method you'd like to use.");
		System.out.println("1. By bill's due date.");
		System.out.print("2. Alphabetically, by company name or ");
		System.out.println("property address.");

		choice = forceIntRangeInput(1, 2);
		
		return choice;
	}
	
	public int getField()
	{
		int field = 0;

		System.out.println("Please enter which field you would like to edit: ");
		System.out.println("1. Date\n" + "2. Company/Address\n" +
				"3. Insurance Provider\n" + "4. Level of Coverage\n" +
				"5. Premium");
		System.out.println(", or enter 0 to exit.");
		field = forceIntRangeInput(0, 5);
		
		return field;
	}
	
	public void setInsuranceEventUI(int field, int index, ListOnDisk<InsuranceEvent> ieList)
	{
		InsuranceEvent tempEvent = ieList.get(index);
		Scanner s = new Scanner(System.in);
		String tempString;
		
		System.out.println("Please enter the following:");
		
		if (field == 1)
		{
			System.out.println("Date payment is due (MM/DD/YY format):");
			Date tempDate = forceDateInput();
			tempEvent.setEventDate(tempDate);
			ieList.set(index, tempEvent);
		}
		else if (field == 2)
		{
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
			ieList.set(index, tempEvent);
		}
		else if (field == 3)
		{
			System.out.println("Name of the insurance carrier:");
			tempEvent.setInsuranceCarrier(s.nextLine());
			ieList.set(index, tempEvent);
		}
		else if (field == 4)
		{
			System.out.println("Level of coverage:");
			double tempLevelOfCoverage = forceDoubleInput();
			tempEvent.setLevelOfCoverage(tempLevelOfCoverage);
			ieList.set(index, tempEvent);
		}
		else 
		{
			System.out.println("Premium:");
			double tempPremium = forceDoubleInput();
			tempEvent.setPremium(tempPremium);
			ieList.set(index, tempEvent);
		}

	}
	
	public void noActionTaken()
	{
		System.out.println("No action taken.");
	}
}