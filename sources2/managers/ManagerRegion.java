package sources2.managers;

import java.util.ArrayList;

import sources2.AbstractManager;
import sources2.Classifier;
import sources2.Tweet;
import sources2.classifiers.ClassifierAsianEurope;
import sources2.classifiers.ClassifierAsie;
import sources2.classifiers.ClassifierEurope;
import sources2.classifiers.ClassifierSimple;

/**
 * Use 3 Classifiers to cut corpus into Asian and European part and find final
 * classes. Managers are responsible for the setting and the concatenation of
 * classifiers. They can be used like classifiers.
 * 
 * @author vincent
 * 
 */
public class ManagerRegion extends AbstractManager {

	// classifiers
	Classifier region = new ClassifierAsianEurope();
	Classifier europe = new ClassifierEurope();
	Classifier asia = new ClassifierAsie();
	Classifier simple = new ClassifierSimple();

	public ManagerRegion() {
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
		this.region.learn(this, datas, k, verbose);
		this.europe.learn(this, datas, k, verbose);
		this.asia.learn(this, datas, k, verbose);
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
		final ArrayList<ArrayList<Tweet>> results = new ArrayList<ArrayList<Tweet>>();
		for (int i = 0; i < this.NB_CLASSES; i++) {
			results.add(new ArrayList<Tweet>());
		}
		final ArrayList<ArrayList<Tweet>> localResult;// = new
														// ArrayList<ArrayList<Tweet>>();
		localResult = this.region.work(dataTest, true);

		final ArrayList<ArrayList<Tweet>> europeResult = this.europe.work(
				localResult.get(0), false);
		results.get(2).addAll(europeResult.get(0));
		results.get(3).addAll(europeResult.get(1));
		results.get(5).addAll(europeResult.get(2));
		results.get(8).addAll(europeResult.get(3));
		final ArrayList<Tweet> euroErrors = europeResult.get(4);

		final ArrayList<ArrayList<Tweet>> asianResult = this.asia.work(
				localResult.get(1), false);
		results.get(0).addAll(asianResult.get(0));
		results.get(1).addAll(asianResult.get(1));
		results.get(4).addAll(asianResult.get(2));
		results.get(6).addAll(asianResult.get(3));
		results.get(7).addAll(asianResult.get(4));
		results.get(9).addAll(asianResult.get(5));
		results.get(10).addAll(asianResult.get(6));
		final ArrayList<Tweet> asianErrors = asianResult.get(7);

		final ArrayList<ArrayList<Tweet>> asianCorrected = this.simple.work(
				asianErrors, false);
		results.get(0).addAll(asianCorrected.get(0));
		results.get(1).addAll(asianCorrected.get(1));
		results.get(2).addAll(asianCorrected.get(2));
		results.get(3).addAll(asianCorrected.get(3));
		results.get(4).addAll(asianCorrected.get(4));
		results.get(5).addAll(asianCorrected.get(5));
		results.get(6).addAll(asianCorrected.get(6));
		results.get(7).addAll(asianCorrected.get(7));
		results.get(8).addAll(asianCorrected.get(8));
		results.get(9).addAll(asianCorrected.get(9));
		results.get(10).addAll(asianCorrected.get(10));

		final ArrayList<ArrayList<Tweet>> euroCorrected = this.simple.work(
				euroErrors, false);
		results.get(0).addAll(euroCorrected.get(0));
		results.get(1).addAll(euroCorrected.get(1));
		results.get(2).addAll(euroCorrected.get(2));
		results.get(3).addAll(euroCorrected.get(3));
		results.get(4).addAll(euroCorrected.get(4));
		results.get(5).addAll(euroCorrected.get(5));
		results.get(6).addAll(euroCorrected.get(6));
		results.get(7).addAll(euroCorrected.get(7));
		results.get(8).addAll(euroCorrected.get(8));
		results.get(9).addAll(euroCorrected.get(9));
		results.get(10).addAll(euroCorrected.get(10));

		return results;
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
		final Classifier simple = new ClassifierSimple();
		final double accuracy = simple.check(res, verbose);
		if (verbose) {
			simple.calculateAndDisplayConfusionMatrix(res);
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
