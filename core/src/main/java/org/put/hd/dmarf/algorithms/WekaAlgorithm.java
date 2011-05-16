package org.put.hd.dmarf.algorithms;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.stopwatches.IStopWatch;
import org.put.hd.dmarf.stopwatches.StopWatch;

import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.associations.Item;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

/**
 * Implementation of Apriori algorithm using Weka tool. Used for verification
 * purposes.
 * 
 * @author Piotr
 * 
 */
public class WekaAlgorithm implements IAlgorithm {

	private List<Rule> listOfRules;
	private Apriori apriori;
	private FastVector<String> decisions;
	private FastVector<Attribute> attributes;
	private Instances wekaData;

	private IStopWatch stopWatch;

	public WekaAlgorithm() {

		// decision about the products
		decisions = new FastVector<String>();
		//decisions.addElement("0")
		decisions.addElement("1");

		// create apriori
		apriori = new Apriori();

		stopWatch = new StopWatch();
	}

	public long getElapsedTimeOverall() {
		return stopWatch.getElapsedTimeSecs();
	}

	public long getElapsedTimeGeneration() {
		return stopWatch.getElapsedTimeSecs();
	}

	public void start(DataRepresentationBase data, double minSupport,
			double minCredibility) {

		stopWatch.start();

		generateAttributes(data);

		// generate data from data representation
		wekaData = generateData(data);

		apriori.setLowerBoundMinSupport(minSupport);

		// no upper bound for support
		apriori.setUpperBoundMinSupport(1);

		// this is confidence parameter - really
		apriori.setMinMetric(minCredibility);

		// run apriori algorithm and save the results
		try {
			apriori.buildAssociations(wekaData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		stopWatch.stop();
	}

	private Instances generateData(DataRepresentationBase data) {
		Instances wekaData = new Instances("Data", attributes, 0);

		// adding the instance class
		for (List<Integer> transaction : data.getTransactionsList()) {

			double[] values = new double[wekaData.numAttributes()];

			// add elements to instance from the transaction on the proper place
			for (int j = 0; j < values.length; j++) {
				if (transaction.contains(j + 1)) {
					values[j] = decisions.indexOf("1");
				} else {
					//values[j] = decisions.indexOf("0");
					values[j] = -1;
				}
			}

			Instance instance = new DenseInstance(1.0, values);
			wekaData.add(instance);

		}
		return wekaData;
	}

	private void generateAttributes(DataRepresentationBase data) {
		attributes = new FastVector<Attribute>();

		// generate vector from attributes (should be sorting somewhere here
		// made)
		int[] vector = new int[data.getAttributesCounter().keySet().size()];
		Iterator<Integer> it = data.getAttributesCounter().keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			vector[i] = it.next().intValue();
			i++;
		}

		// elements in the vector must be sorted !
		Arrays.sort(vector);

		// nominal attribute is required for apriori
		for (int j = 0; j < vector.length; j++) {
			Integer element = new Integer(vector[j]);
			attributes.addElement(new Attribute(element.toString(), decisions));
		}
	}

	public List<Rule> getRules() {

		listOfRules = new LinkedList<Rule>();
		AssociationRules rules = apriori.getAssociationRules();
		int i = 0;
		for (AssociationRule rule : rules.getRules()) {

			Collection<Item> premise = rule.getPremise();
			List<Integer> conditional = new LinkedList<Integer>();
			getRulePart(premise, conditional);

			Collection<Item> consequence = rule.getConsequence();
			List<Integer> executive = new LinkedList<Integer>();
			getRulePart(consequence, executive);

			int confidance = 0;
			try {
				confidance = (int) (rule.getMetricValuesForRule()[0] * 100);
			} catch (Exception e) {
				// this code should throw up if something is bad
				e.printStackTrace();
			}

			Rule dmarfRule = new Rule(i++, conditional, executive, confidance,
					rule.getTotalSupport());

			listOfRules.add(dmarfRule);
		}

		return listOfRules;
	}

	private void getRulePart(Collection<Item> premise, List<Integer> conditional) {
		for (Item item : premise) {
			// total slow conversion
			String s = item.getAttribute().name();
			Integer element = Integer.parseInt(s);
			conditional.add(element);
		}
	}

}
