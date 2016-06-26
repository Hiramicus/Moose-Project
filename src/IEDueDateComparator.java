import java.util.Comparator;

public class IEDueDateComparator implements Comparator<InsuranceEvent>
{
	@Override
	public int compare(InsuranceEvent ie1, InsuranceEvent ie2)
	{
		// We just funnel the Date class's comparison method's results to
		// the sorting algorithm.
		return ie1.getEventDate().compareTo(ie2.getEventDate());
	}
}