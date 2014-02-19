package sources2.classifiers;

import sources2.Classifier;
import sources2.Tweet;

/**
 * Split datas into Asian and European languages Concrete managers are
 * responsible for the management of the concrete local classes and translation
 * betweet local and global class.
 * 
 * @author vincent
 * 
 */
public class ClassifierRegions4Groupes extends Classifier {

	public ClassifierRegions4Groupes() {
		super();
		NB_CLASSES_DERIVEES = 4;

		toDerivatedClasses = new Integer[11];
		toDerivatedClasses[0] = 2;// ARA
		toDerivatedClasses[1] = 1;// CHI
		toDerivatedClasses[2] = 0;// FRE
		toDerivatedClasses[3] = 0;// GER
		toDerivatedClasses[4] = 3;// HIN
		toDerivatedClasses[5] = 0;// ITA
		toDerivatedClasses[6] = 1;// JPN
		toDerivatedClasses[7] = 1;// KOR
		toDerivatedClasses[8] = 0;// SPA
		toDerivatedClasses[9] = 3;// TEL
		toDerivatedClasses[10] = 1;// TUR
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
			return "LAT";
		case 1:
			return "TAI";
		case 2:
			return "ARA";
		case 3:
			return "INT"; // indien / telugu
		case 4:
			return "ARA";
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

}
