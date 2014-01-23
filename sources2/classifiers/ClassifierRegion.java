package sources2.classifiers;

import sources2.Classifier;

public class ClassifierRegion extends Classifier {

	public ClassifierRegion() {
		super();
		// split into Asian / European
		// 0 => European, 1 => Asian
		NB_CLASSES = 11;
		NB_CLASSES_DERIVEES = 2;
	}

	@Override
	public String filter(final String text) {
		final String t = text;
		return t;
	}

	//	/**
	//	 * Nationalities to integer
	//	 * @param polarite the nationality
	//	 * @return the nationality code
	//	 */
	//	@Override
	//	public Integer ntiDerive(final String polarite) {
	//		if(polarite.equals("ARA")) {
	//			return 0;
	//		} else if(polarite.equals("CHI")) {
	//			return 1;
	//		} else if(polarite.equals("FRE")) {
	//			return 0;
	//		} else if(polarite.equals("GER")) {
	//			return 0;
	//		} else if(polarite.equals("HIN")) {
	//			return 1;
	//		} else if(polarite.equals("ITA")) {
	//			return 0;
	//		} else if(polarite.equals("JPN")) {
	//			return 1;
	//		} else if(polarite.equals("KOR")) {
	//			return 1;
	//		} else if(polarite.equals("SPA")) {
	//			return 0;
	//		} else if(polarite.equals("TEL")) {
	//			return 1;
	//		} else if(polarite.equals("TUR")) {
	//			return 1;
	//		} else {
	//			return -1;
	//		}
	//	}

	//	/**
	//	 * integer to nationality
	//	 *
	//	 * @param polarity the nationality code
	//	 * @return the nationality String
	//	 */
	//	@Override
	//	public String itnDerive(final Tweet polarity) {
	//		switch(polarity.getPolarite()) {
	//		case 0:
	//			return "EURO";
	//		case 1:
	//			return "ASI";
	//		default:
	//			return "???";
	//		}
	//	}


	/**
	 * Nationalities to integer
	 * @param polarite the nationality
	 * @return the nationality code
	 */
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

	/**
	 * integer to nationality
	 * 
	 * @param polarity the nationality code
	 * @return the nationality String
	 */
	@Override
	public String itn(final Integer polarity) {
		switch(polarity) {
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
