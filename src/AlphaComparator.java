
import java.util.Comparator;

public class AlphaComparator implements Comparator<InsuranceEvent>
{
	@Override
	public int compare(InsuranceEvent ie1, InsuranceEvent ie2)
	{
		// We just funnel the String class's comparison method's results to
		// the sorting algorithm.
		return ie1.getEventName().compareTo(ie2.getEventName());
	}
}