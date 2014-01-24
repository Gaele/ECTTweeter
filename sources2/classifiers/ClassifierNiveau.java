package sources2.classifiers;

import java.util.ArrayList;
import java.util.HashMap;

import sources2.AbstractManager;
import sources2.Classifier;
import sources2.Tweet;

/**
 * Split datas directly final classes
 * Concrete managers are responsible for the management of the concrete local classes and translation betweet local and global class.
 * @author vincent
 *
 */
public class ClassifierNiveau extends Classifier {

	public ClassifierNiveau() {
		super();
		NB_CLASSES_DERIVEES = 3;

		toDerivatedClasses = new Integer[3];
		toDerivatedClasses[0] = 0;// low
		toDerivatedClasses[1] = 1;// medium
		toDerivatedClasses[2] = 2;// high
	}

	/*
	 * (non-Javadoc)
	 * @see sources2.Classifier#itn(java.lang.Integer)
	 */
	@Override
	public String itn(final Integer polarity) {
		switch(polarity) {
		case 0:
			return "low";
		case 1:
			return "medium";
		case 2:
			return "high";
		default:
			return "???";
		}
	}

	/*
	 * (non-Javadoc)
	 * @see sources2.Classifier#isUsable(sources2.Tweet)
	 */
	@Override
	public boolean isUsable(final Tweet t) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see sources2.Classifier#preTraitement(sources2.AbstractManager, java.util.ArrayList, java.util.HashMap)
	 */
	@Override
	public ArrayList<ArrayList<Tweet>> preTraitement(final AbstractManager man,
			final ArrayList<ArrayList<Tweet>> datas, final HashMap<Integer, Integer> localDictionary) {
		int i=0;
		// initialise new local data structure
		final ArrayList<ArrayList<Tweet>> localDatas = new ArrayList<ArrayList<Tweet>>();
		for(int j=0; j<NB_CLASSES_DERIVEES; j++) {
			localDatas.add(new ArrayList<Tweet>());
		}
		for(final ArrayList<Tweet> classe : datas) {
			for(final Tweet t : classe) {
				localDatas.get(toDerivatedClasses[t.getMarque()]).add(t);
				for(final Integer word : t.getWords()) {
					final Integer res = localDictionary.get(word);
					if(res == null) {
						localDictionary.put(word, nextLocalId);
						nextLocalId++;
					}
				}
			}
			i++;
		}
		//		System.out.println(datas.size());
		return localDatas;
	}

	/*
	 * (non-Javadoc)
	 * @see sources2.Classifier#getTag(sources2.Tweet)
	 */
	@Override
	public int getTag(final Tweet t) {
		return t.getMarque();
	}

}
