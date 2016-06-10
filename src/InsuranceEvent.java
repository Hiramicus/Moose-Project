import java.util.Date;
import java.io.Serializable;

public class InsuranceEvent implements Serializable
{
	private Date eventDate;
	private String eventName;
	private String insuranceCarrier;
	private double levelOfCoverage;
	private double premium;
	private boolean eventHandled;
	private int delays;
	
	public InsuranceEvent(Date eventDate, String eventName, String insuranceCarrier, double levelOfCoverage, double premium)
	{
		this.eventDate = eventDate;
		this.eventName = eventName;
		this.insuranceCarrier = insuranceCarrier;
		this.levelOfCoverage = levelOfCoverage;
		this.premium = premium;
		this.eventHandled = false;
		this.delays = 0;
	}

	public Date getEventDate()
	{
		return eventDate;
	}

	public void setEventDate(Date eventDate)
	{
		this.eventDate = eventDate;
	}

	public String getEventName()
	{
		return eventName;
	}

	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}

	public String getInsuranceCarrier()
	{
		return insuranceCarrier;
	}

	public void setInsuranceCarrier(String insuranceCarrier)
	{
		this.insuranceCarrier = insuranceCarrier;
	}

	public double getLevelOfCoverage()
	{
		return levelOfCoverage;
	}

	public void setLevelOfCoverage(double levelOfCoverage)
	{
		this.levelOfCoverage = levelOfCoverage;
	}

	public double getPremium()
	{
		return premium;
	}

	public void setPremium(double premium)
	{
		this.premium = premium;
	}

	public boolean isEventHandled()
	{
		return eventHandled;
	}

	public void setEventHandled(boolean eventHandled)
	{
		this.eventHandled = eventHandled;
	}
	
	public int getDelays()
	{
		return delays;
	}
	
	private void setDelays(int newDelay)
	{
		this.delays = newDelay;
	}
	
	public void delayPayment()
	{
		delays++;
	}
	
	public void resetDelays()
	{
		delays = 0;
	}
	
	public boolean reminderWorthy()
	{
		return true;
	}
	
	public String toString()
	{
		return String.format("%-35s %-40s %-20s %-20s %-12s\n"
				+ "%-35s %-40s %-20s $%-,20.2f $%-,12.2f\n", 
				"Date", "Event", "Insurance Carrier", "Level of Coverage", "Premium",
				this.eventDate, this.eventName, this.insuranceCarrier, this.levelOfCoverage, this.premium);
	}
	
}
