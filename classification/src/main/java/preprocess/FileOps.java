package preprocess;
import java.io.*;
import java.util.*;
class FileOps
{
	String name;
	String line;
	FileOps(String name)
	{
		this.name=name;
	}
	ArrayList<String> read ()
	{
		ArrayList<String> text=null;
		try
		{
			// FileReader reads text files in the default encoding.
			FileReader fileReader =  new FileReader(name);
		        text=new ArrayList<String>();
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) 
			{
				
				text.add(line);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		return text;
	}
}
