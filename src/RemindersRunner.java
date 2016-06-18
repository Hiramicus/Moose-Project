import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Date;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedReader;

public class RemindersRunner {

	public static void main(String[] args) {
		Path file = Paths.get("FirstInsurance.ser");
		
		try
		{
		Thread.sleep(50000);
		}
		catch (InterruptedException ex)
		{
			ex.printStackTrace();
		}
		System.out.println("Hello world!");
		Scanner s = new Scanner(System.in);
		s.next();

	}

}
