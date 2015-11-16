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
			bufferedReader.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		return text;
	}
	void writeRows (ArrayList<double[]> arr, FileWriter fileWriter)

	{
		try
		{
			System.out.println ("arr size:"+ arr.size() );
			BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);
			for ( int i=0; i<arr.size(); i++) 
			{	
				double row[]=arr.get(i);
				String str="";
			
				for (int j=0;j<row.length;j++)	
					str+=String.valueOf(row[j])+",";
				System.out.println (str);
				bufferedWriter.write(str);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

	}

	FileWriter  writeHeaders (ArrayList<String> columns)
	{
		FileWriter fileWriter=null; 
		try
		{
			// FileWriter writes text files in the default encoding.
			fileWriter =  new FileWriter(name);
			BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);
			String str="";
			for (int i=0;i<columns.size();i++)
			{
				String s=columns.get(i);
				if (s.equals("crsArrTime") || s.equals("crsDepTime") )
					str+="hh"+ "," +  "mm" + "," + "ss" + ",";
				else
					str+=s+",";
			}
			bufferedWriter.write(str);
			//bufferedWriter.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		return fileWriter;
	}

	

}		
