import java.util.Date;

public class SABill extends Bill {

	public SABill()
	{
		// TODO Auto-generated constructor stub
	}

	public SABill(int id, int acctId, double amtDue,
			Date date, int period, String aNote, double coverage)
	{
		super(id, 2, acctId, amtDue, date, period, aNote);
	}

}
