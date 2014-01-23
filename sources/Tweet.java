package sources;

import java.util.HashMap;
import java.util.Map.Entry;
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
	
	//	private Integer[] words;
	HashMap<Integer, Boolean> map; ////////////////////


	public Tweet(final int polarite, final String marque, final Integer[] w) {
		this.polarite = polarite;
		this.marque = marque;

		// get unique word with map
		map = new HashMap<Integer, Boolean>(); ////////////////////////
		for (final Integer element : w) {
			if(!map.containsKey(element)) {
				map.put(element, true);
			}
		}

		// Check for doubles
		// => it seems that it's ok
		//		final LinkedList<Integer> list = new LinkedList<Integer>();
		//		for(final Entry<Integer, Boolean> word : map.entrySet()) {
		//			if(list.contains(word.getKey())) {
		//				System.out.println("DOUBLE !!!!");
		//			}
		//			list.add(word.getKey());
		//		}
		//		list.clear();


		// get unique words
		//		final HashSet<Integer> hs = new HashSet<Integer>();
		//		for (final Integer element : w) {
		//			hs.add(element);
		//		}
		//		words = new Integer[hs.size()];
		//		int cpt = 0;
		//		for (final Integer i : hs) {
		//			words[cpt] = i;
		//			cpt++;
		//		}

	}

	public boolean containWord(final Integer w) {
		return map.containsKey(w);
	}

	// **** GETTERS ****

	public int getPolarite() {
		return polarite;
	}

	public void setPolarite(final int polarite) {
		this.polarite = polarite;
	}

	public String getMarque() {
		return marque;
	}

	public void setMarque(final String marque) {
		this.marque = marque;
	}

	public Integer[] getWords() {
		final Integer[] words = new Integer[map.size()];
		Integer cpt = 0;
		for(final Entry<Integer, Boolean> word : map.entrySet()) {
			words[cpt] = word.getKey();
			cpt++;
		}
		return words;
	}

	public void setWords(final Integer[] words) {
		//		this.words = words;
		map.clear();
		for (final Integer element : words) {
			if(!map.containsKey(element)) {
				map.put(element, true);
			}
		}
	}

	@Override
	public String toString() {
		String s = "p: " + polarite + ", m: " + marque + ", ws: ";
		//		final int length = words.length;
		final int length = map.size();
		int i = 0;
		for(final Entry<Integer, Boolean> word : map.entrySet()) {
			s+= word.getKey();
			if (i != length - 1) {
				s += ", ";
			} else {
				s += " || ";
			}
			i++;
		}

		//		for (int i = 0; i < length; i++) {
		//			//			s += words[i];C
		//			if (i != length - 1) {
		//				s += ", ";
		//			} else {
		//				s += " || ";
		//			}
		//		}
		return s;
	}

}
