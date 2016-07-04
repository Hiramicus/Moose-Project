import java.util.ArrayList;

public class Company
{

    private String nameOrAddress;

    private ArrayList<Integer> billIds;

    public Company(String noa)
    {
        nameOrAddress = noa;
        billIds = new ArrayList<Integer>();
    }

    public String getNameOrAddress()
    {
        return nameOrAddress;
    }

    public void setNameOrAddress(String newName)
    {
        nameOrAddress = newName;
    }

    public void addBillId(int billId)
    {
        billIds.add(billId);
    }

    public Integer[] getBillIds()
    {
        return (Integer[]) billIds.toArray();
    }
    
    public boolean removeBillId(int billId)
    {
        for (int i = 0; i < billIds.size(); i++)
            if (billIds.get(i) == billId)
            {
                billIds.remove(i);
                return true;
            }
        return false;
    }

}