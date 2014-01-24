package sources2.classifiers;

import java.util.ArrayList;
import java.util.HashSet;

import sources2.AbstractManager;
import sources2.Classifier;
import sources2.Tweet;

public class ClassifierSimple extends Classifier {

	public ClassifierSimple() {
		super();
		NB_CLASSES_DERIVEES = 11;

		toDerivatedClasses = new Integer[11];
		for(int i=0; i<11; i++) {
			toDerivatedClasses[i] = i;
		}

	}

	/**
	 * Nationalities to integer
	 * @param polarite the nationality
	 * @return the nationality code
	 */
	@Override
	public Integer nti(final String polarite) {
		if(polarite.equals("ARA")) {
			return 0;
		} else if(polarite.equals("CHI")) {
			return 1;
		} else if(polarite.equals("FRE")) {
			return 2;
		} else if(polarite.equals("GER")) {
			return 3;
		} else if(polarite.equals("HIN")) {
			return 4;
		} else if(polarite.equals("ITA")) {
			return 5;
		} else if(polarite.equals("JPN")) {
			return 6;
		} else if(polarite.equals("KOR")) {
			return 7;
		} else if(polarite.equals("SPA")) {
			return 8;
		} else if(polarite.equals("TEL")) {
			return 9;
		} else if(polarite.equals("TUR")) {
			return 10;
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

	@Override
	public boolean isUsable(final Tweet t) {
		return true;
	}

	@Override
	public ArrayList<ArrayList<Tweet>> preTraitement(final AbstractManager man,
			final ArrayList<ArrayList<Tweet>> datas, final HashSet<Integer> localDictionary) {
		localDictionary.addAll(man.getDictionary().values());
		return datas;
	}

}
