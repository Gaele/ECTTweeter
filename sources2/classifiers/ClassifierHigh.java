package sources2.classifiers;

import sources2.Classifier;
import sources2.Tweet;

/**
 * Split datas directly final classes Concrete managers are responsible for the
 * management of the concrete local classes and translation betweet local and
 * global class.
 * 
 * @author vincent
 * 
 */
public class ClassifierHigh extends Classifier {

	public ClassifierHigh() {
		super();
		this.NB_CLASSES_DERIVEES = 11;

		this.toDerivatedClasses = new Integer[11];
		for (int i = 0; i < 11; i++) {
			this.toDerivatedClasses[i] = i;
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see sources2.Classifier#isUsable(sources2.Tweet)
	 */
	@Override
	public boolean isUsable(final Tweet t) {
		return t.getMarque() == 2;
	}

}
