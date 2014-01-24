package sources2.managers;
import java.util.ArrayList;

import sources2.AbstractManager;
import sources2.Classifier;
import sources2.Tweet;
import sources2.classifiers.ClassifierSimple;

public class SimpleManager extends AbstractManager {

	final ArrayList<ArrayList<Tweet>> datas;
	Classifier simple = new ClassifierSimple();

	public SimpleManager() {
		datas = null;
		NB_CLASSES = 11;
	}

	@Override
	public void learn(final ArrayList<ArrayList<Tweet>> datas, final double k, final boolean verbose) {
		// make all the classifiers learn
		simple.learn(this, datas, k, verbose);
	}

	@Override
	public ArrayList<ArrayList<Tweet>> work(final ArrayList<Tweet> dataTest, final boolean verbose) {
		return simple.work(dataTest, true);

	}

	@Override
	public double check(final ArrayList<ArrayList<Tweet>> res, final boolean verbose) {
		final double accuracy = simple.check(res, verbose);
		if(verbose) {
			simple.calculateAndDisplayConfusionMatrix(
					res);
		}
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
