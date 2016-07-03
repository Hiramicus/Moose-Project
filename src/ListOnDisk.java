import java.util.ArrayList;
import java.util.Collections;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.EOFException;
import java.util.Comparator;

// This class is a wrapper to an ArrayList that simply saves its
// contents to the disk after every change in state.
// Calling methods on individual elements won't result in a permanent change
// in state unless this convention is used: first, get the element, then
// change the state, then set that element back into this list.
public class ListOnDisk<T> {

	// Because we load from disk and save to disk after every change, we
	// only need the file name as permanent data in memory.
	private Path dataFile;
	
	public ListOnDisk(Path file)
	{
		this.dataFile = file;
	}
	
	public ListOnDisk(ListOnDisk<T> old, Path file)
	{
		this.dataFile = file;
		
		saveItems(old.loadItems());
	}

	// This method loads the contents of the file into memory.
	// It should ideally be private, but there are some situations in which
	// the main program needs to have full access to the list. This is so
	// that it can call methods that I've not made a wrapper for. The best
	// example is when iterating over the whole list using the
	// for (item : list) syntax.
	public ArrayList<T> loadItems()
	{
		ArrayList<T> list = new ArrayList<T>();
		boolean fileIsReady = false;

		// Let's try not to open non-existing or empty files.
		if (Files.exists(dataFile))
		{
			try
			{
				if (Files.size(dataFile) > 0)
					fileIsReady = true;
			}
			catch (IOException ioex)
			{
				ioex.printStackTrace();
			}
		}
		if (fileIsReady)
		{
			ObjectInputStream in = null;
			try
			{
				// Quite a bit of indirection ahead. First,
				// Files.readAllBytes takes the file and outputs a raw
				// byte array. That's just an array, so to actually call
				// input stream methods, it gets turned into a
				// ByteArayInputStream, and we use ObjectInputStream to
				// actually deserialize that data.
				byte[] allBytes = Files.readAllBytes(dataFile);
				ByteArrayInputStream byteis = new ByteArrayInputStream(allBytes);
				in = new ObjectInputStream(byteis);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}

			// Reading the objects from the byte array.
			// We will loop through until we get an EOF exception.
			boolean notEOF = true;
			while(notEOF)
			{
				try
				{
					list.add((T) in.readObject());
				}
				catch(EOFException eofex)
				{
					notEOF = false;
				}
				catch (ClassNotFoundException | IOException ioex)
				{
					ioex.printStackTrace();
				}
			}
		}
		// Note that this can return an empty ArrayList.
		return list;
	}

	// This method serializes and saves the array.
	// Note that this method completely overwrites the file.
	public void saveItems(ArrayList<T> list)
	{
		ByteArrayOutputStream byteos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try
		{
			out = new ObjectOutputStream(byteos);
			for (T item : list)
				out.writeObject(item);
			Files.write(dataFile, byteos.toByteArray());
		}
		catch (IOException ioex)
		{
			ioex.printStackTrace();
		}
	}

	public void toNewFile(Path newDataFile)
	{
		ArrayList<T> list = loadItems();
		ByteArrayOutputStream byteos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try
		{
			out = new ObjectOutputStream(byteos);
			for (T item : list)
				out.writeObject(item);
			Files.write(newDataFile, byteos.toByteArray());
		}
		catch (IOException ioex)
		{
			ioex.printStackTrace();
		}
	}

	public int size()
	{
		ArrayList<T> list = loadItems();
		
		return list.size();
	}
	
	public T get(int index)
	{
		ArrayList<T> list = loadItems();

		return list.get(index);
	}
	
	public void set(int index, T item)
	{
		ArrayList<T> list = loadItems();
		
		list.set(index, item);
		saveItems(list);
	}
	
	public void add(T item)
	{
		ArrayList<T> list = loadItems();
		
		list.add(item);
		saveItems(list);
	}
	
	public void remove(int index)
	{
		ArrayList<T> list = loadItems();
		
		list.remove(index);
		saveItems(list);
	}
	
	public void sort(Comparator<T> c)
	{
		ArrayList<T> list = loadItems();
		Collections.sort(list, c);
		saveItems(list);
	}
}