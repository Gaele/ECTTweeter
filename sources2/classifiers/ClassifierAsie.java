package sources2.classifiers;

import java.util.ArrayList;
import java.util.HashMap;

import sources2.AbstractManager;
import sources2.Classifier;
import sources2.Tweet;

/**
 * Split datas among Asian languages into final Asian classes Concrete managers
 * are responsible for the management of the concrete local classes and
 * translation betweet local and global class.
 * 
 * @author vincent
 * 
 */
public class ClassifierAsie extends Classifier {

	public ClassifierAsie() {
		super();
		this.NB_CLASSES_DERIVEES = 7;

		this.toDerivatedClasses = new Integer[11];
		this.toDerivatedClasses[0] = 0;// ARA
		this.toDerivatedClasses[1] = 1;// CHI
		this.toDerivatedClasses[2] = -1;// FRE
		this.toDerivatedClasses[3] = -1;// GER
		this.toDerivatedClasses[4] = 2;// HIN
		this.toDerivatedClasses[5] = -1;// ITA
		this.toDerivatedClasses[6] = 3;// JPN
		this.toDerivatedClasses[7] = 4;// KOR
		this.toDerivatedClasses[8] = -1;// SPA
		this.toDerivatedClasses[9] = 5;// TEL
		this.toDerivatedClasses[10] = 6;// TUR
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
			return "HIN";
		case 3:
			return "JPN";
		case 4:
			return "KOR";
		case 5:
			return "TEL";
		case 6:
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
		if (t.getPolarit() == 0 || t.getPolarit() == 1 || t.getPolarit() == 4
				|| t.getPolarit() == 6 || t.getPolarit() == 7
				|| t.getPolarit() == 9 || t.getPolarit() == 10) {
			return true;
		}
		return false;
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
		// initialise new local data structure
		final ArrayList<ArrayList<Tweet>> localDatas = new ArrayList<ArrayList<Tweet>>();
		for (int j = 0; j < this.NB_CLASSES_DERIVEES; j++) {
			localDatas.add(new ArrayList<Tweet>());
		}
		// copy interesting parts of global datas
		for (final ArrayList<Tweet> classe : datas) {
			if (this.toDerivatedClasses[i] < 0) {
				i++;
				continue;
			}
			localDatas.get(this.toDerivatedClasses[i]).addAll(classe);
			// dictionnary management
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
		return localDatas;
	}

}
