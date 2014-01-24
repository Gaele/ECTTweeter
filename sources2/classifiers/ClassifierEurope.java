package sources2.classifiers;

import java.util.ArrayList;
import java.util.HashMap;

import sources2.AbstractManager;
import sources2.Classifier;
import sources2.Tweet;

/**
 * Split datas among European languages into final European classes
 * Concrete managers are responsible for the management of the concrete local classes and translation betweet local and global class.
 * @author vincent
 *
 */
public class ClassifierEurope extends Classifier {

	public ClassifierEurope() {
		super();
		NB_CLASSES_DERIVEES = 4;

		toDerivatedClasses = new Integer[11];
		toDerivatedClasses[0] = -1;// ARA
		toDerivatedClasses[1] = -1;// CHI
		toDerivatedClasses[2] = 0;// FRE
		toDerivatedClasses[3] = 1;// GER
		toDerivatedClasses[4] = -1;// HIN
		toDerivatedClasses[5] = 2;// ITA
		toDerivatedClasses[6] = -1;// JPN
		toDerivatedClasses[7] = -1;// KOR
		toDerivatedClasses[8] = 3;// SPA
		toDerivatedClasses[9] = -1;// TEL
		toDerivatedClasses[10] = -1;// TUR
	}

	/*
	 * (non-Javadoc)
	 * @see sources2.Classifier#nti(java.lang.String)
	 */
	@Override
	public Integer nti(final String polarite) {
		if(polarite.equals("FRE")) {
			return 0;
		} else if(polarite.equals("GER")) {
			return 1;
		} else if(polarite.equals("ITA")) {
			return 2;
		} else if(polarite.equals("SPA")) {
			return 3;
		} else {
			return -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see sources2.Classifier#itn(java.lang.Integer)
	 */
	@Override
	public String itn(final Integer polarity) {
		switch(polarity) {
		case 0:
			return "FRE";
		case 1:
			return "GER";
		case 2:
			return "ITA";
		case 3:
			return "SPA";
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
		if(t.getPolarite() == 2 || t.getPolarite() == 3 || t.getPolarite() == 5 || t.getPolarite() == 8) {
			return true;
		}
		return false;
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
		// copy interesting parts of global datas
		for(final ArrayList<Tweet> classe : datas) {
			if(toDerivatedClasses[i] < 0) {
				i++;
				continue;
			}
			localDatas.get(toDerivatedClasses[i]).addAll(classe);
			// dictionnary management
			for(final Tweet t : classe) {
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
		return localDatas;
	}

}
