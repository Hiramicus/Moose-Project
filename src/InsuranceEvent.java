import java.util.Date;

public class InsuranceEvent
{
	private Date eventDate;
	private String eventName;
	private String insuranceCarrier;
	private double levelOfCoverage;
	private double premium;
	private boolean eventHandled;
	
	public InsuranceEvent(Date eventDate, String eventName, String insuranceCarrier, double levelOfCoverage, double premium)
	{
		this.eventDate = eventDate;
		this.eventName = eventName;
		this.insuranceCarrier = insuranceCarrier;
		this.levelOfCoverage = levelOfCoverage;
		this.premium = premium;
		this.eventHandled = false;
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
	
	
	
}
