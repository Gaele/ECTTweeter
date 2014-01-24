package sources2.classifiers;

import java.util.ArrayList;
import java.util.HashMap;

import sources2.AbstractManager;
import sources2.Classifier;
import sources2.Tweet;

public class ClassifierAsianEurope extends Classifier {

	public ClassifierAsianEurope() {
		super();
		// split into Asian / European
		// 0 => European, 1 => Asian
		NB_CLASSES_DERIVEES = 2;

		toDerivatedClasses = new Integer[11];
		toDerivatedClasses[0] = 1;// ARA
		toDerivatedClasses[1] = 1;// CHI
		toDerivatedClasses[2] = 0;// FRE
		toDerivatedClasses[3] = 0;// GER
		toDerivatedClasses[4] = 1;// HIN
		toDerivatedClasses[5] = 0;// ITA
		toDerivatedClasses[6] = 1;// JPN
		toDerivatedClasses[7] = 1;// KOR
		toDerivatedClasses[8] = 0;// SPA
		toDerivatedClasses[9] = 1;// TEL
		toDerivatedClasses[10] = 1;// TUR
	}

	/**
	 * Nationalities to integer
	 * @param polarite the nationality
	 * @return the nationality code
	 */
	@Override
	public Integer nti(final String polarite) {
		if(polarite.equals("ARA")) {
			return 1;
		} else if(polarite.equals("CHI")) {
			return 1;
		} else if(polarite.equals("FRE")) {
			return 0;
		} else if(polarite.equals("GER")) {
			return 0;
		} else if(polarite.equals("HIN")) {
			return 1;
		} else if(polarite.equals("ITA")) {
			return 0;
		} else if(polarite.equals("JPN")) {
			return 1;
		} else if(polarite.equals("KOR")) {
			return 1;
		} else if(polarite.equals("SPA")) {
			return 0;
		} else if(polarite.equals("TEL")) {
			return 1;
		} else if(polarite.equals("TUR")) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * integer to nationality
	 *
	 * @param polarity the nationality code
	 * @return the nationality String
	 */
	@Override
	public String itn(final Integer polarity) {
		switch(polarity) {
		case 0:
			return "EURO";
		case 1:
			return "ASI";
		default:
			return "???";
		}
	}

	@Override
	public boolean isUsable(final Tweet t) {
		return true;
	}

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
				continue;
			}
			localDatas.get(toDerivatedClasses[i]).addAll(classe);
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
