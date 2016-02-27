package preprocess;
import java.io.*;
import java.util.*;

import au.com.bytecode.opencsv.CSVReader;

public class FileWrite
{
	String name;
	FileWriter fileWriter;
	BufferedWriter bufferedWriter ;
	public FileWrite(String name)
	{
		this.name=name;
		
		try 
		{	
			
			fileWriter = new FileWriter(name);
			bufferedWriter =new BufferedWriter(fileWriter);
		} 
		catch (IOException e) 
		{
			
			e.printStackTrace();
		}
		
	}
	
	public void writeRows (ArrayList<double[]> arr)

	{
		try
		{	
			for ( int i=0; i<arr.size(); i++) 
			{	
				double row[]=arr.get(i);
				String str="";
			
				for (int j=0;j<row.length;j++)	
					str+=String.valueOf(row[j])+",";
				
				bufferedWriter.write(str);
				bufferedWriter.newLine();
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

	}

	FileWriter  writeHeaders (ArrayList<String> columns)
	{
		
		try
		{	
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
			bufferedWriter.newLine();
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		return fileWriter;
	}
	public void csvAppend(List<String[]> csvContent,List<String> prediction)
	{
		try 
		{
			int count=0;
			for (String[] lines:csvContent)
			{
				String line="";
				for (String col:lines)
				{
					line+=col+",";					
				}
				line+=prediction.remove(0);
				this.bufferedWriter.write(line);
				this.bufferedWriter.newLine();
				
				
				
			}
			
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	public void close()
	{
		try
		{
			this.bufferedWriter.close();
			this.fileWriter.close();
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
		
	}

	

}		
