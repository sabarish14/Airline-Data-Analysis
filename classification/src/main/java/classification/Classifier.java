package classification;

import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.classifier.AbstractVectorClassifier;
import org.apache.mahout.classifier.naivebayes.ComplementaryNaiveBayesClassifier;
import org.apache.mahout.classifier.naivebayes.NaiveBayesModel;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;
import org.apache.mahout.math.Vector;
import preprocess.MahoutVector;

// Classifier class that trains and tests a model
public class Classifier
{
	public static void train() throws Throwable
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

		/*String csvPath = "/home/jossef/Desktop/kdd/KDDTest+.csv";
		
	    CsvToVectors csvToVectors = new CsvToVectors(csvPath);
	    
	    List<MahoutVector> vectors = csvToVectors.vectorize();
	    
	    
	    int total = 0;
	    int success = 0;
	    
	    for (MahoutVector mahoutVector : vectors)
	    {
	    	Vector prediction = classifier.classifyFull(mahoutVector.vector);
	    	
	    	// They sorted alphabetically 
	    	// 0 = anomaly, 1 = normal (because 'anomaly' > 'normal') 
	    	double anomaly = prediction.get(0);
	    	double normal = prediction.get(1);
	    	
	    	String predictedClass = "anomaly";
	    	if (normal > anomaly)
	    	{
	    		predictedClass="normal";
	    	}

	    	if (predictedClass.equals(mahoutVector.classifier))
	    	{
	    		success++;
	    	}
	    	
	    	total ++;
	    }
	    
	    System.out.println(total + " : " + success + " : " + (total - success) + " " + ((double)success/total));
	    
	    
	    */

	    
		//StandardNaiveBayesClassifier classifier = new StandardNaiveBayesClassifier();
		
	}

	public static void main(String[] args) throws Throwable
	{
		
		train();
	}


}
