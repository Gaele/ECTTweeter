package sources2.managers;

import java.util.ArrayList;

import sources2.AbstractManager;
import sources2.Classifier;
import sources2.Tweet;
import sources2.classifiers.ClassifierSimple;

/**
 * This Manger uses only one Classifier to split datas into classes. Managers
 * are responsible for the setting and the concatenation of classifiers. They
 * can be used like classifiers.
 * 
 * @author vincent
 * 
 */
public class ManagerLevel extends AbstractManager {

	Classifier simple = new ClassifierSimple();

	public ManagerLevel() {
		// precise the number of final classes (default = 0)
		this.NB_CLASSES = 11;
	}

	/**
	 * Learn datas from the file
	 * 
	 * @param datas
	 *            the datas to use, ListOfClasses<ListOfTweetsFor1Class<Tweet>>
	 * @param k
	 *            the "k" parameter to use
	 * @param verbose
	 *            diplays infos if true
	 */
	@Override
	public void learn(final ArrayList<ArrayList<Tweet>> datas, final double k,
			final boolean verbose) {
		this.simple.learn(this, datas, k, verbose);
	}

	/**
	 * Calculates the languages of the datasTest
	 * 
	 * @param dataTest
	 *            data to analyse
	 * @param verbose
	 *            diplays infos if true
	 * @return analysed data,
	 *         ListOfDerivedClasses<ListOfTweetsFor1DerivedClass<Tweet>>
	 */
	@Override
	public ArrayList<ArrayList<Tweet>> work(final ArrayList<Tweet> dataTest,
			final boolean verbose) {
		return this.simple.work(dataTest, true);
	}

	/**
	 * Print performance
	 * 
	 * @param res
	 *            the data analysed
	 * @param verbose
	 *            displays infos if true
	 * @return the accuracy in %
	 */
	@Override
	public double check(final ArrayList<ArrayList<Tweet>> res,
			final boolean verbose) {
		final double accuracy = this.simple.check(res, verbose);
		if (verbose) {
			this.simple.calculateAndDisplayConfusionMatrix(res);
		}
		return accuracy;
	}

	/**
	 * Transform the text before it's translated by any classifier or manager
	 * 
	 * @param text
	 *            Text to transform
	 * @return transformed text
	 */
	@Override
	protected String filter(final String text) {
		return text;// .toLowerCase();
	}

	/**
	 * Natural to Integer, gives the code of a final classe
	 * 
	 * @param polarite
	 *            the text of the final classe
	 * @return the code of the final classe
	 */
	@Override
	public Integer nti(final String polarite) {
		if (polarite.equals("ARA")) {
			return 0;
		} else if (polarite.equals("CHI")) {
			return 1;
		} else if (polarite.equals("FRE")) {
			return 2;
		} else if (polarite.equals("GER")) {
			return 3;
		} else if (polarite.equals("HIN")) {
			return 4;
		} else if (polarite.equals("ITA")) {
			return 5;
		} else if (polarite.equals("JPN")) {
			return 6;
		} else if (polarite.equals("KOR")) {
			return 7;
		} else if (polarite.equals("SPA")) {
			return 8;
		} else if (polarite.equals("TEL")) {
			return 9;
		} else if (polarite.equals("TUR")) {
			return 10;
		} else {
			return -1;
		}
	}

	/**
	 * Integer to Natural, gives the string representation of a final classe
	 * code.
	 * 
	 * @param polarite
	 *            the code of the final classe
	 * @return the string description of the final classe
	 */
	@Override
	public String itn(final Integer polarite) {
		switch (polarite) {
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
	 * @see sources2.AbstractManager#nti2(java.lang.String)
	 */
	@Override
	protected int nti2(final String marque) {
		if (marque.equals("low")) {
			return 0;
		} else if (marque.equals("medium")) {
			return 1;
		} else if (marque.equals("high")) {
			return 2;
		} else {
			return -1;
		}
	}
}
