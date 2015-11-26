package classification;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.classifier.AbstractVectorClassifier;
import org.apache.mahout.classifier.naivebayes.ComplementaryNaiveBayesClassifier;
import org.apache.mahout.classifier.naivebayes.NaiveBayesModel;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import preprocess.CsvToVectors;
import preprocess.MahoutVector;

// Classifier class that trains and tests a model
public class Classifier
{
	private static void readSeqFile(Path pathToFile) throws IOException 
	{
		  Configuration conf = new Configuration();
		  FileSystem fs = FileSystem.get(conf);

		  SequenceFile.Reader reader = new SequenceFile.Reader(fs, pathToFile, conf);

		  Text key = new Text(); 
		  VectorWritable val = new VectorWritable(); 
		  int i=0;
		  while (reader.next(key, val)) 
		  {
		    System.out.println(key + ":" + val);
		    if (i>5)
		    	break;
		    i++;
		  }
		  reader.close();
		  
	}
	
	public static  void train() throws Throwable
	{
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.getLocal(conf);
		Path seqFilePath = new Path("sequence");
		
		TrainNaiveBayesJob trainNaiveBayes = new TrainNaiveBayesJob();
		trainNaiveBayes.setConf(conf);
		
		String sequenceFile = "sequence";
		String outputDirectory = "output";
		String tempDirectory = "temp";
		
		fs.delete(new Path(outputDirectory),true);
		fs.delete(new Path(tempDirectory),true);
		
		trainNaiveBayes.run(new String[] { "--input", sequenceFile, "--output", outputDirectory, "-el", "--overwrite", "--tempDir", tempDirectory });
		
		// Train the classifier
		NaiveBayesModel naiveBayesModel = NaiveBayesModel.materialize(new Path(outputDirectory), conf);

		System.out.println("features: " + naiveBayesModel.numFeatures());
		System.out.println("labels: " + naiveBayesModel.numLabels());
		
	    AbstractVectorClassifier classifier = new ComplementaryNaiveBayesClassifier(naiveBayesModel);

		String csvPath = "test1.csv";
		
	    CsvToVectors csvToVectors = new CsvToVectors(csvPath);
	    
	    List<MahoutVector> vectors = csvToVectors.vectorize();
	    
	    
	    int total = 0;
	    int success = 0;
	    
	    for (MahoutVector mahoutVector : vectors)
	    {
	    	Vector prediction = classifier.classifyFull(mahoutVector.vector);    
	    	// They sorted alphabetically 
	    	// -1 = noDelay, 1 = delay 
	    	double noDelayScore= prediction.get(0);
	    	double delayScore = prediction.get(1);
	    	
	    	String predictedClass = "1";
	    	if (noDelayScore > delayScore)
	    	{
	    		predictedClass="-1";
	    	}

	    	if (predictedClass.equals(mahoutVector.label))
	    	{
	    		success++;
	    	}
	    	
	    	total ++;
	    }
	    double accuracy=(double)success/total;
	    System.out.println(total + " : " + success + " : " + (total - success) + " " + accuracy);
	}

	public static void main(String[] args) throws Throwable
	{
		String sequenceFile = "sequence";
		Path seqFilePath = new Path(sequenceFile);
		
		train();
		readSeqFile(seqFilePath);
		
	}


}
