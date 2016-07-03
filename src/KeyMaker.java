import java.nio.file.Path;

public class KeyMaker
{
	
	private static int billCount;

	private static int companyCount;

	private static int categoryCount;
	
	private static boolean initialized;
	
	static
	{
		billCount = 0;
		companyCount = 0;
		categoryCount = 0;
		initialized = false;
	}

	public boolean initialize(Path counts)
	{
		
		initialized = true;
		return initialized;
	}

	public static int generateKey(KeyType keyType)
	{
		switch (keyType)
		{
		case BILL:
			return ++billCount;
		case CAT:
			return ++categoryCount;
		case COM:
			return ++companyCount;
		default:
			return 0;
		}
		
	}
}