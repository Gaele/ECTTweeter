package sources2;

import java.util.Comparator;
import java.util.HashSet;

/**
 * Represents a piece of data which can be a Tweet, a text... It has 2 tags.
 * 
 * @author vincent
 * 
 */
public class Tweet {

	/**
	 * A unique identifier of the tweet in the corpus, by order of appearance.
	 */
	private final int id;

	/**
	 * The main information to analyse
	 */
	private final int polarite;

	/**
	 * The second information to analyse which can help us to forecast {@link #polarite}
	 */
	private final int marque;

	/**
	 * The datas used to forecast the {@link #polarite}
	 */
	private final Integer[] words;

	public Tweet(final int polarite, final int marque, final Integer[] w, int id) {
		this.id = id;
		this.polarite = polarite;
		this.marque = marque;
		// get unique words
		final HashSet<Integer> hs = new HashSet<Integer>();
		for (final Integer element : w) {
			hs.add(element);
		}
		this.words = new Integer[hs.size()];
		int cpt = 0;
		for (final Integer i : hs) {
			this.words[cpt] = i;
			cpt++;
		}

	}

	/**
	 * 
	 * @param w
	 * @return
	 */
	public boolean containWord(final Integer w) {
		for (final Integer word : this.words) {
			if (word == w) {
				return true;
			}
		}
		return false;
	}

	// **** GETTERS ****

	/**
	 * Return the {@link #polarite} of the tweet (which is the global polarity)
	 * 
	 * @return
	 */
	public int getPolarit() {
		return this.polarite;
	}

	/**
	 * Return the second tag {@link #marque} of the tweet (which is the global
	 * polarity)
	 * 
	 * @return
	 */
	public int getMarque() {
		return this.marque;
	}

	/**
	 * Return the words' code of the tweet
	 * 
	 * @return
	 */
	public Integer[] getWords() {
		return this.words;
	}

	/**
	 * Prints the tweet's info for debug
	 */
	@Override
	public String toString() {
		String s = "p: " + this.polarite + ", m: " + this.marque + ", ws: ";
		final int length = this.words.length;
		for (int i = 0; i < length; i++) {
			if (i != length - 1) {
				s += ", ";
			} else {
				s += " || ";
			}
		}
		return s;
	}

	// used to retrieve tweets by their appearance order in the corpus
	public static class MyComp implements Comparator<Tweet> {

		@Override
		public int compare(Tweet tweet1, Tweet tweet2) {
			if (tweet1.id < tweet2.id) {
				return -1;
			} else if (tweet1.id == tweet2.id) {
				return 0;
			} else {
				return 1;
			}
		}
	}

}
