package org.put.hd.dmarf.data.utils;

import java.util.List;

/**
 * Utility class for creating the binary data from some other form of
 * representation.
 * @author Ankhazam
 *
 */
public class BinaryUtils {

	/**
	 * Transforms a list of attributes into CharBitMap representation.
	 * 
	 * @param transaction
	 *            Sorted list of attributes represented by integers.
	 * @param maxAttAligned
	 *            Greatest attribute number aligned to char representation.
	 * @return Char[] array with full length.
	 */
	public static char[] generateCharArray(List<Integer> transaction,
			int maxAttAligned) {

		char[] transactionCharArray = new char[maxAttAligned / 16];

		for (int att : transaction) {
			transactionCharArray[(att - 1) / 16] |= (1 << (((att % 16)) == 0 ? 0
					: 16 - (att % 16)));
		}
		return transactionCharArray;
	}

	/**
	 * Generates the whole CharMap for all transactions
	 * 
	 * @param transactions
	 *            List<List<Integer>> of all transactions. These must not be
	 *            empty.
	 * @param numberOfAttributesClusters
	 *            acquired from DataRepresentationBase. This must be >0
	 * @return the charMap :)
	 */
	public static char[] generateTransactionsCharMap(
			List<List<Integer>> transactionsLists,
			int numberOfAttributesClusters) {

		if (numberOfAttributesClusters == 0 || transactionsLists.size() == 0)
			throw new RuntimeException("Cannot work on empty transactions set.");

		char[] charMap = new char[transactionsLists.size()
				* numberOfAttributesClusters];

		// here comes the TransactionsByteMap population magic
		int transIdx = 0;
		for (List<Integer> transaction : transactionsLists) {
			System.arraycopy(
					generateCharArray(transaction,
							numberOfAttributesClusters * 16), 0, charMap,
					transIdx * numberOfAttributesClusters,
					numberOfAttributesClusters);
			transIdx++;
		}
		return charMap;
	}
}
