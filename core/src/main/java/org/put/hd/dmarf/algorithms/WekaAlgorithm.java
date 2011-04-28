package org.put.hd.dmarf.algorithms;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.put.hd.dmarf.data.DataRepresentationBase;

import weka.associations.Apriori;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ProtectedProperties;

/**
 * Implementation of A Priori algorithm using Weka tool. Used for verification
 * purposes.
 * 
 * @author Piotr
 * 
 */
public class WekaAlgorithm implements IAlgorithm {

	private List<Rule> listOfRules;
	private Apriori apriori;
	private FastVector decisions;

	public WekaAlgorithm(){
		
		// decision about the products
		decisions = new FastVector();
		decisions.addElement("0");
		decisions.addElement("1");
	}
	
	public long getElapsedTimeOverall() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getElapsedTimeGeneration() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void start(DataRepresentationBase data, double minSupport,
			double minCredibility) {

		FastVector attributes = new FastVector();

		// generate vector from attributes (should be sorting somewhere here
		// made)
		int[] vector = new int[data.getAttributesCounter().keySet().size()];
		Iterator<Integer> it = data.getAttributesCounter().keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			vector[i] = it.next().intValue();
		}

		// elements in the vector must be sorted !
		Arrays.sort(vector);

		// numeric attribute
		for (int j = 0; j < vector.length; j++) {
			Integer element = new Integer(vector[j]);
			attributes.addElement(new Attribute(element.toString(),decisions));
		}

		// generate data from data representation
		Instances wekaData = new Instances("Data", attributes, 0);

		// adding the instance class
		for (List<Integer> transaction : data.getTransactionsList()) {

			double[] values = new double[wekaData.numAttributes()];

			// add elements to instance from the transaction on the proper place
			for (int j = 0; j < values.length; j++) {
				if (transaction.contains(j + 1)) {
					values[j] = decisions.indexOf("1");
				} else {
					values[j] = decisions.indexOf("0");
				}
			}

			Instance instance = new Instance(1.0, values);
			wekaData.add(instance);

		}

		System.out.println(wekaData);

		// run apriori algorithm and save the results
		apriori = new Apriori();
		apriori.setLowerBoundMinSupport(minSupport);

		// no upper bound for support
		apriori.setUpperBoundMinSupport(1);

		// this is confidence parameter - really
		apriori.setMinMetric(minCredibility);
		try {
			apriori.buildAssociations(wekaData);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Rule> getRules() {
		listOfRules = new LinkedList<Rule>();

		return listOfRules;
	}

}
