package sources2.test;

import java.util.ArrayList;

public class TestNgrams {

	public static void main(String[] args) {

		String[] tweet = "On récupère le code distant et on le fusionne avec la dernière version en local.".split(" ");
		String[] result = getNumbersFromWords(tweet, 3, false);

		for (String ngram : result) {
			System.out.print(ngram + " ");
		}

	}

	public static String[] getNumbersFromWords(final String[] tweet, int n, boolean letterNgram) {

		final int numberOfWords = tweet.length;

		// n-grams of words
		if (letterNgram == false) {
			// number of n-grams of words in the tweet
			// = n (numberOfWords + numberOfWords - n + 1) / 2
			// = n (2 * numberOfWords - n + 1) / 2
			// example : n = 3, numberOfWords = 5 : n-grams = 5 + 4 + 3
			final int numberOfNGrams = n * (2 * numberOfWords - n + 1) / 2;
			final String[] nGrams = new String[numberOfNGrams];

			int cpt = 0;
			for (int i = 1; i <= n; i++) {
				for (int j = 0; j < tweet.length - i + 1; j++) {
					// concatenation of the n-gram (without spaces between words)
					StringBuilder sb = new StringBuilder();
					for (int k = 0; k < i; k++) {
						sb.append(tweet[j + k]);
					}
					final String nGram = sb.toString();
					nGrams[cpt] = nGram;
					cpt++;
				}
			}
			return nGrams;
		}
		// n-grams of letters (each token considered separately, without spaces)
		else {
			final ArrayList<String> nGrams = new ArrayList<String>();
			for (String token : tweet) {
				final char[] tokenChar = token.toCharArray();
				for (int j = 0; j < tokenChar.length - n + 1; j++) {
					// concatenation of the n-gram
					StringBuilder sb = new StringBuilder();
					for (int k = 0; k < n; k++) {
						sb.append(tokenChar[j + k]);
					}
					final String nGram = sb.toString();
					nGrams.add(nGram);
				}
			}
			return nGrams.toArray(new String[nGrams.size()]);
		}
	}
}
