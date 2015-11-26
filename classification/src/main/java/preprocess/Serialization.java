package preprocess;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Serialization {
	public void serialize( HashMap<String,HashMap<String,Integer>> hmap)
	{
		 try
	     {
	            FileOutputStream fos =
	               new FileOutputStream("hashmap.ser");
	            ObjectOutputStream oos = new ObjectOutputStream(fos);
	            oos.writeObject(hmap);
	            oos.close();
	            fos.close();
	            System.out.printf("Serialized HashMap data is saved in hashmap.ser");
	     }
		catch(IOException ioe)
	      {
	            ioe.printStackTrace();
	      }
	}
	public HashMap<String,HashMap<String,Integer>> deserialize(String fileName)
	{
		HashMap<String,HashMap<String,Integer>> map = null;
		try
	      { 
	         FileInputStream fis = new FileInputStream("hashmap.ser");
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         map = (HashMap<String,HashMap<String,Integer>>) ois.readObject();
	         ois.close();
	         fis.close();
	      }
		catch(IOException ioe)
	      {
	         ioe.printStackTrace();
	      }
		catch(ClassNotFoundException c)
	      {
	         System.out.println("Class not found");
	         c.printStackTrace();
	      }
	      System.out.println("Deserialized HashMap..");
	      return map;
	}

}
