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
		this.NB_CLASSES_DERIVEES = 3;

		this.toDerivatedClasses = new Integer[11];
		this.toDerivatedClasses[0] = 2;// ARA
		this.toDerivatedClasses[1] = 2;// CHI
		this.toDerivatedClasses[2] = 2;// FRE
		this.toDerivatedClasses[3] = 2;// GER
		this.toDerivatedClasses[4] = 0;// HIN *
		this.toDerivatedClasses[5] = 2;// ITA
		this.toDerivatedClasses[6] = 2;// JPN
		this.toDerivatedClasses[7] = 2;// KOR
		this.toDerivatedClasses[8] = 2;// SPA
		this.toDerivatedClasses[9] = 1;// TEL *
		this.toDerivatedClasses[10] = 2;// TUR
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
