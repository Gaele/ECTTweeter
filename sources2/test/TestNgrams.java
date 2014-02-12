package sources2.test;

import java.util.ArrayList;

public class TestNgrams {

	public static void main(String[] args) {

		String[] tweet = "On récupère le code distant.".split(" ");
		String[] result = getNumbersFromWords(tweet, 2, 3);

		for (String ngram : result) {
			System.out.print(ngram + " ");
		}

	}

	public static String[] getNumbersFromWords(final String[] tweet, int n1, int n2) {

		final ArrayList<String> nGrams = new ArrayList<String>();

		// n-grams of words
		// number of n-grams of words in the tweet
		// = n (numberOfWords + numberOfWords - n + 1) / 2
		// = n (2 * numberOfWords - n + 1) / 2
		// example: n = 3, numberOfWords = 5 : n-grams = 5 + 4 + 3
		for (int i = 1; i <= n1; i++) {
			for (int j = 0; j < tweet.length - i + 1; j++) {
				String nGram = null;
				if (i == 1) {
					nGram = tweet[j];
				} else {
					// concatenation of the n-gram (without spaces between words)
					StringBuilder sb = new StringBuilder();
					for (int k = 0; k < i; k++) {
						sb.append(tweet[j + k]);
					}
					nGram = sb.toString();
				}
				nGrams.add(nGram);
			}
		}
		// n-grams of letters (each token considered separately, without spaces)
		if (n2 > 0) {
			for (String token : tweet) {
				final char[] tokenChar = token.toCharArray();
				for (int i = 1; i <= n2; i++) {
					for (int j = 0; j < tokenChar.length - i + 1; j++) {
						// concatenation of the n-gram
						StringBuilder sb = new StringBuilder();
						for (int k = 0; k < i; k++) {
							sb.append(tokenChar[j + k]);
						}
						final String nGram = sb.toString();
						nGrams.add(nGram);
					}
				}
			}
		}
		return nGrams.toArray(new String[nGrams.size()]);
	}
}
