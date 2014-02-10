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
public class ClassifierArabe extends Classifier {

	public ClassifierArabe() {
		super();
		this.NB_CLASSES_DERIVEES = 2;

		this.toDerivatedClasses = new Integer[11];
		this.toDerivatedClasses[0] = 0;// ARA
		this.toDerivatedClasses[1] = 1;// CHI
		this.toDerivatedClasses[2] = 1;// FRE
		this.toDerivatedClasses[3] = 1;// GER
		this.toDerivatedClasses[4] = 1;// HIN
		this.toDerivatedClasses[5] = 1;// ITA
		this.toDerivatedClasses[6] = 1;// JPN
		this.toDerivatedClasses[7] = 1;// KOR
		this.toDerivatedClasses[8] = 1;// SPA
		this.toDerivatedClasses[9] = 1;// TEL
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
			return "ARA";
		case 1:
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
		if (t.getPolarit() == 0) {
			return true;
		}
		return false;
	}

}
