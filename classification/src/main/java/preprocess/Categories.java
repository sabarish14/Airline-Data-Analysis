package preprocess;
import java.util.*;
import org.bson.*;
import java.io.*;
public class Categories
{
	
	public ArrayList<String> findCategories()
	{
		FileRead f = new FileRead("test_categories.txt");
		ArrayList<String> columns = f.read();
		return columns;
	}
	public HashMap<String,HashMap<String,Integer>>  categorize(InputStream stream)
	{
		FileRead f = new FileRead("categories.txt");
		ArrayList<String> columns = f.read();
		BSONDecoder decoder = new BasicBSONDecoder();
		HashMap<String,HashMap<String,Integer>> map=new HashMap<String,HashMap<String,Integer>>();
		try 
		{
			int i=0;
		    while (stream.available() > 0) 
		    {
                	BSONObject obj = decoder.readObject(stream);
					for (String k:columns)
					{
						String val=obj.get(k).toString();
						if (map.containsKey(k))
						{
							HashMap<String, Integer> nestedMap=map.get(k);
							if (!nestedMap.containsKey(val))
							{
								nestedMap.put(val,nestedMap.size()+1);
							}
						}
						else
						{
							HashMap<String, Integer> nestedMap = new HashMap<String, Integer> ();
							nestedMap.put(val,1);
							map.put(k,nestedMap);
						}
					}
				   	 i++;
					 if (i>10000)
					 	break;
			
				    }
		}
		catch (IOException e)
		{
		}
		Serialization s=new Serialization();
		s.serialize(map);
		return map;
	}
}

