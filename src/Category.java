
public class Category
{

	private int categoryId;
	private String categoryName;

	public Category(String name)
	{
		categoryId = KeyMaker.generateKey(KeyType.CAT);
		categoryName = name;
	}

	public String getName()
	{
		return categoryName;
	}
	
	public void setName(String name)
	{
		categoryName = name;
	}
	
	public int getId()
	{
		return categoryId;
	}
}
