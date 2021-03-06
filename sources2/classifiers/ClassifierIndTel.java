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
public class ClassifierIndTel extends Classifier {

	public ClassifierIndTel() {
		super();
		NB_CLASSES_DERIVEES = 3;

		toDerivatedClasses = new Integer[11];
		toDerivatedClasses[0] = 2;// ARA
		toDerivatedClasses[1] = 2;// CHI
		toDerivatedClasses[2] = 2;// FRE
		toDerivatedClasses[3] = 2;// GER
		toDerivatedClasses[4] = 0;// HIN *
		toDerivatedClasses[5] = 2;// ITA
		toDerivatedClasses[6] = 2;// JPN
		toDerivatedClasses[7] = 2;// KOR
		toDerivatedClasses[8] = 2;// SPA
		toDerivatedClasses[9] = 1;// TEL *
		toDerivatedClasses[10] = 2;// TUR
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
		if (t.getPolarit() == 4 || t.getPolarit() == 9) {
			return true;
		}
		return false;
	}

}
