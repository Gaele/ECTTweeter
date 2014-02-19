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
public class ClassifierAltaique3Groupes extends Classifier {

	public ClassifierAltaique3Groupes() {
		super();
		NB_CLASSES_DERIVEES = 4;

		toDerivatedClasses = new Integer[11];
		toDerivatedClasses[0] = 3;// ARA
		toDerivatedClasses[1] = 0;// CHI *
		toDerivatedClasses[2] = 3;// FRE
		toDerivatedClasses[3] = 3;// GER
		toDerivatedClasses[4] = 3;// HIN
		toDerivatedClasses[5] = 3;// ITA
		toDerivatedClasses[6] = 1;// JPN *
		toDerivatedClasses[7] = 2;// KOR *
		toDerivatedClasses[8] = 3;// SPA
		toDerivatedClasses[9] = 3;// TEL
		toDerivatedClasses[10] = 3;// TUR
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
			return "CHI";
		case 1:
			return "JPN";
		case 2:
			return "KOR";
		case 3:
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
		if (t.getPolarit() == 1 || t.getPolarit() == 6 || t.getPolarit() == 7) {
			return true;
		}
		return false;
	}

}
