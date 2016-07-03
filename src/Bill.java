import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.io.Serializable;

public class Bill implements Serializable
{

// This class includes the logic for whether the user should be reminded of
// this event. The rationale is that this logic depends only on information
// contained in the event objects; however, there is an excellent case for
// having a reminder policy class that makes all these decisions. This
// could be a singleton that sets the policy for the entire program or each
// object could have a policy field that is checked for whether a reminder
// should happen or not.
	private final int billId;
	private int categoryId;
	private int companyId;
	private double amountDue;
	private Date dueDate;
	private int periodInDays;
	private String notes;

	// ONEMONTH, etc., mean 'due in less than one month but more than two weeks', etc.
	public enum Urgency { NOTDUESOON, ONEMONTH, TWOWEEKS, ONEWEEK, OVERDUE }
	Urgency lastUrgency; // The status when the last reminder was acknowledged.
	
	public Bill()
	{
		// This is effectively a null bill.
		billId = 0;
		categoryId = 0;
		companyId = 0;
		amountDue = 0.0;
		dueDate = new Date();
		periodInDays = 0;
		lastUrgency = Urgency.NOTDUESOON;
		notes = "";
	}

	public Bill(int id, int catId, int acctId, double amtDue, Date date,
			int period, String aNote)
	{
		billId = id;
		categoryId = 0;
		companyId = acctId;
		amountDue = amtDue;
		dueDate = date;
		periodInDays = period;
		notes = aNote;
		lastUrgency = Urgency.NOTDUESOON;
	}

	private int getBillId()
	{
		return billId;
	}
	
	// private void setBillId(int id) should not be implemented.

	private int getCategoryId()
	{
		return categoryId;
	}
	
	private void setCategoryId(int newCategoryId)
	{
		categoryId = newCategoryId;
	}

	private int getCompanyId()
	{
		return companyId;
	}

	private void setCompanyId(int newCompanyId)
	{
		companyId = newCompanyId;
	}

	private double getAmountDue()
	{
		return amountDue;
	}
  
	private void setAmountDue(double newAmountDue)
	{
		amountDue = newAmountDue;
	}

	private Date getDueDate()
	{
		return dueDate;
	}
  
	private void setDueDate(Date newDueDate)
	{
		dueDate = newDueDate;
	}

	private int getPeriodInDays()
	{
		return periodInDays;
	}
	
	private void setPeriodInDays(int newPeriodInDays)
	{
		periodInDays = newPeriodInDays;
	}

	private Urgency getLastUrgency ()
	{
		return lastUrgency;
	}
	
	private void setLastUrgency(Urgency newLastUrgency)
	{
		lastUrgency = newLastUrgency;
	}
	
	private String getNotes()
	{
		return notes;
	}

	private void setNotes(String newNotes)
	{
		notes = newNotes;
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
		
		dueCalendar.setTime(getDueDate());

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
		
		eventCalendar.setTime(getDueDate());
		eventCalendar.add(Calendar.YEAR, 1);
		setDueDate(eventCalendar.getTime());
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
	
	public String toShortString()
	{
		//DateFormatter dateFormatter = 
		String output = "";
		String formatString;
		//output = String.format(u, arg1)getBillId();
		return output;
	}
	
}