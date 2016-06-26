import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.io.Serializable;
import java.text.SimpleDateFormat;

// This class includes the logic for whether the user should be reminded of
// this event. The rationale is that this logic depends only on information
// contained in the event objects; however, there is an excellent case for
// having a reminder policy class that makes all these decisions. This
// could be a singleton that sets the policy for the entire program or each
// object could have a policy field that is checked for whether a reminder
// should happen or not.
public class InsuranceEvent implements Serializable
{
	private Date eventDate;
	private String eventName;
	private String insuranceCarrier;
	private double levelOfCoverage;
	private double premium;
	// ONEMONTH, etc., mean 'due in less than one month but more than two weeks', etc.
	public enum Urgency { NOTDUESOON, ONEMONTH, TWOWEEKS, ONEWEEK, OVERDUE }
	Urgency lastUrgency; // The status when the last reminder was acknowledged.
	
	public InsuranceEvent()
	{
		eventDate = new Date();
		eventName = "";
		insuranceCarrier = "";
		levelOfCoverage = 0.0;
		premium = 0.0;
		lastUrgency = Urgency.NOTDUESOON;
	}

	public InsuranceEvent(Date eventDate, String eventName,
			String insuranceCarrier, double levelOfCoverage, double premium)
	{
		this.eventDate = eventDate;
		this.eventName = eventName;
		this.insuranceCarrier = insuranceCarrier;
		this.levelOfCoverage = levelOfCoverage;
		this.premium = premium;
		lastUrgency = Urgency.NOTDUESOON;
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
	
	private Urgency getLastUrgency ()
	{
		return lastUrgency;
	}
	
	private void setLastUrgency (Urgency lastUrgency)
	{
		this.lastUrgency = lastUrgency;
	}

	public Urgency getUrgency()
	{
		// This method computes the urgency by checking whether adding a
		// certain number of days to today's date brings it past the due
		// date.
		
		// nowCalendar always holds today's date.
		Calendar nowCalendar = Calendar.getInstance();
		// shiftingCalendar holds today's date plus an offset.
		Calendar shiftingCalendar = Calendar.getInstance();
		// dueCalendar is simply the event's due date.
		Calendar dueCalendar = Calendar.getInstance();
		// currentUrgency holds the return value, defaulting to NOTDUESOON.
		Urgency currentUrgency = Urgency.NOTDUESOON;
		
		dueCalendar.setTime(getEventDate());

		// What follows is a series of tests. Each time, a certain amount
		// of time is added to today's date, and if that time is past the
		// due date, the appropriate urgency level is set. The tests are
		// organized from being due in a month to being overdue. This makes
		// it so that more restrictive urgency levels can simply override
		// the more inclusive ones.
		// If all tests fail, NOTDUESOON is returned.
		shiftingCalendar.setTime(nowCalendar.getTime());
		shiftingCalendar.add(Calendar.DAY_OF_MONTH, 30);
		if (shiftingCalendar.after(dueCalendar))
			currentUrgency = Urgency.ONEMONTH;

		shiftingCalendar.setTime(nowCalendar.getTime());
		shiftingCalendar.add(Calendar.DAY_OF_MONTH, 14);
		if (shiftingCalendar.after(dueCalendar))
			currentUrgency = Urgency.TWOWEEKS;

		shiftingCalendar.setTime(nowCalendar.getTime());
		shiftingCalendar.add(Calendar.DAY_OF_MONTH, 7);
		if (shiftingCalendar.after(dueCalendar))
			currentUrgency = Urgency.ONEWEEK;

		shiftingCalendar.setTime(nowCalendar.getTime());
		if (shiftingCalendar.after(dueCalendar))
			currentUrgency = Urgency.OVERDUE;

		return currentUrgency;
	}
	
	// This makes the due date go up by a year. It doesn't take into
	// account business days.
	public void pay()
	{
		Calendar eventCalendar = Calendar.getInstance();
		
		eventCalendar.setTime(getEventDate());
		eventCalendar.add(Calendar.YEAR, 1);
		setEventDate(eventCalendar.getTime());
		setLastUrgency(Urgency.NOTDUESOON);
	}
	
	// This effectively records that a reminder was given to the user. The
	// user of the class (the programmer using the class, not end-user)
	// needs to decide when to call this, and must do so for the program to
	// make the correct decisions when displaying reminders.
	public void reminderDisplayed ()
	{
		setLastUrgency(getUrgency());
	}
	
	// This is a hardcoded reminder policy. It outputs whether the user
	// should be reminded of the event.
	public boolean shouldRemind ()
	{
		Urgency currentUrgency = getUrgency();
		
		// First we test if it's due in a week or overdue. We return true
		// for either of these cases.
		if (currentUrgency.compareTo(Urgency.TWOWEEKS) > 0)
			return true;
		
		// Then we check whether it's due soon at all, and if it is, we
		// check if its urgency has increased since the last reminder was
		// effected.
		if (currentUrgency.compareTo(Urgency.NOTDUESOON) > 0 &&
				currentUrgency.compareTo(getLastUrgency()) > 0)
			return true;
		else return false;
	}
	
	public String toString()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);		
		String dateString1 = formatter.format((this.eventDate));
		
		return String.format("%-18s %-50s %-20s $%-,20.2f $%-,12.2f %-12s\n", 
				dateString1, this.eventName, this.insuranceCarrier,
				this.levelOfCoverage, this.premium, this.getUrgency().name());
	}

}