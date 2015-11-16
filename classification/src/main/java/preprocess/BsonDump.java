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
import org.apache.mahout.math.*;

public class BsonDump 
{

    Categories c;
    public void vectorize(String filename) throws FileNotFoundException 
    {
    	c=new Categories();
	String columnFile="columns.txt";
	FileOps f=new FileOps(columnFile);
	ArrayList<String> columns=f.read();
	System.out.println("columns:"+columns.toString());
        File file = new File(filename);
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
	InputStream inputStream1 = new BufferedInputStream(new FileInputStream(file));
	List<NamedVector> vector = new LinkedList<NamedVector>();
        BSONDecoder decoder = new BasicBSONDecoder();
	HashMap<String,HashMap<String,Integer>> map=c.categorize(inputStream1);
        int count = 0;
        try 
	{
            while (inputStream.available() > 0) 
	    {
	        NamedVector v1;
		double[] arr=new double[columns.size()+4];
		int i=0;
                BSONObject obj = decoder.readObject(inputStream);
		for (String k:columns)
		{
			 if (map.containsKey(k))
			 	arr[i]=(double)(map.get(k).get(obj.get(k).toString()));
	      		 else
			 {
			 	if (k.equals("crsArrTime")|| (k.equals("crsDepTime")))
				{
					ArrayList<String> split= this.split(obj.get(k).toString());
					for (String str:split)
					{
						arr[i]= Double.parseDouble(str);
						i++;
					}
					System.out.println(k+":"+arr[i]);
					continue;
				}
				else
			 		arr[i]= Double.parseDouble(obj.get(k).toString());
			}
			 System.out.println(k+":"+arr[i]);
			 i++;
		}
		v1=new NamedVector(new DenseVector(arr),String.valueOf(count));
                count++;
		vector.add(v1);
		break;
            }
        }
	catch (IOException e) 
	{
            e.printStackTrace();
        }
	finally
	{
            try
	    {
                inputStream.close();
            } 
	    catch (IOException e) 
	    {
            }
        }
        System.err.println(String.format("%s objects read", count));
    }

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

    public static void main(String args[]) throws Exception 
    {

        if (args.length < 1) 
	{
            throw new IllegalArgumentException("Expected <bson filename> argument");
        }
        String filename = args[0];
        BsonDump bsonDump = new BsonDump();
        bsonDump.vectorize(filename);

    }

}
