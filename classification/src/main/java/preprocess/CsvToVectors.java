package preprocess;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import au.com.bytecode.opencsv.CSVReader;
public class CsvToVectors 
{
	private final String csvPath;
	private HashMap<String,HashMap<String,Integer>> map;

	public CsvToVectors(String csvPath)
	{
		Serialization s=new Serialization();
		this.map=s.deserialize("hashmap.ser");
		this.csvPath = csvPath;
	}

	public List<MahoutVector> vectorize() 
	{

		List<MahoutVector> vectors = new ArrayList<MahoutVector> ();

		// Iterate the CSV records
		try
		{
			CSVReader reader = new CSVReader(new FileReader(this.csvPath));
			String[] line;
			String[] columns= reader.readNext();
			Categories c= new Categories();
			ArrayList<String> categories=c.findCategories();
			while ((line = reader.readNext()) != null)
			{
				MahoutVector vector=this.vectorize(line,columns,categories);
				if (vector!=null)
					vectors.add(vector);
			}
		}
		catch(IOException e)
		{
			
		}
		return vectors;
	}
	 private MahoutVector  vectorize (String line[],String columns[],ArrayList<String> categories )
	    {
	        double[] arr=new double[columns.length+4];
	        // Column count
	        int i=0;
	        String labelStr="";
	    	for (int col=0; col<line.length ; col++)
	        {
	    		try
	    		{
	    			String columnName=columns[col];
	    			String val=line[col];
	    			// If the field is a categorical field, assign the category.
		        	if (categories.contains(columnName) )
		        	{
		        		
		        		arr[i]=(double)(map.get(columnName).get(val));

		        	}
		        	// If not then there are three conditions
		      		else
		      		{
		      			//If it is Arrival or Dep time, convert to hh,mm,ss
		      			if (columnName.equals("CRS_ARR_TIME")|| (columnName.equals("CRS_DEP_TIME")))
		      			{
							ArrayList<String> split= this.split(val);
							for (String str:split)
							{
								arr[i]= Double.parseDouble(str);
								i++;
							}
							continue;
		      			}
		      			//Arrival Delay field is the label: 1- Delay. -1 : No Delay.
		      			else if (columnName.equals("ARR_DELAY"))
		      			{	
		      				double label=Double.parseDouble(val);
		      				if (label>0)
		      					labelStr="1";
		      				else
		      					labelStr="-1";		
		      			}
		      			// Its a numeric field. Convert directly
		      			else
		      			{
		      					if (!val.isEmpty())
		      						arr[i]= Double.parseDouble(val);	
		      					else
		      						arr[i]=0;
		      			}
		      		}
	    		}
	        	catch(Exception e)
				{
					if (columns[col].equals("ARR_DELAY"))
						return null;
					else
						arr[i]=0;
				}
				i++;
	      			
	      	}
	    	
	    	// Create a Dense vector from double array
	    	Vector v1= new DenseVector(arr);
	    	// Initialize the custom class
	    	MahoutVector mahoutVector = new MahoutVector();
	    	//Assign the vector
	    	mahoutVector.vector=v1;
	    	// Assign the label
	    	mahoutVector.label=labelStr;
	    	return mahoutVector;
	    }

	
	 private ArrayList<String> split(String val) 
	{
		ArrayList<String> result= new ArrayList<String> ();
		// Last two denotes mm and 0,len-2 denotes hh.No ss info . Hence add 00 to ss
		//Add the hh
		result.add(val.substring(0,val.length()-2));
		/*//Add the mm
		result.add(val.substring(val.length()-2, val.length()));
		String ss="00";
		result.add(ss);*/
		return result;
	}
		
}
