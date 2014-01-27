package sources2.classifiers;

import java.util.ArrayList;
import java.util.HashMap;

import sources2.AbstractManager;
import sources2.Classifier;
import sources2.Tweet;

/**
 * Split datas directly final classes Concrete managers are responsible for the
 * management of the concrete local classes and translation betweet local and
 * global class.
 * 
 * @author vincent
 * 
 */
public class ClassifierSimple extends Classifier {

	public ClassifierSimple() {
		super();
		this.NB_CLASSES_DERIVEES = 11;

		this.toDerivatedClasses = new Integer[11];
		for (int i = 0; i < 11; i++) {
			this.toDerivatedClasses[i] = i;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sources2.Classifier#itn(java.lang.Integer)
	 */
	@Override
	public String itn(final Integer polarity) {
		switch (polarity) {
		case 0:
			return "ARA";
		case 1:
			return "CHI";
		case 2:
			return "FRE";
		case 3:
			return "GER";
		case 4:
			return "HIN";
		case 5:
			return "ITA";
		case 6:
			return "JPN";
		case 7:
			return "KOR";
		case 8:
			return "SPA";
		case 9:
			return "TEL";
		case 10:
			return "TUR";
		default:
			return "???";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sources2.Classifier#isUsable(sources2.Tweet)
	 */
	@Override
	public boolean isUsable(final Tweet t) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sources2.Classifier#preTraitement(sources2.AbstractManager,
	 * java.util.ArrayList, java.util.HashMap)
	 */
	@Override
	public ArrayList<ArrayList<Tweet>> preTraitement(final AbstractManager man,
			final ArrayList<ArrayList<Tweet>> datas,
			final HashMap<Integer, Integer> localDictionary) {
		int i = 0;
		for (final ArrayList<Tweet> classe : datas) {
			if (this.toDerivatedClasses[i] < 0) {
				i++;
				continue;
			}
			for (final Tweet t : classe) {
				for (final Integer word : t.getWords()) {
					final Integer res = localDictionary.get(word);
					if (res == null) {
						localDictionary.put(word, this.nextLocalId);
						this.nextLocalId++;
					}
				}
			}
			i++;
		}
		return datas;
	}

}
