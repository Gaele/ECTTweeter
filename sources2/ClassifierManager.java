package sources2;
import java.util.ArrayList;

import sources2.classifiers.ClassifierRegion;
import sources2.classifiers.ClassifierSimple;

public class ClassifierManager extends AbstractManager {

	final ArrayList<ArrayList<Tweet>> datas;
	Classifier region = new ClassifierRegion();
	Classifier simple = new ClassifierSimple();
	ArrayList<ArrayList<Tweet>> results = new ArrayList<ArrayList<Tweet>>();

	public ClassifierManager() {
		datas = null;
		NB_CLASSES = 11;
	}

	@Override
	public void learn(final ArrayList<ArrayList<Tweet>> datas, final double k, final boolean verbose) {
		// make all the classifiers learn
		region.learn(this, datas, k, verbose);
		//		simple.learn(this, datas, k, verbose);
	}

	@Override
	public void work(final ArrayList<Tweet> dataTest, final boolean verbose) {
		results = region.work(dataTest);
		//		results = simple.work(dataTest);
	}

	@Override
	public void check() {
		region.check(results, true);
		region.calculateAndDisplayConfusionMatrix(
				results);
		//		simple.check(results, true);
		//		simple.calculateAndDisplayConfusionMatrix(
		//				results);
	}

	@Override
	public String filter(final String text) {
		return text;
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
