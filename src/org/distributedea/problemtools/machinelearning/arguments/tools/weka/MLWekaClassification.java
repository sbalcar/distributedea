package org.distributedea.problemtools.machinelearning.arguments.tools.weka;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.filters.Filter;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;


public class MLWekaClassification {

	  /** the classifier used internally */
	  protected Classifier m_Classifier = null;
	  
	  /** the filter to use */
	  protected Filter m_Filter = null;

	  /** the training file */
	  protected File m_TrainingFile = null;

	  /** the training instances */
	  protected Instances m_Training = null;

	  /** for evaluating the classifier */
	  protected Evaluation m_Evaluation = null;
	  
	  
	  /**
	   * sets the classifier to use
	   * @param name        the classname of the classifier
	   * @param options     the options for the classifier
	   */
	  public void setClassifier(Class<?> classifier, String[] options) throws Exception {

		  m_Classifier = AbstractClassifier.forName(classifier.getName(), options);
	  }

	  /**
	   * sets the filter to use
	   * @param name        the classname of the filter
	   * @param options     the options for the filter
	   */
	  public void setFilter(Class<?> filter, String[] options) throws Exception {
	    m_Filter = (Filter) filter.newInstance();
	    if (m_Filter instanceof OptionHandler)
	      ((OptionHandler) m_Filter).setOptions(options);
	  }

	  /**
	   * sets the file to use for training
	   */
	  public void setTraining(File name) throws Exception {
	    m_TrainingFile = name;
	    m_Training = new Instances(new BufferedReader(
	    		new FileReader(m_TrainingFile)));
	    m_Training.setClassIndex(m_Training.numAttributes() - 1);
	  }

	  /**
	   * outputs some data about the classifier
	   */
	  public String toString() {
	    
		StringBuffer result = new StringBuffer();
	    result.append("Weka - Demo\n===========\n\n");

	    result.append("Classifier...: " 
	        + m_Classifier.getClass().getName() + " " 
	        + Utils.joinOptions(((AbstractClassifier) m_Classifier).getOptions())
	        + "\n");
	    if (m_Filter instanceof OptionHandler)
	      result.append("Filter.......: " 
	          + m_Filter.getClass().getName() + " " 
	          + Utils.joinOptions(((OptionHandler) m_Filter).getOptions()) + "\n");
	    else
	      result.append("Filter.......: "
	          + m_Filter.getClass().getName() + "\n");
	    result.append("Training file: " 
	        + m_TrainingFile + "\n");
	    result.append("\n");

	    result.append(m_Classifier.toString() + "\n");
	    result.append(m_Evaluation.toSummaryString() + "\n");
	    try {
	      result.append(m_Evaluation.toMatrixString() + "\n");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    try {
	      result.append(m_Evaluation.toClassDetailsString() + "\n");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    
	    return result.toString();
	  }

	  /**
	   * runs 10fold CV over the training file
	   */
	  public void execute() throws Exception {
	    // run filter
	    m_Filter.setInputFormat(m_Training);
	    Instances filtered = Filter.useFilter(m_Training, m_Filter);
	    
	    // train classifier on complete file for tree
	    m_Classifier.buildClassifier(filtered);
	    
	    // 10fold CV with seed=1
	    m_Evaluation = new Evaluation(filtered);
	    m_Evaluation.crossValidateModel(
	        m_Classifier, filtered, 10, m_Training.getRandomNumberGenerator(1));
	  }
	  
	  public double getErrorRate() {
		  
		  if (m_Evaluation == null) {
			  return Double.NaN;
		  }
		  
		  return m_Evaluation.errorRate();
	  }
	  
}
