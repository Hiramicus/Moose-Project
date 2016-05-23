import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.InputMismatchException;

public class MooseCalendarRunner {

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
		s.close();
		
		return userChoice;
	}
	public static void main(String[] args)
			throws InterruptedException {
		
		System.out.println("Moose Calendar by TwoGuysInAShed Productions");
		/*
		Runtime runtime = Runtime.getRuntime();
		try {
		runtime.exec("cmd.exe /c start");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		*/
	}
}
