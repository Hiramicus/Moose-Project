import java.util.Date;

public class InsBill extends Bill
{
	
	private double levelOfCoverage;

	public InsBill()
	{
		// TODO Auto-generated constructor stub
	}

	public InsBill(int id, int acctId, double amtDue,
			Date date, int period, String aNote, double coverage)
	{
		super(id, 1, acctId, amtDue, date, period, aNote);
		levelOfCoverage = coverage;
	}

}