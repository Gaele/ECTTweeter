import java.util.HashSet;

/**
 * Represents a Tweet
 * 
 * @author vvanhec
 * 
 */
public class Tweet {

	private int polarite;
	private String marque;
	private Integer[] words;

	public Tweet(final int polarite, final String marque, final Integer[] w) {
		this.polarite = polarite;
		this.marque = marque;

		// get unique words
		final HashSet<Integer> hs = new HashSet<Integer>();
		for (int i = 0; i < w.length; i++) {
			hs.add(w[i]);
		}
		this.words = new Integer[hs.size()];
		int cpt = 0;
		for (final Integer i : hs) {
			this.words[cpt] = i;
			cpt++;
		}

	}

	public int getPolarite() {
		return this.polarite;
	}

	public void setPolarite(final int polarite) {
		this.polarite = polarite;
	}

	public String getMarque() {
		return this.marque;
	}

	public void setMarque(final String marque) {
		this.marque = marque;
	}

	public Integer[] getWords() {
		return this.words;
	}

	public void setWords(final Integer[] words) {
		this.words = words;
	}

	@Override
	public String toString() {
		String s = "p: " + this.polarite + ", m: " + this.marque + ", ws: ";
		final int length = this.words.length;
		for (int i = 0; i < length; i++) {
			s += this.words[i];
			if (i != length - 1) {
				s += ", ";
			}
		}
		return s;
	}

}
