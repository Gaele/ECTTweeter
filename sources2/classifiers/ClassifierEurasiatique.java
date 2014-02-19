package sources2.classifiers;

import sources2.Classifier;
import sources2.Tweet;

/**
 * Split datas among European languages into final European classes Concrete
 * managers are responsible for the management of the concrete local classes and
 * translation betweet local and global class.
 * 
 * @author vincent
 * 
 */
public class ClassifierEurasiatique extends Classifier {

	public ClassifierEurasiatique() {
		super();
		NB_CLASSES_DERIVEES = 5;

		toDerivatedClasses = new Integer[11];
		toDerivatedClasses[0] = 2;// ARA *
		toDerivatedClasses[1] = 4;// CHI
		toDerivatedClasses[2] = 4;// FRE
		toDerivatedClasses[3] = 4;// GER
		toDerivatedClasses[4] = 0;// HIN *
		toDerivatedClasses[5] = 4;// ITA
		toDerivatedClasses[6] = 4;// JPN
		toDerivatedClasses[7] = 4;// KOR
		toDerivatedClasses[8] = 4;// SPA
		toDerivatedClasses[9] = 1;// TEL *
		toDerivatedClasses[10] = 3;// TUR *
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
			return "HIN";
		case 1:
			return "TEL";
		case 2:
			return "ARA";
		case 3:
			return "TUR";
		case 4:
			return "ERR";
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
		if (t.getPolarit() == 0 || t.getPolarit() == 4 || t.getPolarit() == 9 || t.getPolarit() == 10) {
			return true;
		}
		return false;
	}

}
