package sources2.managers;
import java.util.ArrayList;

import sources2.AbstractManager;
import sources2.Classifier;
import sources2.Tweet;
import sources2.classifiers.ClassifierAsianEurope;
import sources2.classifiers.ClassifierAsie;
import sources2.classifiers.ClassifierEurope;
import sources2.classifiers.ClassifierSimple;

public class ManagerAsieEurope extends AbstractManager {

	final ArrayList<ArrayList<Tweet>> datas;
	Classifier region = new ClassifierAsianEurope();
	//	Classifier simple = new ClassifierSimple();
	Classifier europe = new ClassifierEurope();
	Classifier asia = new ClassifierAsie();

	public ManagerAsieEurope() {
		datas = null;
		NB_CLASSES = 11;
	}

	@Override
	public void learn(final ArrayList<ArrayList<Tweet>> datas, final double k, final boolean verbose) {
		// make all the classifiers learn
		region.learn(this, datas, k, verbose);
		europe.learn(this, datas, k, verbose);
		asia.learn(this, datas, k, verbose);
		//		simple.learn(this, datas, k, verbose);
	}

	@Override
	public ArrayList<ArrayList<Tweet>> work(final ArrayList<Tweet> dataTest, final boolean verbose) {
		final ArrayList<ArrayList<Tweet>> results = new ArrayList<ArrayList<Tweet>>();
		for(int i=0; i<NB_CLASSES; i++) {
			results.add(new ArrayList<Tweet>());
		}
		final ArrayList<ArrayList<Tweet>> localResult;// = new ArrayList<ArrayList<Tweet>>();
		localResult = region.work(dataTest, true);

		final ArrayList<ArrayList<Tweet>> europeResult = europe.work(localResult.get(0), false);
		results.get(2).addAll(europeResult.get(0));
		results.get(3).addAll(europeResult.get(1));
		results.get(5).addAll(europeResult.get(2));
		results.get(8).addAll(europeResult.get(3));

		final ArrayList<ArrayList<Tweet>> asianResult = asia.work(localResult.get(1), false);
		results.get(0).addAll(asianResult.get(0));
		results.get(1).addAll(asianResult.get(1));
		results.get(4).addAll(asianResult.get(2));
		results.get(6).addAll(asianResult.get(3));
		results.get(7).addAll(asianResult.get(4));
		results.get(9).addAll(asianResult.get(5));
		results.get(10).addAll(asianResult.get(6));

		return results;
	}

	@Override
	public double check(final ArrayList<ArrayList<Tweet>> res, final boolean verbose) {
		final Classifier simple = new ClassifierSimple();
		final double accuracy = simple.check(res, verbose);
		if(verbose) {
			simple.calculateAndDisplayConfusionMatrix(
					res);
		}
		//		final double accuracy = simple.check(results, verbose);
		//		if(verbose) {
		//			simple.calculateAndDisplayConfusionMatrix(
		//					results);
		//		}
		return accuracy;
	}

	@Override
	public String filter(final String text) {
		return text;//.toLowerCase();
	}

	@Override
	public Integer nti(final String polarite) {
		if(polarite.equals("ARA")) {
			return 0;
		} else if(polarite.equals("CHI")) {
			return 1;
		} else if(polarite.equals("FRE")) {
			return 2;
		} else if(polarite.equals("GER")) {
			return 3;
		} else if(polarite.equals("HIN")) {
			return 4;
		} else if(polarite.equals("ITA")) {
			return 5;
		} else if(polarite.equals("JPN")) {
			return 6;
		} else if(polarite.equals("KOR")) {
			return 7;
		} else if(polarite.equals("SPA")) {
			return 8;
		} else if(polarite.equals("TEL")) {
			return 9;
		} else if(polarite.equals("TUR")) {
			return 10;
		} else {
			return -1;
		}
	}

	@Override
	public String itn(final Integer polarite) {
		switch(polarite) {
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

}
