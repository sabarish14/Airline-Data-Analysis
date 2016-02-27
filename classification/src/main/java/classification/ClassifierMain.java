package classification;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.classifier.naivebayes.NaiveBayesModel;
import org.apache.mahout.classifier.naivebayes.StandardNaiveBayesClassifier;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;
import org.apache.mahout.math.Vector;

import preprocess.CsvToVectors;
import preprocess.FileRead;
import preprocess.FileWrite;
import preprocess.MahoutVector;

// Classifier class that trains and tests a model
class Classifier
{
	
	
	public StandardNaiveBayesClassifier train() throws Throwable
	{
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.getLocal(conf);
		
		
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
		naiveBayesModel.labelWeight(0);
		//System.out.println(naiveBayesModel.featureWeight(0));
		StandardNaiveBayesClassifier classifier = new StandardNaiveBayesClassifier(naiveBayesModel);	
		return classifier;
	}
		
	void test(StandardNaiveBayesClassifier classifier)
	{
		String csvPath = "test.csv";
		FileWrite fw= new FileWrite ("label.csv");
		CsvToVectors csvToVectors = new CsvToVectors(csvPath);
	   	List<MahoutVector> vectors = csvToVectors.vectorize();  
	   	List<String> predictions = new  ArrayList<String> ();
	    int total = 0;
	    int success = 0;	  
	    predictions.add("prediction");
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
	    	predictions.add(predictedClass);
	    	if (predictedClass.equals(mahoutVector.label))
	    	{
	    		success++;
	    	}
	    	
	    	total ++;
	    }
	    double accuracy=(double)success/total;
	    FileRead fr= new FileRead("test.csv");
	    List<String[]> csvContent = fr.readCSV();
	    fw.csvAppend( csvContent,predictions);
	    fw.close();
	    System.out.println(total + " : " + success + " : " + (total - success) + " " + accuracy);
	}
}
	public class ClassifierMain
	{
		public static void main(String[] args) throws Throwable
		{
			Classifier c=new Classifier();
			//String sequenceFile = "sequence";
			//Path seqFilePath = new Path(sequenceFile);
			//readSeqFile(seqFilePath);
			StandardNaiveBayesClassifier classifier=c.train();
			c.test(classifier);
		}
	}



