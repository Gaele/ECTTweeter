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
public class ClassifierAltaique extends Classifier {

	public ClassifierAltaique() {
		super();
		this.NB_CLASSES_DERIVEES = 5;

		this.toDerivatedClasses = new Integer[11];
		this.toDerivatedClasses[0] = 4;// ARA
		this.toDerivatedClasses[1] = 0;// CHI *
		this.toDerivatedClasses[2] = 4;// FRE
		this.toDerivatedClasses[3] = 4;// GER
		this.toDerivatedClasses[4] = 4;// HIN
		this.toDerivatedClasses[5] = 4;// ITA
		this.toDerivatedClasses[6] = 1;// JPN *
		this.toDerivatedClasses[7] = 2;// KOR *
		this.toDerivatedClasses[8] = 4;// SPA
		this.toDerivatedClasses[9] = 4;// TEL
		this.toDerivatedClasses[10] = 3;// TUR *
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
		if (t.getPolarit() == 1 || t.getPolarit() == 6 || t.getPolarit() == 7
				|| t.getPolarit() == 10) {
			return true;
		}
		return false;
	}

}
