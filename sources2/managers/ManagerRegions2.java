package sources2.managers;

import java.util.ArrayList;

import sources2.AbstractManager;
import sources2.Classifier;
import sources2.Tweet;
import sources2.classifiers.ClassifierAltaique3Groupes;
import sources2.classifiers.ClassifierEurasiatique;
import sources2.classifiers.ClassifierLatin;
import sources2.classifiers.ClassifierRegions3Groupes;
import sources2.classifiers.ClassifierSimple;

/**
 * Use 3 Classifiers to cut corpus into Asian and European part and find final
 * classes. Managers are responsible for the setting and the concatenation of
 * classifiers. They can be used like classifiers.
 * 
 * @author vincent
 * 
 */
public class ManagerRegions2 extends AbstractManager {

	// classifiers
	Classifier region = new ClassifierRegions3Groupes();
	Classifier simple = new ClassifierSimple();
	Classifier altaique = new ClassifierAltaique3Groupes();
	Classifier indtel = new ClassifierEurasiatique();
	Classifier latin = new ClassifierLatin();

	public ManagerRegions2() {
		// precise the number of final classes (default = 0)
		NB_CLASSES = 11;
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
	public void learn(final ArrayList<ArrayList<Tweet>> datas, final double k, final boolean verbose) {
		region.learn(this, datas, k, verbose);
		altaique.learn(this, datas, k, verbose);
		indtel.learn(this, datas, k, verbose);
		latin.learn(this, datas, k, verbose);
		simple.learn(this, datas, 0.388984375, verbose);

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
	public ArrayList<ArrayList<Tweet>> work(final ArrayList<Tweet> dataTest, final boolean verbose) {
		// System.out.println("Marqueur 1");
		final ArrayList<ArrayList<Tweet>> results = new ArrayList<ArrayList<Tweet>>();
		for (int i = 0; i < NB_CLASSES; i++) {
			results.add(new ArrayList<Tweet>());
		}
		// System.out.println("Marqueur 2");
		ArrayList<ArrayList<Tweet>> localResult = new ArrayList<ArrayList<Tweet>>();
		localResult = region.work(dataTest, true);
		// System.out.println("Marqueur 3");
		final ArrayList<ArrayList<Tweet>> latinResult = latin.work(localResult.get(0), false);
		results.get(2).addAll(latinResult.get(0));
		results.get(3).addAll(latinResult.get(1));
		results.get(5).addAll(latinResult.get(2));
		results.get(8).addAll(latinResult.get(3));
		final ArrayList<Tweet> euroErrors = latinResult.get(4);

		// System.out.println("Marqueur 4");
		final ArrayList<ArrayList<Tweet>> asianResult = altaique.work(localResult.get(1), false);
		results.get(1).addAll(asianResult.get(0));
		results.get(6).addAll(asianResult.get(1));
		results.get(7).addAll(asianResult.get(2));
		final ArrayList<Tweet> asianErrors = asianResult.get(3);

		// System.out.println("Marqueur 5");
		final ArrayList<ArrayList<Tweet>> indtelResult = indtel.work(localResult.get(1), false);
		results.get(4).addAll(indtelResult.get(0));
		results.get(9).addAll(indtelResult.get(1));
		final ArrayList<Tweet> indtelErrors = indtelResult.get(2);

		// System.out.println("Marqueur 6");
		// final ClassifierSimple simple = new ClassifierSimple();
		final ArrayList<ArrayList<Tweet>> asianCorrected = simple.work(asianErrors, false);
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

		// System.out.println("Marqueur 7");
		final ArrayList<ArrayList<Tweet>> euroCorrected = simple.work(euroErrors, false);
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

		// System.out.println("Marqueur 8");
		final ArrayList<ArrayList<Tweet>> indtelCorrected = simple.work(indtelErrors, false);
		results.get(0).addAll(indtelCorrected.get(0));
		results.get(1).addAll(indtelCorrected.get(1));
		results.get(2).addAll(indtelCorrected.get(2));
		results.get(3).addAll(indtelCorrected.get(3));
		results.get(4).addAll(indtelCorrected.get(4));
		results.get(5).addAll(indtelCorrected.get(5));
		results.get(6).addAll(indtelCorrected.get(6));
		results.get(7).addAll(indtelCorrected.get(7));
		results.get(8).addAll(indtelCorrected.get(8));
		results.get(9).addAll(indtelCorrected.get(9));
		results.get(10).addAll(indtelCorrected.get(10));

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
	public double check(final ArrayList<ArrayList<Tweet>> res, final boolean verbose) {
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
