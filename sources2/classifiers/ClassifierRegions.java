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
public class ClassifierRegions extends Classifier {

	public ClassifierRegions() {
		super();
		this.NB_CLASSES_DERIVEES = 4;

		this.toDerivatedClasses = new Integer[11];
		this.toDerivatedClasses[0] = 2;// ARA
		this.toDerivatedClasses[1] = 1;// CHI
		this.toDerivatedClasses[2] = 0;// FRE
		this.toDerivatedClasses[3] = 0;// GER
		this.toDerivatedClasses[4] = 3;// HIN
		this.toDerivatedClasses[5] = 0;// ITA
		this.toDerivatedClasses[6] = 1;// JPN
		this.toDerivatedClasses[7] = 1;// KOR
		this.toDerivatedClasses[8] = 0;// SPA
		this.toDerivatedClasses[9] = 3;// TEL
		this.toDerivatedClasses[10] = 1;// TUR
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
