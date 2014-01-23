package sources2.classifiers;

import sources2.Classifier;

public class ClassifierTweet extends Classifier {

	public ClassifierTweet() {
		super();
		NB_CLASSES = 4;
	}

	@Override
	public String filter(final String text) {
		//		String t = text.replaceAll("b{2,}+", "b");

		final String t = text;
		//		t = t.replaceAll("d{2,}+", "d");
		//		t = t.replaceAll("f{2,}+", "f");
		//		t = t.replaceAll("g{2,}+", "g");
		//		t = t.replaceAll("h{2,}+", "h");
		//		t = t.replaceAll("k{2,}+", "k");
		//		t = t.replaceAll("l{2,}+", "l");
		//		t = t.replaceAll("m{2,}+", "m");
		//		t = t.replaceAll("n{2,}+", "n");
		//		t = t.replaceAll("p{2,}+", "p");
		//		t = t.replaceAll("r{2,}+", "r");
		//		t = t.replaceAll("s{2,}+", "s");
		//		t = t.replaceAll("t{2,}+", "t");
		//		t = t.replaceAll("v{2,}+", "v");
		//		t = t.replaceAll("x{2,}+", "x");
		//		// pour le son "k" : traitement à part
		//		t = t.replaceAll("ck", "k");
		//		t = t.replaceAll("cq", "k");
		//		t = t.replaceAll("qu", "k");
		//		// and
		//		t = t.replaceAll(" and ", " & ");
		//		// remplacement des nombres
		//		t = t.replaceAll(" (?>-?\\d+(?:[\\./]\\d+)?) ", "<number>");
		return t;


		// Taux moyen sur 10 essais : 31.352900370436465 avec correction ortho
		//		Taux moyen sur 10 essais : 31.979775597573884 sans correction ortho
		//
		//		Taux moyen sur 20 essais : 31.23812303909985 avec correction ortho
		//		Taux moyen sur 20 essais : 31.00534003686634 sans correction ortho

		// sans : 31.1906, avec : 31.1913 sur 50 essais
	}

	/**
	 * Nationalities to integer
	 * @param polarite the nationality
	 * @return the nationality code
	 */
	@Override
	public Integer nti(final String polarite) {
		if(polarite.equals("positive")) {
			return 0;
		} else if(polarite.equals("negative")) {
			return 1;
		} else if(polarite.equals("neutral")) {
			return 2;
		} else if(polarite.equals("irrelevant")) {
			return 3;
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
			return "pos";
		case 1:
			return "neg";
		case 2:
			return "neu";
		default:
			return "irr";
		}
	}

}