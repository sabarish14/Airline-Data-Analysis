package preprocess;
import java.io.*;
import java.util.*;

import au.com.bytecode.opencsv.CSVReader;
public class FileRead
{
	String name;
	String line;
	public FileRead(String name)
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
			bufferedReader.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		return text;
	}
	public List<String[]> readCSV ()
	{
		List<String[]> result= new ArrayList<String[]>();
		try
		{
			CSVReader reader = new CSVReader(new FileReader(this.name));
			String[] line;
			
			while ((line = reader.readNext()) != null)
			{
					if (!line[0].isEmpty())
						result.add(line);
			}
			reader.close();
		}
		
		catch(Exception e)
		{
			
		}
		
		return result;
		
	}
	
	

}		
