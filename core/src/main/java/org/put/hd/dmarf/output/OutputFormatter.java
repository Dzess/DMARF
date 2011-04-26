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
		append("Calkowity czas: " + "TTT");
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
		// TODO: write rules formatting
		// numer reguly: ( item_1 AND item_2 ...AND...item_n) => ( item_m )
		// Support: wartosc wsparcia reguly
		// Confidence: wartosci ufnosci reguly
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

}
