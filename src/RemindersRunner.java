import java.util.Calendar;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.Files;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class RemindersRunner
{
	// This is just a path made up of the filename.
	public static final Path logFileRelPath = Paths.get("LastReminderCheck.log");
	
	// This gives us an absolute path to the file. It depends on
	// MooseCalendarRunner's getAppDataFPath() method.
	public static Path getLogFileFPath()
	{
		Path appDataDirAbsPath = MooseCalendarRunner.getAppDataDirAbsPath();
		return appDataDirAbsPath.resolve(logFileRelPath);
	}
	
	// The write method does not have to check for existence of the file.
	// It simply creates it if it isn't there.
	private static void setLastTimeRun()
	{
		ByteArrayOutputStream byteos = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try
		{
			out = new ObjectOutputStream(byteos);
			out.writeObject(Calendar.getInstance());
			Files.write(getLogFileFPath(), byteos.toByteArray());
		}
		catch (IOException ioex)
		{
			ioex.printStackTrace();
		}
	}

	private static Calendar getLastTimeRun()
	{
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		ObjectInputStream in; 

		try
		{
			byte[] allBytes = Files.readAllBytes(getLogFileFPath());
			ByteArrayInputStream byteis = new ByteArrayInputStream(allBytes);
			in = new ObjectInputStream(byteis);
			Calendar lastTimeRun = (Calendar) in.readObject();
			return lastTimeRun;
		}
		catch (ClassNotFoundException | IOException ex)
		{
			ex.printStackTrace();
			return yesterday;
		}
	
	}
	
	private static boolean hasRunToday()
	{
		Calendar lastTimeRun = getLastTimeRun();
		Calendar currentTime = Calendar.getInstance();
		int today = currentTime.get(Calendar.DAY_OF_YEAR);
		int lastRunDay = lastTimeRun.get(Calendar.DAY_OF_YEAR); 
		
		return today == lastRunDay;
	}

	private static Calendar getNextTimeToRun()
	{
		Calendar now = Calendar.getInstance();

		Calendar today930 = Calendar.getInstance();
		today930.set(Calendar.HOUR, 9);
		today930.set(Calendar.MINUTE, 30);
		today930.set(Calendar.AM_PM, Calendar.AM);

		Calendar tomorrow930 = Calendar.getInstance();
		tomorrow930.add(Calendar.DAY_OF_MONTH, 1);
		tomorrow930.set(Calendar.HOUR, 9);
		tomorrow930.set(Calendar.MINUTE, 30);
		tomorrow930.set(Calendar.AM_PM, Calendar.AM);
		
		Calendar aWhileFromNow = Calendar.getInstance();
		aWhileFromNow.add(Calendar.MINUTE, 45);

		if (hasRunToday())
			return tomorrow930;
		else
			if (now.compareTo(today930) > 0)
				return aWhileFromNow;
			else
				return today930;
	}
	
	private static long getSleepDuration(Calendar nextTimeToRun)
	{
		long sleepDuration = 0;
		Calendar now = Calendar.getInstance(); // Current time
		long nowMS = now.getTimeInMillis();
		long nextTimeToRunMS = nextTimeToRun.getTimeInMillis();
		
		sleepDuration = nextTimeToRunMS - nowMS + 5000;
		
		// In case it comes out negative.
		if (sleepDuration < 5000)
			sleepDuration = 5000;
		
		return sleepDuration;
	}
	
	private static boolean anyReminders()
	{
		Path ieFileFPath = MooseCalendarRunner.getIEFileFPath();
		ListOnDisk<InsuranceEvent> ieList;
		ieList = new ListOnDisk<InsuranceEvent>(ieFileFPath);

		for(int i = 0; i < ieList.size(); i++)
			if (ieList.get(i).shouldRemind())
				return true;

		return false;
	}
	
	private static void runReminders()
	{
		// This calls the windows command interpreter (cmd.exe) with
		// flag /C and arguments start, java, and RemindersRunner. The
		// flag tells it to execute the following strings as a command
		// line command, viz. "start java RemindersRunner". Start is a
		// command that opens a new console window and it itself can
		// receive a command or program along with its arguments as an
		// argument, and in this case, it's java RemindersRunner.
		String[] command = { "cmd.exe", "/C", "start", "java", "MooseCalendarRunner", "r" };
		ProcessBuilder runSomeCode = new ProcessBuilder(command);
	
		runSomeCode.directory(new File("C:\\Program Files\\TwoGuysInAShed\\MooseCalendar"));
	
		try
		{
			runSomeCode.start();
		}
		catch (IOException ex)
		{
			ex.printStackTrace(); 
		}
	
	}

	public static void main(String[] args)
	{
		if (Files.notExists(getLogFileFPath()))
		{
			try
			{
				Files.createFile(getLogFileFPath());
				setLastTimeRun();
			}
			catch (IOException ioex)
			{
				ioex.printStackTrace();
			}
		}
		
		Calendar nextTimeToRun = getNextTimeToRun();
		while (true)
		{
			try
			{
				Thread.sleep(getSleepDuration(nextTimeToRun));
			}
			catch (InterruptedException iex) { }
			if (Calendar.getInstance().compareTo(nextTimeToRun) > 0)
			{
				setLastTimeRun();
				if (anyReminders())
					runReminders();
				nextTimeToRun = getNextTimeToRun();
			}
		}
	}

}