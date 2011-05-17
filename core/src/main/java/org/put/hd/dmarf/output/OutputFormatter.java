package org.put.hd.dmarf.output;

import java.util.List;

import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.algorithms.Rule;

/**
 * Simple class for formatting the output of the algorithm runs. The formatted
 * output resembles the final output, without the two parameters: 1. Pamieć
 * which is written as PPP 2. Całkowity czas which is written as TTT
 * 
 * @author Piotr
 * 
 */
public class OutputFormatter {

	private String inputFileName;
	private IAlgorithm algorithm;
	private double minCredibility;
	private double minSupport;

	private final char lineSeparator = '\n';
	private StringBuilder builder;
	private double totalTime;

	/**
	 * Get the formatted output from the passed values.
	 * 
	 * @return
	 */
	public String getFormattedOutputString() {

		List<Rule> rules = algorithm.getRules();

		builder = new StringBuilder();
		// inputFileName
		append("Dane: " + inputFileName);

		// minimal support
		append("Min sup: " + minSupport);

		// minimal confidence
		append("Min conf: " + minCredibility);

		// number of rules
		append("No of rules: " + rules.size());

		append("Pamiec: " + "PPP");
		append("Calkowity czas: " + totalTime);
		append("Czas bez we/wy: " + algorithm.getElapsedTimeOverall());
		append("Czas gen. zb. czestych: "
				+ algorithm.getElapsedTimeGeneration());

		builder.append(lineSeparator);

		append("**RULES");

		for (Rule rule : rules) {
			appendRule(rule);
		}

		return builder.toString();
	}

	private void appendRule(Rule rule) {

		// creating the first line
		// no: ( item_1 AND item_2 ...AND...item_n) => ( item_m )
		builder.append(rule.getId());
		builder.append(": ( ");

		List<Integer> itemsConditional = rule.getConditionalPart();
		appendItems(itemsConditional);

		builder.append(") => ( ");

		List<Integer> itemsExecutive = rule.getExecutivePart();
		appendItems(itemsExecutive);

		builder.append(")");
		builder.append(lineSeparator);
		

		// Support:
		append("Support: " + rule.getSupport());

		// Confidence:
		append("Confidance: " + rule.getConfidance());
	}

	private void appendItems(List<Integer> items) {
		for (int i = 0; i < items.size(); i++) {
			builder.append(items.get(i) + " ");
			if (i + 1 != items.size())
				builder.append(" AND ");
		}
	}

	private void append(String string) {
		builder.append(string);
		builder.append(lineSeparator);
	}

	public void setMinSupport(double minSupport) {
		this.minSupport = minSupport;
	}

	public void setMinCredibility(double minCredibility) {
		this.minCredibility = minCredibility;

	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;

	}

	public void setAlgorithm(IAlgorithm algorithm) {
		this.algorithm = algorithm;

	}

	public void setTotalTime(double d) {
		this.totalTime = d;
	}

}
