package preprocess;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.bson.BSONDecoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;

import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.math.*;
import org.apache.mahout.math.Vector;

 class PreProcess 
{

    Categories c;
    ArrayList<Double> labels;
    //Method that generates the sequence file
    public void generatesequenceFile(String filename) throws FileNotFoundException 
    {
    	FileSystem fs = null;
    	SequenceFile.Writer writer;
    	Configuration conf = new Configuration();
    	List<MahoutVector> vectors= this.vectorize(filename);
    	// Write the data to SequenceFile
    	try 
    	{
    	    fs = FileSystem.get(conf);
    	    Path path = new Path("sequence");
    	    writer = new SequenceFile.Writer(fs, conf, path, Text.class, VectorWritable.class);   
    	    for (MahoutVector vector : vectors)
    	    {
    	    	VectorWritable vec = new VectorWritable();
    	    	int size=vector.vector.size();
    	        vec.set(vector.vector);
    	    	writer.append(new Text("/" + vector.label + "/"), vec);
    	    }
    	    writer.close();
    	}
    	catch (IOException e)
    	{
    		
    	}
    	
    }
    //This method returns the list of vectors 
    private List<MahoutVector> vectorize (String filename) throws FileNotFoundException 
    {
    	c=new Categories();
    	labels = new ArrayList<Double>();
		String columnFile="columns.txt";
		FileRead fileRead=new FileRead(columnFile);
		String outputFile= "output.csv";
		FileWrite fileWrite=new FileWrite(outputFile);
		ArrayList<String> columns=fileRead.read();
		fileWrite.writeHeaders(columns);
		System.out.println("columns:" + columns.toString());
	    File file = new File(filename);
	    InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		InputStream inputStream1 = new BufferedInputStream(new FileInputStream(file));
		List<MahoutVector> vectors = new LinkedList<MahoutVector>();
	    BSONDecoder decoder = new BasicBSONDecoder();
		HashMap<String,HashMap<String,Integer>> map=c.categorize(inputStream1);
	    int count = 0;
		try 
		{
		   System.out.println ("Total input size:"+ inputStream.available());
	       while (inputStream.available() > 0) 
		   {	               
	            BSONObject obj = decoder.readObject(inputStream);
	            MahoutVector vector = this.vectorize(obj, map, columns);
	            vectors.add(vector);
	            count++;           
	            if (count > 10000000 )
	            	break;
	       }
	       System.out.println("Total rows written:"+count);
    }
	catch (IOException e) 
	{
         e.printStackTrace();
    }
    
	return vectors;
  }

    // This method returns a mahout vector
    private MahoutVector  vectorize (BSONObject obj,HashMap<String, HashMap<String,Integer>> map,ArrayList<String> columns)
    {
        double[] arr=new double[columns.size()+4];
        // Column count
        int i=0;
        String labelStr="";
    	for (String k:columns)
        {
    		try
    		{
    			// If the field is a categorical field, assign the category.
	        	if (map.containsKey(k))
	        		arr[i]=(double)(map.get(k).get(obj.get(k).toString()));
	        	// If not then there are three conditions
	      		else
	      		{
	      			//If it is Arrival or Dep time, convert to hh,mm,ss
	      			if (k.equals("crsArrTime")|| (k.equals("crsDepTime")))
	      			{
	      				//System.out.println(obj.get(k).toString());
						ArrayList<String> split= this.split(obj.get(k).toString());
						for (String str:split)
						{
							arr[i]= Double.parseDouble(str);
							i++;
						}
						continue;
	      			}
	      			//Arrival Delay field is the label: 1- Delay. -1 : No Delay.
	      			else if (k.equals("arrDelay"))
	      			{	
	      				double label=Double.parseDouble(obj.get(k).toString());
	      				if (label>0)
	      					labelStr="1";
	      				else
	      					labelStr="-1";		
	      			}
	      			// Its a numeric field. Convert directly
	      			else
	      			{
	      					arr[i]= Double.parseDouble(obj.get(k).toString());	
	      			}
	      		}
    		}
        	catch(NullPointerException e)
			{
				//System.out.println("Null fields:"+k);
				if (k.equals("arrDelay"))
					labelStr="1";
				else
					arr[i]=0;
			}
			i++;
      			
      	}
    	
    	// Create a Dense vector from double array
    	Vector v1= new DenseVector(arr);
    	int size=v1.size();
    	// Initialize the custom class
    	MahoutVector mahoutVector = new MahoutVector();
    	//Assign the vector
    	mahoutVector.vector=v1;
    	// Assign the label
    	mahoutVector.label=labelStr;
    	return mahoutVector;
    }
    
    // Split the time to hh,mm,ss
        private ArrayList<String> split (String val)
    {
    	ArrayList<String> result= new ArrayList<String> ();
		String[] splitString = val.split(" ");
		splitString =splitString[3].split(":");
		for (int i=0;i<3;i++)
		{
			result.add(splitString[i]);
		}
		return result;
    }
}
 public class PreprocessMain
 {
    public static void main(String args[]) throws Exception 
    {
        if (args.length < 1) 
        {
            throw new IllegalArgumentException("Expected <bson filename> argument");
        }
        String filename = args[0];
        PreProcess bsonDump = new PreProcess();
        bsonDump.generatesequenceFile(filename);

    }

}
